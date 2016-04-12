package pt.upa.broker;

import pt.upa.broker.ws.BrokerEndpointManager;

public class BrokerApplication {

	public static void main(String[] args) throws Exception {
		System.out.println(BrokerApplication.class.getSimpleName() + " starting...");
	
		BrokerEndpointManager brokerServerConnect = new BrokerEndpointManager(args);
		brokerServerConnect.serverConnect();
		
	
		

	}

	}
