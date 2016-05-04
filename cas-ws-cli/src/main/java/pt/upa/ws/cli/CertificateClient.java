package pt.upa.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.Map;

import javax.xml.ws.BindingProvider;

// classes generated from WSDL
import pt.upa.ws.CertificateFileInterface; 
import pt.upa.ws.CertificateImplService; 
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;


// não vai existir main - isto vai ser chamado pelo broker ou trasporter
// adicionar dependencias disto aos transporter e broker ?)
// fazer construtor
// os argumentos do construtor sao os args



public class CertificateClient {

	public static void main(String[] args) throws Exception {
		// Check arguments
		if (args.length < 2) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s uddiURL name%n", CertificateClient.class.getName());
			return;
		}

		String uddiURL = args[0];
		String name = args[1];

		System.out.printf("Contacting UDDI at %s%n", uddiURL);
		UDDINaming uddiNaming = new UDDINaming(uddiURL);

		System.out.printf("Looking for '%s'%n", name);
		String endpointAddress = uddiNaming.lookup(name);

		if (endpointAddress == null) {
			System.out.println("Not found!");
			return;
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
		String result = port.sayHello("friend");  ///////// Alterar
		System.out.println(result);
	}

}
