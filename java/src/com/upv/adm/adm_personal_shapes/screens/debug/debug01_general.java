package com.upv.adm.adm_personal_shapes.screens.debug;

import com.upv.adm.adm_personal_shapes.R;
import com.upv.adm.adm_personal_shapes.classes.CustomListItem;
import com.upv.adm.adm_personal_shapes.classes.GlobalContext;
import com.upv.adm.adm_personal_shapes.classes.SQLite;

import android.app.Activity;
import android.os.Bundle;

public class debug01_general extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.debug01_general);
		GlobalContext.setContext(getApplicationContext());
		SQLite.staticInitialization();
		

		CustomListItem[] layers = GlobalContext.getLayersData();
		
		System.out.println("Nothing to show.");

	}
}
