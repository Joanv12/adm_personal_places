package com.upv.adm.adm_personal_shapes.screens;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.upv.adm.adm_personal_shapes.R;
import com.upv.adm.adm_personal_shapes.classes.BeanShape;
import com.upv.adm.adm_personal_shapes.classes.CustomActionBarActivity;
import com.upv.adm.adm_personal_shapes.classes.CustomListItem;
import com.upv.adm.adm_personal_shapes.classes.GlobalContext;
import com.upv.adm.adm_personal_shapes.classes.SQLite;

public class screen19 extends CustomActionBarActivity {

	private EditText edittext_search;
	
	private ListView listview_places;

	ArrayAdapter<String> adapter;
	ArrayList<BeanShape> list = new ArrayList<BeanShape>();
	
	BeanShape place;

	private Button button_searchplaces;
	
	private void load_data_debug() {
		
		CustomListItem[] FriendsListItems = new CustomListItem[] {
			new CustomListItem("23", "Pedro"),
			new CustomListItem("14", "Susana"),
			new CustomListItem("52", "Tomás"),
			new CustomListItem("12", "Melody"),
			new CustomListItem("74", "Andrea"),
			new CustomListItem("36", "Pepe"),
			new CustomListItem("61", "Ana"),
			new CustomListItem("22", "Pepe7"),
			new CustomListItem("27", "Pepe2"),
			new CustomListItem("28", "Pepe3"),
			new CustomListItem("29", "Pepe4"),
			new CustomListItem("25", "Pepe9")
		};
		
		ArrayAdapter<CustomListItem> adapter = new ArrayAdapter<CustomListItem>(this,android.R.layout.simple_list_item_1, FriendsListItems);
		listview_places.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listview_places.setAdapter(adapter);
	}

		
	public void onCreate(Bundle savedInstanceState) {
		GlobalContext.init(getApplicationContext());
		super.onCreate(savedInstanceState, R.layout.screen19);
		initControls();
	}
	
	private void initControls() {
		
		edittext_search = (EditText) findViewById(R.id.edittext_inputSearch);
		button_searchplaces = (Button) findViewById(R.id.button_searchfriends);
		listview_places = (ListView) findViewById(R.id.listview_search);

		listview_places.setBackgroundColor(Color.BLACK);
		
		button_searchplaces.setOnClickListener(new OnClickListener() {
			public void onClick(View v) { 
				//searchshapeClick(edittext_search); 
			} // por cambiar
		});
		
		
		listview_places.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				

				Intent in = new Intent(getApplicationContext(), screen05.class);


				startActivity(in);

			}
		});
	}
}

