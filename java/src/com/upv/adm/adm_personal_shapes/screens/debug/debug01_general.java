package com.upv.adm.adm_personal_shapes.screens.debug;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.upv.adm.adm_personal_shapes.R;
import com.upv.adm.adm_personal_shapes.classes.BeanShape;
import com.upv.adm.adm_personal_shapes.classes.CustomListItem;
import com.upv.adm.adm_personal_shapes.classes.GlobalContext;
import com.upv.adm.adm_personal_shapes.classes.SQLite;
import com.upv.adm.adm_personal_shapes.classes.Utils;

import android.app.Activity;
import android.os.Bundle;

public class debug01_general extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.debug01_general);
		GlobalContext.setContext(getApplicationContext());
		SQLite.staticInitialization(getApplicationContext());
		

		ArrayList<String[]> array = new ArrayList<String[]>();
		array.add(new String[]{"t01", "Casa"});
		array.add(new String[]{"t02", "Banco"});
		array.add(new String[]{"t03", "Gimnasio"});
		
		boolean inside = Utils.isKeyInsideArrayList(array, "t14");
		
		System.out.println("hello world");

	}
}
