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
	

	private ListView 
				list_places,
				list_plots,
				list_layers;
	
	private CustomListItem[] 
			options_places,
			options_plots,
			options_layers;

	private WebView webview_map;
	
	private AlertDialog.Builder 
						builder_places,
						builder_plots,
						builder_layers;
	private Dialog 
			dialog_places,
			dialog_plots,
			dialog_layers;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.screen07);
		GlobalContext.setContext(getApplicationContext());
		SQLite.staticInitialization();

		initControls();
		//UtilsUpdateMap(webview_map); 
	}
	
	public void checkVisiblePlaceTypes() {
		ArrayList<String> visible_types = SQLite.getVisiblePlaceTypes();
		for (int i = 0; i < list_places.getCount(); i++) {
			CustomListItem item = (CustomListItem)list_places.getItemAtPosition(i);
			if (visible_types.contains(item.getKey()))
				list_places.setItemChecked(i, true);
			else
				list_places.setItemChecked(i, false);
		}
	}
	
	public void checkVisiblePlots() {
		ArrayList<BeanShape> visible_plots = SQLite.getVisiblePlots();
		for (int i = 0; i < list_plots.getCount(); i++) {
			CustomListItem item = (CustomListItem)list_plots.getItemAtPosition(i);
			if (visible_plots.contains(item.getKey()))
				list_plots.setItemChecked(i, true);
			else
				list_plots.setItemChecked(i, false);
		}
	}
	
	public void checkVisibleLayers() {
		ArrayList<String> visible_layers = SQLite.getVisibleLayers();
		for (int i = 0; i < list_layers.getCount(); i++) {
			CustomListItem item = (CustomListItem)list_layers.getItemAtPosition(i);
			if (visible_layers.contains(item.getKey()))
				list_layers.setItemChecked(i, true);
			else
				list_layers.setItemChecked(i, false);
		}
	}
	
	public void initControls() {
		
		options_places = GlobalContext.getTypesData();
		options_plots = GlobalContext.getPlotsData();
		options_layers = GlobalContext.getLayersData();

		webview_map = (WebView) findViewById(R.id.webview_map);
		webview_map.getSettings().setJavaScriptEnabled(true);
		 
		webview_map.setWebViewClient(new WebViewClient()); 
		 
		String url = "http://www.angeltools.tk/map.php";
		webview_map.loadUrl(url);
		
		list_places = new ListView(this);
		ArrayAdapter<CustomListItem> arrayAdapter_places = new ArrayAdapter<CustomListItem>(this, android.R.layout.simple_list_item_multiple_choice, options_places);
		list_places.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		list_places.setAdapter(arrayAdapter_places);
		
		list_plots = new ListView(this);
		ArrayAdapter<CustomListItem> arrayAdapter_plots = new ArrayAdapter<CustomListItem>(this, android.R.layout.simple_list_item_multiple_choice, options_plots);
		list_plots.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		list_plots.setAdapter(arrayAdapter_plots);
		
		list_layers = new ListView(this);
		ArrayAdapter<CustomListItem> arrayAdapter_layers = new ArrayAdapter<CustomListItem>(this, android.R.layout.simple_list_item_multiple_choice, options_layers);
		list_layers.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		list_layers.setAdapter(arrayAdapter_layers);
		
		checkVisiblePlaceTypes();
		checkVisiblePlots();
		checkVisibleLayers();
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
			if (builder_places == null) {
				builder_places = new AlertDialog.Builder(this);
				builder_places.setTitle("Lugares");
				builder_places.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   	ArrayList<String[]> all_types = Utils.getTypes();
							ArrayList<String> selected_types = Utils.getSelectedKeys(list_places);
							for (int i = 0; i < all_types.size(); i++) { 
								if (selected_types.contains(all_types.get(i)[0]))
									SQLite.setPlaceTypeVisibility(all_types.get(i)[0], true);
								else
									SQLite.setPlaceTypeVisibility(all_types.get(i)[0], false);
			           		}
							//UtilsUpdateMap(webview_map); // WebView webview_map
							//llamar también a la función anterior en el momento de carga de esta activity para que
							//al usuario se le muestre su mapa de la forma en que él lo tenía configurado de antes	
			           }
			    });
				builder_places.setView(list_places);
				dialog_places = builder_places.create();
			}
			dialog_places.show();
			return true;
			
		case R.id.submenu_plots:
			if (builder_plots == null) {
				builder_plots = new AlertDialog.Builder(this);
				builder_plots.setTitle("Parcelas");
				builder_plots.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						
						ArrayList<BeanShape> all_plots = SQLite.getPlots();
			        	ArrayList<String> selected_plots = Utils.getSelectedKeys(list_plots);
			        	
			        	for (int i = 0; i < all_plots.size(); i++) { 
			        		if (selected_plots.contains(all_plots.get(i).getId()))
			        			SQLite.setPlotVisibility(String.valueOf(all_plots.get(i).getId()), true);
							else
			        			SQLite.setPlotVisibility(String.valueOf(all_plots.get(i).getId()), false);
			           		}
			           }
			       });
				builder_plots.setView(list_plots);
				dialog_plots = builder_plots.create();
			}
			dialog_plots.show();
			return true;
			
		case R.id.submenu_layers:
			if (builder_layers == null) {
				builder_layers = new AlertDialog.Builder(this);
				builder_layers.setTitle("Capas");
				builder_layers.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int id) {
						ArrayList<String[]> all_layers = Utils.getLayers(); // a cambiar
						ArrayList<String> selected_layers = Utils.getSelectedKeys(list_layers);
							for (int i = 0; i < all_layers.size(); i++) { 
								if (selected_layers.contains(all_layers.get(i)[0]))
									SQLite.setLayerVisibility(all_layers.get(i)[0], true);
								else
									SQLite.setLayerVisibility(all_layers.get(i)[0], false);
							}
						 }
			    });
				builder_layers.setView(list_layers);
				dialog_layers = builder_layers.create();
			}
			dialog_layers.show();
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	
}
