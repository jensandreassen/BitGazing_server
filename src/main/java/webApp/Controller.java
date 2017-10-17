package webApp;

import java.util.Calendar;
import java.util.HashMap;

import javax.annotation.processing.Processor;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Controller {

	private ProcesserStats procesStat;
	private ProcessorMap processMap;
	private JSONObject stats;
	private JSONObject currency;
	private Calendar cal;
	private long statsTime;
	private long currencyTime;
	final long marketIntervall = 18^6; // Ersätt med antal timmar till uppdatering
	final long currencyIntervall = 18^6; // Ersätt med antal timmar till uppdatering
	private JSONObject errorCode = new JSONObject();
	
	public Controller(ProcesserStats procesStat, ProcessorMap processMap) {
		this.procesStat = procesStat;
		this.processMap = processMap;
	}
	
	public JSONObject getStats(String param) {
		try {
			if(stats==null) {
				stats = procesStat.finalData2(param);
				statsTime = cal.getTimeInMillis();
			} else if(statsTime+marketIntervall<cal.getTimeInMillis()) {
				stats = procesStat.finalData2(param);
				statsTime = cal.getTimeInMillis();
			}
		} catch (Exception e) {
			// TODO: handle exception
			if(stats==null) {
				return errorCode;
			}
		}
		return stats;
	}
	
	public JSONObject getCurrency() {
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
			if(currency==null) {
				return errorCode;
			}
		}
		return currency;
	}
	
	public static void main(String[] args) {
		ProcesserStats procesStat = new ProcesserStats();
		ProcessorMap processMap = new ProcessorMap();
		Controller cont = new Controller(procesStat, processMap);
		Api api = new Api(cont);
	}
}
