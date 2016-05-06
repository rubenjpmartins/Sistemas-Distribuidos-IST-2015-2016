package pt.upa.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.Map;

import javax.xml.ws.BindingProvider;

// classes generated from WSDL
import pt.upa.ws.CertificateFileInterface; 
import pt.upa.ws.CertificateImplService; 
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;



// adicionar dependencias disto aos transporter e broker ?)



	public class CertificateClient {
		
		private String uddiURL;
		private String name;
		private String url;
	
	
	public CertificateClient(String[] args){
		
		//Check arguments
		if (args.length < 2) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s uddiURL name%n", CertificateClient.class.getName());
			return;
			}
				
		uddiURL = args[0];
		name = args[1];
		url = args[2];
	}
	
	

	//public static void main(String[] args) throws Exception {
	
	
	
	
	//Martelado
	public CertificateClient(String URL, String nome) {

		uddiURL = URL;
		name = nome;
	
	}



	public String serverConnect() {
		
		String result = null;

		try{

			System.out.printf("Contacting UDDI at %s%n", uddiURL);
			UDDINaming uddiNaming = new UDDINaming(uddiURL);
	
			System.out.printf("Looking for '%s'%n", name);
			String endpointAddress = uddiNaming.lookup(name);
	
			if (endpointAddress == null) {
				System.out.println("Not found!");
				return "null";
			} else {
				System.out.printf("Found %s%n", endpointAddress);
			}

			System.out.println("Creating stub ...");
			CertificateImplService service = new CertificateImplService();
			CertificateFileInterface port = service.getCertificateImplPort();
	
			System.out.println("Setting endpoint address ...");
			BindingProvider bindingProvider = (BindingProvider) port;
			Map<String, Object> requestContext = bindingProvider.getRequestContext();
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);

		

			System.out.println("Remote call ...");
			result = port.sayHello("friend");  ///////// Alterar
			System.out.println(result);
			
			

			
		} catch (Exception e) {
			System.out.printf("Caught exception: %s%n", e);
			e.printStackTrace();
		
	
		}
		
		// para depois eliminar
		return result;

	}
}	

