package webApp;

import static spark.Spark.port;
import static spark.Spark.get;
import com.google.gson.Gson;
	
public class Api {
	private Controller cont;
	
	public Api(Controller cont) {
		this.cont = cont;
		run();
	}
	
	public void run() {
		port(5000);
		
		get("/currency/:currency", "application/json", (request, response) -> {
			response.type("application/json; charset=UTF-8");
			response.status(200);	
			String param = request.params(":currency");
			System.out.println("Förfrågan kom");
			return cont.getStats(param);
		});
		
		
		get("/getinfo", "application/json", (request, response) -> {
			response.type("application/json; charset=UTF-8");
			response.status(200);
			System.out.println("Förfrågan kom");
//			return cont.getMarket();
			return cont.getCurrency();
		});
	}	
}
