package org.wit.myrent.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import app.donation.R;
import app.donation.activity.Login;
import app.donation.activity.Report;
import org.wit.myrent.models.Residence;

public class ResidenceActivity extends Activity implements TextWatcher, CompoundButton.OnCheckedChangeListener {

    private EditText geolocation;
    private Residence residence;

    private CheckBox rented;
    private Button dateButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_residence);

        geolocation = (EditText) findViewById(R.id.geolocation);
        geolocation.addTextChangedListener(this);

        dateButton  = (Button) findViewById(R.id.registration_date);
        dateButton.setEnabled(false);

        rented      = (CheckBox) findViewById(R.id.isrented);
        rented.setOnCheckedChangeListener(this);

        residence = new Residence();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_donate, menu);
        return true;
        // return super.onCreateOptionsMenu(menu);
    };

    public boolean onOptionsItemSelected(MenuItem item){
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

        // return true;
        return super.onOptionsItemSelected(item);
    };

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
}
