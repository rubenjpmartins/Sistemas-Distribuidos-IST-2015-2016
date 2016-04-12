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
	String uddiURL;
	String name;
	String url;
	String transporterServername="UpaTransporter";
	
	public BrokerEndpointManager(String[] args){
		uddiURL = args[0];
		name = args[1];
		url = args[2];
	}

	public void serverConnect() {
		Endpoint endpoint = null;
		UDDINaming uddiNaming = null;
		
		Map<String, TransporterPortType> transporterPorts;
		transporterPorts = new HashMap<String,TransporterPortType >();
		
		try {
			endpoint = Endpoint.create(new BrokerPort());

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
			
			
			
			for (i=1; i < 10; i++){
				String transporterName = transporterServername+Integer.toString(i) ;
				System.out.println("Looking for : " + transporterName);
				String transporterURL = uddiNaming.lookup(transporterName);
				if (transporterURL == null){
					System.out.println(transporterName + " - Not Found ");
				} else {
					System.out.println(transporterName + " - Found ");
					urlTransporterEndpoint.add(transporterURL);
					System.out.println(transporterURL+"\n");
					
					
					// Server stubs creation
					
				
					
					
					//System.out.println("Creating stub ...");
					TransporterService service = new TransporterService();
					TransporterPortType port = service.getTransporterPort();

					//System.out.println("Setting endpoint address ...");
					BindingProvider bindingProvider = (BindingProvider) port;
					Map<String, Object> requestContext = bindingProvider.getRequestContext();
					requestContext.put(ENDPOINT_ADDRESS_PROPERTY, transporterURL);
					
					
					
					// Add Transporter ports to hashmap
					transporterPorts.put(transporterName, port);				
					
				}
		
				// Print de todos os transporter ports disponiveis
				
				for (String s : transporterPorts.keySet()){
					System.out.println(s);
				}
				
				
			}
			
			// Show all Transporters found 
			for(String elem: urlTransporterEndpoint){
				System.out.println(elem + "      " + "234");
			}	
			
			
			// ping de teste
			
			String resposta = transporterPorts.get("UpaTransporter1").ping("larilas");
			System.out.println(resposta);
			
			
			
			
			
			
			
			// wait
			System.out.println("Awaiting connections");
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



