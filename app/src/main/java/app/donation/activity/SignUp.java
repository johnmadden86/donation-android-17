package app.donation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import app.donation.R;
import app.donation.main.DonationApp;
import app.donation.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity implements Callback<User> {
    private DonationApp app;
    private EditText firstName, lastName, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        app = (DonationApp) getApplication();
        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
    }

    public void signUp (View view) {
        User user = new User(
                firstName.getText().toString(),
                lastName.getText().toString(),
                email.getText().toString(),
                password.getText().toString()
        );
        app.newUser(user);
        Call<User> call = (Call<User>) app.donationServiceOpen.createUser(user);
        call.enqueue(this);
    }

    public void login (View view) {
        if (app.donationServiceAvailable) {
            startActivity (new Intent(this, Login.class));
        } else {
            Toast.makeText(this, "Donation Service Unavailable. Try again later", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResponse(Call<User> call, Response<User> response) {
        app.users.add(response.body());
        Toast.makeText(this, "Sign-up successful", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, Donate.class));
    }

    @Override
    public void onFailure(Call<User> call, Throwable t) {
        app.donationServiceAvailable = false;
        Toast.makeText(this, "Donation Service Unavailable. Try again later", Toast.LENGTH_LONG).show();
        startActivity (new Intent(this, Login.class));
    }
}
