package webApp;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;

public class Controller {

	final long marketIntervall = 18^6; // Ersätt med antal timmar till uppdatering
	final long currencyIntervall = 18^6; // Ersätt med antal timmar till uppdatering
	private JSONObject errorCode = new JSONObject("{ \"key1\": \"value1\",\"key2\": \"value2\" }");
	private JSONArray errorCodeArr = new JSONArray();


	private Map<String, MarketPricesWrapper> marketPricesMappedByCurrency = new TreeMap<>();
	private final long MARKET_INTERVAL_MILLIS = TimeUnit.HOURS.toMillis(5);
	
	private JSONObject volumeByCurrency;
	private long volumeLastUpdated;
	private final long VOLUME_INTERVAL_MILLIS = TimeUnit.HOURS.toMillis(5);
	
	
	private class MarketPricesWrapper {
		public JSONArray marketPrices;
		public long lastTimeUpdated;
	}

	private ProcesserStats procesStats;
	
	public Controller() {
		this.procesStats = new ProcesserStats();
	}

	/**
	 * Returns the price of different markets in <code>baseCurrency</code>.
	 * @param baseCurrency The base currency to display the price of the markets in.
	 * @return A JSONObject.
	 */
	public JSONArray getMarketPrices(String baseCurrency) {
		MarketPricesWrapper marketPrices = marketPricesMappedByCurrency.get(baseCurrency);
		try {
			if (marketPrices == null || marketPrices.lastTimeUpdated + MARKET_INTERVAL_MILLIS < System.currentTimeMillis()) {
				System.out.println("Updating prices!");
				marketPrices = new MarketPricesWrapper();
				marketPrices.marketPrices = procesStats.marketMap().get(baseCurrency);
				marketPrices.lastTimeUpdated = System.currentTimeMillis();
				marketPricesMappedByCurrency.put(baseCurrency, marketPrices);
			}
		} catch (Exception e) {
			if (marketPrices.marketPrices == null) {
				return errorCodeArr;
			}
		}
		return marketPrices.marketPrices;
	}

	/**
	 * Returnerar total BTC-handelsvolym per valuta sammantaget från flera marknader.
	 * @return A JSONObject.
	 */
	public JSONObject getBTCVolumeByCurrency() {
		try {
			if (volumeByCurrency == null || volumeLastUpdated + VOLUME_INTERVAL_MILLIS < System.currentTimeMillis()) {
				System.out.println("Updating volumes!");
				volumeByCurrency = ProcessorVolume.getBTCVolumeByCurrency();
				volumeLastUpdated = System.currentTimeMillis();
			}
		} catch (Exception e) {
			if (volumeByCurrency == null) {
				return errorCode;
			}
		}
		return volumeByCurrency;
	}

	public static void main(String[] args) {
		Controller cont = new Controller();
    Api api = new Api(cont);

	}
}
