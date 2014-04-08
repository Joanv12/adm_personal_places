package com.upv.adm.adm_personal_shapes.screens.debug;

import java.util.ArrayList;
import java.util.Hashtable;
import com.upv.adm.adm_personal_shapes.R;
import com.upv.adm.adm_personal_shapes.classes.GlobalContext;
import com.upv.adm.adm_personal_shapes.classes.IListItem;
import com.upv.adm.adm_personal_shapes.classes.WebServerProxy;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

public class debug01_general extends Activity {

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.debug01_general);
		GlobalContext.init(getApplicationContext());

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
				
				System.out.println("hello world");
			}
		}).execute(data);
		
		System.out.println("hello world");
	}

}
