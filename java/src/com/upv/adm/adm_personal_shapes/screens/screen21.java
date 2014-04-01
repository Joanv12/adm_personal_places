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
			button_share, 
			button_use;
	
	private static EditText edittext_coordinates;
	public static String coords;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,R.layout.screen21);
		GlobalContext.setContext(getApplicationContext());
		SQLite.staticInitialization();

		initControls();
		fillFields ();
	}
	
	public void fillFields () {
		if (GlobalContext.shape_id != null) {
			ArrayList<BeanShape> shapes = (ArrayList<BeanShape>)SQLite.getShapes("WHERE id = " + GlobalContext.shape_id);
			if (shapes.size() == 1) {
				BeanShape shape = shapes.get(0);
				edittext_coordinates.setText(shape.getCoords());
			}
		}
	}
	public void initControls(){
	
		button_share = (Button) findViewById(R.id.button_send);
		button_use = (Button) findViewById(R.id.button_use);

		edittext_coordinates = (EditText) findViewById(R.id.edittext_coordinates);
		coords = edittext_coordinates.getText().toString();

		
		button_share.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	shareClick();
		    }
		});
		button_use.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	useClick();
		    }
		});
	}

	public void shareClick(){
		 //obtenemos los datos para el envío del correo 
        //es necesario un intent que levante la actividad deseada 
        Intent itSend = new Intent(android.content.Intent.ACTION_SEND);
 
        //itSend.setType("plain/text");
 
        //colocamos los datos para el envío
        itSend.putExtra(android.content.Intent.EXTRA_TEXT, edittext_coordinates.getText().toString());
        itSend.setType("text/plain");
        //iniciamos la actividad
        startActivity(itSend);
	}
	public void useClick(){
		startActivity(new Intent(getApplicationContext(), screen05.class));
	}
}

