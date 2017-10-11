package webApp;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Processes market data and currencies to become markets volume in one
 * currency.
 */
public class ProcesserStats {
	private static Gson gson = new Gson();
	private FileReader fr = new FileReader();
	private String marketJson;
	private String currencyJson;

	private String currentCurrency = "2.00";

	public ProcesserStats() {
		try {
			this.marketJson = fr.readFile("files/MarketData.txt");
			this.currencyJson = fr.readFile("files/currencyRates.json");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get all market data as Market array.
	 * 
	 * @return Market[]
	 */
	public Market[] getMarketGsonBeans() {
		Market[] data = gson.fromJson(marketJson, Market[].class);
		return data;
	}

	/**
	 * Creates a map out of currency data.
	 * 
	 * @return Map
	 */
	public Map<String, String> getCurrencyHashMap() {
		Type type = new TypeToken<Map<String, String>>() {
		}.getType();
		Map<String, String> myMap = new Gson().fromJson(currencyJson, type);
		return myMap;
	}

	/**
	 * Creates a JSONobject out of currency data.
	 * 
	 * @return
	 */
	public JSONObject getCurrencyJsonObject() {
		JSONObject jo = new JSONObject(currencyJson);
		return jo;
	}

	/**
	 * Creates a JSONObject out of json formatted String.
	 * 
	 * @param jsonString
	 * @return JSONObject
	 */
	public JSONObject stringToJsonObject(String jsonString) {
		JSONObject jo = new JSONObject(jsonString);
		return jo;
	}
	
	

	// Only for testing!
	public static void main(String[] args) {
		ProcesserStats p = new ProcesserStats();
		Market[] d = p.getMarketGsonBeans();
		for (Market da : d) {
			System.out.println(da.symbol);
			System.out.println(da.currency);
			System.out.println(da.latest_trade + "\n");
		}
		JSONObject m = p.getCurrencyJsonObject();
		for (Object key : m.keySet()) {
			String keyStr = (String) key;
			Object keyvalue = m.get(keyStr);

			// Print key and value
			System.out.println("" + keyStr + ": " + keyvalue);
		}

		System.out.println("\n==============================================\n");

		String ratesStr = m.optString("rates");
		JSONObject currencies = p.stringToJsonObject(ratesStr);

		for (Object key : currencies.keySet()) {
			String keyStr = (String) key;
			Object keyvalue = currencies.get(keyStr);
			System.out.println("" + keyStr + ": " + keyvalue);
		}
	}
}
