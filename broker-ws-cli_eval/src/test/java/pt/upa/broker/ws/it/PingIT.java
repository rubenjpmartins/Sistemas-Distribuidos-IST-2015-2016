package pt.upa.broker.ws.it;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Test suite
 */
public class PingIT extends AbstractIT {

	// tests
	// assertEquals(expected, actual);

	// public String ping(String x)

	@Test
	public void pingTest() {
		assertNotNull(CLIENT.ping("test"));
	}

}
