package webApp;

import java.io.IOException;
import com.google.gson.Gson;

public class Processer {
	private Gson gson = new Gson();
	private FileReader fr = new FileReader();
	private String json;
	
	public Processer(){
		try {
			this.json = fr.readFile("files/exampleData.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getJsonString() {
		return this.json;
	}
	
	public Market[] convertAndGetGsonBean() {
		Market[] data = gson.fromJson(this.getJsonString(), Market[].class);
		return data;
	}
	
	//Only for testing!
	public static void main(String[] args) {
		Processer p = new Processer();
		Market[] d = p.convertAndGetGsonBean();
		for(Market da : d) {
			System.out.println(da.symbol);
			System.out.println(da.currency);
			System.out.println(da.latest_trade + "\n" );
		}
	}
}
