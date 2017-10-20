package webApp;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class DataFetcher {
	
	//Förhindra instansiering
	private DataFetcher() {}
	
	/**
	 * Hämtar marknads-data om en specifik marknad från bitcoinaverage.com
	 * @param market
	 * @return
	 * @throws UnirestException
	 * @throws IOException
	 */
	public static JSONObject fetchBTCMarket(String market) throws UnirestException, IOException {
		HttpResponse<JsonNode> response = Unirest.get("https://apiv2.bitcoinaverage.com/exchanges/ticker/" + market)
		.header("accept", "application/json")
		.asJson();
		JSONObject marketData = response.getBody().getObject();
		return marketData;
	}
	
	/**
	 * Hämtar all marknads-data från bitcoincharts.com
	 * Anropa inte mer än 1 gång per 15min!
	 * @return
	 * @throws UnirestException
	 * @throws IOException
	 */
	public static JSONArray fetchAllBTCMarkets() throws UnirestException, IOException {
		HttpResponse<JsonNode> response = Unirest.get("http://api.bitcoincharts.com/v1/markets.json")
		.header("accept", "application/json")
		.asJson();
		JSONArray allMarkets = response.getBody().getArray();
		return allMarkets;
	}
	
	/**
	 * Hämtar valuta-kurser från openexchangerates.org
	 * @return
	 * @throws UnirestException
	 * @throws IOException
	 */
	public static JSONObject fetchCurrencyRates() throws UnirestException, IOException {
		String apiKey = "19c47c8596ba43bd9c7cf51a37928bc3";
		HttpResponse<JsonNode> response = Unirest.get("https://openexchangerates.org/api/latest.json")
		.header("accept", "application/json")
		.queryString("app_id", apiKey)
		.queryString("base", "USD") //Default: USD
		.queryString("prettyprint", "1") //Default: 1
		.asJson();
		JSONObject currencyRates = response.getBody().getObject();
		return currencyRates;
	}
	
	public static void main(String[] args) throws JSONException, UnirestException, IOException {
		//Hämta data och skriv ut i console.
//		System.out.println(fetchAllBTCMarkets().toString(2));
//		System.out.println(fetchBTCMarket("kraken").toString(2));
		System.out.println(fetchCurrencyRates().toString(2));
	}

}
