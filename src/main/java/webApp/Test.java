package webApp;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class Test {
	public static void main(String[] args) {
		
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet(
				"http://date.jsontest.com");
			getRequest.addHeader("accept", "application/json");

			HttpResponse response;
			
				response = httpClient.execute(getRequest);
			
				
			if (response.getStatusLine().getStatusCode() != 200) {
				System.out.println("inte 200");
			}
			
			System.out.println("Svar " + response.toString());
			
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
	}

}
