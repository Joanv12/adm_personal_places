/* Ejemplo tomado de:
 * http://www.androidhive.info/2013/09/android-sqlite-database-with-multiple-tables
 * Para manipular el contenido de la base de datos, usar el: Navicat for SQLite junto con: edit_database.bat
 * Es muy importante llamar al método: getWritableDatabase() dentro del onCreate() para que en caso
 * de que la base de datos no exista pues que se cree. También tener en cuenta que se forzará la re-creación
 * de la base de datos si el valor de DATABASE_VERSION ha cambiado (aunque la base de datos exista)
 */

package com.upv.adm.adm_personal_shapes.classes;

import java.util.ArrayList;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLite {

	private static SQLiteDatabase db = null;

	private static final int DATABASE_VERSION = 71;
	private static final String DATABASE_NAME = "personal_shapes.db";
	
	private static final String TABLE_SHAPES = "shapes";
	private static final String TABLE_VISIBLE_PLACETYPES = "visible_placetypes";
	private static final String TABLE_VISIBLE_LAYERS = "visible_layers";
	
	private static final String FIELD_ID = "id";
	private static final String FIELD_NAME = "name";
	private static final String FIELD_DESCRIPTION = "description";
	private static final String FIELD_PLACETYPE = "place_type";
	private static final String FIELD_COORDS = "coords";
	private static final String FIELD_PHOTO = "photo";
	private static final String FIELD_VISIBLE = "visible";

	private static final String CREATE_TABLE_VISIBLE_LAYERS =
			"CREATE TABLE " + TABLE_VISIBLE_LAYERS + " (" +
			FIELD_ID + " INTEGER PRIMARY KEY)";

	private static final String CREATE_TABLE_VISIBLE_PLACETYPES =
			"CREATE TABLE " + TABLE_VISIBLE_PLACETYPES + " (" +
			FIELD_PLACETYPE + " CHAR(3) PRIMARY KEY)";

	private static final String CREATE_TABLE_SHAPES =
			"CREATE TABLE " + TABLE_SHAPES + " (" +
			FIELD_ID + " INTEGER PRIMARY KEY, " +	FIELD_NAME + " TEXT, " +
			FIELD_DESCRIPTION + " TEXT, " + FIELD_PLACETYPE + " CHAR(3), " +
			FIELD_COORDS + " TEXT, " + FIELD_PHOTO + " TEXT, " + FIELD_VISIBLE + " INTEGER)";

	public static void staticInitialization() {
		if (SQLite.db == null) {
			SQLiteOpenHelper openHelper = new SQLiteOpenHelper(GlobalContext.getContext(), DATABASE_NAME, null, DATABASE_VERSION) {
				@Override
				public void onCreate(SQLiteDatabase db) {
					createDatabase(db);
				}
				@Override
				public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
					upgradeDatabase(db, oldVersion, newVersion);
				}
			};
			SQLite.db = openHelper.getWritableDatabase();
		}
	}

	private static void createDatabase(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_VISIBLE_LAYERS);
		db.execSQL(CREATE_TABLE_VISIBLE_PLACETYPES);
		db.execSQL(CREATE_TABLE_SHAPES);
		
		load_data_debug(db);
	}

	private static void load_data_debug(SQLiteDatabase db) {
		try {
			db.execSQL("INSERT INTO shapes VALUES ('1', 'place 01', 'desc place 01', 't01', '1,2', 'photo01', NULL)");
			db.execSQL("INSERT INTO shapes VALUES ('2', 'place 02', 'desc place 02', 't02', '2,3', 'photo02', NULL)");
			db.execSQL("INSERT INTO shapes VALUES ('3', 'place 03', 'desc place 03', 't03', '3,4', 'photo03', NULL)");
			db.execSQL("INSERT INTO shapes VALUES ('4', 'plot 01', 'desc plot 01', NULL, '1,2', 'photo01', '1')");
			db.execSQL("INSERT INTO shapes VALUES ('5', 'plot 02', 'desc plot 02', NULL, '2,3', 'photo02', NULL)");
			db.execSQL("INSERT INTO shapes VALUES ('6', 'plot 03', 'desc plot 03', NULL, '3,4', 'photo03', '1')");
			
			db.execSQL("INSERT INTO visible_placetypes VALUES ('t02')");
			db.execSQL("INSERT INTO visible_placetypes VALUES ('t03')");
			
			db.execSQL("INSERT INTO visible_layers VALUES ('2')");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void upgradeDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_VISIBLE_LAYERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_VISIBLE_PLACETYPES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHAPES);
		createDatabase(db);
	}
	
	public static long saveShape(BeanShape shape) {
		ContentValues values = new ContentValues();
		values.put(FIELD_NAME, shape.getName());
		values.put(FIELD_DESCRIPTION, shape.getDescription());
		values.put(FIELD_PLACETYPE, shape.getType());
		values.put(FIELD_COORDS, shape.getCoords());
		values.put(FIELD_PHOTO, shape.getPhoto());
		Long shape_id = shape.getId();
		if (shape_id == null)
			shape_id = db.insert(TABLE_SHAPES, null, values);
		else {
			int result = db.update(TABLE_SHAPES, values, FIELD_ID + "=" + shape.getId(), null);
			if (result == 0)
				shape_id = (long)-1;
		}
		return shape_id;
	}

	public static ArrayList<BeanShape> getShapes(String selectQuery, String condition) {
		ArrayList<BeanShape> shapes = new ArrayList<BeanShape>();
		if (condition == null || condition.equals(""))
			condition = "";
		if (selectQuery == null || selectQuery.equals(""))
			selectQuery =
				"SELECT * FROM " + TABLE_SHAPES + " "
				+ condition + " ORDER BY " + FIELD_NAME + " ASC";
		Cursor c = db.rawQuery(selectQuery, null);
		while (c.moveToNext()) {
			BeanShape shape = new BeanShape(
					c.getLong(0), c.getString(1),
					c.getString(2),	c.getString(3),
					c.getString(4), c.getString(5)
			);
			shapes.add(shape);
		}
		return shapes;
	}
	
	public static ArrayList<BeanShape> getShapes() {
		return getShapes(null, null);
	}
	
	public static ArrayList<BeanShape> getShapes(String condition) {
		return getShapes(null, condition);
	}
	
	public static ArrayList<BeanShape> getPlaces() {
		return getPlaces(null);
	}

	public static ArrayList<BeanShape> getPlaces(String condition) {
		String cond_append = "";
		if (!(condition == null || condition.equals("")))
			cond_append = " AND " + condition;
		return getShapes(null, "WHERE " + FIELD_PLACETYPE + " IS NOT NULL" + cond_append);
	}

	public static ArrayList<BeanShape> getVisiblePlaces() {
		return getShapes("SELECT * FROM shapes INNER JOIN visible_placetypes ON shapes.place_type = visible_placetypes.place_type ORDER BY name ASC", null);
	}
	
	public static ArrayList<String> getVisiblePlaceTypes() {
		ArrayList<String> result = new ArrayList<String>();
		String selectQuery = "SELECT * FROM " + TABLE_VISIBLE_PLACETYPES + " ORDER BY " + FIELD_PLACETYPE + " ASC";
		Cursor c = db.rawQuery(selectQuery, null);
		while (c.moveToNext())
			result.add(c.getString(0));
		return result;
	}
	
	public static ArrayList<BeanShape> getPlots() {
		return getPlots(null);
	}

	public static ArrayList<BeanShape> getPlots(String condition) {
		String cond_append = "";
		if (!(condition == null || condition.equals("")))
			cond_append = " AND " + condition;
		return getShapes(null, "WHERE " + FIELD_PLACETYPE + " IS NULL" + cond_append);
	}

	public static ArrayList<BeanShape> getVisiblePlots() {
		return getPlots(FIELD_VISIBLE + " = '1'");
	}
	
	public static ArrayList<String> getVisibleLayers() {
		ArrayList<String> layers = new ArrayList<String>();
		String selectQuery = "SELECT * FROM " + TABLE_VISIBLE_LAYERS + " ORDER BY " + FIELD_ID + " ASC";
		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
				layers.add(String.valueOf(c.getLong(c.getColumnIndex(FIELD_ID))));
			} while (c.moveToNext());
		}
		return layers;
	}

	public static boolean isVisibleLayer(String layer) {
		String selectQuery = "SELECT * FROM " + TABLE_VISIBLE_LAYERS + " WHERE " + FIELD_ID + " = '" + layer + "'";
		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst())
			return true;
		return false;
	}
	
	public static void setLayerVisibility(String layer, boolean visible) {
		boolean visibility_old = isVisibleLayer(layer);
		if (visibility_old == visible)
			return;
		else if (visible)
			db.execSQL("INSERT INTO " + TABLE_VISIBLE_LAYERS + " VALUES ('" + layer + "')");
		else
			db.execSQL("DELETE FROM " + TABLE_VISIBLE_LAYERS + " WHERE " + FIELD_ID + " = '" + layer + "'");
	}
	
	public static boolean isVisiblePlaceType(String place_type) {
		String selectQuery = "SELECT * FROM " + TABLE_VISIBLE_PLACETYPES + " WHERE " + FIELD_PLACETYPE + " = '" + place_type + "'";
		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst())
			return true;
		return false;
	}
	
	public static void setPlaceTypeVisibility(String place_type, boolean visible) {
		boolean visibility_old = isVisiblePlaceType(place_type);
		if (visibility_old == visible)
			return;
		else if (visible)
			db.execSQL("INSERT INTO " + TABLE_VISIBLE_PLACETYPES + " VALUES ('" + place_type + "')");
		else
			db.execSQL("DELETE FROM " + TABLE_VISIBLE_PLACETYPES + " WHERE " + FIELD_PLACETYPE + " = '" + place_type + "'");
	}

	public static boolean isVisiblePlot(String plot_id) {
		String selectQuery = "SELECT * FROM " + TABLE_SHAPES + " WHERE " + FIELD_ID + " = '" + plot_id + "' AND " + FIELD_VISIBLE + " = '1' AND " + FIELD_PLACETYPE + " IS NULL";
		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst())
			return true;
		return false;
	}
	
	public static void setPlotVisibility(String plot_id, boolean visible) {
		boolean visibility_old = isVisiblePlot(plot_id);
		if (visibility_old == visible)
			return;
		db.execSQL("UPDATE " + TABLE_SHAPES + " SET " + FIELD_VISIBLE + " = " + ((visible)?"'1'":"NULL") + " WHERE " + FIELD_ID + " = '" + plot_id + "'");
	}
	
}
