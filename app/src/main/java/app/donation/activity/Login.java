package app.donation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import app.donation.R;
import app.donation.main.DonationApp;

public class Login extends AppCompatActivity {
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

    public void login (View view) {
        String emailInput = email.getText().toString();
        String passwordInput = password.getText().toString();

        for (int i = 0; i < app.users.size(); i++) {
            if (emailInput.equals(app.users.get(i).getEmail()) && passwordInput.equals(app.users.get(i).getPassword())) {
                startActivity(new Intent(this, Donate.class));
                Log.v("Donate", "Login successful " + emailInput);
            } else {
                Toast.makeText(this, "Login failed!", Toast.LENGTH_SHORT).show();
                Log.v("Donate", "Login failed");
                startActivity(new Intent(this, Login.class));
            }
        }
    }

    public void signUp (View view) {
        startActivity(new Intent(this, SignUp.class));
    }


}