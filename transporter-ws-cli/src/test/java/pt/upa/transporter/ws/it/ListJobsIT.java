package pt.upa.transporter.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;

public class ListJobsIT extends BaseTransporterIT {
	
	@Test
	 public void testListJobs() throws BadLocationFault_Exception, BadPriceFault_Exception {
			
		port.requestJob("Faro", "Lisboa", 21);
		
		assertEquals("Faro", port.listJobs().get(0).getJobOrigin());
		assertEquals("Lisboa", port.listJobs().get(0).getJobDestination());
		assertTrue(21 > port.listJobs().get(0).getJobPrice());
		assertEquals("UpaTransporter1", port.listJobs().get(0).getCompanyName());
		assertEquals("UpaTransporter1:1", port.listJobs().get(0).getJobIdentifier());
	}

}
