package pt.upa.broker;

import java.util.Scanner;

import pt.upa.broker.ws.cli.BrokerClient;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;
import java.util.Map;

import javax.xml.ws.BindingProvider;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.broker.ws.BrokerPortType;
import pt.upa.broker.ws.BrokerService;

public class BrokerClientApplication {

	public static void main(String[] args) throws Exception {
		System.out.println(BrokerClientApplication.class.getSimpleName() + " starting...");
		
		// Check arguments
		if (args.length < 2) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s uddiURL name%n", BrokerClient.class.getName());
			return;
		}

		
		// Create a new connection
		// new BrokerClient(args);


		String uddiURL = args[0];
		String name = args[1];

		System.out.printf("Contacting UDDI at %s%n", uddiURL);
		UDDINaming uddiNaming = new UDDINaming(uddiURL);
		
		String targetServer = name;
		System.out.printf("Looking for '%s'%n", targetServer);
		String endpointAddress = uddiNaming.lookup(targetServer);

		if (endpointAddress == null) {
			System.out.println("Not found!");
			return;
		} else {
			System.out.printf("Found %s%n", endpointAddress);
		}

		System.out.println("Creating stub ...");
		BrokerService service = new BrokerService();
		BrokerPortType port = service.getBrokerPort();

		System.out.println("Setting endpoint address ...");
		BindingProvider bindingProvider = (BindingProvider) port;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);


		// create a scanner so we can read the command-line input
	    Scanner scanner = new Scanner(System.in);

	 
	    // get their input as a String
	    // String username = scanner.next();

		// Ciclo principal
	    while(true){

	    	// user choice 
	    	System.out.print("Enter your choice: ");
	    	System.out.print("1 - Ping");
	    	System.out.print("2 - Request Transport com preco referencia = 80");
	    	System.out.print("3 - Consult Status");
		    int userNumber = scanner.nextInt();


	        switch (userNumber) {
	            case 1:  userNumber = 1;
	            		 String result = port.ping("broker teste");
						 System.out.println(result);
	                     break;
	            case 2:  userNumber = 2;
	            
	            		//Para apanhar os args
	            		//int userNumber = scanner.nextInt();
	            		//int userNumber = scanner.nextInt();
	            		//int userNumber = scanner.nextInt();
	            			String resposta;
	                     resposta= port.requestTransport("Faro","Faro", 80);
	                     System.out.println(resposta);
	                     break;
	                     
	                     
	            case 3:  userNumber = 3;
	                     return;

	            case 4:  userNumber = 4;
	                     break;

	            default: 
	            	System.out.println("Invalid choice");
	                break;
	        }


	    }
	    //scanner.close();
	}

}