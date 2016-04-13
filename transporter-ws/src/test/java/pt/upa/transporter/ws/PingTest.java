package pt.upa.transporter.ws;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *  Unit Test example
 *  
 *  Invoked by Maven in the "test" life-cycle phase
 *  If necessary, should invoke "mock" remote servers 
 */

public class PingTest {
	
	 // static members
	static TransporterPort port;
	
    // one-time initialization and clean-up

    @BeforeClass
    public static void oneTimeSetUp() {
    	
    	port = new TransporterPort("UpaTransporter1");
    }

    @AfterClass
    public static void oneTimeTearDown() {

    }


    // members


    // initialization and clean-up for each test

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }


    // tests

    @Test
    public void testPing() {
    	
    	String name = "UpaTransporter1";
    
    	
    	assertEquals(name, port.ping(name));
        // if the assert fails, the test fails
    }

}
