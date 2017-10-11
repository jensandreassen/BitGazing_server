package webApp;

public class Controller {
	private ProcesserStats process;
	private Api api;
	
	public Controller(ProcesserStats process, Api api) {
		this.process = process;
		this.api = api;
		
		
	}
	
	

	
	
	
	public static void main(String[] args) {
		ProcesserStats process = new ProcesserStats();
		Api api = new Api();
		Controller cont = new Controller(process, api);
		
	}
}
