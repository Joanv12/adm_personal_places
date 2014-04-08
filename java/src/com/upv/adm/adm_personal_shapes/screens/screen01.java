package com.upv.adm.adm_personal_shapes.screens;

import java.util.ArrayList;
import java.util.Hashtable;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import com.upv.adm.adm_personal_shapes.R;
import com.upv.adm.adm_personal_shapes.classes.CustomActionBarActivity;
import com.upv.adm.adm_personal_shapes.classes.Utils;
import com.upv.adm.adm_personal_shapes.classes.WebServerProxy;
import com.upv.adm.adm_personal_shapes.classes.GlobalContext;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class screen01 extends CustomActionBarActivity {

	private EditText
		edittext_username,
		edittext_password;
	
	private CheckBox checkbox_remember;

	private Button
		button_login,
		button_skip,
		button_register,
		button_clear;

	private ProgressDialog pd;
	
	@Override
	public Activity getCurrentActivity() {
		return this;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		GlobalContext.init(getApplicationContext());
		super.onCreate(savedInstanceState, R.layout.screen01);
		if (Utils.isUserLoggedIn())
			Utils.startActivity(getCurrentActivity(), screen03.class, true);
		else
			initControls();
	}

	private void initControls() {
		
		edittext_username = (EditText) findViewById(R.id.edittext_username);
		edittext_password = (EditText) findViewById(R.id.edittext_password);
		checkbox_remember = (CheckBox) findViewById(R.id.checkbox_remember);
		button_login = (Button) findViewById(R.id.button_login);
		button_skip = (Button) findViewById(R.id.button_skip);
		button_register = (Button) findViewById(R.id.button_register);
		button_clear = (Button) findViewById(R.id.button_clear);
		
		checkbox_remember.setOnClickListener(new CheckBox.OnClickListener() {
			public void onClick(View v) { rememberClick(); }
		});
		
		button_login.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) { loginClick(); }
		});
		button_skip.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) { skipClick(); }
		});
		button_register.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) { registerClick(); }
		});
		button_clear.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) { clearClick(); }
		});

		String username = GlobalContext.username;
		String password = GlobalContext.password;
		String remember = GlobalContext.remember;
		if (
				remember != null &&
				remember.equals("1") &&				
				username != null &&
				!username.equals("")
		) {
			edittext_username.setText(username);
			edittext_password.setText(password);
			checkbox_remember.setChecked(true);
		}
	}
	
	private void rememberClick() {
		GlobalContext.remember = checkbox_remember.isChecked()? "1": "0";
	}

	@SuppressWarnings("unchecked")
	private void loginClick() {
		String username = edittext_username.getText().toString();
		String password = edittext_password.getText().toString();
		
		Hashtable<String, String> data = new Hashtable<String, String>();
		// username
		if (!Utils.checkUsernameFormat(username)) {
			Utils.messageBox(getCurrentActivity(), "Usuario no válido");
			return;
		}
		data.put("username", username);
		
		// password
		if (!Utils.checkPasswordFormat(password)) {
			Utils.messageBox(getCurrentActivity(), "Contraseña no válida");
			return;
		}
		data.put("password", password);
		
		(new AsyncTask<Hashtable<String, String>, Void, Boolean>() {
			@Override
			protected void onPreExecute() {
				pd = new ProgressDialog(getCurrentActivity());
				pd.setTitle("Comunicando con el servidor");
				pd.setMessage("Por favor, espere mientras verificamos sus credenciales...");
				pd.setCancelable(false);
				pd.setIndeterminate(true);
				pd.show();
			}
			@Override
			protected Boolean doInBackground(Hashtable<String, String>... data) {
				return WebServerProxy.login_user(data[0]);
			}
			@Override
			protected void onPostExecute(Boolean result) {
				if (pd!=null)
					pd.dismiss();
				if (result) {
					// Respuesta correcta por parte del servidor, entonces seguimos adelante
					if (GlobalContext.remember.equals("1")) {
						GlobalContext.setPreference(GlobalContext.PREF.USERNAME, edittext_username.getText().toString());
						GlobalContext.setPreference(GlobalContext.PREF.PASSWORD, edittext_password.getText().toString());
					}
					else {
						GlobalContext.setPreference(GlobalContext.PREF.USERNAME, "");
						GlobalContext.setPreference(GlobalContext.PREF.PASSWORD, "");
					}
					GlobalContext.setPreference(GlobalContext.PREF.REMEMBER, GlobalContext.remember);

					// no incluir las siguientes líneas dentro de: "GlobalContext.setPreference" porque
					// una una cosa es tener los datos disponibles durante la sesión actual
					// y otra cosa es tenerlos disponibles después de cerrar la sesión
					GlobalContext.username = edittext_username.getText().toString();
					GlobalContext.password = edittext_password.getText().toString();
					
					Utils.startActivity(getCurrentActivity(), screen03.class, true);
				}
				else {
				    AlertDialog ad = new AlertDialog.Builder(getCurrentActivity()).create();  
				    ad.setCancelable(false);  
				    ad.setMessage("Login incorrecto. Por favor, verifique su usuario y contraseña.");  
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
	
	private void skipClick() {
		Utils.startActivity(getCurrentActivity(), screen03.class);
	}

	private void registerClick() {
		Utils.startActivity(getCurrentActivity(), screen02.class);
	}
	
	private void clearClick() {
		edittext_username.setText("");
		edittext_password.setText("");
		checkbox_remember.setChecked(false);
	}
	
}
