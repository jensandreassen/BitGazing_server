package webApp;

import java.io.IOException;

import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class DataFetcher {
	
	/**
	 * Hämtar marknads-data som JSON från bitcoincharts.com
	 * Anropa inte mer än 1 gång per 15min!
	 * @return
	 * @throws UnirestException
	 * @throws IOException
	 */
	public static JSONObject fetchBitcoinMarketData() throws UnirestException, IOException {
		HttpResponse<JsonNode> response = Unirest.get("http://api.bitcoincharts.com/v1/markets.json")
		.header("accept", "application/json")
		.asJson(); // Berätta för Unirest att vi får tillbaka JSON.
		
		JsonNode json = response.getBody(); //JSON-objektet ligger gömt som en JsonNode i svaret
		JSONObject data = json.getObject();

		// Man måste stänga ner Unirest innan programmet avslutas
		Unirest.shutdown();
		
		return data;
	}

}
