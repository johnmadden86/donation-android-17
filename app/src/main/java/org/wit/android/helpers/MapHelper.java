package org.wit.android.helpers;

import android.content.Context;
import android.util.Log;

// import com.google.android.gms.maps.model.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.Locale;

public class MapHelper {
    /*
     * @param context Presently redundant, referenct to the application context
     * @param geolocation The geolocation in Residence format, example: "42.122,-7.456"
     * @return The geolocation expressed as a Google LatLng object
     */
    public static LatLng latLng(Context context, String geolocation) {
        String[] g = geolocation.split(",");
        if (g.length == 2) {
            return new LatLng(Double.parseDouble(g[0]), Double.parseDouble(g[1]));
        }
        return new LatLng(0, 0);
    }

    /*
     * parse a GoogleMap LatLng object and return a Residence geolocation string.
     * example: "42.122,-7.456"
     * @param geo Google LatLng object representing a latitude, longitude pair
     * @return A latitude longitude pair in a format suitable for use in Residence class
     */
    public static String latLng(LatLng geo) {
        return  String.format(Locale.getDefault(),"%.6f", geo.getLatitude())
                + ", "
                + String.format(Locale.getDefault(),"%.6f", geo.getLongitude());
    }

    /**
     * Parses a string containing latitude and longitude.
     * @param geolocation The string obtained by concatenating comma separated latitude and longitude
     * @return The latitude component
     */
    public static double latitude(String geolocation) {
        String[] g = geolocation.split(",");
        try {
            if (g.length == 2) {
                return Double.parseDouble(g[0]);
            }
        }
        catch (NumberFormatException e) {
            Log.d("MapHelper", "Number format exception: invalid latitude: " + e.getMessage());
        }
        return 0.0;

    }

    /**
     * Parses a string containing latitude and longitude.
     * @param geolocation The string obtained by concatenating comma separated latitude and longitude
     * @return The longitude component
     */
    public static double longitude(String geolocation) {
        String[] g = geolocation.split(",");
        try {
            if (g.length == 2) {
                return Double.parseDouble(g[1]);
            }
        }
        catch (NumberFormatException e) {
            Log.d("MapHelper", "Number format exception: invalid longitude: " + e.getMessage());
        }
        return 0.0;

    }
}
