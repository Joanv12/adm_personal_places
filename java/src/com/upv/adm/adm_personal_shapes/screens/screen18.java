package com.upv.adm.adm_personal_shapes.screens;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.upv.adm.adm_personal_shapes.R;
import com.upv.adm.adm_personal_shapes.classes.CustomActionBarActivity;
import com.upv.adm.adm_personal_shapes.classes.CustomListItem;
import com.upv.adm.adm_personal_shapes.classes.GlobalContext;
import com.upv.adm.adm_personal_shapes.classes.SQLite;
import com.upv.adm.adm_personal_shapes.classes.Utils;

public class screen18 extends CustomActionBarActivity {

	private Button 
			button_profile, 
			button_reject, 
			button_addfriends;
	
	private ListView requestfriends;
	
	//private Object[] requestfriends_Data;
	
	//ArrayAdapter<String> arrayAdapter_requestfriends;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.screen18);
		GlobalContext.setContext(getApplicationContext());
		SQLite.staticInitialization();

		initControls();
	}
	
	public void initControls(){
		
		button_profile = (Button) findViewById(R.id.button_profile);
		button_addfriends = (Button) findViewById(R.id.button_acept);
		button_reject = (Button) findViewById(R.id.button_delete);
		requestfriends = (ListView) findViewById(R.id.listview_requestfriends);

		CustomListItem[] requestFriendsListItems = new CustomListItem[] {
				new CustomListItem("23", "Pedro"),
				new CustomListItem("14", "Susana"),
				new CustomListItem("52", "Tomás"),
				new CustomListItem("12", "Melody"),
				new CustomListItem("74", "Andrea"),
				new CustomListItem("36", "Pepe"),
				new CustomListItem("61", "Ana"),
				new CustomListItem("22", "César"), };
		
		ArrayAdapter<CustomListItem> arrayAdapter_requestfriends = new ArrayAdapter<CustomListItem>(
				this, android.R.layout.simple_list_item_multiple_choice,requestFriendsListItems);

		requestfriends.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		requestfriends.setAdapter(arrayAdapter_requestfriends);

		button_addfriends.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addFriendClick();
			}
		});
	}
	
	public void addFriendClick() {
		// arraylist de las solicitudes de amigos seleccionados: selectedKeys 
		ArrayList selectedKeys = Utils.getSelectedKeys(requestfriends);

        String[] outputStrArr = new String[selectedKeys.size()];
		 
        for (int i = 0; i < selectedKeys.size(); i++) {
            outputStrArr[i] = (String) selectedKeys.get(i);
        }
		
		startActivity(new Intent(getApplicationContext(), screen13.class));
	}
}
