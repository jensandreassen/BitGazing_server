package webApp;

import static spark.Spark.port;
import static spark.Spark.get;
	
public class Api {
	private Controller cont;
	
	public Api(Controller cont) {
		this.cont = cont;
		run();
	}
	
	public void run() {
		port(5000);
		
  //Förslag: /marketPrices/:baseCurrency
		get("/currency/:baseCurrency", "application/json", (request, response) -> {
			response.type("application/json; charset=UTF-8");
			response.status(200);	
			String baseCurrency = request.params(":baseCurrency");
			System.out.println("Förfrågan kom: " + request.pathInfo());
			return cont.getMarketPrices(baseCurrency.toUpperCase());
		});
		
		get("/volumes", "application/json", (request, response) -> {
			response.type("application/json; charset=UTF-8");
			response.status(200);
			System.out.println("Förfrågan kom: " + request.pathInfo());
			return cont.getBTCVolumeByCurrency();
		});
	}	
}
