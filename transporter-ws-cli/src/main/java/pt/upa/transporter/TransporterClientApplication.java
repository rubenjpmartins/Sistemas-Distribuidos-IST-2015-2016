package pt.upa.transporter;

import java.util.Map;

import javax.xml.ws.BindingProvider;
import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.transporter.ws.TransporterPortType;
import pt.upa.transporter.ws.TransporterService;
import pt.upa.transporter.ws.cli.TransporterClient;

public class TransporterClientApplication {

	public static void main(String[] args) throws Exception {
		System.out.println(TransporterClientApplication.class.getSimpleName() + " starting...");
		
		// Check arguments
				if (args.length < 3) {
					System.err.println("Argument(s) missing!");
					System.err.printf("Usage: java %s uddiURL name%n", TransporterClient.class.getName());
					return;
				}
				
				String uddiURL = args[0];
				String name = args[1];
				String id = args[2];

				System.out.printf("Contacting UDDI at %s%n", uddiURL);
				UDDINaming uddiNaming = new UDDINaming(uddiURL);
				
				String targetServer = name + id;
				System.out.printf("Looking for '%s'%n", targetServer);
				String endpointAddress = uddiNaming.lookup(targetServer);

				if (endpointAddress == null) {
					System.out.println("Not found!");
					return;
				} else {
					System.out.printf("Found %s%n", endpointAddress);
				}

				System.out.println("Creating stub ...");
				TransporterService service = new TransporterService();
				TransporterPortType port = service.getTransporterPort();

				System.out.println("Setting endpoint address ...");
				BindingProvider bindingProvider = (BindingProvider) port;
				Map<String, Object> requestContext = bindingProvider.getRequestContext();
				requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);

				String result = port.ping("friend");
				System.out.println(result);

	}
}
