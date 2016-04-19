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

public class JobStatusIT extends BaseTransporterIT {
	
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
	
	@Test
	public void jobStatusTest() 
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		
		final JobView oddResult1 = port.requestJob("Lisboa","Faro", 40);
		
		jobViewTest.setJobOrigin("Lisboa");
		jobViewTest.setJobDestination("Faro");
		jobViewTest.setCompanyName("UpaTransporter1");
		jobViewTest.setJobIdentifier("UpaTransporter1:1");
		jobViewTest.setJobPrice(50);
		jobViewTest.setJobState(JobStateView.PROPOSED);	
		
		assertEquals("Lisboa", port.jobStatus("UpaTransporter1:1").getJobOrigin());
		assertEquals("Faro", port.jobStatus("UpaTransporter1:1").getJobDestination());
		assertEquals("UpaTransporter1", port.jobStatus("UpaTransporter1:1").getCompanyName());
		assertEquals("UpaTransporter1:1", port.jobStatus("UpaTransporter1:1").getJobIdentifier());
		assertEquals(JobStateView.PROPOSED, port.jobStatus("UpaTransporter1:1").getJobState());
		assertTrue(port.jobStatus("UpaTransporter1:1").getJobPrice() > 40 );	

		
	}
	
	@Test
	public void jobStatusNullTest() 
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		
		final JobView oddResult1 = port.requestJob("Lisboa","Faro", 50);
		jobViewTest.setJobIdentifier("UpaTransporter1:2");
		
		assertEquals(null, port.jobStatus("UpaTransporter2:2"));	
	}
}
