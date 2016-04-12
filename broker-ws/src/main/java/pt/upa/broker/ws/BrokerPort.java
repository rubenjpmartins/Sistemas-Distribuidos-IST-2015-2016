package pt.upa.broker.ws;

import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import pt.upa.transporter.ws.TransporterPortType;

@WebService(
		endpointInterface="pt.upa.broker.ws.BrokerPortType",
		wsdlLocation="broker.1_0.wsdl",
		name="UpaBroker",
		portName="BrokerPort",
		targetNamespace="http://ws.broker.upa.pt/",
		serviceName="BrokerService"
)

public class BrokerPort implements BrokerPortType{	
	private Map<String,TransporterPortType> ports;
	
	public BrokerPort(Map<String,TransporterPortType> transporterPorts){
		ports = transporterPorts;
	}
	
	@Override
	public String ping(String name) {	
		for (TransporterPortType value : ports.values()){
			value.ping(name);
		}
		return "Server";
	}

	@Override
	public String requestTransport(String origin, String destination, int price)
			throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
			UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		// TODO Auto-generated method stub
		return "TransportRequest";
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

	// TO DO

}
