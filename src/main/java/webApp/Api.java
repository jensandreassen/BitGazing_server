package webApp;

import static spark.Spark.port;
import static spark.Spark.get;
import com.google.gson.Gson;
	
public class Api {
	private int port;
	
	public Api(int port) {
		this.port = port;
	}
	
	public static void run() {
		port(5000);
		Gson gson = new Gson();
		
		
		// ska ge ut valutaväxling
		get("/currency", "application/json", (request, response) -> {
			response.type("application/json; charset=UTF-8");
			response.status(200);	
			
			return "test";
//			return new GsonBuilder()
//					.setDateFormat("yyyy-MM-dd HH:mm:ss")
//					.create()
//					.toJson();
		});
		
		// ska ge ut information
		get("/getinfo", "application/json", (request, response) -> {
			response.type("application/json; charset=UTF-8");
			response.status(200);
			
			return "teststräng";
			
//			return new GsonBuilder()
//					.setDateFormat("yyyy-MM-dd HH:mm:ss")
//					.create()
//					.toJson(unicorns);
		});
		
		
	}
	
	public static void main(String[] args) {
		run();
	}
	
	
}
