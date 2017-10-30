package webApp;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.mashape.unirest.http.exceptions.UnirestException;

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
  DecimalFormat numberFormat = new DecimalFormat("#.00");

  /**
   * Construktor at the moment reads files containing market data and
   * currency data Those files will later be delivered by dataFetcher. 
   * @param online
   *        Set TRUE if you want to get data from online or FALSE if
   *        reading from local files.
   */
  public ProcesserStats(boolean online) throws UnirestException, IOException {
    if (online) {
      marketBeans = getMarketGsonBeans(DataFetcher.fetchAllBTCMarkets().toString());// Här ska JSONObject.toString
                                              // in från DataFetcher
      currencyRates = getCurrencyRates(DataFetcher.fetchCurrencyRates());// Här ska JSONObject från DataFetcher
                                        // läggas in.
    } else {
      String marketJson = "";
      try {
        marketJson = fr.readFile("files/MarketData.txt");
        this.currencyJson = fr.readFile("files/currencyRates.json");
      } catch (IOException e) {
        e.printStackTrace();
      }
      marketBeans = getMarketGsonBeans(marketJson);
      currencyRates = getCurrencyRates(this.getCurrencyJsonObject());
                                      
    }
  }

  /**
   * Converts Json fromatted market data string to a array of Market
   * objects.
   * @return Market[]
   *         Array contains Market objects.
   */
  public Market[] getMarketGsonBeans(String marketJson) {
    Market[] data = gson.fromJson(marketJson, Market[].class);
    return data;
  }

  /**
   * Creates a JSONobject out of currency data currently this metod
   * reads a text string and will not be used when data is fetched
   * from datafestcher since it delivers a JSONObject to
   * "getCurrencyRates" method.
   * @return JSONObject
   *         
   */
  public JSONObject getCurrencyJsonObject() {
    JSONObject jo = new JSONObject(currencyJson);
    return jo;
  }

  /**
   * Creates a JSONObject out of the "rates" parameter in the Currency
   * JSONObject. 
   * @param JSONObject containing CurrencyData from DataFetcher. 
   *        
   * @return JSONObject
   *         This object is containing all currencies in USD that
   *         BitGazing currently support.
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
   *         Contains all markets last price in one currency defined
   *         by parameter. It is sorted by price starting with the
   *         lowest price.
   */
  public JSONArray finalData(String currency) {
    JSONArray ja = new JSONArray();
    ArrayList<JSONObject> tmp = new ArrayList<JSONObject>();
    for(Market mrkt : marketBeans) {
      JSONObject js = new JSONObject();
      if(mrkt.volume>0 && !mrkt.currency.contains("SLL") && !mrkt.currency.contains("VEF")) { //SLL and VEF is not supported.
        double finalCurrency = btcCurrencyConverter(mrkt.close, mrkt.currency, currency); 
      js.put("market", mrkt.symbol.substring(0,1).toUpperCase() + mrkt.symbol.substring(1,mrkt.symbol.length() - 3));
        js.put("last_price", round(finalCurrency, 2));
        js.put("marketCurrency", mrkt.currency);
        tmp.add(js);
      }
    }   
    int index = 0;
    for(int i = 0; i < tmp.size(); i++){
      for(int j = i+1; j < tmp.size(); j++){
        if( tmp.get(j).getDouble("last_price") < tmp.get(index).getDouble("last_price")){
          index = j;
        }
      }
      JSONObject lowerPrice = tmp.get(index);
      tmp.set(index, tmp.get(i));
      tmp.set(i, lowerPrice);
    }
    for(int i = 0; i < tmp.size(); i++){
      for(int j = i+1; j < tmp.size(); j++){
        if( tmp.get(j).getDouble("last_price") < tmp.get(index).getDouble("last_price")){
          index = j;
        }
      }
      JSONObject lowerPrice = tmp.get(index);
      tmp.set(index, tmp.get(i));
      tmp.set(i, lowerPrice);
    }

    for(JSONObject jo : tmp){
      ja.put(jo);
    }
    return ja;
  }
  
  /**
   * Get all markets in all currencies.
   * @return HashMap<String, JsonArray>
   *         Map key is currency in tre uppercase letters and will
   *         return JSONArray containing all BTC markets last price in
   *         that currency starting with the market with the lowest price. 
   */
  public HashMap<String, JSONArray> marketMap(){
    HashMap<String, JSONArray> markets = new HashMap<String, JSONArray>();
    for(String CUR : currencyRates.keySet()){
      markets.put(CUR, finalData(CUR));
    }
    return markets;
  }

  /**
   * Converts one amount in one currency to another unafortunately via
   * USD. 
   *
   * @param oneBtCinBeginCUR
   *        How much one BTC cost in the currency specified by the
   *        beginCUR parameter.
   * @param beginCUR
   *        Wish currency you want to convert from.
   * @param finalCUR
   *        Wish currency you want to convert to. 
   * @return Price of one bitcoin in currency defined by parameter
   *         'finalCUR'.
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
      goalCURinUSD = currencyRates.getDouble(finalCUR); 
      double beginCURinUSD = currencyRates.getDouble(beginCUR);
      double goalCURinBeginCUR = (1 / beginCURinUSD) * goalCURinUSD;
      finalPrice = oneBTCinBeginCUR * goalCURinBeginCUR;
    }
    return finalPrice;
  }

  /**
   * Rounds double values to a specific amount of decimals.
   *
   * @param value
   *        The value wish will be formatted.
   * @param places
   *        The number of decimals to be rounded to.
   */
  public static double round(double value, int places) {
      if (places < 0) throw new IllegalArgumentException();

      BigDecimal bd = new BigDecimal(value);
      bd = bd.setScale(places, RoundingMode.HALF_UP);
      return bd.doubleValue();
  }

  // Only for testing! Should be removed any time soon!!!!!!!!!!!!!!
  public static void main(String[] args) throws UnirestException, IOException {
    ProcesserStats p = new ProcesserStats(false);
    HashMap<String, JSONArray> hm = p.marketMap();
    System.out.println(hm.get("USD").toString(2));
    System.out.println("============================================================================");
    System.out.println(hm.get("SEK").toString(2));
    
  }
}
