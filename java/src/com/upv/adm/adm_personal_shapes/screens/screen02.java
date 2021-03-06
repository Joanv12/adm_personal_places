package com.upv.adm.adm_personal_shapes.screens;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
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
import android.widget.Spinner;
import com.upv.adm.adm_personal_shapes.R;
import com.upv.adm.adm_personal_shapes.classes.CustomActionBarActivity;
import com.upv.adm.adm_personal_shapes.classes.CustomListItem;
import com.upv.adm.adm_personal_shapes.classes.GlobalContext;
import com.upv.adm.adm_personal_shapes.classes.Utils;
import com.upv.adm.adm_personal_shapes.classes.WebServerProxy;

public class screen02 extends CustomActionBarActivity {

	private EditText
		edittext_username,
		edittext_password,
		edittext_passwordrepeat,
		edittext_email,
		edittext_phone,
		edittext_name;
	
	private Spinner
		spinner_country,
		spinner_gender;
	
	private ImageView imageview_image;
	
	private Button button_register;

	private String image_path = null;
	
	private ProgressDialog pd;

	private CustomListItem[] 
			countriesListviewItems,
			gendersListviewItems;

	final int ACTIVITYRESULT_CAMERA = 1;
	final int ACTIVITYRESULT_CROP = 2;
	final int ACTIVITYRESULT_GALLERY = 3;

	private void load_data_debug() {
		edittext_username.setText("cameron");
		edittext_password.setText("sevilla");
		edittext_passwordrepeat.setText("sevilla");
		edittext_email.setText("cameron@gmail.com");
		edittext_phone.setText("678123444");
		edittext_name.setText("Cameron Diaz");
		Utils.setSelectedKey(spinner_gender, "F");
		Utils.setSelectedKey(spinner_country, "NO");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		GlobalContext.init(getApplicationContext());
		super.onCreate(savedInstanceState, R.layout.screen02);
		initControls();
		if (!restoreDataIfNecessary(savedInstanceState)) {
			if (Utils.isUserLoggedIn()) {
				image_path = Utils.getUserImagePath();
				fillFields();
			}
		}
	}

	private void initControls() {
		edittext_username = (EditText) findViewById(R.id.edittext_username);
		edittext_password = (EditText) findViewById(R.id.edittext_password);
		edittext_passwordrepeat = (EditText) findViewById(R.id.edittext_passwordrepeat);
		edittext_email = (EditText) findViewById(R.id.edittext_email);
		edittext_phone = (EditText) findViewById(R.id.edittext_phone);
		edittext_name = (EditText) findViewById(R.id.edittext_name);
		spinner_gender = (Spinner) findViewById(R.id.spinner_gender);
		spinner_country = (Spinner) findViewById(R.id.spinner_country);
		imageview_image = (ImageView) findViewById(R.id.imageview_image);
		button_register = (Button) findViewById(R.id.button_register);
		
		if (Utils.isUserLoggedIn())
			edittext_username.setFocusable(false);
		
		if (!Utils.isUserLoggedIn()) {
			this.setTitle(Utils.str(R.string.title_screen02_register));
			button_register.setText(Utils.str(R.string.button_register));
		}
		else {
			this.setTitle(Utils.str(R.string.title_screen02_profile));
			button_register.setText(Utils.str(R.string.button_update));
		}

		imageview_image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectImage();
			}
		});

		button_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				registerClick();
			}
		});

        gendersListviewItems = GlobalContext.getGendersListviewItems();
		ArrayAdapter<CustomListItem> arrayAdapter_genders = new ArrayAdapter<CustomListItem>(this, android.R.layout.simple_spinner_dropdown_item, gendersListviewItems);
		spinner_gender.setAdapter(arrayAdapter_genders);
		
		countriesListviewItems = GlobalContext.getCountriesListviewItems();
        ArrayAdapter<CustomListItem> arrayAdapter_countries = new ArrayAdapter<CustomListItem>(this, android.R.layout.simple_spinner_dropdown_item, countriesListviewItems);
		spinner_country.setAdapter(arrayAdapter_countries);

	}
	
	public void fillFields() {
		edittext_username.setText(GlobalContext.username);
		edittext_email.setText(GlobalContext.getPreference(GlobalContext.PREF.EMAIL));
		edittext_phone.setText(GlobalContext.getPreference(GlobalContext.PREF.PHONE));

		edittext_name.setText(GlobalContext.getPreference(GlobalContext.PREF.NAME));
		Utils.setSelectedKey(spinner_gender, GlobalContext.getPreference(GlobalContext.PREF.GENDER));
		Utils.setSelectedKey(spinner_country, GlobalContext.getPreference(GlobalContext.PREF.COUNTRY));
		
		if (image_path != null && new File(image_path).exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(image_path);
			imageview_image.setImageBitmap(bitmap);
		}
	}
	
	@Override
    protected void onSaveInstanceState (Bundle outState) {
        outState.putString("username", edittext_username.getText().toString());
        outState.putString("password", edittext_password.getText().toString());
        outState.putString("passwordrepeat", edittext_passwordrepeat.getText().toString());
        outState.putString("email", edittext_email.getText().toString());
        outState.putString("phone", edittext_phone.getText().toString());
        outState.putString("name", edittext_name.getText().toString());
        outState.putString("gender", Utils.getSelectedKey(spinner_gender));
        outState.putString("country", Utils.getSelectedKey(spinner_country));
        outState.putString("image_path", image_path);
    }
	
	private boolean restoreDataIfNecessary(Bundle inState) {
		if (inState != null) {
			String username = inState.getString("username");
			String password = inState.getString("password");
			String passwordrepeat = inState.getString("passwordrepeat");
			String email = inState.getString("email");
			String phone = inState.getString("phone");
			String name = inState.getString("name");
			String gender = inState.getString("gender");
			String country = inState.getString("country");
			image_path = inState.getString("image_path");
			if (username != null && username != "")
				edittext_username.setText(username);
			if (password != null && password != "")
				edittext_password.setText(password);
			if (passwordrepeat != null && passwordrepeat != "")
				edittext_passwordrepeat.setText(passwordrepeat);
			if (email != null && email != "")
				edittext_email.setText(email);
			if (phone != null && phone != "")
				edittext_phone.setText(phone);
			if (name != null && name != "")
				edittext_name.setText(name);
			if (gender != null && gender != "")
				Utils.setSelectedKey(spinner_gender, gender);
			if (country != null && country != "")
				Utils.setSelectedKey(spinner_country, country);
			if (image_path != null) {
				Bitmap bitmap = BitmapFactory.decodeFile(image_path);
				imageview_image.setImageBitmap(bitmap);
			}
			return true;
	    }
		else {
			//load_data_debug();
			return false;
		}
	}
	
	private void selectImage() {
		
		//--- begin of preparing options
		ArrayList<String[]> options = new ArrayList<String[]>();
		options.add(new String[]{"C�mara", "1"});
		options.add(new String[]{"Galer�a", "2"});
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
						startActivityForResult(intent, ACTIVITYRESULT_CAMERA);
						break;
					case 2:
						intent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(intent, ACTIVITYRESULT_GALLERY);
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
                startActivityForResult(intent, ACTIVITYRESULT_CROP);
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
			if (requestCode == ACTIVITYRESULT_CAMERA || requestCode == ACTIVITYRESULT_GALLERY) {
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
			else if (requestCode == ACTIVITYRESULT_CROP) {
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
		}
	}

	@SuppressWarnings("unchecked")
	public void registerClick(){
		String username = edittext_username.getText().toString();
		String password = edittext_password.getText().toString();
		String passwordrepeat = edittext_passwordrepeat.getText().toString();
		String email = edittext_email.getText().toString();
		String phone = edittext_phone.getText().toString();
		String name = edittext_name.getText().toString();
		String gender = Utils.getSelectedKey(spinner_gender);
		String country = Utils.getSelectedKey(spinner_country);

		if (!password.equals(passwordrepeat)) {
			AlertDialog ad = new AlertDialog.Builder(getCurrentActivity()).create();
			ad.setCancelable(false);  
			ad.setMessage("No has repetido la contrase�a correctamente. Por favor, int�ntalo nuevamente.");  
			ad.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {  
			    @Override  
			    public void onClick(DialogInterface dialog, int which) {  
			        // whatever                      
			    }  
			});  
			ad.show();
		}
		else {
			Hashtable<String, String> data = new Hashtable<String, String>();
			
			// username
			if (!Utils.checkUsernameFormat(username)) {
				Utils.messageBox(getCurrentActivity(), "Usuario no v�lido");
				return;
			}
			data.put("username", username);
			
			// password
			if (Utils.isUserLoggedIn()) {
				if (!password.equals("") && !Utils.checkPasswordFormat(password)) {
					Utils.messageBox(getCurrentActivity(), "Contrase�a no v�lida");
					return;
				}
				data.put("password_old", GlobalContext.password);
			}
			data.put("password", password);
			
			// name
			if (!Utils.checkNameFormat(name)) {
				Utils.messageBox(getCurrentActivity(), "Nombre no v�lido");
				return;
			}
			data.put("name", name);
			
			// gender
			data.put("gender", gender);
			
			// email
			if (!Utils.checkEmailFormat(email)) {
				Utils.messageBox(getCurrentActivity(), "Email no v�lido");
				return;
			}
			data.put("email", email);
			
			// phone
			if (!Utils.checkPhoneFormat(phone)) {
				Utils.messageBox(getCurrentActivity(), "Tel�fono no v�lido");
				return;
			}
			data.put("phone", phone);
			
			// country
			data.put("country", country);
			
			if (image_path != null)
				data.put("image", image_path);
			
			(new AsyncTask<Hashtable<String, String>, Void, String[]>() {
				@Override
				protected void onPreExecute() {
					String operation_type = (Utils.isUserLoggedIn())? "actualizaci�n": "registro";
					pd = new ProgressDialog(getCurrentActivity());
					pd.setTitle("Comunicando con el servidor");
					pd.setMessage("Por favor, espere mientras procesamos su " + operation_type + "...");
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
						// despu�s del registro, las credenciales se recuerdan por defecto
						GlobalContext.setPreference(GlobalContext.PREF.USERNAME, edittext_username.getText().toString());
						if (!edittext_password.getText().toString().equals("")) 
							GlobalContext.setPreference(GlobalContext.PREF.PASSWORD, edittext_password.getText().toString());
						GlobalContext.setPreference(GlobalContext.PREF.REMEMBER, "1");
						GlobalContext.setPreference(GlobalContext.PREF.EMAIL, edittext_email.getText().toString());
						GlobalContext.setPreference(GlobalContext.PREF.PHONE, edittext_phone.getText().toString());
						GlobalContext.setPreference(GlobalContext.PREF.NAME, edittext_name.getText().toString());
						GlobalContext.setPreference(GlobalContext.PREF.GENDER, Utils.getSelectedKey(spinner_gender));
						GlobalContext.setPreference(GlobalContext.PREF.COUNTRY, Utils.getSelectedKey(spinner_country));
						
						// no incluir las siguientes l�neas dentro de: "GlobalContext.setPreference" porque
						// una una cosa es tener los datos disponibles durante la sesi�n actual
						// y otra cosa es tenerlos disponibles despu�s de cerrar la sesi�n
						GlobalContext.username = edittext_username.getText().toString();
						if (!edittext_password.getText().toString().equals("")) 
							GlobalContext.password = edittext_password.getText().toString();

						Utils.startActivity(getCurrentActivity(), screen03.class, true);
					}
					else {
						String operation_type = (Utils.isUserLoggedIn())? "actualizaci�n": "registro";
					    AlertDialog ad = new AlertDialog.Builder(getCurrentActivity()).create();  
					    ad.setCancelable(false);  
					    ad.setMessage(
					    		"No ha sido posible procesar su " + operation_type + ". El servidor ha devuelto el siguiente c�digo de error:\n\n" +
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
}
