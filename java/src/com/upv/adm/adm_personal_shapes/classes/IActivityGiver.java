package com.upv.adm.adm_personal_shapes.classes;

import android.app.Activity;

// para tener una forma más cómoda de recuperar la activity actual
// por ejemplo dentro de métodos que están dentro de clases anidadas
// por ejemplo, AsyncTask
public interface IActivityGiver {
	public Activity getCurrentActivity();
}
