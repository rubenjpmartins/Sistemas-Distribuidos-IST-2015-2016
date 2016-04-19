package pt.upa.broker.ws.it;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.TransportStateView;
import pt.upa.broker.ws.TransportView;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;
import pt.upa.broker.ws.UnknownTransportFault_Exception;

public class ViewTransport extends BaseBrokerIT {
		
		// members
	
		// initialization and clean-up for each test

		@Before
		
		public void setUp() {
			port.clearTransports();
	    }
		
		@After
		
		public void tearDown() {
			
	    }
		
		@Test (expected = UnknownTransportFault_Exception.class)
		public void testUnknowTranportFault() 
			 throws UnknownTransportFault_Exception, InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		    	
			 port.requestTransport("Braga","Porto", 18);
			 port.requestTransport("Lisboa", "Faro", 19);
		    	
			 port.viewTransport("UpaTransporter3:1");
		}
		
		
		@Test
		public void testTransportViewHeading() 
				throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
			
			List<TransportView> listaTransportViews = new ArrayList<>();
			port.requestTransport("Lisboa", "Faro", 15);
			listaTransportViews = port.listTransports();
			TransportView sacado = (listaTransportViews.get(0));
			
			
			assertEquals(TransportStateView.HEADING,sacado.getState());

		}
		
		
}
