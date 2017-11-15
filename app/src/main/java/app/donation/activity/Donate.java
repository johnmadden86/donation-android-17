package app.donation.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import app.donation.R;
import app.donation.main.DonationApp;
import app.donation.model.Donation;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;

public class Donate extends AppCompatActivity implements View.OnClickListener {

    private DonationApp app;
    private Button donateButton;
    private RadioGroup paymentMethod;
    private ProgressBar progressBar;
    private NumberPicker amountPicker;
    private EditText amountPickerText;
    private TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        app = (DonationApp) getApplication();

        donateButton = (Button) findViewById(R.id.donateButton);
        donateButton.setOnClickListener(this);

        paymentMethod = (RadioGroup)   findViewById(R.id.paymentMethod);

        progressBar   = (ProgressBar)  findViewById(R.id.progressBar);
        progressBar.setMax(app.target);

        amountPicker  = (NumberPicker) findViewById(R.id.amountPicker);
        amountPicker.setMinValue(0);
        amountPicker.setMaxValue(1000);

        amountPickerText  = (EditText) findViewById(R.id.amountPickerText);

        total  = (TextView) findViewById(R.id.textTotal);
        total.setText(app.totalDonatedString);
    }

    public void donateButtonPressed (View view)  {
        int amount = amountPicker.getValue();
        int amountText = amountPickerText.getText().toString().equals("") ? 0 : parseInt(amountPickerText.getText().toString());
        amount = amount != 0 ? amount : amountText;
        int radioId = paymentMethod.getCheckedRadioButtonId();
        String method = radioId == R.id.payPal ? "PayPal" : "Direct";
        app.newDonation(new Donation(amount, method));
        progressBar.setProgress(app.totalDonated);
        total.setText(app.totalDonatedString);
        amountPicker.setValue(0);
        amountPickerText.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuReport:
                startActivity (new Intent(this, Report.class));
                break;
            case R.id.menuSettings:
                Toast.makeText(this, "Settings Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menuLogout:
                startActivity (new Intent(this, Login.class));
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_donate, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.donateButton:
                donateButtonPressed(view);
        }
    }
}
