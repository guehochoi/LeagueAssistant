package statChecker;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class HttpURLCon {
	private final String USER_AGENT = "Mozilla/5.0";
	private static final String SPACE = "%20";

	public HttpURLCon () {
		File f = new File("results.txt");
		if (f.exists()) {
			f.delete();
		}
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param names
	 * @return HashMapping of summoner's ID to the Summoner Object
	 * @throws Exception
	 */
	public Map<String, Summoner> getSummonerIDByName(String[] names) throws Exception{
		StringBuilder sb = new StringBuilder();
		for (int i=0; i < names.length; i++) {
			sb.append(names[i]);
			if ( names.length != 1 || names.length-1 != i)
				sb.append(", ");
		}
		
		String nameURL = URLEncoder.encode(sb.toString(), "UTF-8").replace("+", SPACE);
		String url = 
			"https://na.api.pvp.net/api/lol/na/v1.4/summoner/by-name/"
			+ nameURL +"?api_key=18f24615-d0f4-4b80-aad8-30057d97d433";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		// optional default is GET
		con.setRequestMethod("GET");
 
		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Content-Type", "application/json");
 
		// check for response code, if not 200, there was an error, return -1
		int responseCode = con.getResponseCode();
		if (responseCode != 200)
			return new HashMap<String, Summoner>();
		
		// fetch the network buffer
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		Map<String, Summoner> map = new HashMap<String, Summoner>();
		
		// extract id from the result JSON object
		String resStr = response.toString();
		int summonerID;
		try {
			JSONObject root = new JSONObject (resStr);
			
			for (int i = 0; i < names.length; i++) {
				JSONObject summonerObj = root.getJSONObject(trimName(names[i]).toLowerCase());
				summonerID = summonerObj.getInt("id");
				map.put(names[i], new Summoner(names[i], summonerID));
			}
			
		} catch (JSONException e) {
			System.out.println(resStr);
		}
		return map;
	}
	
	private String trimName(String name) {
		StringBuilder sb = new StringBuilder(name);
		int index = sb.indexOf(" ");
		while (index > -1) {
			sb.deleteCharAt(sb.indexOf(" "));
			index = sb.indexOf(" ");
		}
		return sb.toString();
	}
	
	public HashMap<Integer, Champion> fetchChampions() {
		HashMap map = new HashMap<Integer, Champion>();
		String url = 
			"https://na.api.pvp.net/api/lol/static-data/na/v1.2/champion?api_key=18f24615-d0f4-4b80-aad8-30057d97d433";
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	 
			// optional default is GET
			con.setRequestMethod("GET");
	 
			//add request header
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Content-Type", "application/json");
	 
			// check for response code, if not 200, there was an error, return -1
			int responseCode = con.getResponseCode();
			if (responseCode != 200)
				return new HashMap<Integer, Champion>();
			// fetch the network buffer
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			// extract id from the result JSON object
			String resStr = response.toString();
			try {
				JSONObject root = new JSONObject(resStr);
				JSONObject data = root.getJSONObject("data");
				Iterator itr = data.keys();
				while(itr.hasNext()) {
					Object next = itr.next();
					JSONObject jo = data.getJSONObject(next.toString());
					map.put(jo.getInt("id"), new Champion(jo.getInt("id"), jo.getString("name")));
					
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			System.out.println(map);
		}catch(IOException ex) {
		}
		
		return map;
	}
	
	
	
	public static void main(String[] args) {
		HttpURLCon c = new HttpURLCon();
		try {
			String[] array = {"OG Simba", "frankdanktank", "nakedsnake9"};
			Map m = c.getSummonerIDByName(array);
			for (int i=0; i< array.length; i++) {
				System.out.println(m.get(array[i]));		
			}
			c.fetchChampions();
		} catch (Exception e) {
		}
	}
}
