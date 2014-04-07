package com.upv.adm.adm_personal_shapes.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.upv.adm.adm_personal_shapes.R;
import com.upv.adm.adm_personal_shapes.classes.CustomActionBarActivity;
import com.upv.adm.adm_personal_shapes.classes.GlobalContext;
import com.upv.adm.adm_personal_shapes.classes.Utils;

public class screen03 extends CustomActionBarActivity {

	private Button 
			button_login,
			button_register,
			button_profile,
			button_create, 
			button_list, 
			button_map,
			button_friends, 
			button_search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		GlobalContext.init(getApplicationContext());
		super.onCreate(savedInstanceState, R.layout.screen03);
		initControls();
	}
	private void initControls() {
		
		button_login = (Button) findViewById(R.id.button_login);
		button_register = (Button) findViewById(R.id.button_register);
		button_profile = (Button) findViewById(R.id.button_profile);
		button_create = (Button) findViewById(R.id.button_create);
		button_list = (Button) findViewById(R.id.button_list);
		button_map = (Button) findViewById(R.id.button_map);
		button_friends = (Button) findViewById(R.id.button_friends);
		button_search = (Button) findViewById(R.id.button_search);

		if (!Utils.isUserLoggedIn()) {
			button_register.setText(Utils.str(R.string.button_register));
			button_profile.setVisibility(View.GONE);
		}
		else {
			button_login.setVisibility(View.GONE);
			button_register.setVisibility(View.GONE);
		}
		
		button_login.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Utils.startActivity(getCurrentActivity(), screen01.class);
			}
		});

		button_register.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Utils.startActivity(getCurrentActivity(), screen02.class);
			}
		});

		button_profile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Utils.startActivity(getCurrentActivity(), screen02.class);
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

	}

}
