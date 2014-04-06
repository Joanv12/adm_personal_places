package com.upv.adm.adm_personal_shapes.classes;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GPSListener {

	private double lat; 
	private double lng;
	private Activity activity;

	public GPSListener(Activity activity) {
		this.activity = activity;
		start();
	}
	
	public void start() {
		LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				lat = location.getLatitude();
				lng = location.getLongitude();
			}
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				System.out.println("onStatusChanged");
			}
			@Override
			public void onProviderEnabled(String provider) {
				System.out.println("onProviderEnabled");
			}
			@Override
			public void onProviderDisabled(String provider) {
				System.out.println("onProviderDisabled");
			}
		});
	}
	public double getLatitude() {
		return lat;
	}
	public double getLongitude() {
		return lng;
	}
}