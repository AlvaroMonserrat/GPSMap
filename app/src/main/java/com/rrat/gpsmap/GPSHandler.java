package com.rrat.gpsmap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationRequest;
import android.os.Build;
import android.os.Looper;
import android.util.Log;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;

import java.util.Map;

public class GPSHandler {
    private final ActivityResultLauncher<String[]> locationPermissionRequest;
    private final FusedLocationProviderClient fusedLocationClient;


    @SuppressLint("VisibleForTests")
    public GPSHandler(Context context, ActivityResultRegistry activityResultRegistry) {

        ActivityResultContracts.RequestMultiplePermissions activityContracts
                = new ActivityResultContracts.RequestMultiplePermissions();

        ActivityResultCallback<Map<String, Boolean>> activityResultCallback = result -> {
                Boolean fineLocationGranted = result.getOrDefault(
                        Manifest.permission.ACCESS_FINE_LOCATION, false);
                Boolean coarseLocationGranted = result.getOrDefault(
                        Manifest.permission.ACCESS_COARSE_LOCATION, false);
                if (fineLocationGranted != null && fineLocationGranted) {
                    // Precise location access granted.
                    Log.i("GPS_INFO", "Precise location access granted.");
                } else if (coarseLocationGranted != null && coarseLocationGranted) {
                    // Only approximate location access granted.
                    Log.i("GPS_INFO", "Only approximate location access granted.");
                } else {
                    // No location access granted.
                    Log.i("GPS_INFO", "No location access granted.");
                }

        };

        locationPermissionRequest = activityResultRegistry.register("GPS PERMISSION", activityContracts, activityResultCallback);

        fusedLocationClient = new FusedLocationProviderClient(context);

        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        locationPermissionRequest.launch(permissions);


    }

    public void getLastLocation(Context context, LocationListener locationListener)
    {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i("GPS_INFO", "ActivityCompat#requestPermissions.");
            String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
            locationPermissionRequest.launch(permissions);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            locationListener.onSuccess(location);
                            Log.i("GPS_INFO", String.valueOf(location.getLatitude()));
                        }else{
                            Log.i("GPS_INFO", "LOCATION NULL");
                            locationListener.onError();
                        }
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    public void getCurrentLocation(Context context, LocationListener locationListener)
    {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i("GPS_INFO", "ActivityCompat#requestPermissions.");
            String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
            locationPermissionRequest.launch(permissions);
            return;
        }

        fusedLocationClient.getCurrentLocation(LocationRequest.QUALITY_HIGH_ACCURACY, new CancellationToken() {
            @Override
            public boolean isCancellationRequested() {
                Log.i("GPS_INFO", "isCancellationRequested.");
                return false;
            }

            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                Log.i("GPS_INFO", "onCanceledRequested.");
                return null;
            }
        })
                .addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Log.i("GPS_INFO", String.valueOf(location.getLatitude()));
                            locationListener.onSuccess(location);
                        }else{
                            Log.i("GPS_INFO", "LOCATION NULL");
                            locationListener.onError();
                        }
                    }
                });
    }
}
