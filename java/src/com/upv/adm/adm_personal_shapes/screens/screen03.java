package com.upv.adm.adm_personal_shapes.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.upv.adm.adm_personal_shapes.R;
import com.upv.adm.adm_personal_shapes.classes.CustomActionBarActivity;
import com.upv.adm.adm_personal_shapes.classes.GlobalContext;

public class screen03 extends CustomActionBarActivity {

	private Button 
			button_profile, 
			button_create, 
			button_list, 
			button_map,
			button_friends, 
			button_events, 
			button_search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.screen03);
		initControls();
	}
	private void initControls() {
		
		button_profile = (Button) findViewById(R.id.button_profile);
		button_create = (Button) findViewById(R.id.button_create);
		button_list = (Button) findViewById(R.id.button_list);
		button_map = (Button) findViewById(R.id.button_map);
		button_friends = (Button) findViewById(R.id.button_friends);
		button_events = (Button) findViewById(R.id.button_events);
		button_search = (Button) findViewById(R.id.button_search);

		button_profile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), screen02.class));
			}
		});

		button_list.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), screen04.class));
			}
		});

		button_create.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				GlobalContext.shape_id = null;
				startActivity(new Intent(getApplicationContext(), screen05.class));
			}
		});
		button_search.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), screen19.class));
			}
		});

		button_friends.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), screen13.class));
			}
		});

		button_map.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), screen07.class));
			}
		});

		button_events.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), screen12.class));
			}
		});

	}

}
