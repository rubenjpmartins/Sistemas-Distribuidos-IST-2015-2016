package pt.upa.transporter.ws.it;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 *  Integration Test example
 *  
 *  Invoked by Maven in the "verify" life-cycle phase
 *  Should invoke "live" remote servers 
 */

public class PingClientIT extends BaseTransporterIT {

    @Test
    public void testOddPing() {
    	
    	final String result = port.ping("Ping"); 
    	assertEquals("Ping ok", result);
    }

}