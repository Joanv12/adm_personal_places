package com.upv.adm.adm_personal_shapes.screens;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;

import com.upv.adm.adm_personal_shapes.R;
import com.upv.adm.adm_personal_shapes.classes.BeanShape;
import com.upv.adm.adm_personal_shapes.classes.CustomActionBarActivity;
import com.upv.adm.adm_personal_shapes.classes.CustomListItem;
import com.upv.adm.adm_personal_shapes.classes.GlobalContext;
import com.upv.adm.adm_personal_shapes.classes.SQLite;
import com.upv.adm.adm_personal_shapes.classes.Utils;

public class screen04 extends CustomActionBarActivity {

	private TabHost tabs;
	
	private ListView 
			listview_places,
			listview_plots,
			listview_typeplaces;
	
	protected CustomListItem[] options_places;

	
	private Button button_types;
	
	ArrayList<BeanShape> list_places;
	ArrayList<BeanShape> list_plots;
	
	protected boolean[] selections_placetypes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.screen04);

		initControls();
	}
	private void initControls() {
		
		
		
		options_places = GlobalContext.getTypesData();
		selections_placetypes = new boolean[options_places.length];

		screen04.this.setProgressBarIndeterminateVisibility(false);

		button_types = (Button) findViewById(R.id.button_types);

		tabs = (TabHost) findViewById(android.R.id.tabhost);
		tabs.setup();

		TabHost.TabSpec spec = tabs.newTabSpec(getResources().getString(R.string.tab_places));
		spec.setContent(R.id.tab_places);
		spec.setIndicator(getResources().getString(R.string.tab_places));
		tabs.addTab(spec);

		spec = tabs.newTabSpec(getResources().getString(R.string.tab_plots));
		spec.setContent(R.id.tab_plots);
		spec.setIndicator(getResources().getString(R.string.tab_plots));
		tabs.addTab(spec);

		tabs.setCurrentTab(0);

		listview_places = (ListView) findViewById(R.id.listview_places);
		listview_plots = (ListView) findViewById(R.id.listview_personalplots);

		list_places = (ArrayList<BeanShape>) SQLite.getPlaces();
		list_plots = (ArrayList<BeanShape>) SQLite.getPlots();
		
		ArrayAdapter<BeanShape> adapter_places = new ArrayAdapter<BeanShape>(getApplicationContext(), android.R.layout.simple_list_item_1, list_places);
		ArrayAdapter<BeanShape> adapter_plots = new ArrayAdapter<BeanShape>(getApplicationContext(), android.R.layout.simple_list_item_1, list_plots);
		
		listview_places.setAdapter(adapter_places);
		listview_plots.setAdapter(adapter_plots);

		listview_places.setBackgroundColor(Color.BLACK);
		listview_plots.setBackgroundColor(Color.BLACK);

		listview_places.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				GlobalContext.shape_id = list_places.get(position).getId();
				
				Intent in = new Intent(getApplicationContext(), screen05.class);startActivity(in);

			}
		});
		
		listview_plots.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				GlobalContext.shape_id = list_plots.get(arg2).getId();

				//screen05.radiobutton_plot.isActivated();
				
				Intent in = new Intent(getApplicationContext(), screen05.class);startActivity(in);
			}
		});
		
		button_types.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				typeplace_Click();			}
		});
	}

	public void typeplace_Click(){
		AlertDialog.Builder builder_places = new AlertDialog.Builder(this);
		builder_places.setTitle("Lugares");
		builder_places.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        		ArrayList<String> type_places = Utils.getSelectedKeys(listview_typeplaces);	
	        		//UtilsSetVisiblePlaceTypes(type_places)
	        		//UtilsUpdateMap(webview_map); // WebView webview_map
	        		//llamar también a la función anterior en el momento de carga de esta activity para que
	        		//al usuario se le muestre su mapa de la forma en que él lo tenía configurado de antes
	        		
	           }
	       });
		//builder_places.setMultiChoiceItems(type_places, selections_placetypes, new DialogSelectionClickHandler());
		
		
		listview_typeplaces = new ListView(this);
		
		ArrayAdapter<CustomListItem> arrayAdapter_places = new ArrayAdapter<CustomListItem>(this, android.R.layout.simple_list_item_multiple_choice, options_places);
		listview_typeplaces.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		listview_typeplaces.setAdapter(arrayAdapter_places);
		builder_places.setView(listview_typeplaces);
		Dialog dialog_places = builder_places.create();
		dialog_places.show();
	}
}
