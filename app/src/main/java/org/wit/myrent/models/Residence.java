package org.wit.myrent.models;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Random;

import org.wit.myrent.R;

public class Residence {

    public Long id, date;
    public String geolocation, tenant;
    public boolean rented;
    public double zoom;

    private static final String JSON_ID = "id";
    private static final String JSON_GEOLOCATION = "geolocation";
    private static final String JSON_DATE = "date";
    private static final String JSON_RENTED = "rented";
    private static final String JSON_TENANT = "tenant";
    private static final String JSON_ZOOM = "ZOOM";

    public Residence() {
        id = unsignedLong();
        date = new Date().getTime();
        geolocation = "52.253456,-7.187162";
        tenant = "None at present";
        zoom = 16.0f;
    }

    public Residence(JSONObject json) throws JSONException {
        id = json.getLong(JSON_ID);
        geolocation = json.getString(JSON_GEOLOCATION);
        date = json.getLong(JSON_DATE);
        rented = json.getBoolean(JSON_RENTED);
        tenant = json.getString(JSON_TENANT);
        zoom = json.getDouble(JSON_ZOOM);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID , Long.toString(id));
        json.put(JSON_GEOLOCATION, geolocation);
        json.put(JSON_DATE, date);
        json.put(JSON_RENTED, rented);
        json.put(JSON_TENANT, tenant);
        json.put(JSON_ZOOM, zoom);
        return json;
    }

    /**
     * Generate a long greater than zero
     * @return Unsigned Long value greater than zero
     */
    private Long unsignedLong() {
        long rndVal = 0;
        do {
            rndVal = new Random().nextLong();
        } while (rndVal <= 0);
        return rndVal;
    }

    public void setGeolocation(String geolocation)
    {
        this.geolocation = geolocation;
    }

    public String getGeolocation()
    {
        return geolocation;
    }

    public String getDateString() {
        return "Registered:" + dateString();
    }

    private String dateString() {
        String dateFormat = "EEE d MMM yyyy H:mm";
        return android.text.format.DateFormat.format(dateFormat, date).toString();
    }

    public String getResidenceReport(Context context) {
        String rentedString;
        if (rented) {
            rentedString = context.getString(R.string.residence_report_rented);
        }
        else {
            rentedString = context.getString(R.string.residence_report_not_rented);
        }

        String prospectiveTenant = tenant;
        if (tenant == null) {
            prospectiveTenant = context.getString(R.string.residence_report_nobody_interested);
        }
        else {
            prospectiveTenant = context.getString(R.string.residence_report_prospective_tenant, tenant);
        }
        return "Location " + geolocation + " Date: " + dateString() + " " + rentedString + " " + prospectiveTenant;
    }
}