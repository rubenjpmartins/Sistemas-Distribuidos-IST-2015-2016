package pt.upa.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Map;
import javax.xml.ws.BindingProvider;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import pt.upa.cripto.DigitalSignatureX509;
import pt.upa.cripto.X509CertificateCheck;


// classes generated from WSDL
import pt.upa.ws.CertificateFileInterface;
import pt.upa.ws.CertificateImplService;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;

/**
 * @author rubenjpmartins
 *
 */
public class CertificateClient {

	private String uddiURL;
	private String name;
	private String url;

	// path de cada um dos certificados
	private String pathtrasporter1 = "cakeystore/UpaTransporter1.cer";
	private String pathtrasporter2 = "cakeystore/UpaTransporter2.cer";
	private String pathbrokerserver = "cakeystore/UpaBroker.cer";

	public CertificateClient(String[] args) { //////////////////////////////// a
												//////////////////////////////// eliminiar?????

		// Check arguments
		if (args.length < 2) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s uddiURL name%n", CertificateClient.class.getName());
			return;
		}

		uddiURL = args[0];
		name = args[1];
		url = args[2];
	}

	// public static void main(String[] args) throws Exception
	public CertificateClient(String URL, String nome) {
		uddiURL = URL;
		name = nome;
	}

	
	public Certificate serverConnect(String entidade) {

		//System.out.println("--------Certificate Cliente recebe----------");
		//System.out.println(entidade);
		//System.out.println("-------------------");

		String filepath = "";

		// verifica se recebe a entidade certa e atribui o filepath certo
		if (entidade.equals( "UpaTransporter1")) {
			filepath = pathtrasporter1;

		} else if (entidade.equals( "UpaTransporter2")) {
			filepath = pathtrasporter2;

		} else if (entidade.equals("UpaBroker")) {
			filepath = pathbrokerserver;

		} else {
			// throws exception; ///////////////////// ??????
			//
			System.out.println("\n\n recebo????");
			System.out.println(entidade+"\n\n");
			return null;
		}

		try {

			//System.out.printf("Contacting UDDI at %s%n", uddiURL);
			UDDINaming uddiNaming = new UDDINaming(uddiURL);

			//System.out.printf("Looking for '%s'%n", name);
			String endpointAddress = uddiNaming.lookup(name);

			if (endpointAddress == null) {
				//System.out.println("Not found!");
				//// return "null"; //////////// throws exception aqui
			} else {
				//System.out.printf("Found %s%n", endpointAddress);
			}

			//System.out.println("Creating stub ...");
			CertificateImplService service = new CertificateImplService();
			CertificateFileInterface port = service.getCertificateImplPort();

			//System.out.println("Setting endpoint address ...");
			BindingProvider bindingProvider = (BindingProvider) port;
			Map<String, Object> requestContext = bindingProvider.getRequestContext();
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);


			System.out.println("Getting Public Key...");
						
			
			//saca o certificado/com chave pública do emissor
			
			//converte para inputStream
			InputStream is = new ByteArrayInputStream(port.download(filepath));
			// gera o certificado
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			Certificate cert = cf.generateCertificate(is);
			// confirmação
			//System.out.println(cert.toString());
			
			
			
			
			/*
			// Verifica se um certificado foi devidamente assinado pela CA
			DigitalSignatureX509 assinaturaX509 = null;
			X509CertificateCheck checkCertificateX509 = null;
			
			

			
			
			Certificate chavePublicaCaServer = readCertificateFile();
			
			
			
			PublicKey publicUpaKey = assinaturaX509.getPublicKeyFromCertificate( certUpaBroker );
			
			// Verifica se um certificado foi devidamente assinado pela CA
			checkCertificateX509.verifySignedCertificate(cert, publicUpaKey );
			
			
			
			
			
			
			DigitalSignatureX509 assinaturaX509 = null;
            //Public Key Broker
            // FALTA GUARDAR PARA NÂO ESTAR SEMPRE A IR BUSCAR A CERTIFICATE
			PublicKey publicUpaKey = assinaturaX509.getPublicKeyFromCertificate( certUpaBroker );
			
			
			*/
			
			
			
			
			return cert;

			
		} catch (Exception e) {
			System.out.printf("Caught exception: %s%n", e);
			e.printStackTrace();
			

		}
		return null;
	}
}
