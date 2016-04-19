package pt.upa.transporter.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobStateView;
import pt.upa.transporter.ws.JobView;

public class RequestJobIT extends BaseTransporterIT {
	
		
		// members
		
		private JobView jobViewTest;

	    // initialization and clean-up for each test

		@Before
	    public void setUp() {
			jobViewTest = new JobView();
	    }
		
		@After
	    public void tearDown() {
		    jobViewTest = null; 	
	    	port.clearJobs();
	    	port2.clearJobs();   
	    }


	    // tests

		@Test
		public void testOddRequestJobOddPrice() 
				throws BadLocationFault_Exception, BadPriceFault_Exception {
			
			final JobView result1 = port.requestJob("Lisboa","Faro", 21);
			
			jobViewTest.setJobOrigin("Lisboa");
			jobViewTest.setJobDestination("Faro");
			jobViewTest.setCompanyName("UpaTransporter1");
			jobViewTest.setJobIdentifier("UpaTransporter1:1");
			jobViewTest.setJobState(JobStateView.PROPOSED);
			
			assertEquals("Lisboa", result1.getJobOrigin());
			assertEquals("Faro", result1.getJobDestination());
			assertEquals("UpaTransporter1", result1.getCompanyName());
			assertEquals("UpaTransporter1:1", result1.getJobIdentifier());
			assertEquals(JobStateView.PROPOSED, result1.getJobState());
			assertTrue(result1.getJobPrice() < 21 );
			
			final JobView result2 = port2.requestJob("Lisboa","Faro", 21);
			assertEquals(null, result2);			
		}
		
		@Test
		public void testOddRequestJobEvenPrice() 
				throws BadLocationFault_Exception, BadPriceFault_Exception {
			
			final JobView result1 = port.requestJob("Lisboa","Faro", 20);
			
			jobViewTest.setJobOrigin("Lisboa");
			jobViewTest.setJobDestination("Faro");
			jobViewTest.setCompanyName("UpaTransporter1");
			jobViewTest.setJobIdentifier("UpaTransporter1:1");
			jobViewTest.setJobState(JobStateView.PROPOSED);
			
			assertEquals("Lisboa", result1.getJobOrigin());
			assertEquals("Faro", result1.getJobDestination());
			assertEquals("UpaTransporter1", result1.getCompanyName());
			assertEquals("UpaTransporter1:1", result1.getJobIdentifier());
			assertEquals(JobStateView.PROPOSED, result1.getJobState());
			assertTrue(result1.getJobPrice() > 21 );	
			
			final JobView result2 = port2.requestJob("Lisboa","Faro", 21);
			assertEquals(null, result2);
		}
		
		@Test
		public void testEvenRequestJobEvenPrice() 
				throws BadLocationFault_Exception, BadPriceFault_Exception {
			
			final JobView result2 = port2.requestJob("Lisboa","Braga", 20);
			
			jobViewTest.setJobOrigin("Lisboa");
			jobViewTest.setJobDestination("Braga");
			jobViewTest.setCompanyName("UpaTransporter2");
			jobViewTest.setJobIdentifier("UpaTransporter2:1");
			jobViewTest.setJobState(JobStateView.PROPOSED);
			
			
			assertEquals("Lisboa", result2.getJobOrigin());
			assertEquals("Braga", result2.getJobDestination());
			assertEquals("UpaTransporter2", result2.getCompanyName());
			assertEquals("UpaTransporter2:1", result2.getJobIdentifier());
			assertEquals(JobStateView.PROPOSED, result2.getJobState());
			assertTrue(result2.getJobPrice() < 20 );	
			
			final JobView result1 = port.requestJob("Lisboa","Braga", 20);
			assertEquals(null, result1);
		}
		
		@Test
		public void testEvenRequestJobOddPrice() 
				throws BadLocationFault_Exception, BadPriceFault_Exception {
			
			final JobView result2 = port2.requestJob("Lisboa","Braga", 21);
			
			jobViewTest.setJobOrigin("Lisboa");
			jobViewTest.setJobDestination("Braga");
			jobViewTest.setCompanyName("UpaTransporter2");
			jobViewTest.setJobIdentifier("UpaTransporter2:1");
			jobViewTest.setJobState(JobStateView.PROPOSED);
			
			
			assertEquals("Lisboa", result2.getJobOrigin());
			assertEquals("Braga", result2.getJobDestination());
			assertEquals("UpaTransporter2", result2.getCompanyName());
			assertEquals("UpaTransporter2:1", result2.getJobIdentifier());
			assertEquals(JobStateView.PROPOSED, result2.getJobState());
			assertTrue(result2.getJobPrice() > 21 );	
			
			final JobView result1 = port.requestJob("Lisboa","Braga", 21);
			assertEquals(null, result1);
		}
		
		@Test
		public void RequestJobWithPriceOverLimit() 
				throws BadLocationFault_Exception, BadPriceFault_Exception {
			final JobView oddResult = port.requestJob("Lisboa","Faro", 150);
			final JobView evenResult = port2.requestJob("Lisboa","Braga", 150);
			
			assertEquals(null, oddResult);	
			assertEquals(null, evenResult);
		}
		
		
		@Test
		public void RequestJobWithPriceBetWeen1And10() 
				throws BadLocationFault_Exception, BadPriceFault_Exception {
			final JobView oddResult = port.requestJob("Lisboa","Faro", 8);
			final JobView evenResult = port2.requestJob("Lisboa","Braga", 8);
			
			assertTrue(oddResult.getJobPrice() < 8 );	
			assertTrue(evenResult.getJobPrice() < 8 );	
			
		}
		
		@Test
		public void RequestJobWithPriceBetWeen0And1() 
				throws BadLocationFault_Exception, BadPriceFault_Exception {
			final JobView oddResult0 = port.requestJob("Lisboa","Faro", 0);
			final JobView evenResult0 = port2.requestJob("Lisboa","Braga", 0);
			final JobView oddResult1 = port.requestJob("Lisboa","Faro", 1);
			final JobView evenResult1 = port2.requestJob("Lisboa","Braga", 1);
			
			assertEquals(0, oddResult0.getJobPrice());	
			assertEquals(0, evenResult0.getJobPrice());
			assertEquals(0, oddResult1.getJobPrice());	
			assertEquals(0, evenResult1.getJobPrice());
		}
		
		
		@Test
		public void RequestJobWithPriceBetWeen10And100() 
				throws BadLocationFault_Exception, BadPriceFault_Exception {
			final JobView oddResult30 = port.requestJob("Lisboa","Faro", 30);
			final JobView evenResult30 = port2.requestJob("Lisboa","Braga", 30);
			final JobView oddResult31 = port.requestJob("Lisboa","Faro", 31);
			final JobView evenResult31 = port2.requestJob("Lisboa","Braga", 31);
			
			assertTrue(oddResult30.getJobPrice() > 30 );	
			assertTrue(evenResult30.getJobPrice() < 30 );
			assertTrue(oddResult31.getJobPrice() < 30 );	
			assertTrue(evenResult31.getJobPrice() > 30);
		}
		
		
		@Test
		public void RequestJobSeveralJobSameTransporter() 
				throws BadLocationFault_Exception, BadPriceFault_Exception {
			final JobView oddResult1 = port.requestJob("Lisboa","Faro", 50);
			final JobView oddResult2 = port.requestJob("Lisboa","Faro", 60);
			final JobView oddResult3 = port.requestJob("Lisboa","Faro", 70);
			
			assertEquals("UpaTransporter1:1", oddResult1.getJobIdentifier());
			assertEquals("UpaTransporter1:2", oddResult2.getJobIdentifier());
			assertEquals("UpaTransporter1:3", oddResult3.getJobIdentifier());
			
			final JobView evenResult1 = port2.requestJob("Lisboa","Braga", 50);
			final JobView evenResult2 = port2.requestJob("Lisboa","Braga", 60);
			final JobView evenResult3 = port2.requestJob("Lisboa","Braga", 70);
			
			assertEquals("UpaTransporter2:1", evenResult1.getJobIdentifier());
			assertEquals("UpaTransporter2:2", evenResult2.getJobIdentifier());
			assertEquals("UpaTransporter2:3", evenResult3.getJobIdentifier());
		}
		
		
		@Test (expected = BadLocationFault_Exception.class)
		public void RequestJobOriginBadLocationGiven()
				throws BadLocationFault_Exception, BadPriceFault_Exception {
			
			final JobView oddResult1 = port.requestJob("Oeiras","Faro", 50);		
			final JobView evenResult1 = port2.requestJob("Oeiras","Porto", 50);
		}
		
		@Test (expected = BadLocationFault_Exception.class)
		public void RequestJobDestinationBadLocationGiven()
				throws BadLocationFault_Exception, BadPriceFault_Exception {
			
			final JobView oddResult1 = port.requestJob("Faro","Oeiras", 60);
			final JobView evenResult1 = port2.requestJob("Porto","Oeiras", 60);
		}
		
		@Test (expected = BadPriceFault_Exception.class)
		public void RequestJobBadPriceGiven()
				throws BadLocationFault_Exception, BadPriceFault_Exception {
			
			final JobView oddResult1 = port.requestJob("Lisboa","Faro", -1);
			final JobView evenResult1 = port2.requestJob("Lisboa","Braga", -1);
		}
}
