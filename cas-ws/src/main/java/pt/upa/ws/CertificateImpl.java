package pt.upa.ws;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.jws.WebService;


@WebService(endpointInterface = "pt.upa.ws.CertificateFileInterface")
public class CertificateImpl implements CertificateFileInterface {

	
	
	// para testes
	/*
	public String sayHello(String name) {
		return "Hello " + name + "!";
	}*/


	
	// deve receber uma string apenas com o nome e deve retornar um array de bytes com o certificado
	/**
	 * Reads a certificate from a file
	 * 
	 * @return
	 * @throws Exception
	 */
	public Certificate readCertificateFile(String certificateFilePath) throws Exception {
		FileInputStream fis;

		try {
			fis = new FileInputStream(certificateFilePath);
		} catch (FileNotFoundException e) {
			System.err.println("Certificate file <" + certificateFilePath + "> not found.");
			return null;
		}
		BufferedInputStream bis = new BufferedInputStream(fis);

		CertificateFactory cf = CertificateFactory.getInstance("X.509");

		if (bis.available() > 0) {
			Certificate cert = cf.generateCertificate(bis);
			return cert;
			// It is possible to print the content of the certificate file:
			// System.out.println(cert.toString());
		}
		bis.close();
		fis.close();
		System.out.println("Retornei Null");
		return null;
		
	}	
}
