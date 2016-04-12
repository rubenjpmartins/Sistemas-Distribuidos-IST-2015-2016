package pt.upa.transporter.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.jws.WebService;


@WebService(
		endpointInterface="pt.upa.transporter.ws.TransporterPortType",
		wsdlLocation="transporter.1_0.wsdl",
		name="UpaTransporter${ws.i}",
		portName="TransporterPort",
		targetNamespace="http://ws.transporter.upa.pt/",
		serviceName="TransporterService"
)
public class TransporterPort implements TransporterPortType {
	private static HashMap<String,String> transportersByRegion;	
	private List<JobView> jobViewsList;
	private int transporterRegionSelecion;
	private String transporterName;
	private int contadorID;
	
	private JobStateView estadoJob;
	
		
	public TransporterPort(String name){
		
		//relativo ao ID
		
		contadorID= 1;

		// create list to store the jobViews
		jobViewsList = new ArrayList<>();
		transporterName = name;
		
		// checks Transporter region 
		String transporterIndex = transporterName.substring(transporterName.length() - 1);
		transporterRegionSelecion = Integer.parseInt(transporterIndex);
		
		//Create map to store the Transporters by region
		transportersByRegion = new HashMap<>();
		
		transportersByRegion.put("Viana do Castelo","Zona Norte");
		transportersByRegion.put("Braga","Zona Norte");
		transportersByRegion.put("Porto","Zona Norte");
		transportersByRegion.put("Vila Real","Zona Norte");
		transportersByRegion.put("Bragança","Zona Norte");
		transportersByRegion.put("Aveiro","Zona Centro");
		transportersByRegion.put("Viseu","Zona Centro");
		transportersByRegion.put("Guarda","Zona Centro");
		transportersByRegion.put("Coimbra","Zona Centro");
		transportersByRegion.put("Leiria","Zona Centro");
		transportersByRegion.put("Lisboa","Zona Centro");
		transportersByRegion.put("Santarem","Zona Centro");
		transportersByRegion.put("Castelo Branco","Zona Centro");
		transportersByRegion.put("Portalegre","Zona Sul");
		transportersByRegion.put("Setubal","Zona Sul");
		transportersByRegion.put("Evora","Zona Sul");
		transportersByRegion.put("Faro","Zona Sul");
		transportersByRegion.put("Beja","Zona Sul");
	}
	
	@Override
	public String ping(String name) {
		return name;
	}

	@Override
	public JobView requestJob(String origin, String destination, int price)
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		
			
			
			
		
			
			int precoProposto = 0;
			String jobIdBuilder = transporterName.concat(":") ;
			 
			// Tratamento de erros 
			if(price<0){
				BadPriceFault faultInfo = new BadPriceFault();
				faultInfo.setPrice(price);
				throw new BadPriceFault_Exception("Negative Price", faultInfo);
			}
			
			if(!transportersByRegion.containsKey(origin)){
				BadLocationFault faultInfo = new BadLocationFault();
				faultInfo.setLocation(origin);
				throw new BadLocationFault_Exception("Unknown Origin", faultInfo);	
			}
		
			if(!transportersByRegion.containsKey(destination)){
				BadLocationFault faultInfo = new BadLocationFault();
				faultInfo.setLocation(destination);
				throw new BadLocationFault_Exception("Unknown Destination", faultInfo);		
			}
			
			
			// Analisa o Preco e faz a proposta
			
			if(price>100){
				return null;
			}else if(price<=10){
				if(price <=1){
					precoProposto = 0;
				}
				else{
					precoProposto = geradorPrecoRandom(price, 0);  ////////////// random para preço < 10 ??????''	
				}
      
			}
			else{ // (price>10 || price <= 100)
				
				if(price%2==1){	
					if(transporterRegionSelecion%2==1 ){ // preco impar e transportadora impar
					precoProposto = geradorPrecoRandom(price, 0);
					}
					else{
						precoProposto = geradorPrecoRandom(100, price+1); // limite maximo de uma proposta é 100???=???????????
					}	
				}
				
				if(price%2==0) {
					if(transporterRegionSelecion%2==0 ){ // preco par e transportadora par
						precoProposto = geradorPrecoRandom(price, 0);
						}
					
					else{
						precoProposto = geradorPrecoRandom(100, price+1); // limite maximo de uma proposta é 100???=???????????
					}	
				}
			}
			

			// Começa a construção jobView
			JobView concreteJobView = new JobView();
			concreteJobView.setCompanyName(transporterName);
			
			//inicializa o estado e dá-lhe como requestd
			estadoJob.valueOf("REQUESTED");
			concreteJobView.setJobState(estadoJob); 
			
			
			// Gera ID 
		
			String concatena= Integer.toString(contadorID);
			jobIdBuilder.concat(concatena);
			contadorID++;
			concreteJobView.setJobIdentifier(jobIdBuilder);
						
			
			
			
			
			// Escolha de zona ( PAR ou IMPAR)
			// Empresas Norte
			if(transporterRegionSelecion%2==0 ){ // se o transporter for PAR -> Norte
				
				if(transportersByRegion.get(origin) == "Zona Norte" || transportersByRegion.get(origin) == "Zona Centro" ){
					concreteJobView.setJobOrigin(origin);
					if(transportersByRegion.get(destination) == "Zona Norte" || transportersByRegion.get(destination) == "Zona Centro" ){
						concreteJobView.setJobDestination(destination);
						
						// Preenche concrete job
						concreteJobView.setJobPrice(precoProposto);
						
						//addiciona na lista de jobviews
						jobViewsList.add(concreteJobView);
						
						//retorna o jobview
						return concreteJobView;
	 
					}	
				}	
			}
			
			// Empresas Sul
			if(transporterRegionSelecion%2==1 ){ // se o transporter for Impar -> Sul
				
				if(transportersByRegion.get(origin) == "Zona Sul" || transportersByRegion.get(origin) == "Zona Centro" ){
					concreteJobView.setJobOrigin(origin);
					if(transportersByRegion.get(destination) == "Zona Sul" || transportersByRegion.get(destination) == "Zona Centro" ){
						concreteJobView.setJobDestination(destination);
						
						// Preenche concrete job
						concreteJobView.setJobPrice(precoProposto);
						
						//addiciona na lista de jobviews
						jobViewsList.add(concreteJobView);
						
						//retorna o jobview
						return concreteJobView;
	
					}	
				}	
			}
		
		// caso seja do norte para o sul vai retornar null
		
		return null;
	}

	@Override
	public JobView decideJob(String id, boolean accept) throws BadJobFault_Exception {
		// TODO Auto-generated method stub


		
		
		return null;
	}

	@Override
	public JobView jobStatus(String id) {
		
		for(JobView j : jobViewsList ){
			if ((j.getJobIdentifier()).equals(id)){
				return j;
			}		
		}
		return null;
	}

	@Override
	public List<JobView> listJobs() {
		return jobViewsList;
	}

	@Override
	public void clearJobs() {
		jobViewsList.clear();
	}
	
	/////////////////////////////////////////METODO RAMDOM??
	
	private int geradorPrecoRandom(int max, int min){
		Random gerador = new Random();
	    int numeroresultado; 
	    numeroresultado= gerador.nextInt(max-min)+min;
	    return numeroresultado;
	}
	
	
	
	// TODO

}
