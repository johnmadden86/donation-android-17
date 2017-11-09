package app.donation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import app.donation.R;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
    }

    public void signUpButtonPressed (View view) {
        startActivity(new Intent(this, SignUp.class));
    }

    public void loginButtonPressed (View view) {
        startActivity(new Intent(this, Login.class));
    }

}