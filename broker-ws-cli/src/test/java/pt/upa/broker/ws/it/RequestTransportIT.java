package pt.upa.broker.ws.it;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;

public class RequestTransportIT extends BaseBrokerIT {
	
	// members
	
	
	// initialization and clean-up for each test

	@Before
	
	public void setUp() {
		port.clearTransports();
    }
	
	@After
	
	public void tearDown() {
		
    }
	
    @Test (expected = InvalidPriceFault_Exception.class)
    public void testPriceLowerThanZero() 
    		throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
    	
    	final String result = port.requestTransport("Faro","Lisboa", -6); 
    }
    
    @Test (expected = UnknownLocationFault_Exception.class)
    public void testUnknownLocationEmptyStringOrigin() 
    		throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
    	
    	final String result = port.requestTransport("","Faro", 25); 
    }
    

    @Test (expected = UnknownLocationFault_Exception.class)
    public void testUnknownLocationEmptyStringDestination() 
    		throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
    	
    	final String result = port.requestTransport("Faro","", 25); 
    }
    
    @Test (expected = UnknownLocationFault_Exception.class)
    public void testUnknownLocationOrigin() 
    		throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
    	
    	final String result = port.requestTransport("China","Faro", 25); 
    }
 
    @Test (expected = UnknownLocationFault_Exception.class)
    public void testUnknownLocationDestination() 
    		throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
    	
    	final String result = port.requestTransport("Lisboa","China", 25); 
    }
    
    @Test (expected = UnavailableTransportFault_Exception.class)
    public void testUnavailableTransportFaultOddPrice() 
    		throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
    	
    	final String result = port.requestTransport("Porto","Faro", 29); 
    }
    
    @Test (expected = UnavailableTransportFault_Exception.class)
    public void testUnavailableTransportFaultEvenPrice() 
    		throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
    	
    	final String result = port.requestTransport("Porto","Faro", 30); 
    }
    
    
    @Test (expected = UnavailableTransportPriceFault_Exception.class)
    public void testUnavailableTransportPriceFault() 
    		throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
    	
    	final String result = port.requestTransport("Lisboa","Faro", 60); 

    }
    
    @Test
    public void testPriceZero() 
    		throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
    	
    	final String result = port.requestTransport("Lisboa","Faro", 11); 
    	
    	assertEquals("1", result);

    }
    
    @Test (expected = UnavailableTransportFault_Exception.class)
    public void testPriceGreaterThan100() 
    		throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
    	
    	final String result = port.requestTransport("Faro","Beja", 150); 
    }
    
    @Test
    public void testPriceBetween20and100() 
    		throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
    	
    	final String result = port.requestTransport("Porto","Braga", 50); 
    	

    	assertEquals("1", result);
    }
    
    @Test
    public void testePriceLowerThan10() 
    		throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
    	
    	final String result = port.requestTransport("Porto","Braga", 8); 
    	

    	assertEquals("1", result);


    }
    
    @Test
    public void testePrice0or1() 
    		throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
    	
    	final String result = port.requestTransport("Porto","Braga", 1);    	

    	assertEquals("1", result);

    }
    
}
