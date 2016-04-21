package pt.upa.transporter.ws.it;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.upa.transporter.ws.BadJobFault_Exception;
import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobStateView;
import pt.upa.transporter.ws.JobView;

public class DecideJobIT extends BaseTransporterIT {
			
			// members
	
			private JobView jobViewTest;
			private JobView jobViewTest2;
			private List<JobView> allJobs;
			

		    // initialization and clean-up for each test

			@Before
		    public void setUp() {
				jobViewTest = new JobView();
				jobViewTest2 = new JobView();
		    	allJobs = new ArrayList<>();
		    }
			
			@After
		    public void tearDown() {
			    jobViewTest = null; 
			    jobViewTest2 = null;
		    	port.clearJobs();
		    	port2.clearJobs();   
		        allJobs = null;
		    }

		    // Adicionado depois da entrega da 1ª Parte


		    @Test(expected=BadJobFault_Exception.class)
		    public void testDecideJobAlreadyFailed() throws BadJobFault_Exception, BadLocationFault_Exception, BadPriceFault_Exception{
		    	
		    	jobViewTest2 = port2.requestJob("Lisboa", "Braga", 25);
		    	JobView changed2 = null;
		    	allJobs.add(jobViewTest2);
		    	
		    	for(JobView j2: allJobs){
		    		if(j2.getJobIdentifier().equals("UpaTransporter2:1")){
		    			changed2 = port2.decideJob("UpaTransporter2:1", false);
		    		}
		    	}

		    	// envia o decide outravez mas retorna BadJobFault_Exception

		    	for(JobView j2: allJobs){
		    		if(j2.getJobIdentifier().equals("UpaTransporter2:1")){
		    			changed2 = port2.decideJob("UpaTransporter2:1", false);
		    		}
		    	}

		    }


		    @Test(expected=BadJobFault_Exception.class)
		    public void testDecideJobAlreadyAccepted() throws BadJobFault_Exception, BadLocationFault_Exception, BadPriceFault_Exception{
		    	
		    	int x = 1;
		    	
		    	jobViewTest2 = port2.requestJob("Lisboa", "Braga", 75);
		    	JobView changed2 = null;
		    	allJobs.add(jobViewTest2);
		    	
		    	jobViewTest = port.requestJob("Lisboa", "Faro", 56);
		    	JobView changed = null;
		    	allJobs.add(jobViewTest);
		    	    	
		    	for(JobView j: allJobs){
		    		if(j.getJobIdentifier().equals("UpaTransporter1:1")){
		    			changed = port.decideJob("UpaTransporter1:1", true);
		    		}
		    	
		    	}

				// envia o decide outravez mas retorna BadJobFault_Exception

		    	for(JobView j: allJobs){
		    		if(j.getJobIdentifier().equals("UpaTransporter1:1")){
		    			changed = port.decideJob("UpaTransporter1:1", true);
		    		}
		    	
		    	}
		    	
		    }


		    // tests
		

		    // tests
			
			@Test(expected=BadJobFault_Exception.class)
		    public void testDecideNoJobID() throws BadJobFault_Exception, BadLocationFault_Exception, BadPriceFault_Exception{
		    	
		    	jobViewTest = port.requestJob("Lisboa", "Faro", 33);
		    	JobView changed = null;
		    	allJobs.add(jobViewTest);
		    	
		    	jobViewTest2 = port2.requestJob("Lisboa", "Braga", 35);
		    	JobView changed2 = null;
		    	allJobs.add(jobViewTest2);

		    	for(JobView j: allJobs){
		    		if(j.getJobIdentifier().equals("UpaTransporter1:1")){
		    			changed = port.decideJob("UpaTransporter3:1", true);
		    		}
		    	}
		    }
			
			
			@Test
		    public void testDecideJobYes() throws BadJobFault_Exception, BadLocationFault_Exception, BadPriceFault_Exception, InterruptedException{
		    	
		    	int x = 1;
		    	
		    	jobViewTest2 = port2.requestJob("Lisboa", "Braga", 75);
		    	JobView changed2 = null;
		    	allJobs.add(jobViewTest2);
		    	
		    	jobViewTest = port.requestJob("Lisboa", "Faro", 56);
		    	JobView changed = null;
		    	allJobs.add(jobViewTest);
		    	    	
		    	for(JobView j: allJobs){
		    		if(j.getJobIdentifier().equals("UpaTransporter1:1")){
		    			changed = port.decideJob("UpaTransporter1:1", true);
		    		}
		    	
		    	}
		    	
		    	assertEquals(JobStateView.ACCEPTED,port.jobStatus(changed.getJobIdentifier()).getJobState());
		    	System.out.println("Waiting for status change...");
		    	Thread.sleep(16000); // Espera pelas mudancas do status
		    	assertEquals(JobStateView.COMPLETED,port.jobStatus(changed.getJobIdentifier()).getJobState());


		    }
			
			
		    @Test
		    public void testDecideJobNo() throws BadJobFault_Exception, BadLocationFault_Exception, BadPriceFault_Exception{
		    	
		    	jobViewTest2 = port2.requestJob("Lisboa", "Braga", 25);
		    	JobView changed2 = null;
		    	allJobs.add(jobViewTest2);
		    	
		    	jobViewTest = port.requestJob("Lisboa", "Faro", 29);
		    	JobView changed = null;
		    	allJobs.add(jobViewTest);
		    	    	
		    	for(JobView j: allJobs){
		    		if(j.getJobIdentifier().equals("UpaTransporter1:1")){
		    			changed = port.decideJob("UpaTransporter1:1", false);
		    		}
		    	}
		    	
		    	for(JobView j2: allJobs){
		    		if(j2.getJobIdentifier().equals("UpaTransporter2:1")){
		    			changed2 = port2.decideJob("UpaTransporter2:1", false);
		    		}
		    	}
		    	
		    	assertEquals(JobStateView.REJECTED,port.jobStatus(changed.getJobIdentifier()).getJobState());
		    	assertEquals(JobStateView.REJECTED,port2.jobStatus(changed2.getJobIdentifier()).getJobState());

		    }
}
