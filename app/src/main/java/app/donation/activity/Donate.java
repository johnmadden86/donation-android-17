package app.donation.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import app.donation.R;
import app.donation.main.DonationApp;
import app.donation.model.Candidate;
import app.donation.model.Donation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;
import static org.wit.android.helpers.LogHelpers.info;

public  class Donate
        extends AppCompatActivity
        implements View.OnClickListener, Callback<Donation> {

    private DonationApp app;
    private Button donateButton;
    private RadioGroup paymentMethod;
    private ProgressBar progressBar;
    private NumberPicker amountPicker;
    private EditText amountPickerText;
    private TextView total;
    private Spinner candidateSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        app = (DonationApp) getApplication();

        donateButton = (Button) findViewById(R.id.donateButton);
        donateButton.setOnClickListener(this);

        paymentMethod = (RadioGroup) findViewById(R.id.paymentMethod);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(app.totalDonated);
        progressBar.setMax(app.target);

        amountPicker = (NumberPicker) findViewById(R.id.amountPicker);
        amountPicker.setMinValue(0);
        amountPicker.setMaxValue(1000);

        amountPickerText = (EditText) findViewById(R.id.amountPickerText);

        total = (TextView) findViewById(R.id.textTotal);
        total.setText(app.totalDonatedString);

        candidateSelection = (Spinner) findViewById(R.id.spinner);
        CandidateAdapter adapter = new CandidateAdapter(app.candidates);
        candidateSelection.setAdapter(adapter);
    }

    public void donateButtonPressed(View view) {
        int amount = amountPicker.getValue();
        int amountText = amountPickerText.getText().toString().equals("")
                        ? 0
                        : parseInt(amountPickerText.getText().toString());
        amount =    amount != 0
                    ? amount
                    : amountText;
        int radioId = paymentMethod.getCheckedRadioButtonId();
        String method = radioId == R.id.payPal ? "PayPal" : "Direct";

        if (amount > 0) {
            Donation donation = new Donation(amount, method);
            Candidate candidate = (Candidate) candidateSelection.getSelectedItem();
            Log.v("Donate", candidate._id);
            Call<Donation> call = (Call<Donation>) app.donationService.createDonation(candidate._id, donation);
            call.enqueue(this);
            info(this, this.toString());
        }
        amountPicker.setValue(0);
        amountPickerText.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuReport:
                startActivity(new Intent(this, Report.class));
                break;
            case R.id.menuSettings:
                Toast.makeText(this, "Settings Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menuLogout:
                startActivity(new Intent(this, Login.class));
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_donate, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.donateButton:
                donateButtonPressed(view);
        }
    }

    @Override
    public void onResponse(Call<Donation> call, Response<Donation> response) {
        Toast.makeText(this, "Donation Accepted", Toast.LENGTH_SHORT).show();
        app.newDonation(response.body());
        progressBar.setProgress(app.totalDonated);
        total.setText(app.totalDonatedString);
        Log.v("Donate", "Donation successful");
    }

    @Override
    public void onFailure(Call<Donation> call, Throwable t) {
        Toast.makeText(this, "Error making donation", Toast.LENGTH_LONG).show();
        Log.v("Donate", "Donation failed");
    }


    private class       CandidateAdapter
            extends     BaseAdapter
            implements  SpinnerAdapter {

        private final List<Candidate> candidateData;

        public CandidateAdapter(List<Candidate> data) {
            this.candidateData = data;
        }

        @Override
        public int getCount() {
            return candidateData.size();
        }

        @Override
        public Object getItem(int position) {
            return candidateData.get(position);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View recycle, ViewGroup parent) {
            TextView text;
            if (recycle != null)
            {
                text = (TextView) recycle;
            }
            else
            {
                text = (TextView)   getLayoutInflater()
                                    .inflate(
                                                android.R.layout.simple_dropdown_item_1line,
                                                parent,
                                                false
                                            );
            }
            text.setTextColor(Color.BLACK);
            String fullName = candidateData.get(position).firstName + " " + candidateData.get(position).lastName;
            text.setText(fullName);
            return text;
        }
    }
}