package com.example.jay.finddoc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FindDoctorActivity extends FragmentActivity implements OnMapReadyCallback, ServerCallAsyncTask.OnAsyncJSONResponse, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int DOCTOR = 1;
    private GoogleMap mMap;
    ArrayList<Doctor> doctorArrayList;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_doctor);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        String url = ServerCallAsyncTask.BASE_URL + "find_admin_doc";
        HashMap<String, String> hashMap = new HashMap<String, String>();
        ServerCallAsyncTask asyncTask = new ServerCallAsyncTask(FindDoctorActivity.this, url, hashMap, DOCTOR);
        asyncTask.execute();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        /*LatLng vadoli = new LatLng(21.028,73.083);
        mMap.addMarker(new MarkerOptions().position(vadoli).title("Vadoli"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(vadoli));

        LatLng vadoli1 = new LatLng(21,73);
        mMap.addMarker(new MarkerOptions().position(vadoli1).title("Vadoli1"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(vadoli1))*/
        ;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    void addMarker(LatLng location, String title) {
        //LatLng vadoli = new LatLng(21.028,73.083);
        mMap.addMarker(new MarkerOptions().position(location).title(title));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }

    void showMyLocation(LatLng location, String title) {
        mMap.addMarker(new MarkerOptions().position(location).title(title));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }


    @Override
    public void asyncGetSMSResponse(String response, int flag) {
        if (flag == DOCTOR) {
            if (response.length() > 0) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("DocAll");
                    doctorArrayList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject doctorJson = jsonArray.getJSONObject(i);
                        Doctor doctor = new Doctor();
                        doctor.setFirstName(doctorJson.getString("fname"));
                        doctor.setLastName(doctorJson.getString("lname"));
                        doctor.setLat(doctorJson.getString("latitude"));
                        doctor.setLng(doctorJson.getString("longitude"));
                        doctorArrayList.add(doctor);
                        if(doctor.getLat().equals("") || doctor.getLng().equals(""))
                        {

                        }else {
                            double lat = Double.parseDouble(doctor.getLat());
                            double lng = Double.parseDouble(doctor.getLng());
                            addMarker(new LatLng(lat, lng), doctor.getFirstName() + " " + doctor.getLastName());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(this, "Invalid !!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if(mLastLocation!=null) {
            showMyLocation(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLatitude()), "I'm Here");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
