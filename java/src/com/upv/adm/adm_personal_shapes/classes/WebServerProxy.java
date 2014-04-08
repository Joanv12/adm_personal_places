package com.upv.adm.adm_personal_shapes.classes;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.json.JSONObject;

public class WebServerProxy {

	// Importante: El internet del móvil debe estar habilitado y el servidor web accesible desde el móvil (probar antes el acceso con un navegador)
	// NetworkOnMainThreadException exception will be thrown if the application attempts to perform a networking operation on its main thread
	// Usar AsyncTask:
	// http://developer.android.com/reference/android/os/AsyncTask.html
	// http://www.tutorialspoint.com/android/android_network_connection.htm
	
	/**
	 * Required library: commons-httpclient.jar (it works properly and no more libraries are needed)
	 */

	public static final String RESPONSE_ERROR_UNKNOWN = "error_unknown";
	
	public static JSONObject post(String url, ArrayList<StringPart> fields, ArrayList<FilePart> files) throws Exception {
		try {
			HttpClient httpClient = new HttpClient();
			PostMethod postMethod = new PostMethod(url);
			
			Part[] parts = new Part[fields.size()+((files!=null)?files.size():0)];
			for (int i = 0; i < fields.size(); i++)
				parts[i] = fields.get(i);
			for (int i = 0; files != null && i < files.size(); i++)
				parts[fields.size() + i] = files.get(i);

			postMethod.setRequestEntity(new MultipartRequestEntity(parts, postMethod.getParams()));
			httpClient.executeMethod(postMethod);
			if (postMethod.getStatusCode() == HttpStatus.SC_OK) {
				String response = postMethod.getResponseBodyAsString();
				JSONObject json = new JSONObject(response);
				return json;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String[] register_user(Hashtable<String, String> data) {

		String[] result = null;
		
		try {

			Properties props = Utils.getCustomProperties();
			final String post_url = ((String)props.get("server_url")) + "/includes/do.php";

			ArrayList<StringPart> fields = new ArrayList<StringPart>();
			ArrayList<FilePart> files = new ArrayList<FilePart>();

			fields.add(new StringPart("action", "register_user"));
			fields.add(new StringPart("username", data.get("username"), "UTF-8"));
			fields.add(new StringPart("password", data.get("password"), "UTF-8"));
			if (data.containsKey("password_old"))
				fields.add(new StringPart("password_old", data.get("password_old"), "UTF-8"));
			fields.add(new StringPart("name", data.get("name"), "UTF-8"));
			fields.add(new StringPart("gender", data.get("gender"), "UTF-8"));
			fields.add(new StringPart("email", data.get("email"), "UTF-8"));
			fields.add(new StringPart("phone", data.get("phone"), "UTF-8"));
			fields.add(new StringPart("country", data.get("country"), "UTF-8"));
			
			if (data.get("image") != null)
				files.add(new FilePart("image", new File(data.get("image"))));

			JSONObject json = post(post_url, fields, files);			
			
			if (json != null && json.has("status")) {
				result = new String[2];
				result[0] = json.getString("status");
				if (result[0].equals("error")) {
					result[1] = json.getString("error_reason");
				}
				else {
					if (data.get("image") != null)
						new File(data.get("image")).renameTo(new File(Utils.getAppDir(), "user_image.jpg"));
					else {
						File user_image = new File(Utils.getAppDir(), "user_image.jpg");
						if (user_image.exists())
							user_image.delete();
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static Boolean login_user(Hashtable<String, String> data) {

		try {

			Properties props = Utils.getCustomProperties();
			final String post_url = ((String)props.get("server_url")) + "/includes/do.php";

			ArrayList<StringPart> fields = new ArrayList<StringPart>();
			
			fields.add(new StringPart("action", "login_user"));
			fields.add(new StringPart("username", data.get("username"), "UTF-8"));
			fields.add(new StringPart("password", data.get("password"), "UTF-8"));
			
			JSONObject json = post(post_url, fields, null);			
			
			if (json != null && json.has("status")) {
				if (json.getString("status").equals("success")) {
					JSONObject user_data = json.getJSONObject("data");
					String name = user_data.getString("name");
					String gender = user_data.getString("gender");
					String email = user_data.getString("email");
					String phone = user_data.getString("phone");
					String country_iso2 = user_data.getString("country_iso2");
					String image_uri = user_data.getString("image_uri");
					
					GlobalContext.setPreference(GlobalContext.PREF.NAME, name);
					GlobalContext.setPreference(GlobalContext.PREF.GENDER, gender);
					GlobalContext.setPreference(GlobalContext.PREF.EMAIL, email);
					GlobalContext.setPreference(GlobalContext.PREF.PHONE, phone);
					GlobalContext.setPreference(GlobalContext.PREF.COUNTRY, country_iso2);
					
					String server_url = Utils.getCustomProperty("server_url");
					if (image_uri != null && !image_uri.equals(""))
						Utils.downloadFile(server_url+"/images/"+data.get("username")+".jpg", new File(Utils.getAppDir(), "user_image.jpg").getPath());  
					
					return true;
				}
				else
					return false;
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public static void add_friends(ArrayList<String> ids_list) {

		try {

			String ids_list_str = Utils.ArrayListToStringComma(ids_list);
			
			Properties props = Utils.getCustomProperties();
			final String post_url = ((String)props.get("server_url")) + "/includes/do.php";

			ArrayList<StringPart> fields = new ArrayList<StringPart>();

			fields.add(new StringPart("action", "register_friendship"));
			fields.add(new StringPart("username", GlobalContext.username));
			fields.add(new StringPart("password", GlobalContext.password));
			fields.add(new StringPart("ids_list", ids_list_str));
			fields.add(new StringPart("is_adding", "1"));
			
			post(post_url, fields, null);

		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

}
