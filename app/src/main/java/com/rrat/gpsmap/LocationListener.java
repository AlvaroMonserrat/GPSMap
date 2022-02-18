package com.rrat.gpsmap;

import android.location.Location;

public interface LocationListener {
    void onSuccess(Location location);
    void onError();
}
