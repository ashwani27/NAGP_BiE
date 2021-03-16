package rest.nagp.assignment.utilities;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JSONUtils {
	
	public static JSONObject getRequestBodyTemplate(String path) {
		String relativePath = System.getProperty("user.dir") + path;
		JSONParser parser = new JSONParser();
		Object obj = null;
		try {
			obj = parser.parse(new FileReader(relativePath));			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return (JSONObject) obj;		
	}
	
	@SuppressWarnings("unchecked")
	public static String getAddUserRequestBody(String login, String email, String pwd, String path) {		
		JSONObject baseObj =  (JSONObject) JSONUtils.getRequestBodyTemplate(path);
		JSONObject user = (JSONObject) baseObj.get("user");
		user.put("login", login);
		user.put("email", login + email);
		user.put("password", pwd);
		
		return baseObj.toJSONString();
	}
	
	@SuppressWarnings("unchecked")
	public static String updateUserRequestBody(String login, String email, String pwd, String path) {
		JSONObject baseObj =  (JSONObject) JSONUtils.getRequestBodyTemplate(path);
		JSONObject user = (JSONObject) baseObj.get("user");
		user.put("login", login);
		user.put("email", email);
		user.put("password", pwd);
		user.put("twitter_username", "");
		user.put("facebook_username", "");
		user.put("pic", "facebook");
		user.put("profanity_filter", false);
		user.put("public_themes", false);
		
		return baseObj.toJSONString();
	}
	
	public static String getUserSessionRequestBody(String login, String pwd, String path) {
		JSONObject baseObj =  (JSONObject) JSONUtils.getRequestBodyTemplate(path);
		JSONObject user = (JSONObject) baseObj.get("user");
		user.put("login", login);
		user.put("password", pwd);
		
		return baseObj.toJSONString();
	}
	
	public static String getAddQuoteRequestBody(String author, String body, String path) {
		JSONObject baseObj =  (JSONObject) JSONUtils.getRequestBodyTemplate(path);
		JSONObject user = (JSONObject) baseObj.get("quote");
		user.put("author", author);
		user.put("body", body);
		
		return baseObj.toJSONString();
	}
}