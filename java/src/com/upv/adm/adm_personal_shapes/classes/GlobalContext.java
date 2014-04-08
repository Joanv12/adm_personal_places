package com.upv.adm.adm_personal_shapes.classes;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;

public class GlobalContext {

	private static Context context = null;
	private static CustomLocationListener cll;
	
	public static String username = null;
	public static String password = null;
	public static String remember = null; // {"0", "1"}
	
	public static Long shape_id = null;
	
	private static CustomListItem[] countriesListviewItems = null;
	private static CustomListItem[] gendersListviewItems = null;
	private static CustomListItem[] layersData = null;
	private static CustomListItem[] typesData = null;
	private static CustomListItem[] plotsData = null;

	public static class PREF {

		public static final String USERNAME	= "USERNAME";
		public static final String PASSWORD	= "PASSWORD";
		public static final String EMAIL	= "EMAIL";
		public static final String PHONE	= "PHONE";
		public static final String NAME		= "NAME";
		public static final String GENDER	= "GENDER";
		public static final String COUNTRY	= "COUNTRY";
		public static final String REMEMBER	= "REMEMBER";
		
		public static String[] getPrefs() {
			return new String[]{ USERNAME, PASSWORD, REMEMBER };
		}
	}
	
	public static void init(Context context) {
		if (GlobalContext.context == null) {
			GlobalContext.context = context;
			username = GlobalContext.getPreference(GlobalContext.PREF.USERNAME);
			password = GlobalContext.getPreference(GlobalContext.PREF.PASSWORD);
			remember = GlobalContext.getPreference(GlobalContext.PREF.REMEMBER);
			SQLite.staticInitialization();
			cll = new CustomLocationListener();
			File app_dir = new File(Utils.getAppDir());
			if (!app_dir.exists())
				app_dir.mkdir();
		}
	}

	public static Context getContext() {
		return context;
	}

	/**
	 * @param key GlobalContext.PREF.{keys}
	 */
	public static void setPreference(String key, String value) {
		SharedPreferences preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	/**
	 * @param key GlobalContext.PREF.{keys}
	 */
	public static String getPreference(String key) {
		SharedPreferences preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
		return preferences.getString(key, "");
	}

	/**
	 * @param key GlobalContext.PREF.{keys}
	 */
	public static void removePreference(String key) {
		SharedPreferences preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.remove(key);
		editor.commit();
	}

	public static void removeAllPreferences() {
		SharedPreferences preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
	}

	@SuppressWarnings("unchecked")
	public static Hashtable<String, String> getPreferences() {
		return (Hashtable<String, String>)context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
	}

	public static CustomListItem[] getCountriesListviewItems() {
		if (countriesListviewItems == null)
			countriesListviewItems = Utils.getListViewItemsFromXMLData("countries.xml");
		return countriesListviewItems;
	}
	
	public static CustomListItem[] getGendersListviewItems() {
		if (gendersListviewItems == null)
			gendersListviewItems = Utils.getListViewItemsFromXMLData("genders.xml");
		return gendersListviewItems;
	}
	
	public static CustomListItem[] getLayersData() {
		if (layersData == null)
			layersData = Utils.getListViewItemsFromXMLData("layers.xml");
		return layersData;
	}
	
	public static CustomListItem[] getPlotsData() {
		if (plotsData == null) {
			ArrayList<BeanShape> plots = SQLite.getPlots();
			plotsData = new CustomListItem[plots.size()];
			for (int i = 0; i < plots.size(); i++)
				plotsData[i] = new CustomListItem(String.valueOf(plots.get(i).getId()), plots.get(i).getName());
		}
		return plotsData;
	}
	
	public static CustomListItem[] getTypesData() {
		if (typesData == null)
			typesData = Utils.getListViewItemsFromXMLData("types.xml");
		return typesData;
	}
	
	public static CustomLocationListener getCustomLocationListener() {
		return cll;
	}
	
}
