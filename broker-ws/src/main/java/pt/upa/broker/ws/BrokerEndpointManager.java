package pt.upa.broker.ws;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Endpoint;

import example.ws.handler.UpaHeaderHandler;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.cripto.DigitalSignatureX509;
import pt.upa.transporter.ws.TransporterPortType;
import pt.upa.transporter.ws.TransporterService;

public class BrokerEndpointManager {
	private String uddiURL;
	private String name;
	private String url;
	private String transporterServername="UpaTransporter";
	private static Map<String, TransporterPortType> transporterPorts;
	private BrokerPortType secondaryPort;
	
	
	//Relativo ao handler
	public static final String TOKEN = "UpaBroker1";
	public static final String CLASS_NAME = BrokerEndpointManager.class.getSimpleName();
	
	
	
	
	
	// Relativo ao getPrivateKeyFromKeystore
	private final String keyStoreFilePath = "UpaBrokerkeystore/UpaBroker.jks";
	private final String keyStorePassword = "ins3cur3";	 
	private final String keyAlias = "upabroker"; 
    private final String keyPassword = "1nsecure"; //    private final String keyPassword = "example";
	
  
	public BrokerEndpointManager(String[] args){
		uddiURL = args[0];
		name = args[1];
		url = args[2];
	}
	
	public class Timer extends Thread{
		public void run(){
			while(true){
				secondaryPort.pingToBroker("I'm alive");
				
				try{
					Thread.sleep(5000);
				} catch(InterruptedException e){
					Thread.currentThread().interrupt();
					return;
				}
			}
		}
	}
	
	public void serverConnect() {
		Endpoint endpoint = null;
		UDDINaming uddiNaming = null;
		DigitalSignatureX509 assinaturaX509 = null;
		
		transporterPorts = new HashMap<String,TransporterPortType>();
		
		try {
			uddiNaming = new UDDINaming(uddiURL);

			
			//distinção
			// If it is the Primary Broker Server
			if (name.equals("UpaBroker1")){
				
			
				String secondaryBrokerName = "UpaBroker2";
				String secondaryBrokerURL = uddiNaming.lookup(secondaryBrokerName);
				
				// Secondary Broker Found
				if (secondaryBrokerURL != null){		
					
					System.out.println(secondaryBrokerName + " was Found at:");
					//urlTransporterEndpoint.add(secondaryBrokerURL);
					System.out.println(secondaryBrokerURL);
															
					// stubs creation for Secondary Broker
					BrokerService secondaryService = new BrokerService();
					secondaryPort = secondaryService.getBrokerPort();

					// endpoint setting for Secondary Broker
					BindingProvider bindingProvider = (BindingProvider) secondaryPort;
					Map<String, Object> requestContext = bindingProvider.getRequestContext();
					requestContext.put(ENDPOINT_ADDRESS_PROPERTY, secondaryBrokerURL);
					
					secondaryPort.pingToBroker("Ping Teste");
					
					
					Timer t1 = new Timer();
					t1.start();
				}
				
				//Publicaçao do seu serviço
				
				BrokerPort teste = new BrokerPort(transporterPorts, secondaryPort);
				endpoint = Endpoint.create(teste);

				// publish endpoint
				System.out.printf("Starting %s%n", url);
				endpoint.publish(url);

				// publish to UDDI
				System.out.printf("Publishing '%s' to UDDI at %s%n", name, uddiURL);
				uddiNaming.rebind(name, url);
					
				// searching for transporters
				int i;
				Collection<String> urlTransporterEndpoint= new ArrayList<String>();
				
				System.out.println("Looking for Transporters...");
				for (i=1; i < 10; i++){
					String transporterName = transporterServername+Integer.toString(i) ;				
					String transporterURL = uddiNaming.lookup(transporterName);
					
					// Transporter Found
					if (transporterURL != null){		
						
						System.out.println(transporterName + " was Found at:");
						urlTransporterEndpoint.add(transporterURL);
						System.out.println(transporterURL);
																
						// stubs creation for Transporter
						TransporterService service = new TransporterService();
						TransporterPortType port = service.getTransporterPort();

						// endpoint setting for Transporter
						BindingProvider bindingProvider = (BindingProvider) port;
						Map<String, Object> requestContext = bindingProvider.getRequestContext();
						
						//ADICIONADO HANDLER CONTEXT
						String initialValue = TOKEN;
						requestContext.put(UpaHeaderHandler.CONTEXT_PROPERTY, initialValue);
						
						requestContext.put(ENDPOINT_ADDRESS_PROPERTY, transporterURL);
											
						// add Transporter name and port to map
						transporterPorts.put(transporterName, port);						
					}				
				}		
				if(transporterPorts.isEmpty()){
					System.out.println("There are no active transporters");
					return;
				}
					
			}
			
			
			
			
			
			
			
			
			// Broker Secundario
			else{
			
				//Publicaçao do serviço
				BrokerPort teste = new BrokerPort(transporterPorts);
				endpoint = Endpoint.create(teste);

				// publish endpoint
				System.out.printf("Starting %s%n", url);
				endpoint.publish(url);

				// publish to UDDI
				System.out.printf("Publishing '%s' to UDDI at %s%n", name, uddiURL);
				uddiNaming.rebind(name, url);
				
				
				// searching for transporters
				int i;
				Collection<String> urlTransporterEndpoint= new ArrayList<String>();
				
				System.out.println("Looking for Transporters...");
				for (i=1; i < 10; i++){
					String transporterName = transporterServername+Integer.toString(i) ;				
					String transporterURL = uddiNaming.lookup(transporterName);
					
					// Transporter Found
					if (transporterURL != null){		
						
						System.out.println(transporterName + " was Found at:");
						urlTransporterEndpoint.add(transporterURL);
						System.out.println(transporterURL);
																
						// stubs creation for Transporter
						TransporterService service = new TransporterService();
						TransporterPortType port = service.getTransporterPort();

						// endpoint setting for Transporter
						BindingProvider bindingProvider = (BindingProvider) port;
						Map<String, Object> requestContext = bindingProvider.getRequestContext();
						
						//ADICIONADO HANDLER CONTEXT
						String initialValue = TOKEN;
						requestContext.put(UpaHeaderHandler.CONTEXT_PROPERTY, initialValue);
						
						requestContext.put(ENDPOINT_ADDRESS_PROPERTY, transporterURL);
											
						// add Transporter name and port to map
						transporterPorts.put(transporterName, port);						
					}				
				}

				
				if(transporterPorts.isEmpty()){
					System.out.println("There are no active transporters");
					return;
				}
					
				
				//Ciclo para procurar a provadevida
				
				boolean b = true;
				while(true){
					
					if(teste.provaDeVida == false){
						
						try{
							Thread.sleep(1000);
						} catch(InterruptedException ex) {
							Thread.currentThread().interrupt();
							return;
						}
					}
					else{
						System.out.println("Primary Broker Connected");
						while(true){
							if(teste.provaDeVida == true){
								teste.provaDeVida = false;
							}
							else{
								System.out.println("WARNING: Primary Broker Failed");
								b = false;
								break;
							}
							try{
								Thread.sleep(7000);
							} catch(InterruptedException ex) {
								Thread.currentThread().interrupt();
								return;
							}
						}
					}
				if(b==false){
					break;
				}
					
				}
				
				Thread.sleep(2000);
				// publica o servidor secundario para substituir o primario
				System.out.printf("Re-Publishing UpaBroker to UDDI at %s%n", uddiURL);
				try{
					uddiNaming.rebind("UpaBroker1", url);
		
				} catch(JAXRException e) {
					e.printStackTrace();
				}
				
							
				
				
			}
			

			

			// wait
			System.out.println("Broker Server is awaiting connections");
			System.out.println("Press enter to shutdown");
			
			System.in.read();
						

		} catch (Exception e) {
			System.out.printf("Caught exception: %s%n", e);
			e.printStackTrace();

		} finally {
			try {
				if (endpoint != null) {
					// stop endpoint
					endpoint.stop();
					System.out.printf("Stopped %s%n", url);
				}
			} catch (Exception e) {
				System.out.printf("Caught exception when stopping: %s%n", e);
			}
			try {
				if (uddiNaming != null) {
					// delete from UDDI
					uddiNaming.unbind(name);
					System.out.printf("Deleted '%s' from UDDI%n", name);
				}
			} catch (Exception e) {
				System.out.printf("Caught exception when deleting: %s%n", e);
			}
		}
	}
}



