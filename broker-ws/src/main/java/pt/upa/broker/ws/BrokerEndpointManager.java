package pt.upa.broker.ws;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.Endpoint;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.transporter.ws.TransporterPortType;
import pt.upa.transporter.ws.TransporterService;

public class BrokerEndpointManager {
	private String uddiURL;
	private String name;
	private String url;
	private String transporterServername="UpaTransporter";
	private static Map<String, TransporterPortType> transporterPorts;
	
	public BrokerEndpointManager(String[] args){
		uddiURL = args[0];
		name = args[1];
		url = args[2];
	}

	public void serverConnect() {
		Endpoint endpoint = null;
		UDDINaming uddiNaming = null;
		
		transporterPorts = new HashMap<String,TransporterPortType>();
		
		try {
			BrokerPort teste = new BrokerPort(transporterPorts);
			endpoint = Endpoint.create(teste);

			// publish endpoint
			System.out.printf("Starting %s%n", url);
			endpoint.publish(url);

			// publish to UDDI
			//????Object uddiURL;
			System.out.printf("Publishing '%s' to UDDI at %s%n", name, uddiURL);
			uddiNaming = new UDDINaming(uddiURL);
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
					requestContext.put(ENDPOINT_ADDRESS_PROPERTY, transporterURL);
										
					// add Transporter name and port to map
					transporterPorts.put(transporterName, port);						
				}
				
				
				
				/*
				// Print de todos os transporter ports disponiveis
				
				for (String s : transporterPorts.keySet()){
					System.out.println(s);
				}*/				
			}
			
			if(transporterPorts.isEmpty()){
				System.out.println("There are no active transporters");
				return;
			}
			
			// verifica se foi encontrado algum transporter
			
			
			/*
			// Show all Transporters found 
			for(String elem: urlTransporterEndpoint){
				System.out.println(elem + "      " + "234");
			}	*/
			
			
			// ping de teste
			/*
			String resposta = transporterPorts.get("UpaTransporter1").ping("larilas");
			System.out.println(resposta);*/
						
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



