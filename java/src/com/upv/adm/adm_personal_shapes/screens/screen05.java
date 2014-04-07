package com.upv.adm.adm_personal_shapes.screens;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Hashtable;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewManager;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableRow;
import com.upv.adm.adm_personal_shapes.R;
import com.upv.adm.adm_personal_shapes.classes.BeanShape;
import com.upv.adm.adm_personal_shapes.classes.CustomActionBarActivity;
import com.upv.adm.adm_personal_shapes.classes.CustomListItem;
import com.upv.adm.adm_personal_shapes.classes.WebServerProxy;
import com.upv.adm.adm_personal_shapes.classes.GlobalContext;
import com.upv.adm.adm_personal_shapes.classes.SQLite;
import com.upv.adm.adm_personal_shapes.classes.Utils;

public class screen05 extends CustomActionBarActivity {

	private EditText 
			edittext_name,
			edittext_description;

	private TableRow tablerow_placetype;
	
	private CustomListItem[] 
			typesListviewItems;
			
	private static final int DIALOG_ALERT = 10;
	
	private String resizedImagePath;

	ArrayList<BeanShape> list_places = new ArrayList<BeanShape>();
	ArrayList<BeanShape> list_plots = new ArrayList<BeanShape>();

	private Object[] typesData;
	private Spinner spinner_types;
	
	private Button 
			button_share, 
			button_delete;
	private Button 
			button_fillqr,
			button_map,
			button_coordinates,
			button_save; 
	
	private RadioGroup radiogroup_shapetype;
	
	public static RadioButton
					radiobutton_place,
					radiobutton_plot;
	
	private ImageView imageview_image;
	
	final int ACTIVITYRESULT_CAMERA = 1;
	final int ACTIVITYRESULT_CROP = 2;

	ProgressDialog pd;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		GlobalContext.init(getApplicationContext());
		super.onCreate(savedInstanceState, R.layout.screen05);
		initControls();

		GlobalContext.shape_id = (long) 13;
		if (GlobalContext.shape_id != null) {
			ArrayList<BeanShape> shapes = (ArrayList<BeanShape>)SQLite.getShapes("WHERE id = " + GlobalContext.shape_id);
			if (shapes.size() == 1) {
				BeanShape shape = shapes.get(0);
				fillFields(shape);
			}
		}
	}
	
	public void initControls(){
	
		radiogroup_shapetype = (RadioGroup) findViewById(R.id.radiogroup_shapetype);
		
		radiobutton_place = (RadioButton) findViewById(R.id.radiobutton_place);
		radiobutton_plot = (RadioButton) findViewById(R.id.radiobutton_plot);

		tablerow_placetype = (TableRow) findViewById(R.id.tablerow_placetype);
		
		spinner_types = (Spinner) findViewById(R.id.spinner_types);

		button_fillqr = (Button) findViewById(R.id.button_fillqr);

		edittext_name = (EditText) findViewById(R.id.edittext_placename);
		edittext_description = (EditText) findViewById(R.id.edittext_description);
		imageview_image = (ImageView) findViewById(R.id.imageview_image);
		
		button_map = (Button) findViewById(R.id.button_map);
		button_coordinates = (Button) findViewById(R.id.button_coordinates);
		button_save = (Button) findViewById(R.id.button_save);

		button_share = (Button) findViewById(R.id.button_share);
		button_delete = (Button) findViewById(R.id.button_delete);
		
		imageview_image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (v.getId() == R.id.imageview_image) {
					try {
						selectImage();
					} catch (ActivityNotFoundException anfe) {
						@SuppressWarnings("unused")
						String errorMessage = "Whoops - your device doesn't support capturing images!";
					}
				}
			}
		});

		typesListviewItems = GlobalContext.getTypesData();
		ArrayAdapter<CustomListItem> arrayAdapter_genders = new ArrayAdapter<CustomListItem>(this, android.R.layout.simple_spinner_dropdown_item, typesListviewItems);
		spinner_types.setAdapter(arrayAdapter_genders);
		
		button_fillqr.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fillQrClick();
			}
		});

		if (GlobalContext.shape_id == null) {
			((ViewManager) button_share.getParent()).removeView(button_share);
			((ViewManager) button_delete.getParent()).removeView(button_delete);

		}
		
		button_map.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mapClick();
			}
		});
		
		button_coordinates.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				coordinatesClick();
			}
		});
		
		button_share.setOnClickListener(new OnClickListener() {  @Override  public void onClick(View v) {
			AlertDialog.Builder builder_share =new AlertDialog.Builder(screen05.this);
			builder_share.setTitle("Compartir con...");
			
			builder_share.setNeutralButton("Personal Shapes",new DialogInterface.OnClickListener() {  
				@Override  
				public void onClick(DialogInterface dialog, int which) {
					shareClick();
				
				}  
			});  
			builder_share.setPositiveButton("Redes Sociales",new DialogInterface.OnClickListener() {  
				@Override  
				public void onClick(DialogInterface dialog, int which) {
						
				
				}  
			}); 
			builder_share.show();  
			}  
		});
		
		button_save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				saveClick();
			}
		});
		
		radiogroup_shapetype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				if (checkedId == R.id.radiobutton_place) {
					button_fillqr.setVisibility(View.VISIBLE);
					tablerow_placetype.setVisibility(View.VISIBLE);
					screen22.isPlace = true;
					screen22.coords = "";
				}
				else if (checkedId == R.id.radiobutton_plot) {
					button_fillqr.setVisibility(View.GONE);
					tablerow_placetype.setVisibility(View.GONE);
					screen22.isPlace = false;
					screen22.coords = "";
				}
			}
		});
	}
	
	public void fillFields(BeanShape shape) {
		if (shape.getType() != null) {
			radiobutton_place.setChecked(true);
			button_fillqr.setVisibility(View.VISIBLE);
			tablerow_placetype.setVisibility(View.VISIBLE);
			Utils.setSelectedKey(spinner_types, shape.getType());
		}
		else {
			radiobutton_plot.setChecked(true);
			button_fillqr.setVisibility(View.GONE);
			tablerow_placetype.setVisibility(View.GONE);
		}
		edittext_name.setText(shape.getName());
		edittext_description.setText(shape.getDescription());	
		screen22.isPlace = (shape.getType() != null); 
		screen22.coords = shape.getCoords(); 
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_ALERT:
			Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Compartir con...");
			builder.setCancelable(true);
			builder.setPositiveButton("PersonalPlaces", new OkOnClickListener());
			builder.setNegativeButton("Redes Sociales", new CancelOnClickListener());
			AlertDialog dialog = builder.create();
			dialog.show();
		}
		return super.onCreateDialog(id);
	}

	private final class CancelOnClickListener implements DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {

			// es necesario un intent que levante la actividad deseada
			Intent itSend = new Intent(android.content.Intent.ACTION_SEND);

			// itSend.setType("plain/text");

			itSend.putExtra(android.content.Intent.EXTRA_TEXT, edittext_name.getText().toString());
			itSend.putExtra(android.content.Intent.EXTRA_TITLE,edittext_description.getText().toString());

			itSend.setType("text/plain");
			startActivity(itSend);
		}
	}

	private final class OkOnClickListener implements DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			shareClick();
		}
	}

	public void fillQrClick() {
		@SuppressWarnings("unchecked")
		ArrayList<String> typesDataCodes = (ArrayList<String>) typesData[0];
		String typesCode = typesDataCodes.get(spinner_types.getSelectedItemPosition());
		edittext_name.setText(typesCode);
	}
	
	public void saveClick() {
		
		Long id = GlobalContext.shape_id;
		String name = edittext_name.getText().toString();
		String description = edittext_description.getText().toString();
		String coords = screen22.coords;

		String type = null;
		if (radiobutton_place.isChecked())
			type = Utils.getSelectedKey(spinner_types);
			
		BeanShape shape = new BeanShape(id, name, description, type, coords, resizedImagePath);

		SQLite.saveShape(shape);

		//this.finish();
		
	}
	
	public void coordinatesClick() {
		startActivity(new Intent(getApplicationContext(), screen21.class));
	}
	
	public void mapClick() {
		startActivity(new Intent(getApplicationContext(), screen22.class));
	}

	public void UtilsAddBeanPlotToMap(BeanShape plot, WebView webview_map){
		
	}
	public void selectImage(View v) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, 0);
	}

	private void selectImage() {

		final CharSequence[] options = { "Cámara", "Galeria", "Cancelar" };
		
		AlertDialog.Builder builder = new AlertDialog.Builder(screen05.this);
		builder.setTitle("Añadir Imagen!");
		builder.setItems(options, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (options[item].equals("Cámara")) {
					Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(captureIntent, ACTIVITYRESULT_CAMERA);
				}
				else if (options[item].equals("Galeria")) {
					Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(intent, 1);
				}
				else if (options[item].equals("Cancelar")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			if (requestCode == ACTIVITYRESULT_CAMERA) {
				Uri uri = data.getData();
				startCropImage(uri);
			}
			else if (requestCode == ACTIVITYRESULT_CROP) {
				Bundle extras = data.getExtras();
				Bitmap bitmap = extras.getParcelable("data");
				Bitmap bitmap_resized = Bitmap.createScaledBitmap(bitmap, 512,512, false);
				imageview_image.setImageBitmap(bitmap_resized);
				try {
					File file = File.createTempFile("temp_file_",".jpg");
					FileOutputStream fos = new FileOutputStream(file);
					bitmap_resized.compress(Bitmap.CompressFormat.JPEG, 100, fos);
					resizedImagePath = file.getPath();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void startCropImage(Uri uri) {
		try {
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(uri, "image/*");
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", 512);
			intent.putExtra("outputY", 512);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, ACTIVITYRESULT_CROP);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void shareClick(){
		
		String name = edittext_name.getText().toString();
		String type = Utils.getSelectedKey(spinner_types);
		String description = edittext_description.getText().toString();

		Hashtable<String, String> data = new Hashtable<String, String>();
		data.put("name", name);
		data.put("description", description);
		data.put("type",  type);

		if (resizedImagePath != null)
			data.put("image", resizedImagePath);
		
		(new AsyncTask<Hashtable<String, String>, Void, String[]>() {
			@Override
			protected void onPreExecute() {
				pd = new ProgressDialog(getCurrentActivity());
				pd.setTitle("Comunicando con el servidor");
				pd.setMessage("Por favor, espere mientras se comparte...");
				pd.setCancelable(false);
				pd.setIndeterminate(true);
				pd.show();
			}
			@Override
			protected String[] doInBackground(Hashtable<String, String>... data) {
				String[] result = WebServerProxy.register_user(data[0]);  //share_shape
				
				return result;
			}
			@Override
			protected void onPostExecute(String[] result) {
				if (pd!=null)
					pd.dismiss();
				if (result[0].equals("success")) {
					getCurrentActivity().finish(); 
					startActivity(new Intent(getApplicationContext(), screen04.class));
				}
				else {
				    AlertDialog ad = new AlertDialog.Builder(getCurrentActivity()).create();  
				    ad.setCancelable(false);  
				    ad.setMessage(
				    		"No ha sido posible compartir. El servidor ha devuelto el siguiente código de error:\n\n" +
				    		result[1]
				    );  
				    ad.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {  
				        @Override  
				        public void onClick(DialogInterface dialog, int which) {  
				            dialog.dismiss();                      
				        }  
				    });  
				    ad.show();  
				}
			}
		}).execute(data);

	}
}
