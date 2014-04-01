package com.upv.adm.adm_personal_shapes.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

import com.upv.adm.adm_personal_shapes.R;
import com.upv.adm.adm_personal_shapes.classes.CustomActionBarActivity;
import com.upv.adm.adm_personal_shapes.classes.GlobalContext;
import com.upv.adm.adm_personal_shapes.classes.SQLite;

public class screen12 extends CustomActionBarActivity implements OnTouchListener {

	private Button button_newevent;
	
	private TextView edit_pendingrequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,R.layout.screen12);
		GlobalContext.setContext(getApplicationContext());
		SQLite.staticInitialization();

		initControls();
	}
	
	public void initControls(){
		
		edit_pendingrequest = (TextView) findViewById(R.id.edittext_pendingrequest);
		button_newevent = (Button) findViewById(R.id.button_newevent);

		// bind listeners
		edit_pendingrequest.setOnTouchListener(this); 
		
		button_newevent.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			startActivity(new Intent(getApplicationContext(), screen15.class));

		}
	});
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		startActivity(new Intent(getApplicationContext(), screen23.class));
return true;
	}
}
