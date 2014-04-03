package com.upv.adm.adm_personal_shapes.screens;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Hashtable;
import android.app.AlertDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.upv.adm.adm_personal_shapes.R;
import com.upv.adm.adm_personal_shapes.classes.CustomActionBarActivity;
import com.upv.adm.adm_personal_shapes.classes.CustomListItem;
import com.upv.adm.adm_personal_shapes.classes.GlobalContext;
import com.upv.adm.adm_personal_shapes.classes.SQLite;
import com.upv.adm.adm_personal_shapes.classes.Utils;
import com.upv.adm.adm_personal_shapes.classes.WebServerProxy;

public class screen02 extends CustomActionBarActivity implements OnClickListener {

	private EditText
		edittext_username,
		edittext_password,
		edittext_passrepeat,
		edittext_name,
		edittext_email,
		edittext_phone;
	
	private Spinner
		spinner_countries,
		spinner_genders;
	
	private Button button_register;

	private String resizedImagePath = null;
	
	private ProgressDialog pd;

	private CustomListItem[] 
			countriesListviewItems,
			gendersListviewItems;

	// keep track of camera capture intent
	final int CAMERA_CAPTURE = 1;
	// keep track of cropping intent
	final int PIC_CROP = 2;
	// captured picture uri
	private Uri picUri;

	private void load_data_debug() {
		edittext_username.setText("cameron");
		edittext_password.setText("sevilla");
		edittext_passrepeat.setText("sevilla");
		edittext_name.setText("Cameron Diaz");
		Utils.setSelectedKey(spinner_genders, "M");
		Utils.setSelectedKey(spinner_countries, "NO");
		edittext_email.setText("cameron@gmail.com");
		edittext_phone.setText("678123444");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.screen02);
		GlobalContext.setContext(getApplicationContext());
		SQLite.staticInitialization();

		GlobalContext.screen02 = this;

		initControls();
	}

	private void initControls() {

		edittext_username = (EditText) findViewById(R.id.edittext_user);
		edittext_password = (EditText) findViewById(R.id.edittext_pass);
		edittext_passrepeat = (EditText) findViewById(R.id.edittext_passrepeat);
		edittext_name = (EditText) findViewById(R.id.edittext_name);

		spinner_countries = (Spinner) findViewById(R.id.spinner_countries);
		spinner_genders = (Spinner) findViewById(R.id.spinner_genders);
		ImageView image_photo2 = (ImageView) findViewById(R.id.image_photo);

		edittext_email = (EditText) findViewById(R.id.edittext_email);
		edittext_phone = (EditText) findViewById(R.id.edittext_phone);

		button_register = (Button) findViewById(R.id.button_register);

		image_photo2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (v.getId() == R.id.image_photo) {
					try {
						selectImage();
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

		button_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				registerClick();
			}
		});

		countriesListviewItems = GlobalContext.getCountriesListviewItems();
        ArrayAdapter<CustomListItem> arrayAdapter_countries = new ArrayAdapter<CustomListItem>(this, android.R.layout.simple_spinner_dropdown_item, countriesListviewItems);
		spinner_countries.setAdapter(arrayAdapter_countries);

        gendersListviewItems = GlobalContext.getGendersListviewItems();
		ArrayAdapter<CustomListItem> arrayAdapter_genders = new ArrayAdapter<CustomListItem>(this, android.R.layout.simple_spinner_dropdown_item, gendersListviewItems);
		spinner_genders.setAdapter(arrayAdapter_genders);

		load_data_debug();

	}

	private void selectImage() {

		final CharSequence[] options = { "Cámara", "Galeria","Cancelar" };
		AlertDialog.Builder builder = new AlertDialog.Builder(screen02.this);
		builder.setTitle("Añadir Imagen Perfil!");
		builder.setItems(options, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (options[item].equals("Cámara")) {

					// use standard intent to capture an image
					Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					// we will handle the returned data in onActivityResult
					startActivityForResult(captureIntent, CAMERA_CAPTURE);
				} else if (options[item].equals("Galeria")) {
					// acción para buscar una imagen en la galeria
					Intent pickPhoto = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(pickPhoto, 1);// one can be replaced with any action code
				} else if (options[item].equals("Cancelar")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			// user is returning from capturing an image using the camera
			if (requestCode == CAMERA_CAPTURE) {
				// get the Uri for the captured image
				picUri = data.getData();
				// carry out the crop operation
				performCrop();
			}
			// user is returning from cropping the image
			else if (requestCode == PIC_CROP) {
				// get the returned data
				Bundle extras = data.getExtras();
				// get the cropped bitmap
				Bitmap thePic = extras.getParcelable("data");
				// retrieve a reference to the ImageView
				ImageView picView = (ImageView) findViewById(R.id.image_photo);
				// display the returned cropped image

				Bitmap thePicResized = Bitmap.createScaledBitmap(thePic, 1024, 1024, false);
				picView.setImageBitmap(thePicResized);

				try {
					File tempFile = File.createTempFile("temp_file_", ".jpg");
					FileOutputStream fos = new FileOutputStream(tempFile);
					thePicResized.compress(Bitmap.CompressFormat.JPEG, 100, fos);
					resizedImagePath = tempFile.getPath();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void selectImage(View v) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, 0);
	}

	/**
	 * Helper method to carry out crop operation
	 */
	private void performCrop() {
		// take care of exceptions
		try {
			// call the standard crop action intent (the user device may not
			// support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// indicate image type and Uri
			cropIntent.setDataAndType(picUri, "image/*");
			// set crop properties
			cropIntent.putExtra("crop", "true");
			// indicate aspect of desired crop
			cropIntent.putExtra("aspectX", 1);
			cropIntent.putExtra("aspectY", 1);
			// indicate output X and Y
			cropIntent.putExtra("outputX", 512);
			cropIntent.putExtra("outputY", 512);
			// retrieve data on return
			cropIntent.putExtra("return-data", true);
			// start the activity - we handle returning in onActivityResult

			startActivityForResult(cropIntent, PIC_CROP);

		}
		// respond to users whose devices do not support the crop action
		catch (ActivityNotFoundException anfe) {
			// display an error message
			String errorMessage = "Whoops - your device doesn't support the crop action!";
			Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	@Override
	public void onClick(View v) {

	}
	
	@SuppressWarnings("unchecked")
	public void registerClick(){
		
		String username = edittext_username.getText().toString();
		String password = edittext_password.getText().toString();
		@SuppressWarnings("unused")
		String passrepeat = edittext_passrepeat.getText().toString();
		String name = edittext_name.getText().toString();
		String gender = Utils.getSelectedKey(spinner_genders);
		String country = Utils.getSelectedKey(spinner_countries);
		String email = edittext_email.getText().toString();
		String phone = edittext_phone.getText().toString();

		Hashtable<String, String> data = new Hashtable<String, String>();
		data.put("username", username);
		data.put("password", password);
		data.put("name",  name);
		data.put("gender", gender);
		data.put("email", email);
		data.put("phone", phone);
		data.put("country", country);
		
		if (resizedImagePath != null)
			data.put("photo", resizedImagePath);
		
		(new AsyncTask<Hashtable<String, String>, Void, String[]>() {
			@Override
			protected void onPreExecute() {
				pd = new ProgressDialog(getCurrentActivity());
				pd.setTitle("Comunicando con el servidor");
				pd.setMessage("Por favor, espere mientras procesamos su registro...");
				pd.setCancelable(false);
				pd.setIndeterminate(true);
				pd.show();
			}
			@Override
			protected String[] doInBackground(Hashtable<String, String>... data) {
				String[] result = WebServerProxy.register_user(data[0]);
				return result;
			}
			@Override
			protected void onPostExecute(String[] result) {
				if (pd!=null)
					pd.dismiss();
				if (result[0].equals("success")) {
					// Respuesta correcta por parte del servidor, entonces seguimos adelante
					GlobalContext.username = edittext_username.getText().toString();
					GlobalContext.password = edittext_password.getText().toString();
					getCurrentActivity().finish(); // esta activity no se necesitará más, por tanto la cerramos
					GlobalContext.screen01.finish();
					startActivity(new Intent(getApplicationContext(), screen03.class)); // abrimos (creación) la siguiente activity
				}
				else {
				    AlertDialog ad = new AlertDialog.Builder(getCurrentActivity()).create();  
				    ad.setCancelable(false);  
				    ad.setMessage(
				    		"No ha sido posible procesar su registro. El servidor ha devuelto el siguiente código de error:\n\n" +
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
