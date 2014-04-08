package com.upv.adm.adm_personal_shapes.screens.debug;

import java.io.File;
import java.io.FileOutputStream;

import com.upv.adm.adm_personal_shapes.R;
import com.upv.adm.adm_personal_shapes.classes.BeanShape;
import com.upv.adm.adm_personal_shapes.classes.GlobalContext;
import com.upv.adm.adm_personal_shapes.classes.SQLite;
import com.upv.adm.adm_personal_shapes.classes.Utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

public class debug01_general extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.debug01_general);
		GlobalContext.init(getApplicationContext());

		BeanShape shape = new BeanShape(null, "Nueva Parcela", "Esta es la nueva parcela", null, "1,2", true);
		
		SQLite.saveShape(shape);
		
		System.out.println("Nothing to show.");

	}

}
