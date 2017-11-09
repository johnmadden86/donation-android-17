package app.donation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import app.donation.R;
import app.donation.DonationApp;
import app.donation.User;

public class SignUp extends AppCompatActivity {
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
        User user = new User(firstName.getText().toString(),
                lastName.getText().toString(), email.getText().toString(), password.getText().toString());
        app.newUser(user);
        startActivity(new Intent(this, Donate.class));
    }

}