package ca.sciencestudio.util.net;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Map;
import java.util.LinkedHashMap;

public abstract class QueryUtils {

	public static final String URL_DECODE_ENCODING = "UTF-8";
	public static final String URL_ENCODE_ENCODING = "UTF-8";
	
	public static String toQuery(Map<String,String[]> queryMap) {
		if(queryMap == null) {
			return "";
		}
		
		boolean first = true;
		StringBuilder query = new StringBuilder();
		for(Map.Entry<String,String[]> entry: queryMap.entrySet()) {
			String key = urlEncode(entry.getKey());
			for(String value : entry.getValue()) {
				if(first) {
					first = false;
				} else {
					query.append("&");
				}
				query.append(key);
				query.append("=");
				query.append(urlEncode(value));
			}
		}
		return query.toString();
	}
	
	public static String toSimpleQuery(Map<String,String> queryMap) {
		if(queryMap == null) {
			return "";
		}
		
		boolean first = true;
		StringBuilder query = new StringBuilder();
		for(Map.Entry<String,String> entry: queryMap.entrySet()) {			
			if(first) {
				first = false;
			} else {
				query.append("&");
			}
			query.append(urlEncode(entry.getKey()));
			query.append("=");
			query.append(urlEncode(entry.getValue()));
		}
		return query.toString();
	}
	
	public static Map<String,String[]> toMap(String query) {
		if(query == null) {
			return Collections.emptyMap();
		}
		
		String[] params = query.split("[&;]+");
		Map<String,String[]> map = new LinkedHashMap<String,String[]>();  
		for (String param : params)  {  
			String[] kv = param.split("=");
			if((kv.length > 0)) {
				String key = urlDecode(kv[0]);
				
				if(key.length() > 0) {
					String value = "";
					if(kv.length > 1) {
						value = urlDecode(kv[1]);
					}

					if(map.containsKey(key)) {
						String[] cvalues = map.get(key);
						int clength = cvalues.length;
						String[] nvalues = new String[clength + 1];
						System.arraycopy(cvalues, 0, nvalues, 0, clength);
						nvalues[clength] = value;
						map.put(key, nvalues);
					}
					else {
						map.put(key, new String[] { value });
					}
				}
			}
		}  
		return map;  
	}
	
	public static Map<String,String> toSimpleMap(String query) {
		if(query == null) {
			return Collections.emptyMap();
		}
		
		String[] params = query.split("[&;]+");  
		Map<String,String> map = new LinkedHashMap<String,String>();  
		for (String param : params)  { 
			String[] kv = param.split("=");
			if((kv.length > 0)) {
				String key = urlDecode(kv[0]);
			
				if(key.length() > 0) {
					String value = "";
					if(kv.length > 1) {
						value = urlDecode(kv[1]);
					}
					
					map.put(key, value);
				}
			}
		}  
		return map;  
	}
	
	public static String urlDecode(String query) {
		try {
			return URLDecoder.decode(query, URL_DECODE_ENCODING);
		}
		catch (UnsupportedEncodingException e) {
			return query;
		}
	}
	
	public static String urlEncode(String query) {
		try {
			return URLEncoder.encode(query, URL_ENCODE_ENCODING);
		}
		catch (UnsupportedEncodingException e) {
			return query;
		}
	}
}
