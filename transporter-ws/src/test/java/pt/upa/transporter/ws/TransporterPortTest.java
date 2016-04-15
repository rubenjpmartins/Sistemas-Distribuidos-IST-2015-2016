package pt.upa.transporter.ws;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 *  Unit Test suite
 *  The purpose of this class is to test TransportPort locally.
 */

public class TransporterPortTest {
	
	// members

    private TransporterPort localOddPort;
    private TransporterPort localEvenPort;   
    private JobView jobViewTest;
    private JobView jobViewTest2;
    private List<JobView> allJobs;


    // initialization and clean-up for each test

    @Before
    public void setUp() {
    	localOddPort = new TransporterPort("UpaTransporter1");
    	localEvenPort = new TransporterPort("UpaTransporter2");   	
    	jobViewTest = new JobView();
    	jobViewTest2 = new JobView();
    	allJobs = new ArrayList<>();
    }

    @After
    public void tearDown() {
        localOddPort = null;
        localEvenPort = null;     
        jobViewTest = null;
        jobViewTest = null;
        allJobs = null;
    }


    // tests

	@Test
	public void testPing() 
			throws Exception {
		final String oddResult = localOddPort.ping("UpaTransporter1");
		final String evenResult = localEvenPort.ping("UpaTransporter2");
		
		assertEquals("UpaTransporter1 ok", oddResult);	
		assertEquals("UpaTransporter2 ok", evenResult);
	}

	@Test
	public void testOddRequestJobOddPrice() 
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		
		final JobView result = localOddPort.requestJob("Lisboa","Faro", 21);
		
		jobViewTest.setJobOrigin("Lisboa");
		jobViewTest.setJobDestination("Faro");
		jobViewTest.setCompanyName("UpaTransporter1");
		jobViewTest.setJobIdentifier("UpaTransporter1:1");
		jobViewTest.setJobState(JobStateView.PROPOSED);
		
		assertEquals(jobViewTest.getJobOrigin(), result.getJobOrigin());
		assertEquals(jobViewTest.getJobDestination(), result.getJobDestination());
		assertEquals(jobViewTest.getCompanyName(), result.getCompanyName());
		assertEquals(jobViewTest.getJobIdentifier(), result.getJobIdentifier());
		assertEquals(jobViewTest.getJobState(), result.getJobState());
		assertTrue(result.getJobPrice() < 21 );	
	}
	
	@Test
	public void testOddRequestJobEvenPrice() 
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		
		final JobView result = localOddPort.requestJob("Lisboa","Faro", 20);
		
		jobViewTest.setJobOrigin("Lisboa");
		jobViewTest.setJobDestination("Faro");
		jobViewTest.setCompanyName("UpaTransporter1");
		jobViewTest.setJobIdentifier("UpaTransporter1:1");
		jobViewTest.setJobState(JobStateView.PROPOSED);
		
		assertEquals(jobViewTest.getJobOrigin(), result.getJobOrigin());
		assertEquals(jobViewTest.getJobDestination(), result.getJobDestination());
		assertEquals(jobViewTest.getCompanyName(), result.getCompanyName());
		assertEquals(jobViewTest.getJobIdentifier(), result.getJobIdentifier());
		assertEquals(jobViewTest.getJobState(), result.getJobState());
		assertTrue(result.getJobPrice() > 21 );	
	}
	
	@Test
	public void testEvenRequestJobEvenPrice() 
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		
		final JobView result = localEvenPort.requestJob("Lisboa","Braga", 20);
		
		jobViewTest.setJobOrigin("Lisboa");
		jobViewTest.setJobDestination("Braga");
		jobViewTest.setCompanyName("UpaTransporter2");
		jobViewTest.setJobIdentifier("UpaTransporter2:1");
		jobViewTest.setJobState(JobStateView.PROPOSED);
		
		
		assertEquals(jobViewTest.getJobOrigin(), result.getJobOrigin());
		assertEquals(jobViewTest.getJobDestination(), result.getJobDestination());
		assertEquals(jobViewTest.getCompanyName(), result.getCompanyName());
		assertEquals(jobViewTest.getJobIdentifier(), result.getJobIdentifier());
		assertEquals(jobViewTest.getJobState(), result.getJobState());
		assertTrue(result.getJobPrice() < 20 );	
	}
	
	@Test
	public void testEvenRequestJobOddPrice() 
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		
		final JobView result = localEvenPort.requestJob("Lisboa","Braga", 21);
		
		jobViewTest.setJobOrigin("Lisboa");
		jobViewTest.setJobDestination("Braga");
		jobViewTest.setCompanyName("UpaTransporter2");
		jobViewTest.setJobIdentifier("UpaTransporter2:1");
		jobViewTest.setJobState(JobStateView.PROPOSED);
		
		assertEquals(jobViewTest.getJobOrigin(), result.getJobOrigin());
		assertEquals(jobViewTest.getJobDestination(), result.getJobDestination());
		assertEquals(jobViewTest.getCompanyName(), result.getCompanyName());
		assertEquals(jobViewTest.getJobIdentifier(), result.getJobIdentifier());
		assertEquals(jobViewTest.getJobState(), result.getJobState());
		assertTrue(result.getJobPrice() > 21 );	
	}
	
	@Test
	public void RequestJobWithPriceOverLimit() 
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		final JobView oddResult = localOddPort.requestJob("Lisboa","Faro", 150);
		final JobView evenResult = localEvenPort.requestJob("Lisboa","Braga", 150);
		
		assertEquals(null, oddResult);	
		assertEquals(null, evenResult);
	}
	
	@Test
	public void RequestJobWithPriceBetWeen1And10() 
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		final JobView oddResult = localOddPort.requestJob("Lisboa","Faro", 8);
		final JobView evenResult = localEvenPort.requestJob("Lisboa","Braga", 8);
		
		assertTrue(oddResult.getJobPrice() < 8 );	
		assertTrue(evenResult.getJobPrice() < 8 );	
		
	}
	
	@Test
	public void RequestJobWithPriceBetWeen0And1() 
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		final JobView oddResult0 = localOddPort.requestJob("Lisboa","Faro", 0);
		final JobView evenResult0 = localEvenPort.requestJob("Lisboa","Braga", 0);
		final JobView oddResult1 = localOddPort.requestJob("Lisboa","Faro", 1);
		final JobView evenResult1 = localEvenPort.requestJob("Lisboa","Braga", 1);
		
		assertEquals(0, oddResult0.getJobPrice());	
		assertEquals(0, evenResult0.getJobPrice());
		assertEquals(0, oddResult1.getJobPrice());	
		assertEquals(0, evenResult1.getJobPrice());
	}
	
	@Test
	public void RequestJobWithPriceBetWeen10And100() 
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		final JobView oddResult30 = localOddPort.requestJob("Lisboa","Faro", 30);
		final JobView evenResult30 = localEvenPort.requestJob("Lisboa","Braga", 30);
		final JobView oddResult31 = localOddPort.requestJob("Lisboa","Faro", 31);
		final JobView evenResult31 = localEvenPort.requestJob("Lisboa","Braga", 31);
		
		assertTrue(oddResult30.getJobPrice() > 30 );	
		assertTrue(evenResult30.getJobPrice() < 30 );
		assertTrue(oddResult31.getJobPrice() < 30 );	
		assertTrue(evenResult31.getJobPrice() > 30);
	}
	
	@Test
	public void RequestJobSeveralJobSameTransporter() 
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		final JobView oddResult1 = localOddPort.requestJob("Lisboa","Faro", 50);
		final JobView oddResult2 = localOddPort.requestJob("Lisboa","Faro", 60);
		final JobView oddResult3 = localOddPort.requestJob("Lisboa","Faro", 70);
		
		assertEquals("UpaTransporter1:1", oddResult1.getJobIdentifier());
		assertEquals("UpaTransporter1:2", oddResult2.getJobIdentifier());
		assertEquals("UpaTransporter1:3", oddResult3.getJobIdentifier());
		
		final JobView evenResult1 = localEvenPort.requestJob("Lisboa","Braga", 50);
		final JobView evenResult2 = localEvenPort.requestJob("Lisboa","Braga", 60);
		final JobView evenResult3 = localEvenPort.requestJob("Lisboa","Braga", 70);
		
		assertEquals("UpaTransporter2:1", evenResult1.getJobIdentifier());
		assertEquals("UpaTransporter2:2", evenResult2.getJobIdentifier());
		assertEquals("UpaTransporter2:3", evenResult3.getJobIdentifier());
	}
	
	@Test (expected = BadLocationFault_Exception.class)
	public void RequestJobOriginBadLocationGiven()
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		
		final JobView oddResult1 = localOddPort.requestJob("Oeiras","Faro", 50);		
		final JobView evenResult1 = localEvenPort.requestJob("Oeiras","Porto", 50);
	}
	
	@Test (expected = BadLocationFault_Exception.class)
	public void RequestJobDestinationBadLocationGiven()
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		
		final JobView oddResult1 = localOddPort.requestJob("Faro","Oeiras", 60);
		final JobView evenResult1 = localEvenPort.requestJob("Porto","Oeiras", 60);
	}
	
	@Test (expected = BadPriceFault_Exception.class)
	public void RequestJobBadPriceGiven()
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		
		final JobView oddResult1 = localOddPort.requestJob("Lisboa","Faro", -1);
		final JobView evenResult1 = localEvenPort.requestJob("Lisboa","Braga", -1);
	}
	
	@Test
	public void listJobsTest() 
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		
		final JobView oddResult1 = localOddPort.requestJob("Lisboa","Faro", 50);
		allJobs.add(oddResult1);
		final JobView oddResult2 = localOddPort.requestJob("Lisboa","Leiria", 50);
		allJobs.add(oddResult2);
		
		assertEquals(allJobs, localOddPort.listJobs());
		
	}
	
	@Test
	public void clearJobsTest() 
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		
		final JobView oddResult1 = localOddPort.requestJob("Lisboa","Faro", 50);
		final JobView oddResult2 = localOddPort.requestJob("Lisboa","Leiria", 50);
		
		localOddPort.clearJobs();
		
		assertEquals(allJobs, localOddPort.listJobs());
		
	}
	
	@Test
	public void jobStatusTest() 
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		
		final JobView oddResult1 = localOddPort.requestJob("Lisboa","Faro", 50);
		
		jobViewTest.setJobOrigin("Lisboa");
		jobViewTest.setJobDestination("Faro");
		jobViewTest.setCompanyName("UpaTransporter1");
		jobViewTest.setJobIdentifier("UpaTransporter1:1");
		jobViewTest.setJobPrice(50);
		jobViewTest.setJobState(JobStateView.PROPOSED);	
		
		assertEquals(jobViewTest.getJobOrigin(), localOddPort.jobStatus("UpaTransporter1:1").getJobOrigin());
		assertEquals(jobViewTest.getJobDestination(), localOddPort.jobStatus("UpaTransporter1:1").getJobDestination());
		assertEquals(jobViewTest.getCompanyName(), localOddPort.jobStatus("UpaTransporter1:1").getCompanyName());
		assertEquals(jobViewTest.getJobIdentifier(), localOddPort.jobStatus("UpaTransporter1:1").getJobIdentifier());
		assertEquals(jobViewTest.getJobState(), localOddPort.jobStatus("UpaTransporter1:1").getJobState());
		assertTrue(localOddPort.jobStatus("UpaTransporter1:1").getJobPrice() > 50 );	

		
	}
	
	public void jobStatusNullTest() 
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		
		final JobView oddResult1 = localOddPort.requestJob("Lisboa","Faro", 50);
		
		jobViewTest.setJobIdentifier("UpaTransporter1:2");
		
		assertEquals(null, localOddPort.jobStatus("UpaTransporter2:2"));

		
	}
	

	
	
	
	
	@Test(expected=BadJobFault_Exception.class)
    public void testDecideNoJobID() throws BadJobFault_Exception, BadLocationFault_Exception, BadPriceFault_Exception{
    	
    	jobViewTest = localOddPort.requestJob("Lisboa", "Beja", 51);
    	JobView changed = null;
    	allJobs.add(jobViewTest);
    	
    	jobViewTest2 = localEvenPort.requestJob("Lisboa", "Braga", 60);
    	JobView changed2 = null;
    	allJobs.add(jobViewTest2);

    	for(JobView j: allJobs){
    		if(j.getJobIdentifier().equals("UpaTransporter1:1")){
    			changed = localOddPort.decideJob("UpaTransporter3:1", true);
    		}
    	}

        // if the assert fails, the test fails
    }
    
    @Test
    public void testDecideJobYes() throws BadJobFault_Exception, BadLocationFault_Exception, BadPriceFault_Exception{
    	
    	jobViewTest2 = localEvenPort.requestJob("Lisboa", "Braga", 60);
    	JobView alterado2 = null;
    	allJobs.add(jobViewTest2);
    	
    	jobViewTest = localOddPort.requestJob("Lisboa", "Beja", 51);
    	JobView alterado = null;
    	allJobs.add(jobViewTest);
    	    	
    	for(JobView j: allJobs){
    		if(j.getJobIdentifier().equals("UpaTransporter1:1")){
    			alterado = localOddPort.decideJob("UpaTransporter1:1", true);
    		}
    	}
    	assertEquals(JobStateView.ACCEPTED,alterado.getJobState());

        // if the assert fails, the test fails
    }
    
    @Test
    public void testDecideJobNo() throws BadJobFault_Exception, BadLocationFault_Exception, BadPriceFault_Exception{
    	
    	jobViewTest2 = localEvenPort.requestJob("Lisboa", "Braga", 60);
    	JobView alterado2 = null;
    	allJobs.add(jobViewTest2);
    	
    	jobViewTest = localOddPort.requestJob("Lisboa", "Beja", 51);
    	JobView alterado = null;
    	allJobs.add(jobViewTest);
    	    	
    	for(JobView j: allJobs){
    		if(j.getJobIdentifier().equals("UpaTransporter1:1")){
    			alterado = localOddPort.decideJob("UpaTransporter1:1", false);
    		}
    	}
    	assertEquals(JobStateView.REJECTED,alterado.getJobState());

        // if the assert fails, the test fails
    }
	
	

	
	
}
