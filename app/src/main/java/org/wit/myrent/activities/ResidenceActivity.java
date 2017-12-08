package org.wit.myrent.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import app.donation.R;
import app.donation.activity.Login;
import app.donation.activity.Report;

import org.wit.android.helpers.IntentHelper;
import org.wit.myrent.app.MyRentApp;
import org.wit.myrent.models.Portfolio;
import org.wit.myrent.models.Residence;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.wit.android.helpers.ContactHelper.getContact;
import static org.wit.android.helpers.ContactHelper.getDisplayName;
import static org.wit.android.helpers.ContactHelper.getEmail;
import static org.wit.android.helpers.ContactHelper.sendEmail;
import static org.wit.android.helpers.IntentHelper.navigateUp;
import static org.wit.android.helpers.IntentHelper.selectContact;

public class ResidenceActivity extends AppCompatActivity
        implements TextWatcher, CompoundButton.OnCheckedChangeListener, View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private EditText geolocation;
    private Residence residence;
    private CheckBox rented;
    private Button dateButton, tenantButton, reportButton;
    private Portfolio portfolio;
    private static final int REQUEST_CONTACT = 1;
    private String emailAddress;

    private Intent data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_residence);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        geolocation = (EditText) findViewById(R.id.geolocation);
        geolocation.addTextChangedListener(this);

        dateButton = (Button) findViewById(R.id.registration_date);
        dateButton.setOnClickListener(this);

        tenantButton = (Button) findViewById(R.id.tenant);
        tenantButton.setOnClickListener(this);

        reportButton = (Button) findViewById(R.id.residence_reportButton);
        reportButton.setOnClickListener(this);

        rented = (CheckBox) findViewById(R.id.isrented);
        rented.setOnCheckedChangeListener(this);

        residence = new Residence();

        MyRentApp app = (MyRentApp) getApplication();
        portfolio = app.portfolio;
        Long resId = (Long) getIntent().getExtras().getSerializable("RESIDENCE_ID");
        residence = portfolio.getResidence(resId);
        if (residence != null) {
            updateControls(residence);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        portfolio.saveResidences();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_donate, menu);
        return true;
        // return super.onCreateOptionsMenu(menu);
    };

    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()) {
            /*
            case R.id.menuReport:
                startActivity (new Intent(this, Report.class));
                break;
            case R.id.menuSettings:
                Toast.makeText(this, "Settings Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menuLogout:
                startActivity (new Intent(this, Login.class));
                break;
                */
            case android.R.id.home:
                navigateUp(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        residence.setGeolocation(editable.toString());
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        Log.i(this.getClass().getSimpleName(), "rented Checked");
        residence.rented = isChecked;
    }

    public void updateControls(Residence residence) {
        geolocation.setText(residence.geolocation);
        rented.setChecked(residence.rented);
        dateButton.setText(residence.getDateString());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Date date = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
        residence.date = date.getTime();
        dateButton.setText(residence.getDateString());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registration_date :
                Calendar c = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog (this, this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dpd.show();
                break;
            case R.id.tenant:
                selectContact(this, REQUEST_CONTACT);
                break;
            case R.id.residence_reportButton:
                sendEmail(this, emailAddress, getString(R.string.residence_report_subject), residence.getResidenceReport(this));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONTACT:
                this.data = data;
                checkContactsReadPermission();
        }
    }

    private void checkContactsReadPermission() {
        if (ContextCompat.checkSelfPermission
                (this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions
                    (this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CONTACT);
        }
        else
        {
            readContact();
        }
    }


    private void readContact() {
        String name = getDisplayName(this, data);
        emailAddress = getEmail(this, data);
        String fullDetails = name + " " + emailAddress;
        tenantButton.setText(fullDetails);
        residence.tenant = name;
    }
}
