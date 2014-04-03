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
import com.upv.adm.adm_personal_shapes.classes.IListItem;
import com.upv.adm.adm_personal_shapes.classes.SQLite;
import com.upv.adm.adm_personal_shapes.classes.Utils;

public class screen19 extends CustomActionBarActivity {

	private EditText edittext_search;
	private CustomListItem[] shapesListItems;
	private ListView listview_shapes;
	private Button button_search;
	
	private ArrayAdapter<CustomListItem> adapter;
	private ArrayList<? extends IListItem> shapes;
	private Button button_searchplaces;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.screen19);
		GlobalContext.setContext(getApplicationContext());
		SQLite.staticInitialization();

		initControls();
	}
	
	private void initControls() {
		edittext_search = (EditText) findViewById(R.id.edittext_inputSearch);
		listview_shapes = (ListView) findViewById(R.id.listview_search);
		button_search = (Button) findViewById(R.id.button_search);
		
		button_search.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String query = edittext_search.getText().toString();
				shapes = SQLite.getPlaces("WHERE name LIKE '%" + query + "%'");
				shapesListItems = Utils.ArrayListToCustomListItemArray(shapes);
				adapter = new ArrayAdapter<CustomListItem>(screen19.this,android.R.layout.simple_list_item_1, shapesListItems);
				listview_shapes.setAdapter(adapter);
			} 
		});
		
		listview_shapes.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				GlobalContext.shape_id = Long.valueOf(shapesListItems[position].getKey());
				startActivity(new Intent(getApplicationContext(), screen05.class));

			}
		});
	}
}

