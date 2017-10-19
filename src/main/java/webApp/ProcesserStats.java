package webApp;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

/**
 * Processes market data and currencies to become markets volume in one
 * currency.
 */
public class ProcesserStats {
  private static Gson gson = new Gson();
  private FileReader fr = new FileReader();
  private String currencyJson;
  private Market[] marketBeans;
  JSONObject currencyRates;
  DataFetcher datafetcher = new DataFetcher();

  /**
   * Construktor at the moment reads files containing market data and
   * currency data Those files will later be delivered by dataFetcher. 
   */
  public ProcesserStats() {
    String marketJson = "";
    try {
      marketJson = fr.readFile("files/MarketData.txt");
      this.currencyJson = fr.readFile("files/currencyRates.json");
    } catch (IOException e) {
      e.printStackTrace();
    }
    marketBeans = getMarketGsonBeans(marketJson);//Här ska JSONObject.toString in från DataFetcher
    currencyRates = getCurrencyRates(this.getCurrencyJsonObject());//Här ska JSONObject från DataFetcher läggas in.
  }

  /**
   * Get all market data as a array.
   * 
   * @return Market[]
   */
  public Market[] getMarketGsonBeans(String marketJson) {
    Market[] data = gson.fromJson(marketJson, Market[].class);
    return data;
  }

  /**
   * Creates a JSONobject out of currency data.
   * 
   * @return JSONObject containing 
   */
  public JSONObject getCurrencyJsonObject() {
    JSONObject jo = new JSONObject(currencyJson);
    return jo;
  }

  /**
   * Creates a JSONObject out of json formatted String.
   * @param jsonString
   *
   * @return JSONObject
   */
  public JSONObject getCurrencyRates(JSONObject currencyJson) {
    String ratesStr = currencyJson.optString("rates");
    JSONObject jo = new JSONObject(ratesStr);
    return jo;
  }
  
  /**
   * Marchaling bitcoin markets data and produce Json Array containing all markets
   * last price in one currency.
   * @param currency
   *        The currency of the output.
   * @return JSONArray
   */
  public JSONArray finalData(String currency) {
    JSONArray ja = new JSONArray();
    for(Market mrkt : marketBeans) {
      JSONObject js = new JSONObject();
      if(mrkt.volume>0) {
        double finalCurrency = btcCurrencyConverter(mrkt.close, mrkt.currency, currency); 
        js.put("market", mrkt.symbol);
        js.put("last_price", finalCurrency);
        ja.put(js);
      }
    }   
    return ja;
  }
  
  /**
   * Get all markets in all currencies
   */
  public HashMap<String, JSONArray> marketMap(){
    HashMap<String, JSONArray> markets = new HashMap<String, JSONArray>();
    for(String CUR : currencyRates.keySet()){
      markets.put(CUR, finalData(CUR));
    }
    System.out.println(markets.get("SEK").toString());
    return markets;
  }

  /**
   *Formats JsonObject to its final form with "base" parameter currency output and
   *Json string containing all markets in a "markets" Json parameter. .
   *@param currency
   *       The currency of the output.
   *@return JSONObject
   */
  public JSONObject finalData2(String currency) {
    JSONObject jo = new JSONObject();
    jo.put("markets", this.finalData(currency).toString());
    jo.put("Base", currency);
    return jo;
  }

  /**
   * Converts one amount in one currency to another. 
   * @param oneBtCinBeginCUR
   *        How much one BTC cost in the currency specified by the
   *        beginCUR parameter.
   * @param beginCUR
   *        Wish currency you want to convert from.
   * @param finalCUR
   *        Wish currency you want to convert to. 
   * @return Price of one bitcoin.
   */
  public double btcCurrencyConverter(double oneBTCinBeginCUR, String beginCUR, String finalCUR) {
    double finalPrice = -1;
    double goalCURinUSD;
    if (beginCUR.contentEquals(finalCUR)) {
      finalPrice = oneBTCinBeginCUR;
    } else if (beginCUR.equals("USD")) {
      goalCURinUSD = currencyRates.getDouble(finalCUR);
      finalPrice = oneBTCinBeginCUR * goalCURinUSD;
    } else if (finalCUR.equals("USD")) {
      finalPrice = oneBTCinBeginCUR *(1/(currencyRates.getDouble(beginCUR)));
    } else {
      goalCURinUSD = currencyRates.getDouble(finalCUR); //Här är det någit som är vajsing, output är int(heltal) så funkar det inte. 
      double beginCURinUSD = currencyRates.getDouble(beginCUR);
      double goalCURinBeginCUR = (1 / beginCURinUSD) * goalCURinUSD;
      finalPrice = oneBTCinBeginCUR * goalCURinBeginCUR;
    }
    return finalPrice;
  }

  // Only for testing!
  public static void main(String[] args) {
    ProcesserStats p = new ProcesserStats();
//    String finalOutput = p.finalData("SEK").toString(2);
//    System.out.println(finalOutput);
    HashMap<String, JSONArray> hm = p.marketMap();
    System.out.println(hm.get("USD").toString(2));
    
  }
}
