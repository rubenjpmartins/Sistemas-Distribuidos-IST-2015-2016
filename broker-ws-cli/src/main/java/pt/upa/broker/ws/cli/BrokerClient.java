package pt.upa.broker.ws.cli;

import java.util.List;
import javax.jws.WebService;
import pt.upa.broker.ws.BrokerPortType;

import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.TransportView;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;
import pt.upa.broker.ws.UnknownTransportFault_Exception;

@WebService(
		endpointInterface="pt.upa.broker.ws.BrokerPortType",
		wsdlLocation="broker.1_1.wsdl",
		name="UpaBroker",
		portName="BrokerPort",
		targetNamespace="http://ws.broker.upa.pt/",
		serviceName="BrokerService"
)
public class BrokerClient implements BrokerPortType{
	
	
	@Override
	public String ping(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String pingToBroker(String name) {
		
		return null;
	}

	@Override
	public String requestTransport(String origin, String destination, int price)
			throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
			UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		// TODO Auto-generated method stub		
		return null;
	}

	@Override
	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportView> listTransports() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearTransports() {
		// TODO Auto-generated method stub
		
	}

}
