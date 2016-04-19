package pt.upa.transporter.ws.it;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobView;

public class ClearJobsIT {
	public class ListJobsIT extends BaseTransporterIT {
		
		// members

		private List<JobView> allJobs;
		
		@Before
	    public void setUp() {
	    	allJobs = new ArrayList<>();
	    }
		
		@After
	    public void tearDown() {  
	        allJobs = null;
	    }
		
		@Test
		public void clearJobsTest() 
				throws BadLocationFault_Exception, BadPriceFault_Exception {
			
			final JobView oddResult1 = port.requestJob("Lisboa","Faro", 50);
			final JobView oddResult2 = port.requestJob("Lisboa","Leiria", 50);
			
			port.clearJobs();
			
			assertEquals(allJobs, port.listJobs());
			
		}
    }
}

