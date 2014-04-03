package com.upv.adm.adm_personal_shapes.screens;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.upv.adm.adm_personal_shapes.R;
import com.upv.adm.adm_personal_shapes.classes.CustomActionBarActivity;
import com.upv.adm.adm_personal_shapes.classes.GlobalContext;
import com.upv.adm.adm_personal_shapes.classes.SQLite;

public class screen22 extends CustomActionBarActivity{

	private Button 
			button_fixed,
			button_gps,
			button_delete;
	
	public WebView webview_map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.screen22);
		GlobalContext.setContext(getApplicationContext());
		SQLite.staticInitialization();
		
		//Utils.addBeanPlotToMap(plot,webview_map);
		initControls();
	}
	
	//UtilsAddBeanPlotToMap(BeanShape plot, WebView webview)
	
	public void initControls(){
		
		webview_map = (WebView) findViewById(R.id.mapview_map);
		webview_map.getSettings().setJavaScriptEnabled(true);
		 
		webview_map.setWebViewClient(new WebViewClient()); 
		 
		String url = "http://www.angeltools.tk/map.php";
		webview_map.loadUrl(url);
		
		
	}
}
