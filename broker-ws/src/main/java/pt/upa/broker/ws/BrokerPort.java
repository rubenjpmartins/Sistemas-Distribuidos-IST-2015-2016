package pt.upa.broker.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;
import pt.upa.transporter.ws.BadJobFault_Exception;
import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobStateView;
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
	

	// Estrutura que contem os ports de todos os transportersServer
	private Map<String,TransporterPortType> ports;
	// Estrutura que contem os o ID (1 ,2, 3 etc .. ) e o TransportView associado
	private Map<String,TransportView> transportersViews;
	// Estrutura que contem os o ID (1 ,2, 3 etc .. ) e o ID do job (ex: UPATRANSPORTER1:34)
	private Map<String,String> associateIdentifiers;
	// Contador de ID ID (1 ,2, 3 etc .. )
	private int counterId;
	
	//Construtor
	public BrokerPort(Map<String,TransporterPortType> transporterPorts){
		
		// Map com a sequencia (Upatransporter1, Port desse transporter)
		ports = transporterPorts;
				
		// inicializa lista para guardar estados transportadoras
		associateIdentifiers = new HashMap<>();
		transportersViews = new HashMap<>();
		
		int counterId = 0;	
	}
	
	
	@Override
	public String ping(String name) {	

		// numero de tranportadoras online
		int tamanhoports = ports.size();

		int numeroRespostasPing = 0;
		//System.out.println(name);

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
			
			// cria e inicializa lista para guardar estado dos jobs
			List<JobView> jobStates = new ArrayList<>();
		
			//Cria uma TransportView e preenche
			TransportView clientRequest = new TransportView();
			clientRequest.setDestination(destination);
			clientRequest.setOrigin(origin);
			clientRequest.setPrice(price);
			
			// ID
			counterId++;
			clientRequest.setId(Integer.toString(counterId));
			clientRequest.setState(TransportStateView.REQUESTED);
	
			// adiciona no map de <BrokerID,TransportView>
			transportersViews.put(Integer.toString(counterId), clientRequest);

			int minValueProposed = 99999; // inicializa com valor grande para depois ser decrementado com o valor min
			String nomeTransportadoraMin ="";
			String idTransportadoraMin = "";
			int contaNull = 0;
			
				try{

					// Envia os pedidos para os TransporterServers
					for (TransporterPortType value : ports.values()){
	
						// envio da funcao
						JobView respostaTrasportadora = new JobView();
						respostaTrasportadora = value.requestJob(origin, destination, price);


						//Verificacao se funcionou ou recebeu Null
						if (respostaTrasportadora != null){
							
							// adiciona à lista jobstates cada 
							jobStates.add(respostaTrasportadora);
							
							//Actualiza o estado para BUDGETED
							transportersViews.get(Integer.toString(counterId)).setState(TransportStateView.BUDGETED);
							transportersViews.get(Integer.toString(counterId)).setPrice(respostaTrasportadora.getJobPrice());
							
							
							
							// Escolhe o valor minimo do preco para adjudicar o servico
							if(respostaTrasportadora.getJobPrice()< minValueProposed ){
								// SACA O ID DO VENCEDOR
								minValueProposed = respostaTrasportadora.getJobPrice();
								nomeTransportadoraMin = respostaTrasportadora.getCompanyName();	
								idTransportadoraMin = respostaTrasportadora.getJobIdentifier();
								
								}
							
							}

						else{ // caso seja retornado null from transporter server
							
							contaNull++;
							}
				
					}// Fim ciclo for de requests
					
					
					
					// mete o nomedotransporter no transporterview
					clientRequest.setTransporterCompany(nomeTransportadoraMin);
					
					// verifica o caso se recebeu NULL de todos os transportes
					if(contaNull==ports.size()){
						
						transportersViews.get(Integer.toString(counterId)).setState(TransportStateView.FAILED);
						
						// mete o nomedotransporter no transporterview
						clientRequest.setTransporterCompany("NoTransporterCompany");
						
						UnavailableTransportFault faultInfo = new UnavailableTransportFault();
						faultInfo.setOrigin(origin); // envia a cidade origem onde não trabalha
						faultInfo.setDestination(destination); // envia a cidade origem onde não trabalha
						throw new UnavailableTransportFault_Exception("No transporter found for this origin/destination:", faultInfo);
					}
					
					
			
					if(minValueProposed>price){ // verifica se o valor minimo é cumprido
						
						
						//Actualiza o estado para FAILED
						transportersViews.get(Integer.toString(counterId)).setState(TransportStateView.FAILED);
						
						
						for (JobView jobRejected : jobStates){
							
							String companyName = jobRejected.getCompanyName();
							String jobId = jobRejected.getJobIdentifier();
							
							TransporterPortType portRejected = ports.get(companyName);
							
							try{
								portRejected.decideJob(jobId, false);
							} catch(BadJobFault_Exception e){
								
							    System.err.println("BadJobFault_Exception " + e.getMessage());

							}
							
						}
						
	
						String contador2 = Integer.toString(counterId);
						associateIdentifiers.put(contador2, idTransportadoraMin);
						
						UnavailableTransportPriceFault faultInfo = new UnavailableTransportPriceFault();
						faultInfo.setBestPriceFound(minValueProposed); // envia o preço minimo proposto
						throw new UnavailableTransportPriceFault_Exception("Max price proposed is: ", faultInfo);
						
						//vai enviar fail para todas
	
					}
					else{  // corresponde às condiçoes todas
						
						//Actualiza o estado para BOOKed
						transportersViews.get(Integer.toString(counterId)).setState(TransportStateView.BOOKED);

						
						//vai enviar accepted para 1 e fail para o resto

						for (JobView jobAccepted : jobStates){
							
							String companyName = jobAccepted.getCompanyName();
							String jobId = jobAccepted.getJobIdentifier();
							
							
							TransporterPortType portAccepted = ports.get(companyName);
							
							if(idTransportadoraMin.equals(jobId)){ // envia decide para a unica tranportadora winner
								
								// adiciona na lista ID - ID --> ex ( 1 , UPAtransporter1:3 )
								String contador2 = Integer.toString(counterId);
								associateIdentifiers.put(contador2, idTransportadoraMin);
							    
								
								try{
									portAccepted.decideJob(jobId, true); // envia accepted para o trasportr winner
								} catch(BadJobFault_Exception e){
									
								    System.err.println("BadJobFault_Exception " + e.getMessage());
									
								}
							}else{
								
								
								String contador2 = Integer.toString(counterId);
								associateIdentifiers.put(contador2, idTransportadoraMin);
								
								try{
									portAccepted.decideJob(jobId, false);// envia accepted para o trasportr lose
								} catch(BadJobFault_Exception e){
									
								    System.err.println("BadJobFault_Exception " + e.getMessage());
								}	
							}
						}// fim for para 1 e fail para o resto
					}//acaba o ciclo for

					//return "TransportRequest Sucessful"
					return Integer.toString(counterId);



				} catch (BadPriceFault_Exception e) {
					// Retorna erro de preço inferior a 0
				    System.err.println("BadPriceFault Exception: " + e.getMessage());
				    
				    // Muda o estado para failed
				    transportersViews.get(Integer.toString(counterId)).setState(TransportStateView.FAILED);
				    
				    // mete o nomedotransporter no transporterview
					clientRequest.setTransporterCompany("NoTransporterCompany-Failed");
					
					// faz trows do invalid price <0 
					InvalidPriceFault faultInfo = new InvalidPriceFault();
					faultInfo.setPrice(e.getFaultInfo().getPrice());
					throw new InvalidPriceFault_Exception("Invalid Price - Price lower that 0", faultInfo);				 				    
				    
					
				} catch (BadLocationFault_Exception e) {
				    System.err.println("BadLocationFault Exception: " + e.getMessage());
				    transportersViews.get(Integer.toString(counterId)).setState(TransportStateView.FAILED);
				    // mete o nomedotransporter no transporterview
					clientRequest.setTransporterCompany("NoTransporterCompany-Failed");

				    // Retorna erro de cidade inválida 
				    UnknownLocationFault faultInfo = new UnknownLocationFault();
					faultInfo.setLocation(e.getFaultInfo().getLocation());
					throw new UnknownLocationFault_Exception("Unknown City: ", faultInfo);		

				}
		
	}

	@Override
	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception {

		if(transportersViews.containsKey(id)){

			String nomeTransporterCompany = (transportersViews.get(id)).getTransporterCompany();
			
			if(nomeTransporterCompany != "NoTransporterCompany" && nomeTransporterCompany != "NoTransporterCompany-Failed" ){
				
				// Actualiza o estado se puder
				actualizaEstado(id);
				return transportersViews.get(id);	
	
			}else{
				return transportersViews.get(id);
			}
			
		}else{
			// Retorna erro de id não presente
			UnknownTransportFault faultInfo = new UnknownTransportFault();
			faultInfo.setId(id);
			throw new UnknownTransportFault_Exception("Unknown TransportID: ", faultInfo);		
		}	
	}

	
	@Override
	public List<TransportView> listTransports() {

		List<TransportView> listaTransportViews = new ArrayList<>();
		
		for (TransportView value : transportersViews.values()){	
			
			// saca o ID contador ( 1 , 2 , 3 ... )
			String IDvalue = value.getId();
			
			
			//System.out.println(value.getTransporterCompany());
			

			if(value.getTransporterCompany() != "NoTransporterCompany" && value.getTransporterCompany() != "NoTransporterCompany-Failed" ){

				// se for diferente dos estados que não precisam de ser actualizados --> ele vai buscar os estados mais recentes ao transporter
				if((value.getState().compareTo(TransportStateView.REQUESTED) != 0 ) ||  (value.getState().compareTo(TransportStateView.BUDGETED) != 0 ) || (value.getState().compareTo(TransportStateView.FAILED) != 0 ) ){

					actualizaEstado(IDvalue);
				}	
			}
			
			// adiciona na lista o transportView
			listaTransportViews.add(value);	
		}
		return listaTransportViews;
	}

	
	@Override
	public void clearTransports() {

		transportersViews.clear();	
		associateIdentifiers.clear();	
		counterId=0;
		
		// for para fazer clear a cada uma
		for (TransporterPortType value : ports.values()){
			value.clearJobs();	
			}
	}
	
	
	//funcao para actualizar o estado no broker
	public void actualizaEstado (String IDBroker){
		
		
		
		String IDtransporter= associateIdentifiers.get(IDBroker); //saca algo tipo: UPATRANSPORTER1:32 
		
		String[] parts = IDtransporter.split(":");
		String part1 = parts[0]; // ex:UPATRANSPORTER1
		
		// vai buscar o port do respectivo upa tranporter
		TransporterPortType portConnect = ports.get(part1);
		
		//Recebe um jobView
		JobView recebido = portConnect.jobStatus(IDtransporter);
		
		
		//verifica os states e altera na lista do broker
		if (recebido.getJobState().compareTo(JobStateView.HEADING) == 0){
			transportersViews.get(IDBroker).setState(TransportStateView.HEADING);
		}
		
		if (recebido.getJobState().compareTo(JobStateView.ONGOING) == 0){
			transportersViews.get(IDBroker).setState(TransportStateView.ONGOING);
		}
		
		if (recebido.getJobState().compareTo(JobStateView.COMPLETED) == 0){
			transportersViews.get(IDBroker).setState(TransportStateView.COMPLETED);
		}		
	}
}
