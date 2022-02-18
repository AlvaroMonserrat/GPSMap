package com.rrat.gpsmap;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView tvLatitud;
    TextView tvLongitud;

    GPSHandler gpsHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLatitud = findViewById(R.id.tv_text_latitud);
        tvLongitud = findViewById(R.id.tv_text_logitud);

        gpsHandler = new GPSHandler(this, this.getActivityResultRegistry());
    }


    public void onGetGPS(View view) {
        //getLastLocation();
        gpsHandler.getLastLocation(this, new LocationListener() {
            @Override
            public void onSuccess(Location location) {
                tvLatitud.setText(String.valueOf(location.getLatitude()));
                tvLongitud.setText(String.valueOf(location.getLongitude()));
            }

            @Override
            public void onError() {

            }
        });
    }



}