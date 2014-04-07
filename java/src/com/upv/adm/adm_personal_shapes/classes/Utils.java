package com.upv.adm.adm_personal_shapes.classes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.google.zxing.BarcodeFormat;
import com.upv.adm.adm_personal_shapes.screens.screen01;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.SparseBooleanArray;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.Spinner;

public class Utils {

	public static ArrayList<String> getSelectedKeys(ListView listView) {
		ArrayList<String> selectedKeys = new ArrayList<String>();
		SparseBooleanArray checked = listView.getCheckedItemPositions();
        for (int i = 0; i < checked.size(); i++) {
            int position = checked.keyAt(i);
            if (checked.valueAt(i))
                selectedKeys.add(((CustomListItem)listView.getItemAtPosition(position)).getKey());
        }				
        return selectedKeys;
	}
	
	public static String getSelectedKey(Spinner spinner) {
		return ((CustomListItem)spinner.getSelectedItem()).getKey();
	}
	
	
	public static void setSelectedKey(Spinner spinner, String key) {
		for (int i = 0; i < spinner.getCount(); i++) {
			if (((CustomListItem)spinner.getItemAtPosition(i)).getKey().equals(key)) {
				spinner.setSelection(i);
				return;
			}
		}

	}
	
	private static String getXML(String path) {

		String xmlString = null;
		AssetManager am = GlobalContext.getContext().getAssets();
		try {
			InputStream is = am.open(path);
			int length = is.available();
			byte[] data = new byte[length];
			is.read(data);
			xmlString = new String(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return xmlString;
	}
	
	public static ArrayList<String[]> getArrayListFromXMLData(String xmlFileName) {
		XMLParser parser = new XMLParser();
		String XMLContent = Utils.getXML(xmlFileName);
		Document doc = parser.getDomElement(XMLContent);
		NodeList nl = doc.getFirstChild().getChildNodes();
		ArrayList<String[]> result = new ArrayList<String[]>();
		for (int i = 0; nl != null && i < nl.getLength(); i++) {
			if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Node itemNode = nl.item(i);	
				String key = itemNode.getAttributes().getNamedItem("value").getTextContent();
				String text = itemNode.getTextContent();
				result.add(new String[]{key, text});
			}
		}
		return result;
	}
	
	public static CustomListItem[] getListViewItemsFromXMLData(String xmlFileName) {
		ArrayList<String[]> data = getArrayListFromXMLData(xmlFileName);
		CustomListItem[] result = new CustomListItem[data.size()];
		for (int i = 0; i < data.size(); i++)
			result[i] = new CustomListItem(data.get(i)[0], data.get(i)[1]);
		return result;
	}
	
	public static Properties getCustomProperties() {
		
		Properties props = null;
		
		Resources resources = GlobalContext.getContext().getResources();
		AssetManager assetManager = resources.getAssets();

		try {
			// el archivo: "custom.properties" está ubicado en el directorio: /assets
		    InputStream inputStream = assetManager.open("custom.properties");
		    props = new Properties();
		    props.load(inputStream);
		}
		catch (IOException e) {
		    e.printStackTrace();
		}
		
		return props;
		
	}
	
	public static String ArrayListToStringComma(ArrayList<String> arrayList) {
		String result = "";
		for (int i = 0; i < arrayList.size(); i++)
			result += arrayList.get(i) + ", ";
		if (result.length() != 0)
			result = result.substring(0, result.length()-2);
		return result;
	}
	
	public static String parsePlaceType(String type) {
		ArrayList<String[]> types = getTypes();
		for (int i = 0; i < types.size(); i++)
			if (types.get(i)[0].equals(type))
				return types.get(i)[1];
		return null;
	}

	public static void addLayerToMap(String layer, WebView webview) {
		String url = String.format("javascript: add_layer('%s')", layer);
		webview.loadUrl(url);
	}
	public static void addBeanPlaceToMap(BeanShape place, WebView webview) {
		String coords = place.getCoords();
		coords = coords.replaceAll("\\s+","");
		String[] coordsArr = coords.split(",");
		String name = place.getName();
		String type = place.getType();
		String description = place.getDescription();
		
		type = Utils.parsePlaceType(type);
		
		String url = String.format("javascript: add_place(%s, %s, '%s', '%s', '%s')",
				coordsArr[0],
				coordsArr[1],
				name,
				type,
				description
		);
		
		webview.loadUrl(url);
	}
	
	public static void addBeanPlotToMap(BeanShape plot, WebView webview) {
		String coords = plot.getCoords();
		coords = coords.replaceAll("\\s+","");
		String name = plot.getName();
		String description = plot.getDescription();
		
		String url = String.format("javascript: add_plot('%s', '%s', '%s')",
				coords,
				name,
				description
		);
		
		webview.loadUrl(url);
	}

	public static void clearMap(WebView webview) {
		webview.loadUrl("javascript: initialize_map()");		
	}
	
	public static void fixLastPoint(WebView webview) {
		webview.loadUrl("javascript: fixLastPoint()");		
	}
	
	public static void deleteLastPoint(WebView webview) {
		webview.loadUrl("javascript: deleteLastPoint()");		
	}
	
	public static void placeMarker(WebView webview, double lat, double lng) {
		String url = "javascript: placeMarker(" + lat + ", " + lng + ");";
		webview.loadUrl(url);
	}
	
	public static ArrayList<String[]> getTypes() {
		return getArrayListFromXMLData("types.xml");		
	}
	
	public static ArrayList<String[]> getLayers() {
		return getArrayListFromXMLData("layers.xml");		
	}
	
	public static boolean isKeyInsideArrayList(ArrayList<String[]> arraylist, String key) {
		for (int i = 0; i < arraylist.size(); i++)
			if (arraylist.get(i)[0].equals(key))
				return true;
		return false;
	}
	
	public static String getTextFromQR(Bitmap bitmap) {
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			byte[] imageData = stream.toByteArray();			
			return QRCodeDecoder.decode(imageData);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Bitmap getQRFromText(String text) {
		try {
			QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(text, null, Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(), 300);
			return qrCodeEncoder.encodeAsBitmap();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getTempFilePathFromBitmap(Bitmap bitmap) {
		try {
			File tempFile = File.createTempFile("temp_file_", ".jpg");
			FileOutputStream fos = new FileOutputStream(tempFile);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.close();
			return tempFile.getPath();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// Opcionalmente usar junto con: Utils.getTempFilePathFromBitmap(bitmap)
	public static Bitmap getQRFromPlace(BeanShape place) {
		String name = place.getName();
		name = name.substring(0, 40);
		String desc = place.getDescription();
		desc = desc.substring(0, 100);
		String type = place.getType();
		String coords = place.getCoords();
		String[] coordsArr = coords.split(";");
		coords = coordsArr[0];
		String text = name + "||" + desc + "||" + type + "||" + coords;
		return getQRFromText(text);
	}

	public static BeanShape getPlaceFromQR(Bitmap bitmap) {
		String text = getTextFromQR(bitmap);
		String[] data = text.split("\\|\\|");
		String name = data[0];
		String desc = data[1];
		String type = data[2];
		String coords = data[3];
		return new BeanShape(null, name, desc, type, coords, null);
	}

	public static CustomListItem[] ArrayListToCustomListItemArray(ArrayList<IListItem> arrayList) {
		CustomListItem[] result = new CustomListItem[arrayList.size()];
		for (int i = 0; i < arrayList.size(); i++)
			result[i] = new CustomListItem(arrayList.get(i).getKey(), arrayList.get(i).getValue());
		return result;
	}
	
	public static void GPSTurnOn() {
		Context context = GlobalContext.getContext();
		Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
		intent.putExtra("enabled", true);
		context.sendBroadcast(intent);
		String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if (!provider.contains("gps")) { //if gps is disabled
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("3")); 
			context.sendBroadcast(poke);
		}
    }

	public static void GPSTurnOff() {
		Context context = GlobalContext.getContext();
	    String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
	    if(provider.contains("gps")) {
	        final Intent poke = new Intent();
	        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
	        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	        poke.setData(Uri.parse("3")); 
	        context.sendBroadcast(poke);
	    }
	}

	public static String getAppDir() {
		String app_directory_name = (String)Utils.getCustomProperties().get("app_directory_name");
		return new File(Environment.getExternalStorageDirectory(), app_directory_name).getPath();
	}
	
	public static String str(int id) {
		return GlobalContext.getContext().getResources().getString(id);
	}
	
	public static boolean isUserLoggedIn() {
		return (!GlobalContext.username.equals("") && !GlobalContext.password.equals(""));
	}
	
	public static void logoutUser() {
		GlobalContext.username = "";
		GlobalContext.password = "";
		GlobalContext.remember = "";
		GlobalContext.removePreference(GlobalContext.PREF.USERNAME);
		GlobalContext.removePreference(GlobalContext.PREF.PASSWORD);
		GlobalContext.removePreference(GlobalContext.PREF.REMEMBER);
	}
	
	public static void startActivity(Activity fromActivity, Class<?> toCls, boolean clearStack) {
		Intent intent = new Intent(fromActivity, toCls);
		if (clearStack)
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		else
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		fromActivity.startActivityForResult(intent, 12345);
	}
	
	public static void startActivity(Activity fromActivity, Class<?> toCls) {
		startActivity(fromActivity, toCls, false);
	}
	
	public static void confirmExit(final Activity activity) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage("¿Realmente deseas salir de la aplicación?");
		builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	        	activity.setResult(CustomActionBarActivity.ACTIVITYRESULT_FINISH);
	        	activity.finish();
	        }

	    });
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	        }
	    });
		builder.show();
	}

	public static boolean checkRegex(String value, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}
	
	public static boolean checkUsernameFormat(String value) {
		return checkRegex(value, "^[a-zA-Z][a-zA-Z0-9\\_\\-]{2,19}$");
	}
	
	public static boolean checkPasswordFormat(String value) {
		return checkRegex(value, "^[a-zA-Z0-9]{4,20}$");
	}
	
	public static boolean checkNameFormat(String value) {
		return checkRegex(value, "^[^0-9].{0,49}$");
	}
	
	public static boolean checkEmailFormat(String value) {
		return checkRegex(value, "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$");
	}
	
	public static boolean checkPhoneFormat(String value) {
		return checkRegex(value, "^[0-9]{4,20}");
	}
	
	public static void messabeBox(final Activity activity, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(message);
		builder.setNeutralButton("Aceptar", null);
		builder.show();
	}
	
}
