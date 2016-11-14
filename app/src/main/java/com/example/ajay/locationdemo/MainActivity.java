package com.example.ajay.locationdemo;

import android.Manifest;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;


public class MainActivity extends MarshMallowSupportActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    //EnJoY
    private final int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 1;
    private Permission.PermissionBuilder permissionBuilder;
    private GoogleApiClient googleApiClient;
    private Location mCurrentLocation;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] CAMERA_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};
        Permission.PermissionCallback mPermissionCallback = new Permission.PermissionCallback() {

            @Override
            public void onPermissionGranted(int requestCode) {
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                if (googleApiClient == null) {
                    googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                            .addApi(LocationServices.API)
                            .addConnectionCallbacks(MainActivity.this)
                            .addOnConnectionFailedListener(MainActivity.this).build();
                    googleApiClient.connect();

                    final LocationRequest locationRequest = LocationRequest.create();
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    locationRequest.setInterval(10000);
                    locationRequest.setFastestInterval(5000);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);

                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            final LocationSettingsStates state = result.getLocationSettingsStates();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, MainActivity.this);
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied. But could be fixed by showing the user
                                    // a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        status.startResolutionForResult(
                                                MainActivity.this, 1000);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied. However, we have no way to fix the
                                    // settings so we won't show the dialog.
                                    break;
                            }
                        }
                    });
                }
            }

            @Override
            public void onPermissionDenied(int requestCode) {

            }

            @Override
            public void onPermissionAccessRemoved(int requestCode) {

            }
        };

        permissionBuilder =
                new Permission.PermissionBuilder(CAMERA_PERMISSIONS, MY_PERMISSIONS_REQUEST_ACCESS_LOCATION, mPermissionCallback);
        permissionBuilder.enableDefaultRationalDialog("Ration dialog title", "Ration Dialog message")
                .enableDefaultSettingDialog("Setting Dialog title", "Setting dialog message");
        requestAppPermissions(permissionBuilder.build());

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        Log.d(TAG, "onLocationChanged: lat " + mCurrentLocation.getLatitude());
        Log.d(TAG, "onLocationChanged: lat " + mCurrentLocation.getLongitude());
    }
}
