package webApp;

import java.io.IOException;
import java.nio.charset.Charset;

import com.google.gson.Gson;

public class Processer {
	private Gson g = new Gson();
	private FileReader fr = new FileReader();
	private String json;
	
	public Processer(){
		try {
			this.json = fr.readFile("/home/osc/User_Oscar/Mah/mahSysHT17/Workspace2/BitGazing/files/exampleData.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getJson() {
		return json;
	}
	//Only for testing!
	public static void main(String[] args) {
		Processer p = new Processer();
	}
}
