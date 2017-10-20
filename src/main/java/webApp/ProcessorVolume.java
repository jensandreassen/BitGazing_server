package webApp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.exceptions.UnirestException;


/**
 * Klassen hämtar data från extent API och bearbetar den.
 */
public final class ProcessorVolume {

	//Förhindrar instansiering av klassen.
	private ProcessorVolume() {}
	
	/**
	 * Läs in data från en text-fil.
	 * @param fileName
	 * @return
	 * @throws IOException 
	 */
	private static String readDataFromFile(String fileName) throws IOException {
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

	/**
	 * Metoden returnerar total BTC-handelsvolym per valuta sammantaget från flera markets.
	 * @return Ett JSONObject.
	 */
	public static JSONObject getBTCVolumeByCurrency() throws JSONException, IOException, UnirestException {
		//Fetch data from API or text-file
//		JSONArray markets = DataFetcher.fetchAllBTCMarkets();
		JSONArray markets = new JSONArray(readDataFromFile("files/MarketData.txt"));
		
		//Read and extract relevant market data.
		Map<String, Double> volumeByCurrencyMap = new TreeMap<>();
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
		//Kör för att få exempel på utdata.
		System.out.println(ProcessorVolume.getBTCVolumeByCurrency().toString(2));
	}
}
