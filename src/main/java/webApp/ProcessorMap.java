package webApp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.exceptions.UnirestException;

//Hej kalle! 
//Hej Oscar!

/**
 * ProcessorMap hämtar data från extent API och bearbetar den.
 */
public final class ProcessorMap {

	public ProcessorMap() {}
	
	public static String readDataFromFile(String fileName) throws Exception {
		BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
		StringBuilder sb = new StringBuilder();
		while (true) {
			String word = r.readLine();
			if (word == null) {
				break;
			}
			sb.append(word);
		}
		return sb.toString();
	}

	//Anropas från Controller för att hämta ny data.
	
	//Data beskrivs som följande:
	//Total volym(BTC eller procent) per valuta(kartan)
	
	/**
	 * Metoden returnerar total BTC-handelsvolym per valuta.
	 * @throws IOException 
	 * @throws UnirestException 
	 */
	public static JSONObject getBTCVolumeByCurrency() throws Exception {
		//Fetch data
//		JSONArray allMarketsData = DataFetcher.fetchAllBTCMarkets();
		JSONArray markets = new JSONArray(readDataFromFile("files/MarketData.txt"));
		
		//Read and extract relevant market data.
		HashMap<String, Double> volumeByCurrencyMap = new HashMap<>();
		for (int i = 0; i < markets.length(); i++) {
			JSONObject market = markets.getJSONObject(i);
			String currency = market.getString("currency");
			double marketVolume = market.getDouble("volume");
			if (marketVolume > 0) {
				if(volumeByCurrencyMap.containsKey(currency)) {
					double currentVolume = volumeByCurrencyMap.get(currency);
					volumeByCurrencyMap.put(currency, new Double(currentVolume + marketVolume));
				} else {
					volumeByCurrencyMap.put(currency, new Double(marketVolume));
				}
			}
		}
		
		//Build JSONObject
		JSONObject jsonVolumeByCurrency = new JSONObject();
		for(String currency : volumeByCurrencyMap.keySet()) {
			double volume = volumeByCurrencyMap.get(currency);
			jsonVolumeByCurrency.put(currency, volume);
		}
		
		return jsonVolumeByCurrency;
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(ProcessorMap.getBTCVolumeByCurrency().toString());
	}
}
