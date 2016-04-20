package pt.upa.transporter.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import java.util.Timer;
import java.util.TimerTask;

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
	
	// Estrutura que contem as Regioes todas sobre as quais as Transportadoras Operam
	private static HashMap<String,String> transportersByRegion;	
	
	// Estrutura que contem os Jobs Realizados
	private List<JobView> jobViewsList;
	
	private int transporterRegionSelecion;
	private String transporterName;
	private int contadorID;
	Random value = new Random();

	//temporizador
	private Timer timer1;
	private Timer timer2;
	private Timer timer3;
	

	
	// Contrutor
	public TransporterPort(String name){
		
		//Inicializa o contador a 1
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
	
	
	// Changes the state of the job 
	class ChangeJobState extends TimerTask {
		
		JobView jobjob;
		Timer timertimer;
		
		public ChangeJobState(JobView job, Timer timer){
			
			this.jobjob = job;
			this.timertimer = timer;
			
		}
		
        public void run() {

        	if ((jobjob.getJobState()).compareTo(JobStateView.ACCEPTED) == 0){
        		jobjob.setJobState(JobStateView.HEADING);
        		System.out.println("State changed to HEADING");
        		timertimer.cancel(); //Terminate the timer thread
        		return; 
        	}
        		
        	if ((jobjob.getJobState()).compareTo(JobStateView.HEADING) == 0){
        		jobjob.setJobState(JobStateView.ONGOING);
        		System.out.println("State changed to ONGOING");
        		timertimer.cancel(); //Terminate the timer thread
        		return;
        	}
        			
        	if ((jobjob.getJobState()).compareTo(JobStateView.ONGOING) == 0){
        		jobjob.setJobState(JobStateView.COMPLETED);
        		System.out.println("State changed to COMPLETED");
        		timertimer.cancel(); //Terminate the timer thread
        		return;
        	}
        }
    }

	
	@Override
	public String ping(String name) {
		String retorno = name + " ok";
		return retorno;
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
				System.out.println("Didn't send any Deal Proposal");
				return null;
			}
			
			if(price<=10){
				if(price <=1){
					precoProposto = 0;
				}
				else{
					precoProposto = geradorPrecoRandom(price, 0);  ////////////// random para preco < 10
				}
				
				
			}else
				{ // (price>10 || price <= 100)
				
				if(price%2==1){	
					if(transporterRegionSelecion%2==1 ){ // preco impar e transportadora impar
						precoProposto = geradorPrecoRandom(price, 0);
					}
					else{
						precoProposto = geradorPrecoRandom(120, price); // limite maximo de uma proposta é 120
					}	
				}
				if(price%2==0) {
					if(transporterRegionSelecion%2==0 ){ // preco par e transportadora par
						precoProposto = geradorPrecoRandom(price, 0);
						}
					else{
						precoProposto = geradorPrecoRandom(120, price); // limite maximo de uma proposta é 120			
						}	
				}
			}
			

			// Começa a construção jobView
			JobView concreteJobView = new JobView();
			concreteJobView.setCompanyName(transporterName);
			
			
			
			//inicializa o estado e dá-lhe como PROPOSED
			concreteJobView.setJobState(JobStateView.PROPOSED); 
	
			// Gera ID 
			String concatena= Integer.toString(contadorID);
			jobIdBuilder = jobIdBuilder.concat(concatena);
			
			System.out.println("\nReceived a transport request - ID: " + jobIdBuilder);
			
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
						
						System.out.println("Proposed Price: " + (precoProposto));
						
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
						System.out.println("Proposed Price: " + (precoProposto));
						
						
						//addiciona na lista de jobviews
						jobViewsList.add(concreteJobView);
						
						//retorna o jobview
						return concreteJobView;
	
					}	
				}	
			}
		
		// caso seja do norte para o sul vai retornar null ou BadLocationFault_Exception??
	    System.out.println("Didn't send any Deal Proposal");
		return null;
	}

	
	@Override
	public JobView decideJob(String id, boolean accept) throws BadJobFault_Exception {

		JobView jobChoosen = jobStatus(id); //searchJobs - procura pelo job

		if(jobChoosen == null){
			BadJobFault job = new BadJobFault();			
			job.setId(id); 
			throw new BadJobFault_Exception("The job is not present", job);
		}
		


		//	Adicionado depois da entrega Part 1 : A função não pode 
		if( jobChoosen.getJobState().equals(JobStateView.ACCEPTED) || jobChoosen.getJobState().equals(JobStateView.REJECTED)) {

			BadJobFault job = new BadJobFault();			
			job.setId(id); 
			throw new BadJobFault_Exception("The job was already ACCEPTED/REJECTED", job);

		}



		else{
			if(accept){
				jobChoosen.setJobState(JobStateView.ACCEPTED);
				System.out.println("Got the Deal - Working");
				timer(jobChoosen);
				return jobChoosen;
			}
			else{
				jobChoosen.setJobState(JobStateView.REJECTED);
				System.out.println("Deal Rejected");
				return jobChoosen;
			}
		}	
	}
	
	
	
	// retorna um Job para que o broker depois veja o seu estado
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
		contadorID=1;
	}
	

	

	// METODO RAMDOM
	private int geradorPrecoRandom(int max, int min){
		Random gerador = new Random();
	    int numeroresultado; 
	  
	    numeroresultado= gerador.nextInt(max-min)+min;
	    return numeroresultado;
	}
	
	
	public void timer(JobView job) {
		
		int duration1 = geradorPrecoRandom(5000,1000);
		int duration2 = duration1 + geradorPrecoRandom(5000,1000);
		int duration3 = duration2 + geradorPrecoRandom(5000,1000);
        
		timer1 = new Timer();
        timer2 = new Timer();
        timer3 = new Timer();
        
        timer1.schedule(new ChangeJobState(job, timer1), 1*duration1);
        timer2.schedule(new ChangeJobState(job, timer2), 1*duration2);
        timer3.schedule(new ChangeJobState(job, timer3), 1*duration3);
    }
	
}

