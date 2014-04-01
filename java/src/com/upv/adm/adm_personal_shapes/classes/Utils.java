package com.upv.adm.adm_personal_shapes.classes;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
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
	
	private static String getXML(Context context, String path) {

		String xmlString = null;
		AssetManager am = context.getAssets();
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
	
	public static CustomListItem[] getListViewItemsFromXMLData(Context context, String xmlFileName, String itemName) {

		XMLParser parser = new XMLParser();
		String XMLContent = Utils.getXML(context, xmlFileName);
		Document doc = parser.getDomElement(XMLContent);
		 
		NodeList nl = doc.getElementsByTagName(itemName);
		CustomListItem[] result = new CustomListItem[nl.getLength()];
		
		for (int i = 0; i < nl.getLength(); i++) {
			Node itemNode = nl.item(i);	
			String key = itemNode.getAttributes().getNamedItem("value").getTextContent();
			String text = itemNode.getTextContent();
			result[i] = new CustomListItem(key, text);
		}
		
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
		Hashtable<String, String> types = new Hashtable<String, String>();
		types.put("t01", "Restaurante");
		types.put("t02", "Bar");
		types.put("t03", "Biblioteca");
		types.put("t04", "Parque");
		types.put("t05", "Gimnasio");
		return types.get(type);
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
	
}
