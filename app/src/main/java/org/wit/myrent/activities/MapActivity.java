package org.wit.myrent.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.wit.android.helpers.MapHelper;
import org.wit.myrent.R;
import org.wit.myrent.app.MyRentApp;
import org.wit.myrent.models.Residence;

import static org.wit.android.helpers.IntentHelper.navigateUp;

public  class       MapActivity
        extends     AppCompatActivity
        implements  OnMapReadyCallback,
                    GoogleMap.OnMarkerDragListener,
                    GoogleMap.OnInfoWindowClickListener,
                    GoogleMap.OnCameraIdleListener,
                    GoogleMap.OnMarkerClickListener {

    /*
   * We use the current residence when navigating back to parent class - ResidenceFragment as
   * this is required in ResidenceFragment onCreate.
   */
    Long resId;
    Residence residence; // The residence associated with this map pane
    MyRentApp app;
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        resId = (Long) getIntent()
                .getSerializableExtra(ResidenceFragment.EXTRA_RESIDENCE_ID);
        app = (MyRentApp) getApplication();
        residence = app.portfolio.getResidence(resId);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng latLng = MapHelper.latLng(this, residence.geolocation);
        map.addMarker(new MarkerOptions()
                        .title("Residence")
                        .snippet("GPS: " + latLng.toString())
                        .draggable(true)
                        .position(latLng)
        );

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, (float) residence.zoom));
        map.setOnInfoWindowClickListener(this);
        map.setOnMarkerClickListener(this);
        map.setOnMarkerDragListener(this);
        map.setOnCameraIdleListener(this);
        this.map = map;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigateUp(this, ResidenceFragment.EXTRA_RESIDENCE_ID, resId);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    /**
     * When marker drag ends, save Residence model geolocation and zoom.
     * @param marker The map marker representing current residence geolocation.
     */
    @Override
    public void onMarkerDragEnd(Marker marker) {
        residence.geolocation = MapHelper.latLng(marker.getPosition());
        residence.zoom = map.getCameraPosition().zoom;
        map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        marker.hideInfoWindow();
    }

    @Override
    public void onCameraIdle() {
        residence.zoom = map.getCameraPosition().zoom;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        LatLng latLng = MapHelper.latLng(this, residence.geolocation);
        marker.setSnippet("GPS : " + latLng.toString());
        return false;
    }
}
