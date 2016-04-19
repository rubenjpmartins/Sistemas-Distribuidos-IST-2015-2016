package pt.upa.broker.ws.it;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.TransportView;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;

public class ListTransportsIT extends BaseBrokerIT {

	// members
	
	// initialization and clean-up for each test

	@Before
	
	public void setUp() {
		port.clearTransports();
    }
	
	@After
	
	public void tearDown() {
		port.clearTransports();
    }
	

	@Test
    public void testListTransports() 
    		throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		
		port.requestTransport("Faro", "Lisboa", 21);
		
		assertEquals("Faro", port.listTransports().get(0).getOrigin());
		assertEquals("Lisboa", port.listTransports().get(0).getDestination());
		assertTrue(21 > port.listTransports().get(0).getPrice());
		assertEquals("UpaTransporter1", port.listTransports().get(0).getTransporterCompany());
		assertEquals("1", port.listTransports().get(0).getId());
		
		
	}
}
