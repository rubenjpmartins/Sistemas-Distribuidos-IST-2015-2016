package pt.upa.broker.ws.it;

import static org.junit.Assert.assertEquals;

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

public class ClearBrokerIT extends BaseBrokerIT {
	// members

	// initialization and clean-up for each test

	private List<TransportView> allJobs;
	
	@Before
    public void setUp() {
    	allJobs = new ArrayList<>();
    }
	
	@After
	
	public void tearDown() {
		port.clearTransports();
		allJobs=null;
    }
	
	@Test
	public void clearTransportsTest() 
			throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		
		port.requestTransport("Lisboa","Faro", 49);
		port.requestTransport("Lisboa","Leiria", 37);
		
		port.clearTransports();
		
		assertEquals(allJobs, port.listTransports());	
	}
}
