package com.upv.adm.adm_personal_shapes.classes;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.widget.Toast;

public class CustomLocationListener implements LocationListener {
	
	private LocationManager lm;
	private Context context;

	private double lat;
	private double lng;
	
	public CustomLocationListener() {
		context = GlobalContext.getContext();
		lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	}

	@Override
	public void onLocationChanged(Location location) {
		lng = location.getLongitude();
		lat = location.getLatitude();
	}

	@Override
	public void onProviderDisabled(String provider) {
		try {
			Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(context, "GPS Enabled", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		switch (status) {
			case LocationProvider.OUT_OF_SERVICE:
				Toast.makeText(context, "GPS status Changed: Out of Service", Toast.LENGTH_SHORT).show();
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				Toast.makeText(context, "GPS status Changed: Temporarily Unavailable", Toast.LENGTH_SHORT).show();
				break;
			case LocationProvider.AVAILABLE:
				Toast.makeText(context, "GPS status Changed: Available", Toast.LENGTH_SHORT).show();
				break;
		}
	}
	
	public void enable() {
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
	}

	public void disable() {
		lm.removeUpdates(this);
	}


	public double getLatitude() {
		return lat;
	}
	
	public double getLongitude() {
		return lng;
	}
	
}
