/**
 * 
 */
package com.ats;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.xml.sax.InputSource;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

/**
 * @author suhail
 *
 */
public class JsonParserUtil {
	
	static Set<String> keylist;

	public static List<String> getJsonPathsForGivenJson(String json) {
		
		keylist = new LinkedHashSet<String>();
		
		JSONObject jsonObjectExpected = new JSONObject(json);
		
		getAllKeys(jsonObjectExpected);
		
		List<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		List<String> paths = new ArrayList<String>();
		
		for(String i : keylist) {
			if(i != null) {
				Configuration conf = Configuration.builder().options(Option.AS_PATH_LIST).build();
				ArrayList<String> pathList = JsonPath.using(conf).parse(json).read("$.."+i+"");
				result.add(pathList);
			}
		}
		
		for(ArrayList<String> j : result) {
			for(String k : j) {
				paths.add(k);
			}
		}
		
		return paths;
	}

	private static String getAllKeys(JSONObject x) {
		JSONArray keys = x.names();
		for(int i = 0; i < keys.length(); i++) {
			String current_key = keys.get(i).toString();
			if(x.get(current_key).getClass().getName().equals("org.json.JSONObject")) {
				keylist.add(current_key);
				keylist.add(getAllKeys((JSONObject) x.get(current_key)));			
			}
			else if(x.get(current_key).getClass().getName().equals("org.json.JSONArray")) {
				for(int j = 0; j<((JSONArray) x.get(current_key)).length();j++) {
					if(((JSONArray) x.get(current_key)).get(j).getClass().getName().equals("org.json.JSONObject")){
						keylist.add(current_key);
						keylist.add(getAllKeys((JSONObject)((JSONArray) x.get(current_key)).get(j)));
					}else {
						keylist.add(current_key);
					}
				}
			} else {
				keylist.add(current_key);
			}
		}
		return null;
	}

	public static String getJsonFromFile(String fileLocation) {
		InputStream is = null;
		try {
			is = new FileInputStream(fileLocation);
		} catch (Exception e) {
			e.printStackTrace();
		
		}
		JSONTokener tokener = new JSONTokener(is);
		JSONObject object = new JSONObject(tokener);
		return object.toString();
	}
	
	

}
