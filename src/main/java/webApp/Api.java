package webApp;

import static spark.Spark.port;
import static spark.Spark.get;
import com.google.gson.Gson;
	
public class Api {
	private Controller cont;
	
	public Api(Controller cont) {
		this.cont = cont;
	}
	
	public static void run() {
		port(5000);
		
		// ska ge ut valutav�xling
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
			
			return "teststr�ng";
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
