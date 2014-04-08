package com.upv.adm.adm_personal_shapes.screens;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.upv.adm.adm_personal_shapes.R;
import com.upv.adm.adm_personal_shapes.classes.BeanUser;
import com.upv.adm.adm_personal_shapes.classes.CustomActionBarActivity;
import com.upv.adm.adm_personal_shapes.classes.GlobalContext;
import com.upv.adm.adm_personal_shapes.classes.SQLite;
import com.upv.adm.adm_personal_shapes.classes.Utils;

public class screen13 extends CustomActionBarActivity implements OnTouchListener {

	private TextView textview_pendingrequest_part1;
	private TextView textview_pendingrequest_part2;
	
	private ListView listview_friends;
	
	private Button 
			button_send, 
			button_seeprofile, 
			button_delete, 
			button_searchfriends;


	ArrayList<BeanUser> list_friends = new ArrayList<BeanUser>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		GlobalContext.init(getApplicationContext());
		super.onCreate(savedInstanceState, R.layout.screen13);
		initControls();
	}
	
	public void initControls(){

		textview_pendingrequest_part1 = (TextView) findViewById(R.id.edittext_pendingrequest);

		button_send = (Button) findViewById(R.id.button_send);
		button_delete = (Button) findViewById(R.id.button_profile);
		button_searchfriends = (Button) findViewById(R.id.button_addfriend);
		listview_friends = (ListView) findViewById(R.id.listview_requestfriends);

		// bind listeners
		textview_pendingrequest_part1.setOnTouchListener(this);

		ArrayAdapter<BeanUser> adapter_friends = new ArrayAdapter<BeanUser>(getApplicationContext(), android.R.layout.simple_list_item_1, list_friends);

		listview_friends.setAdapter(adapter_friends);
		listview_friends.setBackgroundColor(Color.BLACK);

		button_searchfriends.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), screen17.class));
			}
		});
		button_send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				share_Click();
			}
		});
		button_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// check which textview it is and do what you need to do

		startActivity(new Intent(getApplicationContext(), screen18.class));

		// return true if you don't want it handled by any other touch/click
		// events after this

		return true;
	}
	
	public void share_Click(){
		
		ArrayList<String> selectedKeys = Utils.getSelectedKeys(listview_friends);

		String[] outputStrArr = new String[selectedKeys.size()];

		for (int i = 0; i < selectedKeys.size(); i++) {
			outputStrArr[i] = (String) selectedKeys.get(i);
		}

		if (selectedKeys.size() != 0) {
			
			
			
			
			
			
		
		}
		else {
			AlertDialog ad = new AlertDialog.Builder(getCurrentActivity()).create();
			ad.setCancelable(false);
			ad.setMessage("Ningún amigo seleccionado. Por favor, selecciona algún amigo para compartir.");
			ad.setButton(DialogInterface.BUTTON_NEUTRAL, "OK",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}
			);
			ad.show();
		}
		
		
	}
}
