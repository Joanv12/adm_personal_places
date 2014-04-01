package com.upv.adm.adm_personal_shapes.screens.debug;

import java.util.ArrayList;

import com.upv.adm.adm_personal_shapes.R;
import com.upv.adm.adm_personal_shapes.classes.BeanShape;
import com.upv.adm.adm_personal_shapes.classes.GlobalContext;
import com.upv.adm.adm_personal_shapes.classes.SQLite;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

@SuppressLint("SetJavaScriptEnabled")
public class debug03_elementsvisibility extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.debug03_elementsvisibility);
		GlobalContext.setContext(getApplicationContext());
		SQLite.staticInitialization(getApplicationContext());

		ArrayList<BeanShape> visible_places = SQLite.getVisiblePlaces();
		ArrayList<BeanShape> visible_plots = SQLite.getVisiblePlots();
		ArrayList<String> visible_layers = SQLite.getVisibleLayers();
		
		boolean a = SQLite.isVisiblePlaceType("t01");
		SQLite.setPlaceTypeVisibility("t01", false);
		boolean b = SQLite.isVisiblePlaceType("t01");
		
		boolean c = SQLite.isVisibleLayer("2");
		SQLite.setLayerVisibility("2", false);
		boolean d = SQLite.isVisibleLayer("2");
		
		boolean e = SQLite.isVisiblePlot("5");
		SQLite.setPlotVisibility("5", true);
		boolean f = SQLite.isVisiblePlot("5");
		
		System.out.println("hello world");
		
	}
		
}
