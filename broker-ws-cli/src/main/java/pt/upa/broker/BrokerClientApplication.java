package pt.upa.broker;

import java.util.Scanner;

import pt.upa.broker.ws.cli.BrokerClient;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.broker.ws.BrokerPortType;
import pt.upa.broker.ws.BrokerService;
import pt.upa.broker.ws.TransportView;

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
	    	System.out.print("Enter your choice:\n");
	    	System.out.print("1 - Ping\n");
	    	System.out.print("2 - Request Transport\n");
	    	System.out.print("3 - Consult All Transport Status\n");
	    	System.out.print("4 - Consult Transport by ID\n");
	    	System.out.print("5 - Clear Transports\n");
	    	System.out.print("6 - Exit\n\n\n");
		    int userNumber = scanner.nextInt();


	        switch (userNumber) {
	            case 1:  userNumber = 1;
	            		 String result = port.ping("broker teste");
						 System.out.println(result);
	                     break;
	            case 2:  userNumber = 2;
	            
	            		 //Para apanhar os args
	            		 System.out.print("1 - Insira a origem\n");
	            		 String escolha1 = scanner.next();
	            		 System.out.print("2 - Insira o destino\n");
	            		 String escolha2 = scanner.next();
	            		 System.out.print("3 - Insira o preco\n");
	            	     int escolhapreco3 = scanner.nextInt();
	            		 String resposta;
	                     resposta= port.requestTransport(escolha1,escolha2, escolhapreco3);
	                     System.out.println(resposta);
	                     break;
	                     
	                     
	            case 3:  userNumber = 3;
	            		 List<TransportView> listaTransportViewsRecebida = new ArrayList<>();
	            		 listaTransportViewsRecebida = port.listTransports();
	            		 
	            		 for (TransportView value : listaTransportViewsRecebida){	
	            				// Imprime o Status do TransportView

	            			 	System.out.println("O estado do Transporte " + value.getTransporterCompany() +" com o ID " +value.getId()+ " é : " + value.getState());
	            			}
	            		 
	                     break;
	                     
	            case 4:  userNumber = 4;
	            		 System.out.print("1 - Insira o ID\n");
	            		 String escolhaid = scanner.next();
	            		 TransportView transportrecebido = port.viewTransport(escolhaid);	 
	            		 System.out.println("O estado do Transporte " + transportrecebido.getTransporterCompany() +" com o ID " +transportrecebido.getId()+ " é : " + transportrecebido.getState());
	            		 
       		 			 break;
       		 			 
	            case 5:  userNumber = 5;
	            		 port.clearTransports();
	            		 System.out.println("All Cleaned");
	                     break;
	            
	            case 6:  userNumber = 6;
                		 return;         

	            default: 
	            	System.out.println("Invalid choice");
	                break;
	        }


	    }
	    //scanner.close();
	}

}