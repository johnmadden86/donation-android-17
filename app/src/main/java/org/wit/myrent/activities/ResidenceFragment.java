package org.wit.myrent.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;

import org.wit.myrent.app.MyRentApp;
import org.wit.myrent.models.Portfolio;
import org.wit.myrent.models.Residence;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.wit.myrent.R;

import static org.wit.android.helpers.ContactHelper.getContact;
import static org.wit.android.helpers.ContactHelper.getEmail;
import static org.wit.android.helpers.ContactHelper.sendEmail;
import static org.wit.android.helpers.IntentHelper.navigateUp;
import static org.wit.android.helpers.IntentHelper.startActivityNoData;
import static org.wit.android.helpers.IntentHelper.startActivityWithData;

public  class       ResidenceFragment
        extends     Fragment
        implements  TextWatcher,
                    CompoundButton.OnCheckedChangeListener,
                    View.OnClickListener,
                    DatePickerDialog.OnDateSetListener {


    private static final int    REQUEST_CONTACT = 1;
    public  static final String EXTRA_RESIDENCE_ID = "myrent.RESIDENCE_ID";

    private EditText    geolocation;
    private Residence   residence;
    private CheckBox    rented;
    private Button      dateButton, tenantButton, reportButton;
    private Portfolio   portfolio;
    private String      emailAddress;
    private Intent      data;

    MyRentApp app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        app = MyRentApp.getApp();
        portfolio = app.portfolio;

        Long resId = (Long) getArguments()
                            .getSerializable(EXTRA_RESIDENCE_ID);
        residence = portfolio.getResidence(resId);
    }

    private void addListeners(View v) {
        geolocation = (EditText) v.findViewById(R.id.geolocation);
        geolocation.addTextChangedListener(this);

        dateButton = (Button) v.findViewById(R.id.registration_date);
        dateButton.setOnClickListener(this);

        tenantButton = (Button) v.findViewById(R.id.tenant);
        tenantButton.setOnClickListener(this);

        reportButton = (Button) v.findViewById(R.id.residence_reportButton);
        reportButton.setOnClickListener(this);

        rented = (CheckBox) v.findViewById(R.id.isrented);
        rented.setOnCheckedChangeListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup      parent,
                             Bundle         savedInstanceState) {
        super.onCreateView(inflater,  parent, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_residence, parent, false);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        addListeners(v);
        updateControls(residence);

        return v;
    }

    public void updateControls(Residence residence) {
        geolocation.setText(residence.geolocation);
        rented.setChecked(residence.rented);
        dateButton.setText(residence.getDateString());
        String tenant = "Tenant: " + residence.tenant;
        tenantButton.setText(tenant);
    }

    @Override
    public void onPause() {
        super.onPause();
        portfolio.saveResidences();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: navigateUp(getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable editable) {
        Log.i(this.getClass().getSimpleName(), "geolocation " + editable.toString());
        residence.geolocation = editable.toString();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        Log.i(this.getClass().getSimpleName(), "rented Checked");
        residence.rented = isChecked;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Date date = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
        residence.date = date.getTime();
        dateButton.setText(residence.getDateString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registration_date:
                Calendar c = Calendar.getInstance();
                DatePickerDialog dpd =
                        new DatePickerDialog(
                                getActivity(), this,
                                c.get(Calendar.YEAR),
                                c.get(Calendar.MONTH),
                                c.get(Calendar.DAY_OF_MONTH)
                        );
                dpd.show();
                break;
            case R.id.tenant:
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(i, REQUEST_CONTACT);
                String tenant = "Tenant: " + residence.tenant;
                tenantButton.setText(tenant);
                break;
            case R.id.residence_reportButton:
                sendEmail(
                        getActivity(),
                        emailAddress,
                        getString(R.string.residence_report_subject),
                        residence.getResidenceReport(getActivity())
                );
                break;
            case R.id.fab:
                startActivityWithData(getActivity(), MapActivity.class, EXTRA_RESIDENCE_ID, residence.id);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_CONTACT:
                this.data = data;
                checkContactsReadPermission();
                break;
        }
    }

    private void checkContactsReadPermission() {
        if (ContextCompat.checkSelfPermission(
                getActivity(),
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CONTACT);
        }
        else {
            readContact();
        }
    }

    private void readContact() {
        String name = getContact(getActivity(), data);
        emailAddress = getEmail(getActivity(), data);
        residence.tenant = name;
        String tenant = "Tenant: " + residence.tenant;
        tenantButton.setText(tenant);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CONTACT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    readContact();
                }
            }
        }
    }
}