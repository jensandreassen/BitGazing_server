package webApp;

import java.util.Calendar;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Controller {

	private ProcesserStats process;
	private JsonObject market;
	private JsonObject currency;
	private Calendar cal;
	private long marketTime;
	private long currencyTime;
	final long marketIntervall = 18^6;
	final long currencyIntervall = 18^6;
	private String errorCode = "INSERT ERROR CODE";
	
	public Controller(ProcesserStats process) {
		this.process = process;
	}
	
	public String getMarket() {
		try {
			if(market==null) {
				//market = process.get
				marketTime = cal.getTimeInMillis();
			} else if(marketTime+marketIntervall<cal.getTimeInMillis()) {
				//market = process.get
				marketTime = cal.getTimeInMillis();
			}
		} catch (Exception e) {
			// TODO: handle exception
			return errorCode;
		}
		return market.toString();
	}
	
	public String getCurrency() {
		try {
			if(currency==null) {
				//currency = process.get
				currencyTime = cal.getTimeInMillis();
			} else if(currencyTime+currencyIntervall<cal.getTimeInMillis()) {
				//currency = process.get
				currencyTime = cal.getTimeInMillis();
			}
		} catch (Exception e) {
			// TODO: handle exception
			return errorCode;
		}
		return currency.toString();
	}
	
	public static void main(String[] args) {
		ProcesserStats process = new ProcesserStats();
		Controller cont = new Controller(process);
		Api api = new Api(cont);
	}
}
