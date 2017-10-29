package webApp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.exceptions.UnirestException;
/**
 * Acts as Controller and starting-class for the server-application.
 * @author Jens Andreassen, Kalle Paradis
 *
 */
public class Controller {

	private Map<String, JSONArray> marketPrices;
	private long marketLastTimeUpdated;
	private final long MARKET_INTERVAL_MILLIS = TimeUnit.HOURS.toMillis(5);
	
	private JSONObject volumeByCurrency;
	private long volumeLastTimeUpdated;
	private final long VOLUME_INTERVAL_MILLIS = TimeUnit.HOURS.toMillis(5);
	
	public Controller() {}

	/**
	 * Returns the price of different markets in <code>baseCurrency</code>.
	 * @param baseCurrency The base currency to display the price of the markets in.
	 * @return A JSONObject.
	 */
	public String getMarketPrices(String baseCurrency) throws FileNotFoundException, JSONException, IOException, UnirestException {
		if (marketPrices == null || marketLastTimeUpdated + MARKET_INTERVAL_MILLIS < System.currentTimeMillis()) {
			System.out.println("Updating marketPrices!");
			marketPrices = new ProcesserStats().marketMap();
			marketLastTimeUpdated = System.currentTimeMillis();
		} else if (!marketPrices.containsKey(baseCurrency)) {
			throw new FileNotFoundException(baseCurrency);	//404 Not Found
		}
		return marketPrices.get(baseCurrency).toString(2);
	}

	/**
	 * Returns total BTC-volume per currency, collected from multiple markets
	 * @return A JSONObject.
	 */
	public String getBTCVolumeByCurrency() throws JSONException, IOException, UnirestException {
		if (volumeByCurrency == null || volumeLastTimeUpdated + VOLUME_INTERVAL_MILLIS < System.currentTimeMillis()) {
			System.out.println("Updating volumes!");
			volumeByCurrency = ProcessorVolume.getBTCVolumeByCurrency();
			volumeLastTimeUpdated = System.currentTimeMillis();
		}
		return volumeByCurrency.toString(2);
	}

	public static void main(String[] args) {
		Controller cont = new Controller();
		Api api = new Api(cont);
	}
}
