package com.upv.adm.adm_personal_shapes.screens;

import java.util.ArrayList;
import java.util.Hashtable;

import android.app.AlertDialog;
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
import com.upv.adm.adm_personal_shapes.classes.IListItem;
import com.upv.adm.adm_personal_shapes.classes.SQLite;
import com.upv.adm.adm_personal_shapes.classes.Utils;
import com.upv.adm.adm_personal_shapes.classes.WebServerProxy;

public class screen13 extends CustomActionBarActivity implements OnTouchListener {

	private TextView textview_pendingrequest_part2;
	
	private ListView listview_friendslist;
	
	private Button
			button_seerequests,
			button_send, 
			button_seeprofile, 
			button_remove, 
			button_searchfriends;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		GlobalContext.init(getApplicationContext());
		try {
			super.onCreate(savedInstanceState, R.layout.screen13);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		initControls();
	}
	
	@SuppressWarnings("unchecked")
	public void initControls() {

		textview_pendingrequest_part2 = (TextView) findViewById(R.id.edittext_pendingrequest);

		button_seerequests = (Button) findViewById(R.id.button_seerequests);
		button_send = (Button) findViewById(R.id.button_send);
		button_remove = (Button) findViewById(R.id.button_remove);
		button_searchfriends = (Button) findViewById(R.id.button_searchfriends);
		listview_friendslist = (ListView) findViewById(R.id.listview_friendslist);

		GlobalContext.username = "miriam";
		GlobalContext.password = "valencia";

		Hashtable<String, String> data = new Hashtable<String, String>();
		data.put("username", GlobalContext.username);
		data.put("password", GlobalContext.password);
		(new AsyncTask<Hashtable<String, String>, Void, ArrayList<ArrayList<IListItem>>>() {
			@Override
			protected void onPreExecute() {
			}
			@Override
			protected ArrayList<ArrayList<IListItem>> doInBackground(Hashtable<String, String>... data) {
				return WebServerProxy.retrieve_friends_list(data[0]);
			}
			@Override
			protected void onPostExecute(ArrayList<ArrayList<IListItem>> result) {
				if (result.size() == 3) {
					ArrayList<IListItem> friends_from = result.get(0);
					ArrayList<IListItem> friends_to = result.get(1);
					ArrayList<IListItem> friends_both = result.get(2);
					
					try {
						ArrayAdapter<IListItem> adapter_friends = new ArrayAdapter<IListItem>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, friends_both);
						listview_friendslist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
						listview_friendslist.setAdapter(adapter_friends);
						listview_friendslist.setBackgroundColor(0xFF7AAAFF);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).execute(data);
		
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
		
		ArrayList<String> selectedKeys = Utils.getSelectedKeys(listview_friendslist);

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
