package webApp;

import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

public class Controller {

	private ProcesserStats procesStats;

	private JSONObject marketPrices; // Bugg: behövs lagras ett JSONObject för varje bas-valuta.
	private JSONObject volumeByCurrency;
	private JSONObject errorCode = new JSONObject("{ \"key1\": \"value1\",\"key2\": \"value2\" }");

	private long marketLastUpdated; // Bugg: behövs lagras en tid för varje JSONObject.
	private long volumeLastUpdated;

	private final long MARKET_INTERVAL_MILLIS = TimeUnit.HOURS.toMillis(5);
	private final long VOLUME_INTERVAL_MILLIS = TimeUnit.HOURS.toMillis(5);

	public Controller() {
		this.procesStats = new ProcesserStats();
	}

	/**
	 * Returns the price of different markets in <code>baseCurrency</code>.
	 * @param baseCurrency The base currency to display the price of the markets in.
	 * @return A JSONObject.
	 */
	public JSONObject getMarketPrices(String baseCurrency) {
		try {
			if (marketPrices == null || marketLastUpdated + MARKET_INTERVAL_MILLIS < System.currentTimeMillis()) {
				System.out.println("Updating prices!");
				marketPrices = procesStats.finalData2(baseCurrency.toUpperCase());
				marketLastUpdated = System.currentTimeMillis();
			}
		} catch (Exception e) {
			if (marketPrices == null) {
				return errorCode;
			}
		}
		return marketPrices;
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
