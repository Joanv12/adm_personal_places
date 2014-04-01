package com.upv.adm.adm_personal_shapes.screens;

import com.upv.adm.adm_personal_shapes.R;
import com.upv.adm.adm_personal_shapes.classes.CustomActionBarActivity;
import com.upv.adm.adm_personal_shapes.classes.GlobalContext;
import com.upv.adm.adm_personal_shapes.classes.SQLite;

import android.os.Bundle;

//solicitudes pendientes de eventos

public class screen23 extends CustomActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState, R.layout.screen23);
		GlobalContext.setContext(getApplicationContext());
		SQLite.staticInitialization();

		initControls();
	}

	public void initControls() {

	}

}
