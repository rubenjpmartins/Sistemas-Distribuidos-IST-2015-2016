package pt.upa.ws;

import javax.jws.WebService;

@WebService(endpointInterface = "pt.upa.ws.CertificateFileInterface")
public class CertificateImpl implements CertificateFileInterface {

	public String sayHello(String name) {
		return "Hello " + name + "!";
	}


	/*

	// deve receber uma string apenas com o nome e deve retornar um array de bytes com o certificado

	/**
	 * Reads a certificate from a file
	 * 
	 * @return
	 * @throws Exception
	 */


	/*
	public static Certificate readCertificateFile(String certificateFilePath) throws Exception {
		

		//final static String CERTIFICATE_FILE = "example.cer";
		// recebe o nome do certificado

		FileInputStream fis;

		try {
			fis = new FileInputStream(certificateFilePath);
		} catch (FileNotFoundException e) {
			System.err.println("Certificate file <" + certificateFilePath + "> not fount.");
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
		return null;
	}

	*/
}
