package pt.upa.transporter;

import java.util.HashMap;
import java.util.Map;

import pt.upa.transporter.ws.TransporterEndpointManager;

public class TransporterApplication {
	
	// Create map to store the transporters by region
	//Map<String,String> transportersByRegion = new HashMap<String,String>();

	public static void main(String[] args) throws Exception {
		System.out.println(TransporterApplication.class.getSimpleName() + " starting...");
		
		// Check arguments
		if (args.length < 3) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s uddiURL wsName wsURL%n", TransporterApplication.class.getSimpleName() + " starting...");
			return;
		}
		
		TransporterEndpointManager transporterServerConnect = new TransporterEndpointManager();
		transporterServerConnect.TransporterConnect(args);
		
		
		
		

	}

}
