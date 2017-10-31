package webApp;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * This class contains methods for fetching data from three different APIs.
 * @author Kalle Paradis
 */
public class DataFetcher {
	
	//Prevent instantiation of this class since all methods are static.
	private DataFetcher() {}
	
	/**
	 * Fetches data about a specific market using bitcoinaverage.com API.
	 * @param market The market to fetch data about.
	 * @return A JSONObject containing the result.
	 */
	public static JSONObject fetchBTCMarket(String market) throws UnirestException, IOException {
		HttpResponse<JsonNode> response = Unirest.get("https://apiv2.bitcoinaverage.com/exchanges/ticker/" + market)
		.header("accept", "application/json")
		.asJson();
		JSONObject marketData = response.getBody().getObject();
		return marketData;
	}
	
	/**
	 * Fetches data about all markets using bitcoincharts.com API.
	 * @return A JSONArray containing the result.
	 */
	public static JSONArray fetchAllBTCMarkets() throws UnirestException, IOException {
		HttpResponse<JsonNode> response = Unirest.get("http://api.bitcoincharts.com/v1/markets.json")
		.header("accept", "application/json")
		.asJson();
		JSONArray allMarkets = response.getBody().getArray();
		return allMarkets;
	}
	
	/**
	 * Fetches currency exchange rates from openexchangerates.org API where USD is the base currency. 
	 * @return A JSONObject containing the result.
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
		//Fetch data and print result in console(for tesing).
//		System.out.println(fetchAllBTCMarkets().toString(2));
//		System.out.println(fetchBTCMarket("kraken").toString(2));
		System.out.println(fetchCurrencyRates().toString(2));
	}

}
