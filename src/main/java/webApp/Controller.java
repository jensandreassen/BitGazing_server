package webApp;

public class Controller {
	private Processer process;
	private Api api;
	
	public Controller(Processer process, Api api) {
		this.process = process;
		this.api = api;
		
		
	}
	
	

	
	
	
	public static void main(String[] args) {
		Processer process = new Processer();
		Api api = new Api();
		Controller cont = new Controller(process, api);
		
	}
}
