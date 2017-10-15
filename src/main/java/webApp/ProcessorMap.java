package webApp;

import java.io.IOException;

import org.json.JSONObject;

import com.mashape.unirest.http.exceptions.UnirestException;

//Hej kalle! 
//Hej Oscar!

/**
 * ProcessorMap hämtar data från extent API och bearbetar den.
 */
public class ProcessorMap {
	
	//Anropas från Controller för att hämta ny data.
	
	//Data beskrivs som följande:
	//Total volym(BTC eller procent) per valuta(kartan)
	
	/**
	 * Metoden returnerar total BTC-handelsvolym per valuta.
	 * @throws IOException 
	 * @throws UnirestException 
	 */
	public static void getBTCVolumeByCurrency() throws UnirestException, IOException {
		JSONObject allMarketsData = DataFetcher.fetchAllBTCMarkets();
		
	}

}
