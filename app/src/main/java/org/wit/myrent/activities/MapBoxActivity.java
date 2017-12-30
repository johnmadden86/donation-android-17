package org.wit.myrent.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import org.wit.android.helpers.MapHelper;
import org.wit.myrent.R;
import org.wit.myrent.app.MyRentApp;
import org.wit.myrent.models.Residence;

public  class       MapBoxActivity
        extends     Activity
        implements  OnMapReadyCallback,
                    MapboxMap.OnMarkerClickListener,
                    MapboxMap.OnMapLongClickListener {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private Marker residenceMarker;
    Long resId;
    Residence residence;
    LatLng residenceLatLng;
    MyRentApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapboxAccountManager.start(this, "pk.eyJ1Ijoiam9obm1hZGRlbjg2IiwiYSI6ImNqYmk1NHQzajNpbmwycW5vendwNDY0eDQifQ.NkQBPEGxQKXbWEg4kFD5XA");

        setContentView(R.layout.activity_mapbox);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        resId = (Long) getIntent().getSerializableExtra(ResidenceFragment.EXTRA_RESIDENCE_ID);
        app = (MyRentApp) getApplication();
        residence = app.portfolio.getResidence(resId);
        if (residence != null) {
            residenceLatLng = new LatLng(
                                            MapHelper.latitude(residence.geolocation),
                                            MapHelper.longitude(residence.geolocation)
                                        );
        }
    }

    private void setMarker() {
        MarkerViewOptions marker = new MarkerViewOptions().position(residenceLatLng);
        residenceMarker = mapboxMap.addMarker(marker);
    }

    private void positionCamera() {
        CameraPosition position = new CameraPosition.Builder()
                                                    .target(residenceLatLng) // Sets the new camera position
                                                    .zoom(residence.zoom) // Sets the zoom
                                                    .build(); // Creates a CameraPosition from the builder

        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        mapboxMap.setOnMarkerClickListener(this);
        mapboxMap.setOnMapLongClickListener(this);
        this.mapboxMap = mapboxMap;
        positionCamera();
        setMarker();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        residence.zoom = mapboxMap.getCameraPosition().zoom;
        residence.geolocation = MapHelper.latLng(residenceMarker.getPosition());
        // app.portfolio.updateResidence(residence);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        String snippet = "GPS : " + residenceMarker.getPosition();
        marker.setSnippet(snippet);
        return false;
    }

    @Override
    public void onMapLongClick(@NonNull LatLng point) {
        residenceMarker.setPosition(point);
    }
}
