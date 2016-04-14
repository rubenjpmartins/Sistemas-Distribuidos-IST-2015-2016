// versao final

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

	private Map<String,TransporterPortType> ports;
	//private List<TransportView> transportersStates;
	
	private Map<String,TransportView> transportersViews;
	private Map<String,String> associateIdentifiers;
	private int counterId;
	
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
							
							// adiciona à lista jobstates cada 
							jobStates.add(respostaTrasportadora);
							
							//Actualiza o estado para BUDGETED
							transportersViews.get(Integer.toString(counterId)).setState(TransportStateView.BUDGETED);
							
							
							// Escolhe o valor minimo do preco para adjudicar o servico
							if(respostaTrasportadora.getJobPrice()< minValueProposed ){
								
								minValueProposed = respostaTrasportadora.getJobPrice();
								nomeTransportadoraMin = respostaTrasportadora.getCompanyName();	
								idTransportadoraMin = respostaTrasportadora.getJobIdentifier();
								
								
								
								
								System.out.println("id traportadora");
								System.out.println(idTransportadoraMin);


								// SACA O ID DO VENCEDOR	
								}
							
							}

						


						else{ // caso seja retornado null from transporter server
							
							System.out.println("não consegui funcionar - retornou Null");
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
						
						// VER -- falta destino! - so envia origem no faultinfo
						UnavailableTransportFault faultInfo = new UnavailableTransportFault();
						faultInfo.setOrigin(origin); // envia o preço minimo proposto
						throw new UnavailableTransportFault_Exception("No transporter found for this origin:", faultInfo);
					}
					
					
			
					if(minValueProposed>price){ // verifica se o valor minimo é cumprido
						
						System.out.println(minValueProposed);
						System.out.println(price);
						
						//Actualiza o estado para FAILED
						transportersViews.get(Integer.toString(counterId)).setState(TransportStateView.FAILED);
						
						
						for (JobView jobRejected : jobStates){
							
							String companyName = jobRejected.getCompanyName();
							String jobId = jobRejected.getJobIdentifier();
							
							TransporterPortType portRejected = ports.get(companyName);
							
							try{
								portRejected.decideJob(jobId, false);
							} catch(BadJobFault_Exception e){
								
								
								//inserir catch
							}
							
						}
						UnavailableTransportPriceFault faultInfo = new UnavailableTransportPriceFault();
						faultInfo.setBestPriceFound(minValueProposed); // envia o preço minimo proposto
						throw new UnavailableTransportPriceFault_Exception("Max price proposed is:", faultInfo);
						
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
								
								// DAVA ERRO AQUI
								String contador2 = Integer.toString(counterId);
								
								System.out.println("aqui esta");
								System.out.println(contador2);
								
								System.out.println(idTransportadoraMin);
								
								
								associateIdentifiers.put(contador2, idTransportadoraMin);
							    
								
								try{
									portAccepted.decideJob(jobId, true); // envia accepted para o trasportr winner
								} catch(BadJobFault_Exception e){
									
									//////////////////////////
									
								}
							}else{

								try{
									portAccepted.decideJob(jobId, false);// envia accepted para o trasportr lose
								} catch(BadJobFault_Exception e){
									
									//////////////////////////////
								}	
							}
						}// fim for para 1 e fail para o resto
					}		  //acaba o ciclo for
				
					
					return "TransportRequest Sucessful";
					
				} catch (BadPriceFault_Exception e) {
				    System.err.println("BadPriceFault Exception: " + e.getMessage());
				    // Retorna erro de preço inferior a 0
				    transportersViews.get(Integer.toString(counterId)).setState(TransportStateView.FAILED);
				 // mete o nomedotransporter no transporterview
					clientRequest.setTransporterCompany("NoTransporterCompanyFailed");
					
					InvalidPriceFault faultInfo = new InvalidPriceFault();
					faultInfo.setPrice(e.getFaultInfo().getPrice());
					throw new InvalidPriceFault_Exception("Invalid Price - Price lower that 0", faultInfo);				 				    
				    
				} catch (BadLocationFault_Exception e) {
				    System.err.println("BadLocationFault Exception: " + e.getMessage());
				    transportersViews.get(Integer.toString(counterId)).setState(TransportStateView.FAILED);
				 // mete o nomedotransporter no transporterview
					clientRequest.setTransporterCompany("NoTransporterCompanyFailed");

				    // Retorna erro de cidade inválida 
				    UnknownLocationFault faultInfo = new UnknownLocationFault();
					faultInfo.setLocation(e.getFaultInfo().getLocation());
					throw new UnknownLocationFault_Exception("Unknown Destination:", faultInfo);		

				}
		
	}

	@Override
	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception {
		
		if(transportersViews.containsKey(id)){
			// Actualiza o estado 
			actualizaEstado(id);
			return transportersViews.get(id);	
		}else{
			// Retorna erro de id não presente
			UnknownTransportFault faultInfo = new UnknownTransportFault();
			faultInfo.setId(id);
			throw new UnknownTransportFault_Exception("Unknown TransportID:", faultInfo);		
		}	
	}

	
	@Override
	public List<TransportView> listTransports() {
		
		List<TransportView> listaTransportViews = new ArrayList<>();
		
		for (TransportView value : transportersViews.values()){	
			
			// saca o ID contador ( 1 , 2 , 3 ... )
			String IDvalue = value.getId();
			
			// se for diferente dos estados que não precisam de ser actualizados --> ele vai buscar os estados mais recentes ao transporter
			if((value.getState().compareTo(TransportStateView.REQUESTED) != 0 ) ||  (value.getState().compareTo(TransportStateView.BUDGETED) != 0 ) || (value.getState().compareTo(TransportStateView.FAILED) != 0 ) ){

				actualizaEstado(IDvalue);
				
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
		System.out.println("IDtransporter sacado da associateIdentifiers ");
		System.out.println(IDtransporter);
		
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
		
	};

	// TO DO

}
