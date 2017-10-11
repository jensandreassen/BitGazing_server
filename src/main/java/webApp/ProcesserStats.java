package webApp;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ProcesserStats {
	private static Gson gson = new Gson();
	private FileReader fr = new FileReader();
	private String marketJson;
	private static String currencyJson;
	
	public ProcesserStats(){
		try {
			this.marketJson = fr.readFile("files/MarketData.txt");
			this.currencyJson = fr.readFile("files/currencyRates.json");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getJsonString() {
		return this.marketJson;
	}
	
	public Market[] getMarketGsonBean() {
		Market[] data = gson.fromJson(marketJson, Market[].class);
		return data;
	}
	
	public Currency getCurrencyGsonBean() {
		Currency data = gson.fromJson(currencyJson, Currency.class);
		return data;
	}
	
	/**
	 * Skapar en Map av Json data.
	 * @return Map
	 */
	public static Map<String,String> getCurrencyHashMap() {
		Type type = new TypeToken<Map<String, String>>(){}.getType();
		Map<String, String> myMap = new Gson().fromJson(currencyJson, type);
		return myMap;
	}
	
	public static JSONObject getJson() {
		JSONObject jo = new JSONObject(currencyJson);
		return jo;
	}
	
	public JSONObject stringToJsonObject(String jsonString) {
		JSONObject jo = new JSONObject(jsonString);
		return jo;
	}
	
	//Only for testing!
	public static void main(String[] args) {
		ProcesserStats p = new ProcesserStats();
//		Market[] d = p.getMarketGsonBean();
//		for(Market da : d) {
//			System.out.println(da.symbol);
//			System.out.println(da.currency);
//			System.out.println(da.latest_trade + "\n" );
//		}
		JSONObject m = getJson();
		for(Object key : m.keySet()) {
			String keyStr = (String)key;
	        Object keyvalue = m.get(keyStr);

	        //Print key and value
	        System.out.println(""+ keyStr + ": " + keyvalue);
		}
		
		System.out.println("\n==============================================\n");
		
		String ratesStr = m.optString("rates");
		JSONObject currencies = p.stringToJsonObject(ratesStr);
		
		for(Object key : currencies.keySet()) {
		String keyStr = (String)key;
        Object keyvalue = currencies.get(keyStr);
        System.out.println(""+ keyStr + ": " + keyvalue);
	}
	}
}

