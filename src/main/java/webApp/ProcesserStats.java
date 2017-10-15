package webApp;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Processes market data and currencies to become markets volume in one
 * currency.
 */
public class ProcesserStats {
  private static Gson gson = new Gson();
  private FileReader fr = new FileReader();
  private String marketJson;
  private String currencyJson;

  private String currentCurrency = "0.000208886471";//Just nu hur mycket en dollar kostar i btc. 
//  1 USD = 0.000208886471 BTC
//  1 BTC = USD/BTC = 4787.28941

  public ProcesserStats() {
    try {
      this.marketJson = fr.readFile("files/MarketData.txt");
      this.currencyJson = fr.readFile("files/currencyRates.json");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Get all market data as Market array.
   * 
   * @return Market[]
   */
  public Market[] getMarketGsonBeans() {
    Market[] data = gson.fromJson(marketJson, Market[].class);
    return data;
  }

  /**
   * Creates a map out of currency data.
   * 
   * @return Map
   */
  public Map<String, String> getCurrencyHashMap() {
    Type type = new TypeToken<Map<String, String>>() {
    }.getType();
    Map<String, String> myMap = new Gson().fromJson(currencyJson, type);
    return myMap;
  }

  /**
   * Creates a JSONobject out of currency data.
   * 
   * @return
   */
  public JSONObject getCurrencyJsonObject() {
    JSONObject jo = new JSONObject(currencyJson);
    return jo;
  }

  /**
   * Creates a JSONObject out of json formatted String.
   * 
   * @param jsonString
   * @return JSONObject
   */
  public JSONObject stringToJsonObject(String jsonString) {
    JSONObject jo = new JSONObject(jsonString);
    return jo;
  }
  
  /**
   * Marchaling the data and produce Json Array containing all markets
   * final price in one currency.
   * @return JSONArray
   */
  public JSONArray finalData(String cc) {
    Market[] data = this.getMarketGsonBeans();
    JSONArray ja = new JSONArray();
    for(Market mrkt : data) {
      JSONObject js = new JSONObject();
      if(mrkt.volume>0) {
        double finalCurrency = btcCurrencyConverter(mrkt.close, mrkt.currency, cc); 
        js.put("market", mrkt.symbol);
        js.put("last_price", finalCurrency);
        ja.put(js);
      }
    }   
    return ja;
  }
  
  /**
   *Formats JsonObject to its final form.
   *@return JSONObject
   */
  public JSONObject finalData2(String currency) {
    JSONObject jo = new JSONObject();
    jo.put("markets", this.finalData(currency).toString());
    jo.put("Base", "SEK");
    return jo;
  }

  /**
   * Converts between currencies
   */
  public double btcCurrencyConverter(double oneBTCinBeginCUR, String beginCUR, String finalCUR) {
    JSONObject m = this.getCurrencyJsonObject();
    String ratesStr = m.optString("rates");
    JSONObject currencies = this.stringToJsonObject(ratesStr);
    double finalPrice = -1;
    double goalCURinUSD;
    if (beginCUR.contentEquals(finalCUR)) {
      finalPrice = oneBTCinBeginCUR;
    } else if (beginCUR.equals("USD")) {
      goalCURinUSD = (Double) currencies.get(finalCUR);
      finalPrice = oneBTCinBeginCUR * goalCURinUSD;
    } else if (finalCUR.equals("USD")) {
      finalPrice = oneBTCinBeginCUR *(1/((Double) currencies.get(beginCUR)));
    } else {
      goalCURinUSD = (Double) currencies.get(finalCUR);
      double beginCURinUSD = (Double) currencies.get(beginCUR);
      double goalCURinBeginCUR = (1 / beginCURinUSD) * goalCURinUSD;
      finalPrice = oneBTCinBeginCUR * goalCURinBeginCUR;
    }
    return finalPrice;
  }

  // Only for testing!
  public static void main(String[] args) {
    ProcesserStats p = new ProcesserStats();
    String finalOutput = p.finalData2("DKK").toString();
    System.out.println(finalOutput);
  }
}
