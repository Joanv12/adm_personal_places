package com.upv.adm.adm_personal_shapes.screens;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.view.View.OnClickListener;
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
	
	private CustomListItem[] typesListviewItems;
			
	private Spinner spinner_types;
	
	private Button 
			button_fillqr,
			button_map,
			button_coordinates,
			button_save,
			button_cancel,
			button_share,
			button_delete;
	
	private RadioGroup radiogroup_shapetype;
	
	private RadioButton
					radiobutton_place,
					radiobutton_plot;
	
	private ImageView imageview_image;
	
	private String image_path = null;

	private ProgressDialog pd;

	final int ACTIVITYRESULT_CAMERA_SHAPEIMAGE = 1;
	final int ACTIVITYRESULT_CROP_SHAPEIMAGE = 2;
	final int ACTIVITYRESULT_GALLERY_SHAPEIMAGE = 3;
	
	final int ACTIVITYRESULT_CAMERA_QR = 4;
	final int ACTIVITYRESULT_GALLERY_QR = 5;
	
	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		GlobalContext.init(getApplicationContext());
		super.onCreate(savedInstanceState, R.layout.screen05);
		GlobalContext.shape_id = (long) 11;
		initControls();
		
		if (GlobalContext.shape_id != null) {
			ArrayList<BeanShape> shapes = (ArrayList<BeanShape>)SQLite.getShapes("WHERE id = " + GlobalContext.shape_id);
			if (shapes.size() == 1) {
				BeanShape shape = shapes.get(0);
				if (shape.hasImage())
					image_path = new File(Utils.getAppDir(), String.format("shape_%010d.jpg", shape.getId())).getPath();
				fillFields(shape);
			}
			else {
				GlobalContext.shape_id = null;
			}
		}
		if (GlobalContext.shape_id == null) { // is correct
			screen22.isPlace = true;
			screen22.coords = "";
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

		button_cancel = (Button) findViewById(R.id.button_cancel);
		button_share = (Button) findViewById(R.id.button_share);
		button_delete = (Button) findViewById(R.id.button_delete);
		
		imageview_image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectImage();
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
		
		button_save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				saveClick();
			}
		});
		
		button_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteClick();
			}
		});
		
		button_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getCurrentActivity().finish();
			}
		});
		
		button_share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				shareClick();
			}
		});
		
		if (GlobalContext.shape_id == null) {
			button_share.setVisibility(View.GONE);
			button_delete.setVisibility(View.GONE);
		}
		
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
			button_fillqr.setVisibility(View.VISIBLE);
			tablerow_placetype.setVisibility(View.VISIBLE);
			radiobutton_place.setChecked(true);
			Utils.setSelectedKey(spinner_types, shape.getType());
		}
		else {
			button_fillqr.setVisibility(View.GONE);
			tablerow_placetype.setVisibility(View.GONE);
			radiobutton_plot.setChecked(true);
		}
		edittext_name.setText(shape.getName());
		edittext_description.setText(shape.getDescription());
		if (shape.hasImage() && image_path != null && new File(image_path).exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(image_path);
			imageview_image.setImageBitmap(bitmap);
		}
		else
			imageview_image.setImageResource(android.R.drawable.ic_menu_gallery);
		screen22.isPlace = (shape.getType() != null); 
		screen22.coords = shape.getCoords(); 
	}
	
	public void fillQrClick() {

		//--- begin of preparing options
		ArrayList<String[]> options = new ArrayList<String[]>();
		options.add(new String[]{"Cámara", "1"});
		options.add(new String[]{"Galería", "2"});
		options.add(new String[]{"Cancelar", "3"});
		
		final String[] options_arr  = new String[options.size()];
		final Hashtable<String, Integer> options_ht = new Hashtable<String, Integer>();
		for (int i = 0; i < options.size(); i++) {
			options_arr[i] = options.get(i)[0];
			options_ht.put(options.get(i)[0], Integer.valueOf(options.get(i)[1]));
		}
		//--- begin of preparing options
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Código QR");
		builder.setItems(options_arr, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int position) {
				int selected_option = options_ht.get(options_arr[position]);
				Intent intent;
				switch (selected_option) {
					case 1:
						intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						startActivityForResult(intent, ACTIVITYRESULT_CAMERA_QR);
						break;
					case 2:
						intent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(intent, ACTIVITYRESULT_GALLERY_QR);
						break;
					case 3:
						dialog.dismiss();
						break;
				}
			}
		});
		builder.show();
	}
	
	public boolean checkFields(String name, String description, String coords) {
		if (!Utils.checkNameFormat(name)) {
			Utils.messageBox(this, "Nombre no válido");
			return false;
		}
		if (description.trim().length() == 0) {
			Utils.messageBox(this, "Descripción no válida");
			return false;
		}
		if (coords.trim().length() == 0) {
			try {
				Utils.messageBox(this, "Por favor, posicione el shape en el mapa");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
		return true;
	}
	public void saveClick() {
		Long id = GlobalContext.shape_id;
		String name = edittext_name.getText().toString();
		String description = edittext_description.getText().toString();
		String coords = screen22.coords;
		if (!checkFields(name, description, coords))
			return;
		String type = null;
		if (radiobutton_place.isChecked())
			type = Utils.getSelectedKey(spinner_types);
		BeanShape shape = new BeanShape(id, name, description, type, coords, (image_path != null));
		SQLite.saveShape(shape);
		if (image_path != null && new File(image_path).exists()) {
			new File(Utils.getAppDir(), "crop_temp.jpg").renameTo(new File(Utils.getAppDir(), String.format("shape_%010d.jpg", shape.getId())));
		}
		this.finish();
	}
	
	public void shareClick(){
		
		final String name = edittext_name.getText().toString();
		final String description = edittext_description.getText().toString();
		final String coords = screen22.coords;
		if (!checkFields(name, description, coords))
			return;
		final String type = radiobutton_place.isChecked()? Utils.getSelectedKey(spinner_types): null;
		
		//--- begin of preparing options
		ArrayList<String[]> options = new ArrayList<String[]>();
		options.add(new String[]{"Internamente", "1"});
		if (type != null) // is a place
			options.add(new String[]{"Redes sociales", "2"});
		options.add(new String[]{"Cancelar", "3"});
		
		final String[] options_arr  = new String[options.size()];
		final Hashtable<String, Integer> options_ht = new Hashtable<String, Integer>();
		for (int i = 0; i < options.size(); i++) {
			options_arr[i] = options.get(i)[0];
			options_ht.put(options.get(i)[0], Integer.valueOf(options.get(i)[1]));
		}
		//--- begin of preparing options
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Opciones de compartir");
		builder.setItems(options_arr, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int position) {
				int selected_option = options_ht.get(options_arr[position]);
				Intent intent;
				switch (selected_option) {
					case 1:
						break;
					case 2:
						try {
							File qr_temp_file = new File(Utils.getAppDir(), "qr_temp.jpg");
							BeanShape shape = new BeanShape(null, name, description, type, coords, false);
							Bitmap bitmap = Utils.getQRFromPlace(shape);
							FileOutputStream fos = new FileOutputStream(qr_temp_file);
							bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
							
							//Uri uri = Uri.fromFile(qr_temp_file);

							intent = new Intent();
							intent.setAction(Intent.ACTION_SEND);
							intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(qr_temp_file));
							intent.setType("image/jpeg");
							intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

							startActivity(Intent.createChooser(intent, "Where?"));

						}
						catch (Exception e) {
							e.printStackTrace();
						}
						break;
					case 3:
						dialog.dismiss();
						break;
				}
			}
		});
		builder.show();

	}

	@SuppressLint("DefaultLocale")
	public void deleteClick() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getCurrentActivity());
		builder.setMessage("¿Realmente deseas eliminar este shape?");
		builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	    		SQLite.deleteShape(GlobalContext.shape_id);
	    		String image_path = new File(Utils.getAppDir(), String.format("shape_%010d.jpg", GlobalContext.shape_id)).getPath();
	    		if (new File(image_path).exists())
	    			new File(image_path).delete();
	    		getCurrentActivity().finish();
	        }

	    });
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	        }
	    });
		builder.show();
	}
	
	public void coordinatesClick() {
		Utils.startActivity(getCurrentActivity(), screen21.class);
	}
	
	public void mapClick() {
		Utils.startActivity(getCurrentActivity(), screen22.class);
	}

	private void selectImage() {
		
		//--- begin of preparing options
		ArrayList<String[]> options = new ArrayList<String[]>();
		options.add(new String[]{"Cámara", "1"});
		options.add(new String[]{"Galería", "2"});
		if (image_path != null)
			options.add(new String[]{"Eliminar", "3"});
		options.add(new String[]{"Cancelar", "4"});
		
		final String[] options_arr  = new String[options.size()];
		final Hashtable<String, Integer> options_ht = new Hashtable<String, Integer>();
		for (int i = 0; i < options.size(); i++) {
			options_arr[i] = options.get(i)[0];
			options_ht.put(options.get(i)[0], Integer.valueOf(options.get(i)[1]));
		}
		//--- begin of preparing options
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Imagen de perfil");
		builder.setItems(options_arr, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int position) {
				int selected_option = options_ht.get(options_arr[position]);
				Intent intent;
				switch (selected_option) {
					case 1:
						intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						startActivityForResult(intent, ACTIVITYRESULT_CAMERA_SHAPEIMAGE);
						break;
					case 2:
						intent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(intent, ACTIVITYRESULT_GALLERY_SHAPEIMAGE);
						break;
					case 3:
						image_path = null;
						imageview_image.setImageResource(android.R.drawable.ic_menu_gallery);
						break;
					case 4:
						dialog.dismiss();
						break;
				}
			}
		});
		builder.show();
	}

	private void startCropImage(Uri fromUri, Uri toUri) {
		try {
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(fromUri, "image/*");
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", 512);
			intent.putExtra("outputY", 512);
			intent.putExtra("return-data", false);
			intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            try {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, toUri);
                startActivityForResult(intent, ACTIVITYRESULT_CROP_SHAPEIMAGE);
            }
            catch (Exception e) {
            	e.printStackTrace();
            }
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == RESULT_OK) {
			if (requestCode == ACTIVITYRESULT_CAMERA_SHAPEIMAGE || requestCode == ACTIVITYRESULT_GALLERY_SHAPEIMAGE) {
				try  {
					Uri fromUri = intent.getData();
					File toFile = new File(Utils.getAppDir(), "crop_temp.jpg");
					if (toFile.exists())
						toFile.delete();
	                Uri toUri = Uri.fromFile(toFile);
					startCropImage(fromUri, toUri);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if (requestCode == ACTIVITYRESULT_CROP_SHAPEIMAGE) {
				File imageFile = new File(Utils.getAppDir(), "crop_temp.jpg");
				if (imageFile.exists()) {
					image_path = imageFile.getPath();
					Bitmap bitmap = BitmapFactory.decodeFile(image_path);
					imageview_image.setImageBitmap(bitmap);
				}
				else {
					image_path = null;
					imageview_image.setImageResource(android.R.drawable.ic_menu_gallery);
				}
			}
			else if (requestCode == ACTIVITYRESULT_CAMERA_QR || requestCode == ACTIVITYRESULT_GALLERY_QR) {
				try  {
					Uri imageUri = intent.getData();
					String imagePath = Utils.getRealPathFromURI(imageUri);
					Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
					BeanShape place = Utils.getPlaceFromQR(bitmap);
					if (place == null)
						Utils.messageBox(getCurrentActivity(), "No se ha podido interpretar el código QR. Por favor, inténtelo nuevamente.");
					else {
						new File(Utils.getAppDir(), "crop_temp.jpg").delete();
						fillFields(place);
					}
					System.out.println("hello world");
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
