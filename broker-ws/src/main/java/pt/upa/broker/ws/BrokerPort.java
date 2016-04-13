package pt.upa.broker.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobView;
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
		
		// Map com a sequencia (Upatransporter1, Port desse transporter)
		ports = transporterPorts;
				
		// inicializa lista para guardar estados transportadoras
		List <TransportView> transportersStates = new ArrayList<>();
		
		// inicializa lista para guardar estado dos jobs
		List <JobView> jobStates = new ArrayList<>();		
		
	}
	
	@Override
	public String ping(String name) {	

		int tamanhoports = ports.size();
		System.out.println(tamanhoports);
		int numeroRespostasPing = 0;
		System.out.println(name);

		// Envia os pedidos para os TransporterServers
		for (TransporterPortType value : ports.values()){
			if (value.ping(name) != null){
				numeroRespostasPing++;		
			}
		}

		if(numeroRespostasPing == tamanhoports){
			return "All OK - Ping Sucessful";
		}else{
			return "Ping Fail ";
		}
		
		
	}

	@Override
	public String requestTransport(String origin, String destination, int price)
			throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
			UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {

		
				int minValueProposed = 99999; // inicializa com valor grande para depois ser decrementado com o valor min
				String nomeTransportadoraMin;
				try{

					int tamanhoports = ports.size();
					System.out.println(tamanhoports);

					// Envia os pedidos para os TransporterServers
					for (TransporterPortType value : ports.values()){
	
						// envio da funcao
						JobView respostaTrasportadora = new JobView();
						respostaTrasportadora = value.requestJob(origin, destination, price);
						
						//Verificacao se funcionou ou recebeu Null
						if (respostaTrasportadora != null){
							System.out.println("consegui enviar");

							//adicionar à lista e por para resquested
							


							// Escolhe a a transportadora a enviar a adjudicar o servico
							if(respostaTrasportadora.getJobPrice()< minValueProposed ){
								minValueProposed = respostaTrasportadora.getJobPrice();
								nomeTransportadoraMin = respostaTrasportadora.getCompanyName();	
							}
							
							// verifica se o valor minimo é cumprido
							if(minValueProposed>price){
									
								UnavailableTransportPriceFault faultInfo = new UnavailableTransportPriceFault();
								faultInfo.setBestPriceFound(minValueProposed); // envia o preço minimo proposto
								throw new UnavailableTransportPriceFault_Exception("Max price proposed is:", faultInfo);

							}
							
								
							
							
							
							
							
							//return "primeira tentativa"
							}
						else{
							
							System.out.println("não consegui funcionar");
							
							// Tratamento de erros 
							
							BadPriceFault faultInfo = new BadPriceFault();
							faultInfo.setPrice(price);
							throw new BadPriceFault_Exception("Negative Price", faultInfo);
					///////
							
							
							
						}
	
							// falta acrescentar else?
			
						}
					return "TransportRequest Sucessful";
					
				} catch (BadPriceFault_Exception e) {

				    System.err.println("BadPriceFault Exception: " + e.getMessage());

				    // Retorna erro de preço inferior a 0
					
					InvalidPriceFault faultInfo = new InvalidPriceFault();
					faultInfo.setPrice(e.getFaultInfo().getPrice());
					throw new InvalidPriceFault_Exception("Invalid Price - Price lower that 0", faultInfo);				 				    
				    
				} catch (BadLocationFault_Exception e) {
				    System.err.println("BadLocationFault Exception: " + e.getMessage());

				    // Retorna erro de cidade inválida 
				    UnknownLocationFault faultInfo = new UnknownLocationFault();
					faultInfo.setLocation(e.getFaultInfo().getLocation());
					throw new UnknownLocationFault_Exception("Unknown Destination:", faultInfo);		

				}
		
	}

	@Override
	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportView> listTransports() {
		return null;
	}

	@Override
	public void clearTransports() {
		// TODO Auto-generated method stub
		
	}

	// TO DO

}
