package pt.upa.broker.ws.it;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.broker.ws.BrokerPortType;
import pt.upa.broker.ws.BrokerService;

/**
 *  Integration Test example
 *  
 *  Invoked by Maven in the "verify" life-cycle phase
 *  Should invoke "live" remote servers 
 */


public abstract class BaseBrokerIT {

		protected static BrokerPortType port = null;

		@BeforeClass
		public static void oneTimeSetup() throws Exception {

			String uddiURL = "http://localhost:9090/";
			String wsName = "UpaBroker1";
			
			//UpaTransporter1
			System.out.printf("Contacting UDDI at %s%n", uddiURL);
			UDDINaming uddiNaming = new UDDINaming(uddiURL);

			System.out.printf("Looking for '%s'%n", wsName);
			String endpointAddress = uddiNaming.lookup(wsName);
			
			BrokerService service = new BrokerService();
			port = service.getBrokerPort();
			
			System.out.println("Setting endpoint address ...");
			BindingProvider bindingProvider = (BindingProvider) port;
			Map<String, Object> requestContext = bindingProvider.getRequestContext();
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
			
		}

		@AfterClass
		public static void cleanup() {
			port = null;
		}

}