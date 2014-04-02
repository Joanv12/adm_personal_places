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
		GlobalContext.setContext(getApplicationContext());
		SQLite.staticInitialization();

		/*
		BeanShape place = new BeanShape(null, "Cantina Mariachi Mariachi. Lugar de Tapas y mucho más.", "Un lugar muy bueno para ir a comer todos los días. Es bueno, bonito y barato. Tapas, Vinos y Más esto es una prueba", "t01", "-360.34699738025665283,-360.34699738025665283", null);
		Bitmap bitmap = Utils.getQRFromPlace(place);
		String path = Utils.getTempFilePathFromBitmap(bitmap);
		*/
		
		String imagePath = "/storage/extSdCard/test_qr.jpg";
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
		
		BeanShape place = Utils.getPlaceFromQR(bitmap);
		
		System.out.println("Nothing to show.");

	}

}
