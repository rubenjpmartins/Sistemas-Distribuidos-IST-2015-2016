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
import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.TransportView;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;
import pt.upa.broker.ws.UnknownTransportFault_Exception;

public class BrokerClientApplication {

	public static void main(String[] args) throws Exception {
		System.out.println(BrokerClientApplication.class.getSimpleName() + " starting...");
		
		// Check arguments
		if (args.length < 2) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s uddiURL name%n", BrokerClient.class.getName());
			return;
		}

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


		// Ciclo principal
	    while(true){

	    	// user choice 
	    	System.out.print("\n\nEnter your choice:\n");
	    	System.out.print("1 - Ping\n");
	    	System.out.print("2 - Request Transport\n");
	    	System.out.print("3 - Consult All Transport Status\n");
	    	System.out.print("4 - Consult Transport by ID\n");
	    	System.out.print("5 - Clear Transports\n");
	    	System.out.print("6 - Exit\n");
	    	
	    	
	    	//Deixa apenas inserir um Inteiro
	   		while(!scanner.hasNextInt()) scanner.next();
	   		int userNumber = scanner.nextInt();

	        switch (userNumber) {
	        
	        	//PING
	            case 1:  userNumber = 1;
	            		 String result = port.ping("\nPing Para Broker");
						 System.out.println(result);
	                     break;
	            
	            
	            //REQUEST TRANSPORT
	            case 2:  userNumber = 2;
	            
	            		 //Para apanhar os args
	            		 System.out.print("1 - Insert origin\n");
	            		 String escolha1 = scanner.next();
	            		 System.out.print("2 - Insert destination\n");
	            		 String escolha2 = scanner.next();
	            		 
	            		 System.out.print("3 - Insert price\n");
	            		 //Deixa apenas inserir um Inteiro
	            		 while(!scanner.hasNextInt()) scanner.next();
	            		 int escolhapreco3 = scanner.nextInt();

	            		 String resposta;
	            		 
	            		 //Envia o pedido
	            		 try{
	            			 resposta= port.requestTransport(escolha1,escolha2, escolhapreco3); 
	            			 System.out.println(resposta);
	            		 }
	            		 // Retorna erro de preço inferior a 0
	            		 catch(InvalidPriceFault_Exception e){
	            			
	     				    System.err.println("InvalidPriceFault_Exception: " + e.getMessage());
	            		 }
	            		 // Retorna erro origem/destino desconhecido
	            		 catch(UnknownLocationFault_Exception e){

	            			String cidadeErrada = e.getFaultInfo().getLocation();
		     				System.err.println("UnknownLocationFault_Exception: " + e.getMessage() + cidadeErrada);	
	            		 }
	            		 //preço max ultrapassado
	            		 catch(UnavailableTransportPriceFault_Exception e){
	          
	            			 	int precoMax = e.getFaultInfo().getBestPriceFound();
	            			 	String strI = Integer.toString(precoMax);
			     				System.err.println("UnavailableTransportPriceFault_Exception: " + e.getMessage() + strI);	
		            	 }
	            		 
	            		 //Nenhuma Trasnportadora enviou orçamento - Todas retornaram Null
	            		 catch(UnavailableTransportFault_Exception e){

	            			String origem1 = e.getFaultInfo().getOrigin();
	            			String destino1 = e.getFaultInfo().getDestination();
			     			System.err.println("UnavailableTransportFault_Exception: " + e.getMessage() + origem1+"/"+destino1);	
		            	 }
	                     break;
 
	                     
	            //Consult All Transport Status         
	            case 3:  userNumber = 3;
	            
	            		 List<TransportView> listaTransportViewsRecebida = new ArrayList<>();
	            		 
	            		 
	            		 listaTransportViewsRecebida = port.listTransports();
	            		 
	            		
	            		 
	            		 for (TransportView value : listaTransportViewsRecebida){	
	            				// Imprime o Status do TransportView

	            			 	System.out.println("The state of the transport accomplished by " + value.getTransporterCompany() +" com o ID " +value.getId()+ " é : " + value.getState());
	            			}
	            		 
	                     break;
	            
	            //Consult Transport by ID
	            case 4:  userNumber = 4;
	            		 System.out.print("1 - Insira o ID\n");
	            		 String escolhaid = scanner.next();

	            		 try{
	            			 TransportView transportrecebido = port.viewTransport(escolhaid);	 
	            			 System.out.println("O estado do Transporte " + transportrecebido.getTransporterCompany() +" com o ID " +transportrecebido.getId()+ " é : " + transportrecebido.getState());
            			 } 
	            		 catch(UnknownTransportFault_Exception e){
	            			String IDfault = e.getFaultInfo().getId();
	            			
		     				System.err.println("UnknownLocationFault_Exception: " + e.getMessage() + IDfault);	
		            	 }
	            		 
       		 			 break;
       		 		
       		    //Clear Transports
	            case 5:  userNumber = 5;
	            		 port.clearTransports();
	            		 System.out.println("All Cleaned");
	                     break;
	            
	            //Exit
	            case 6:  userNumber = 6;
	            		 scanner.close(); 
                		 return;         

	            default: 
	            	System.out.println("Invalid choice");
	                break;
	        }	           
	    }       
	}
}