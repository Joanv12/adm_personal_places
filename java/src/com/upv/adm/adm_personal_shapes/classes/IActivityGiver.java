package com.upv.adm.adm_personal_shapes.classes;

import android.app.Activity;

// para tener una forma m�s c�moda de recuperar la activity actual
// por ejemplo dentro de m�todos que est�n dentro de clases anidadas
// por ejemplo, AsyncTask
public interface IActivityGiver {
	public Activity getCurrentActivity();
}
