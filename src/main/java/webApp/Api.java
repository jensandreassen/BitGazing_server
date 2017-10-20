package webApp;

import static spark.Spark.port;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import static spark.Spark.get;
	
public class Api {
	private Controller cont;
	private JSONObject errorInternal = new JSONObject("{ \"status\": 500,\"message\": \"500 Internal Server Error\" }");
	private JSONObject errorNotFound = new JSONObject("{ \"status\": 404,\"message\": \"404 Not Found\" }");
	
	public Api(Controller cont) {
		this.cont = cont;
		run();
	}
	
	public void run() {
		port(5000);
		
		get("/marketPrices/:baseCurrency", (request, response) -> {
			response.type("application/json; charset=UTF-8");
			response.status(200);
			String baseCurrency = request.params(":baseCurrency");
			System.out.println("Förfrågan kom: " + request.pathInfo());
			try {
				return cont.getMarketPrices(baseCurrency.toUpperCase());
			} catch (FileNotFoundException e) {
				response.status(404);
				return errorNotFound.toString(2);
			} catch (JSONException | IOException e) {
				response.status(500);
				return errorInternal.toString(2);
			}
		});
		
		get("/volumes", (request, response) -> {
			response.type("application/json; charset=UTF-8");
			response.status(200);
			System.out.println("Förfrågan kom: " + request.pathInfo());
			try {
				return cont.getBTCVolumeByCurrency();
			} catch (JSONException | IOException e) {
				response.status(500);
				return errorInternal.toString(2);
			}
		});
	}	
}
