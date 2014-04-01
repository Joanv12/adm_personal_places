package com.upv.adm.adm_personal_shapes.classes;

import java.util.Hashtable;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class GlobalContext {

	private static Context context = null;
	public static Hashtable<String, String> preferences = null;
	
	public static String username;
	public static String password;
	
	public static Long shape_id;
	
	public static Activity screen01;
	public static CustomActionBarActivity screen02;
	
	private static CustomListItem[] countriesListviewItems = null;
	private static CustomListItem[] gendersListviewItems = null;
	private static CustomListItem[] typesData = null;

	public static class PREF {

		public static final String USERNAME	= "USERNAME";
		public static final String PASSWORD	= "PASSWORD";
		public static final String REMEMBER	= "REMEMBER";
		
		public static String[] getPrefs() {
			return new String[]{ USERNAME, PASSWORD, REMEMBER };
		}
	}
	
	public static void setContext(Context context) {
		if (GlobalContext.context == null)
			GlobalContext.context = context;
	}

	public static Context getContext() {
		return context;
	}

	/**
	 * @param key GlobalContext.PREF.{keys}
	 */
	public static void setPreference(String key, String value) {
		if (preferences == null)
			preferences = getPreferences();
		preferences.put(key, value);
		savePreference(context, key, value);
	}
	
	/**
	 * @param key GlobalContext.PREF.{keys}
	 */
	public static String getPreference(String key) {
		if (preferences == null)
			preferences = getPreferences();
		return preferences.get(key);
	}

	private static void savePreference(Context context, String key, String value) {
		SharedPreferences preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	private static Hashtable<String, String> getPreferences() {
		Context context = GlobalContext.getContext();
		Hashtable<String, String> result = new Hashtable<String, String>();
		SharedPreferences preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
		if (preferences != null) {
			String[] prefs = PREF.getPrefs();
			for (int i = 0; i < prefs.length; i++)
				result.put(prefs[i], preferences.getString(prefs[i], ""));
		}
		return result;
	}

	public static CustomListItem[] getCountriesListviewItems() {
		if (countriesListviewItems == null)
			countriesListviewItems = Utils.getListViewItemsFromXMLData(context, "countries.xml", "country");
		return countriesListviewItems;
	}
	
	public static CustomListItem[] getGendersListviewItems() {
		if (gendersListviewItems == null)
			gendersListviewItems = Utils.getListViewItemsFromXMLData(context, "genders.xml", "gender");
		return gendersListviewItems;
	}
	
	public static CustomListItem[] getTypesData() {
		if (typesData == null)
			typesData = Utils.getListViewItemsFromXMLData(context, "types.xml", "type");
		return typesData;
	}
}
