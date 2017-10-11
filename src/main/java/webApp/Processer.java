package webApp;

import java.io.IOException;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

public class Processer {
	private static Gson gson = new Gson();
	private FileReader fr = new FileReader();
	private String marketJson;
	private static String currencyJson;
	
	public Processer(){
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
	
	public static HashMap<String,Object> getCurrencyHashMap() {
		HashMap<String,Object> result = gson.fromJson(currencyJson , HashMap.class);
		return result;
	}
	
	//Only for testing!
	public static void main(String[] args) {
		Processer p = new Processer();
//		Market[] d = p.getMarketGsonBean();
//		for(Market da : d) {
//			System.out.println(da.symbol);
//			System.out.println(da.currency);
//			System.out.println(da.latest_trade + "\n" );
//		}
		HashMap m = getCurrencyHashMap();
		System.out.println(m.get("SEK"));
	}
}

