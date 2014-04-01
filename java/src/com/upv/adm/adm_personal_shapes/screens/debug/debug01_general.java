package com.upv.adm.adm_personal_shapes.screens.debug;

import java.util.List;

import com.upv.adm.adm_personal_shapes.R;
import com.upv.adm.adm_personal_shapes.classes.BeanShape;
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
		SQLite.staticInitialization(getApplicationContext());
		
		//BeanShape shape = new BeanShape((long) 15, "Cantina Mariachi Modificado", "Tapas y más tapas", "t01", "0.1234, 5.6789", null);
		//long result = SQLite.saveShape(shape);
		
		List<BeanShape> shapes = SQLite.getShapes("WHERE `id` > 10");
		
		
		System.out.println("hello world");

	}
}
