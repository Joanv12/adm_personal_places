package com.upv.adm.adm_personal_shapes.screens;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.upv.adm.adm_personal_shapes.R;
import com.upv.adm.adm_personal_shapes.classes.BeanShape;
import com.upv.adm.adm_personal_shapes.classes.CustomActionBarActivity;
import com.upv.adm.adm_personal_shapes.classes.GlobalContext;
import com.upv.adm.adm_personal_shapes.classes.SQLite;

public class screen21 extends CustomActionBarActivity {

	private Button
		button_use,
		button_share; 
	
	private static EditText edittext_coordinates;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.screen21);

		initControls();
		fillFields ();
	}
	
	public void initControls() {
	
		button_share = (Button) findViewById(R.id.button_share);
		button_use = (Button) findViewById(R.id.button_use);

		edittext_coordinates = (EditText) findViewById(R.id.edittext_coordinates);
		edittext_coordinates.setText(screen22.coords);

		button_use.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	screen22.coords = edittext_coordinates.getText().toString();
		    	useClick();
		    }
		});

		button_share.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	shareClick();
		    }
		});
	}

	public void fillFields () {
		if (screen22.coords != null)
			edittext_coordinates.setText(screen22.coords);
	}

	public void useClick() {
		screen22.coords = edittext_coordinates.getText().toString();
		this.finish();
	}

	public void shareClick(){
        Intent itSend = new Intent(android.content.Intent.ACTION_SEND);
        itSend.putExtra(android.content.Intent.EXTRA_TEXT, edittext_coordinates.getText().toString());
        itSend.setType("text/plain");
        startActivity(itSend);
	}
}

