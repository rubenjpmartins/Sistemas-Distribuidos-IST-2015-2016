package pt.upa.broker.ws.it;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BrokerPingIT extends BaseBrokerIT{

	// members

    @Test
    public void testPing() {
    	
    	final String result = port.ping("Ping"); 
    	assertEquals("All OK - Ping Sucessful", result);
    }
	
	
	
}
