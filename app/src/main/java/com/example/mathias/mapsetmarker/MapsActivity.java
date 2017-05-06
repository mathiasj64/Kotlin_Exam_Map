package com.example.mathias.mapsetmarker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final LatLng Coordinate = new LatLng(55, 30);

    //En permission kode som vi sætter for fine location. Hvis vi havde andre permission ville de have en anden kode.
    //Disse koder vil blive brugt i switch-statement efter brugeren har trykket allow eller deny til en permission.
    private final static int MY_PERMISSION_FINE_LOCATION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.addMarker(new MarkerOptions().position(Coordinate).title("testMarker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Coordinate, 13));

        //Efter API 23 skal have tilladelse til at få fat i brugerens lokation, fordi det anses for at være en "dangerouse permission".
        //Tjekker om der er blevet givet tilladelse til FINE_LOCATION

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            mMap.setMyLocationEnabled(true);

        }
        //Hvis ikke tjekkes API versionen
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION);
            }
        }

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    //Denne callback bliver kørt når brugeren har trykket enten allow eller deny til location
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            //Hvis requestCode er finelocation (101)
            case MY_PERMISSION_FINE_LOCATION:
                //Hvis vi får permission granted til FINE_LOCATION. Herefter bliver MyLocationEnabled sat til true.
                //Hvilket gør at vi kan se brugerens nuværende position med den blå prik på kortet.
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        mMap.setMyLocationEnabled(true);
                    }
                }
                //Hvis vi ikke få permission granted til FINE_LOCATION.
                //Der bliver toastet en besked og appen bliver stoppet.
                else {
                    Toast.makeText(getApplicationContext(), "This application requiers location permissions", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }

}
