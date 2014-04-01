package com.upv.adm.adm_personal_shapes.screens;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.upv.adm.adm_personal_shapes.R;
import com.upv.adm.adm_personal_shapes.classes.BeanShape;
import com.upv.adm.adm_personal_shapes.classes.CustomActionBarActivity;
import com.upv.adm.adm_personal_shapes.classes.CustomListItem;
import com.upv.adm.adm_personal_shapes.classes.GlobalContext;
import com.upv.adm.adm_personal_shapes.classes.SQLite;
import com.upv.adm.adm_personal_shapes.classes.Utils;

public class screen07 extends CustomActionBarActivity {
	

	private ListView list_places;
	private ListView list_plots;
	private ListView list_layers;
	
	protected CustomListItem[] options_places;
	protected CustomListItem[] options_layers;
	public boolean selections_placetypes[];

	public WebView webview_map;

	//protected CustomListItem[] options_plots;
	ArrayList<BeanShape> options_plots;
	
	ArrayAdapter<CustomListItem> arrayAdapter_places;
	ArrayAdapter<CustomListItem> arrayAdapter_layers;
	CheckedTextView  checkBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.screen07);
		
		initControls();
		//UtilsUpdateMap(webview_map); 
	}
	
	public void initControls() {
		
	options_places = GlobalContext.getTypesData();
	//options_layers = GlobalContext.get();
    checkBox = (CheckedTextView ) findViewById(R.id.text1);

	options_plots = (ArrayList<BeanShape>) SQLite.getPlots();

	webview_map = (WebView) findViewById(R.id.webview_map);
	webview_map.getSettings().setJavaScriptEnabled(true);
	 
	webview_map.setWebViewClient(new WebViewClient()); 
	 
	String url = "http://www.angeltools.tk/map.php";
	webview_map.loadUrl(url);
	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_screen07, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_new:
			Log.i("ActionBar", "New!");
			return true;
		case R.id.submenu_places:
			
			AlertDialog.Builder builder_places = new AlertDialog.Builder(this);
			builder_places.setTitle("Lugares");
			builder_places.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   
		        	   	ArrayList<String[]> all_types = Utils.getTypes(getApplicationContext());
		        	   	
						ArrayList<String> places_type = Utils.getSelectedKeys(list_places);
						
						
						for (int i = 0; i < all_types.size(); i++) { 
							
							
		           		}
						
						for (int i = 0; i < places_type.size(); i++){
							
							String place_type = places_type.get(i);
						
						}
						
								
						//SQLite.setPlaceTypeVisibility(place_type, visible)
						//UtilsUpdateMap(webview_map); // WebView webview_map
						//llamar también a la función anterior en el momento de carga de esta activity para que
						//al usuario se le muestre su mapa de la forma en que él lo tenía configurado de antes
		        		
		           }
		       });
			//builder_places.setMultiChoiceItems(type_places, selections_placetypes, new DialogSelectionClickHandler());
    		
			list_places = new ListView(this);
			
			ArrayAdapter<CustomListItem> arrayAdapter_places = new ArrayAdapter<CustomListItem>(this, android.R.layout.simple_list_item_multiple_choice, options_places);
			list_places.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			
			list_places.setAdapter(arrayAdapter_places);
			builder_places.setView(list_places);
			Dialog dialog_places = builder_places.create();
			dialog_places.show();
			
			return true;

		case R.id.submenu_plots:
			AlertDialog.Builder builder_plots = new AlertDialog.Builder(this);
			builder_plots.setTitle("Parcelas");
			builder_plots.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        		//ArrayList<BeanShape> plots = Utils.getSelectedKeys(list_plots);
		        		//a cambiar
		        	   
		           }
		       });
			
			list_plots = new ListView(this);
		
			ArrayAdapter<BeanShape> arrayAdapter_plots = new ArrayAdapter<BeanShape>(this, R.layout.debug07_listview, options_plots);
		
			list_plots.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			list_plots.setAdapter(arrayAdapter_plots);
			builder_plots.setView(list_plots);
			Dialog dialog_plots = builder_plots.create();
			dialog_plots.show();
			
			return true;
			
		case R.id.submenu_layers:

			AlertDialog.Builder builder_layers = new AlertDialog.Builder(this);
			builder_layers.setTitle("Lugares");
			builder_layers.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        		ArrayList<String> layers = Utils.getSelectedKeys(list_layers);	
		        		for (int i = 0; i < layers.size(); i++){
		        			
		        			String layer = layers.get(i);

		        		}
		           }
		       });
			
			list_layers = new ListView(this);
			
			list_layers.setFastScrollEnabled(true);
			list_layers.setTextFilterEnabled(true);
			
			
			ArrayAdapter<CustomListItem> arrayAdapter_layers = new ArrayAdapter<CustomListItem>(this, android.R.layout.simple_list_item_multiple_choice, options_layers);
			list_layers.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			list_layers.setAdapter(arrayAdapter_layers);
			builder_layers.setView(list_layers);
			Dialog dialog_layers = builder_layers.create();
			builder_layers.show();
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	
}
