package app.donation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import app.donation.R;
import app.donation.main.DonationApp;
import app.donation.model.Candidate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.wit.android.helpers.LogHelpers.info;

public  class Login
        extends AppCompatActivity
        implements Callback<List<Candidate>> {

    private DonationApp app;
    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        app = (DonationApp) getApplication();
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
    }

    @Override
    public void onResume() {
        super.onResume();
        app.currentUser = null;

        Call<List<Candidate>> call = (Call<List<Candidate>>) app.donationServiceOpen.getAllCandidates();
        call.enqueue(this);
    }

    public void login (View view) {
        if (app.donationServiceAvailable) {
            String emailInput = email.getText().toString();
            String passwordInput = password.getText().toString();

            if (app.validUser(emailInput, passwordInput)) {
                startActivity(new Intent(this, Donate.class));
                Log.v("Donate", "Login successful " + emailInput);
            }
            else {
                Toast.makeText(this, "Login failed!", Toast.LENGTH_SHORT).show();
                Log.v("Donate", "Login failed");
                email.setText("");
                password.setText("");
            }
        } else {
            serviceUnavailableMessage();
        }
    }

    public void signUp (View view) {
        if (app.donationServiceAvailable) {
            startActivity (new Intent(this, SignUp.class));
        } else {
            serviceUnavailableMessage();
        }
    }

    void serviceUnavailableMessage() {
        info(this, "Service Unavailable");
        app.donationServiceAvailable = false;
        Toast.makeText(this, "Donation Service Unavailable. Try again later", Toast.LENGTH_LONG).show();
    }

    void serviceAvailableMessage() {
        info(this, "Service Available");
        app.donationServiceAvailable = true;
        Toast.makeText(this, "Donation Contacted Successfully", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(Call<List<Candidate>> call, Response<List<Candidate>> response) {
        serviceAvailableMessage();
        app.candidates = response.body();
    }

    @Override
    public void onFailure(Call<List<Candidate>> call, Throwable t) {
        serviceUnavailableMessage();
    }
}