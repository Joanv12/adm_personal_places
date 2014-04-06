package com.upv.adm.adm_personal_shapes.screens;

import java.util.ArrayList;
import java.util.Hashtable;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import com.upv.adm.adm_personal_shapes.R;
import com.upv.adm.adm_personal_shapes.classes.CustomActionBarActivity;
import com.upv.adm.adm_personal_shapes.classes.WebServerProxy;
import com.upv.adm.adm_personal_shapes.classes.GlobalContext;
import com.upv.adm.adm_personal_shapes.classes.IActivityGiver;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
		super.onCreate(savedInstanceState, R.layout.screen01);
		GlobalContext.init(getApplicationContext());
		initControls();
		
		// guardarmos la screen01 en el contexto porque luego de que el usuario se haya logueado
		// no nos interesará más volver a a ella, y querremos invocar su método: finish().
		// No es necesario guardar todas las activities en el contexto, sino sólo aquellas
		// que se puedan necesitar más adelante
		GlobalContext.screen01 = this;
	}

	private void initControls() {
		
		edittext_username = (EditText) findViewById(R.id.edittext_username);
		edittext_password = (EditText) findViewById(R.id.edittext_password);
		checkbox_remember = (CheckBox) findViewById(R.id.checkbox_remember);
		button_login = (Button) findViewById(R.id.button_login);
		button_skip = (Button) findViewById(R.id.button_skip);
		button_register = (Button) findViewById(R.id.button_register);
		button_clear = (Button) findViewById(R.id.button_clear);
		
		edittext_username.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence seq, int start, int before, int count) {
				if (checkbox_remember.isChecked())
					GlobalContext.setPreference(GlobalContext.PREF.USERNAME, seq.toString());
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void afterTextChanged(Editable s) {}
		});
		
		edittext_password.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence seq, int start, int before, int count) {
				if (checkbox_remember.isChecked())
					GlobalContext.setPreference(GlobalContext.PREF.PASSWORD, seq.toString());
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void afterTextChanged(Editable s) {}
		});
		
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

		String username = GlobalContext.getPreference(GlobalContext.PREF.USERNAME);
		String password = GlobalContext.getPreference(GlobalContext.PREF.PASSWORD);
		String remember = GlobalContext.getPreference(GlobalContext.PREF.REMEMBER);
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
		String remember = checkbox_remember.isChecked()? "1": "0";
		GlobalContext.setPreference(GlobalContext.PREF.REMEMBER, remember);
		if (remember.equals("1")) {
			GlobalContext.setPreference(GlobalContext.PREF.USERNAME, edittext_username.getText().toString());
			GlobalContext.setPreference(GlobalContext.PREF.PASSWORD, edittext_password.getText().toString());
		}
		else {
			GlobalContext.setPreference(GlobalContext.PREF.USERNAME, "");
			GlobalContext.setPreference(GlobalContext.PREF.PASSWORD, "");
		}
	}

	@SuppressWarnings("unchecked")
	private void loginClick() {
		
		ArrayList<StringPart> fields = new ArrayList<StringPart>();
		fields.add(new StringPart("action", "login_user"));

		Hashtable<String, String> data = new Hashtable<String, String>();
		data.put("username", edittext_username.getText().toString());
		data.put("password", edittext_password.getText().toString());
		
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
					GlobalContext.username = edittext_username.getText().toString();
					GlobalContext.password = edittext_password.getText().toString();
					startActivity(new Intent(getApplicationContext(), screen03.class)); // abrimos (creación) la siguiente activity
					getCurrentActivity().finish(); // esta activity no se necesitará más, por tanto la cerramos
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
		startActivity(new Intent(getApplicationContext(), screen03.class));
		getCurrentActivity().finish();
	}

	private void registerClick() {
		startActivity(new Intent(getApplicationContext(), screen02.class));
	}
	
	private void clearClick() {
		edittext_username.setText("");
		edittext_password.setText("");
		checkbox_remember.setChecked(false);
	}
	
}
