package com.upv.adm.adm_personal_shapes.screens;

import java.util.ArrayList;
import java.util.Collection;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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
import com.upv.adm.adm_personal_shapes.classes.CustomListItem;
import com.upv.adm.adm_personal_shapes.classes.GlobalContext;
import com.upv.adm.adm_personal_shapes.classes.SQLite;
import com.upv.adm.adm_personal_shapes.classes.Utils;

public class screen13 extends CustomActionBarActivity implements OnTouchListener {

	private Button 
			button_share, 
			button_profile, 
			button_addfriend;

	private TextView edittext_pendingrequest;
	
	private ListView listview_friends;
	private ProgressDialog pd;
	
	ArrayList list_friends = new ArrayList();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.screen13);

		initControls();
	}
	
	@SuppressWarnings("unchecked")
	public void initControls(){

		edittext_pendingrequest = (TextView) findViewById(R.id.edittext_pendingrequest);

		button_share = (Button) findViewById(R.id.button_send);
		button_profile = (Button) findViewById(R.id.button_profile);
		button_addfriend = (Button) findViewById(R.id.button_addfriend);
		listview_friends = (ListView) findViewById(R.id.listview_requestfriends);

		// bind listeners
		edittext_pendingrequest.setOnTouchListener(this);

		
		button_addfriend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), screen17.class));
			}
		});
		button_share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				share_Click();
			}
		});
		button_profile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});

		list_friends.add("element_1");
		list_friends.add("element_2");
		list_friends.add("element_3");
		list_friends.add("element_4");
		list_friends.add("element_5");
		
		(new AsyncTask<ArrayList<BeanUser>, Void, Boolean>() {
			@Override
			protected void onPreExecute() {
				pd = new ProgressDialog(getCurrentActivity());
				pd.setTitle("Comunicando con el servidor");
				pd.setMessage("Importando amigos...");
				pd.setCancelable(false);
				pd.setIndeterminate(true);
				pd.show();
			}
			@Override
			protected Boolean doInBackground(ArrayList<BeanUser>... data) {
				//WebServerProxy.searchFriends(list_friends);
				return true;
			}
			@Override
			protected void onPostExecute(Boolean result) {
				if (pd != null)
					pd.dismiss();
				if (result) {
					
					//usersListItems = Utils.ArrayListToCustomListItemArray(users);
					
					ArrayAdapter adapter_friends = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice,list_friends);
					listview_friends.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
					listview_friends.setAdapter(adapter_friends);

				}
			}
		}).execute(list_friends);
		
		
		
		
	}

	
	
	
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {

		startActivity(new Intent(getApplicationContext(), screen18.class));

		

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
