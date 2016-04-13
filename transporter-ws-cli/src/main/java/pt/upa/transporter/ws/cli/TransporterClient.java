package pt.upa.transporter.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.List;
import java.util.Map;

import javax.jws.WebService;
import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.transporter.ws.BadJobFault_Exception;
import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobView;
import pt.upa.transporter.ws.TransporterPortType;
import pt.upa.transporter.ws.TransporterService;




@WebService(
		endpointInterface="pt.upa.transporter.ws.TransporterPortType",
		wsdlLocation="transporter.1_0.wsdl",
		name="UpaTransporter${ws.i}",
		portName="TransporterPort",
		targetNamespace="http://ws.transporter.upa.pt/",
		serviceName="TransporterService"
)

public class TransporterClient implements TransporterPortType {
	
	public TransporterClient(String[] args) throws JAXRException{
		String id = args[0];
		String uddiURL = args[1];
		String name = args[2];
		

		System.out.printf("Contacting UDDI at %s%n", uddiURL);
		UDDINaming uddiNaming = new UDDINaming(uddiURL);
		
		String targetServer = name + id;
		System.out.printf("Looking for '%s'%n", targetServer);
		String endpointAddress = uddiNaming.lookup(targetServer);

		if (endpointAddress == null) {
			System.out.println("Not found!");
			return;
		} else {
			System.out.printf("Found %s%n", endpointAddress);
		}

		System.out.println("Creating stub ...");
		TransporterService service = new TransporterService();
		TransporterPortType port = service.getTransporterPort();

		System.out.println("Setting endpoint address ...");
		BindingProvider bindingProvider = (BindingProvider) port;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);

	}

	@Override
	public String ping(String name) {
		if (name == "UpaBroker"){
			System.out.println("Correu bem");
		}
		else{
			System.out.println("Erro");
		}
		return "bem sucedido";
	}

	@Override
	public JobView requestJob(String origin, String destination, int price)
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JobView decideJob(String id, boolean accept) throws BadJobFault_Exception {

		/*JobView Job = searchJobs(id);
		if(Job == null){
			BadJobFault badJob = new BadJobFault();
			badJob.setId(id);
			throw new BadJobFault_Exception("A job with that id is unavailable", badJob);
		}
		else{
			if(accept){
				Job.setJobState(JobStateView.ACCEPTED);
				timer(Job);
				return Job;
			}
			else{
				Job.setJobState(JobStateView.REJECTED);
				timer(Job);
				return Job;

		*/

		return null;
	}

	@Override
	public JobView jobStatus(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<JobView> listJobs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearJobs() {
		// TODO Auto-generated method stub
		
	}


}
