package com.upv.adm.adm_personal_shapes.screens;

import java.net.URLEncoder;
import java.util.Properties;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.view.View.OnClickListener;
import com.upv.adm.adm_personal_shapes.R;
import com.upv.adm.adm_personal_shapes.classes.CustomActionBarActivity;
import com.upv.adm.adm_personal_shapes.classes.GlobalContext;
import com.upv.adm.adm_personal_shapes.classes.Utils;

public class screen22 extends CustomActionBarActivity{

	private Button 
			button_gps,
			button_fix,
			button_delete;
	
	public WebView webview_map;
	
	public static boolean isPlace;
	public static String coords;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		GlobalContext.init(getApplicationContext());
		super.onCreate(savedInstanceState, R.layout.screen22);
		initControls();
		
		if (isPlace) {
			button_fix.setVisibility(View.GONE);
			button_delete.setVisibility(View.GONE);
		}
	}
	
	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	public void initControls(){
		
		webview_map = (WebView) findViewById(R.id.mapview_map);
		webview_map.getSettings().setJavaScriptEnabled(true);
		webview_map.addJavascriptInterface(this, "JavascriptManager");
		webview_map.setWebViewClient(new WebViewClient());

	    String query = "";
	    try {
	    	query = "?isplace="+(isPlace?"true":"false")+"&coords="+URLEncoder.encode(coords, "ISO-8859-1");
	    }
	    catch(Exception e) {
	    	e.printStackTrace();
	    }
		
		Properties props = Utils.getCustomProperties();
		String url = ((String)props.get("server_url")) + "/screen22.php" + query;
		webview_map.loadUrl(url);
		
		button_gps = (Button) findViewById(R.id.button_gps);
		button_fix = (Button) findViewById(R.id.button_fix);
		button_delete = (Button) findViewById(R.id.button_delete);

		button_fix.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utils.fixLastPoint(webview_map);
			}
		});
		
		button_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utils.deleteLastPoint(webview_map);
			}
		});
		
		button_gps.setOnClickListener(new OnClickListener() {
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
					Utils.placeMarker(webview_map, lat, lng);
				}
			}
		});

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

	@JavascriptInterface
    public void setDataFromJavaScript(String data) {
		coords = data;
    }
	
}
