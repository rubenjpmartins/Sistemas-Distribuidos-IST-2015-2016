package pt.upa.ws;

import javax.jws.WebService;
import java.security.cert.Certificate;


@WebService
public interface CertificateFileInterface {

	//Certificate readCertificateFile(String certificateFilePath) throws Exception;

	//String sayHello(String name);
	
	
	public byte[] download(String fileName);
}	




