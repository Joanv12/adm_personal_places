package com.upv.adm.adm_personal_shapes.screens;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.upv.adm.adm_personal_shapes.R;
import com.upv.adm.adm_personal_shapes.classes.BeanShape;
import com.upv.adm.adm_personal_shapes.classes.BeanUser;
import com.upv.adm.adm_personal_shapes.classes.CustomActionBarActivity;
import com.upv.adm.adm_personal_shapes.classes.CustomListItem;
import com.upv.adm.adm_personal_shapes.classes.GlobalContext;
import com.upv.adm.adm_personal_shapes.classes.IListItem;
import com.upv.adm.adm_personal_shapes.classes.SQLite;
import com.upv.adm.adm_personal_shapes.classes.Utils;
import com.upv.adm.adm_personal_shapes.classes.WebServerProxy;

public class screen17 extends CustomActionBarActivity {

	private EditText edittext_name;

	private Button 
			button_searchfriends, 
			button_addfriends, 
			button_viewprofile;

	private ListView listview_users;
	private CustomListItem[] usersListItems;

	private ArrayAdapter<CustomListItem> adapter;
	
	private ProgressDialog pd;

	private String query;
	
	private void load_data_debug() {
		
		CustomListItem[] FriendsListItems = new CustomListItem[] {
			new CustomListItem("23", "Pedro"),
			new CustomListItem("14", "Susana"),
			new CustomListItem("52", "Tom�s"),
			new CustomListItem("12", "Melody"),
			new CustomListItem("74", "Andrea"),
			new CustomListItem("36", "Pepe"),
			new CustomListItem("61", "Ana"),
			new CustomListItem("22", "Pepe7"),
			new CustomListItem("82", "Susana"),
			new CustomListItem("28", "Pepe3"),
			new CustomListItem("83", "Mabel"),
			new CustomListItem("25", "Pepe9")
		};

		}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.screen17);

		initControls();
		load_data_debug();
	}

	private void initControls() {

		button_addfriends = (Button) findViewById(R.id.button_addfriend);
		button_viewprofile = (Button) findViewById(R.id.button_profile);
		listview_users = (ListView) findViewById(R.id.list_searchfriends);
		edittext_name = (EditText) findViewById(R.id.edittext_inputSearch);
		
		edittext_name.setOnClickListener(new OnClickListener() {
			public void onClick(View v) { 
				searchFriendsClick(); //  SQLite.searchFriends(edittext_name);
			}
		});

		button_addfriends.setOnClickListener(new OnClickListener() {
			public void onClick(View v) { 
				addFriendsClick(); 
				}
		});
		
		button_viewprofile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) { 
				viewProfileClick(); 
				}
		});

	}

	public void searchFriendsClick() {
		query = edittext_name.getText().toString();
		(new AsyncTask<String, Void, Boolean>() {
			@Override
			protected void onPreExecute() {
				pd = new ProgressDialog(getCurrentActivity());
				pd.setTitle("Comunicando con el servidor");
				pd.setMessage("Buscando...");
				pd.setCancelable(false);
				pd.setIndeterminate(true);
				pd.show();
			}
			@Override
			protected Boolean doInBackground(String... data) {
				//ArrayList<BeanUser> users = WebServerProxy.searchFriends(query);
				return true;
			}
			@Override
			protected void onPostExecute(Boolean result) {
				if (pd != null)
					pd.dismiss();
				if (result) {
					//usersListItems = Utils.ArrayListToCustomListItemArray(users);
					adapter = new ArrayAdapter<CustomListItem>(screen17.this,android.R.layout.simple_list_item_multiple_choice, usersListItems);
					listview_users.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
					listview_users.setAdapter(adapter);
				}
			}
		}).execute(query);
	}
	
	@SuppressWarnings("unchecked")
	public void addFriendsClick() {
		ArrayList<String> selectedKeys = Utils.getSelectedKeys(listview_users);
		String[] outputStrArr = new String[selectedKeys.size()];
		for (int i = 0; i < selectedKeys.size(); i++) {
			outputStrArr[i] = (String) selectedKeys.get(i);
		}

		if (selectedKeys.size() != 0) {
			(new AsyncTask<ArrayList<String>, Void, Boolean>() {
				@Override
				protected void onPreExecute() {
					pd = new ProgressDialog(getCurrentActivity());
					pd.setTitle("Comunicando con el servidor");
					pd.setMessage("Agregando amigo...");
					pd.setCancelable(false);
					pd.setIndeterminate(true);
					pd.show();
				}
				@Override
				protected Boolean doInBackground(ArrayList<String>... data) {
					WebServerProxy.add_friends(data[0]);
					return true;
				}
				@Override
				protected void onPostExecute(Boolean result) {
					if (pd != null)
						pd.dismiss();
					if (result) {
						// esta activity no se necesitar� m�s, por tanto la cerramos
						getCurrentActivity().finish(); 
						startActivity(new Intent(getApplicationContext(), screen13.class));
					}
				}
			}).execute(selectedKeys);
		}
		else {
			AlertDialog ad = new AlertDialog.Builder(getCurrentActivity()).create();
			ad.setCancelable(false);
			ad.setMessage("Ning�n amigo seleccionado. Por favor, selecciona alg�n amigo para a�adir.");
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
	
	public void viewProfileClick() {
		ArrayList<String> selectedKeys = Utils.getSelectedKeys(listview_users);
		String[] outputStrArr = new String[selectedKeys.size()];
		
		outputStrArr[0] = (String) selectedKeys.get(0);
		System.out.println("hola");
		startActivity(new Intent(getApplicationContext(), screen02.class));
		
	}
}
