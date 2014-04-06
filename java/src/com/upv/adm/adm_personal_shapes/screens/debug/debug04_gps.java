package com.upv.adm.adm_personal_shapes.screens.debug;

import com.upv.adm.adm_personal_shapes.R;
import com.upv.adm.adm_personal_shapes.classes.GlobalContext;
import com.upv.adm.adm_personal_shapes.classes.IActivityGiver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class debug04_gps extends Activity implements IActivityGiver {

	private Button button_gps_turn_on;
	private Button button_gps_turn_off;
	private Button button_gps_read;
	private TextView textview_gps;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.debug04_gps);
		GlobalContext.init(getApplicationContext());
		
		button_gps_turn_on = (Button)findViewById(R.id.button_gps_turn_on);
		button_gps_turn_off = (Button)findViewById(R.id.button_gps_turn_off);
		button_gps_read = (Button)findViewById(R.id.button_gps_read);
		textview_gps = (TextView) findViewById(R.id.textview_gps);

		button_gps_turn_on.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				GlobalContext.getCustomLocationListener().enable();
			}
		});
		
		button_gps_turn_off.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				GlobalContext.getCustomLocationListener().disable();
			}
		});
		
		button_gps_read.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				double lat = GlobalContext.getCustomLocationListener().getLatitude();
				double lng = GlobalContext.getCustomLocationListener().getLongitude();
				if (lat == 0 && lng == 0) {
					AlertDialog ad = new AlertDialog.Builder(getCurrentActivity()).create();  
					ad.setCancelable(false);  
					ad.setMessage("No es posible obtener información del GPS. Por favor, asegúrese de que está correctamente configurado e inténtelo nuevamente. Si lo desea puede comprobar el GPS con la aplicación Google Maps.");  
					ad.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {  
					    @Override  
					    public void onClick(DialogInterface dialog, int which) {  
					        // whatever                      
					    }  
					});  
					ad.show();  
				}
				else {
					textview_gps.setText(lng + ", " + lat);
				}
			}
		});
		
		System.out.println("Nothing to show.");
	}

	@Override
	public Activity getCurrentActivity() {
		return this;
	}	

	@Override
	protected void onResume() {
		GlobalContext.getCustomLocationListener().enable();
		super.onResume();
	}

	@Override
	protected void onPause() {
		GlobalContext.getCustomLocationListener().disable();
		super.onPause();
	}


}
