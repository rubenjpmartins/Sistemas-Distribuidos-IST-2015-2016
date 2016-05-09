package pt.upa.broker.ws.handler;

import java.io.ByteArrayOutputStream;
import java.security.PrivateKey;
import java.util.Set;

import javax.jws.HandlerChain;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import pt.upa.cripto.DigitalSignatureX509;

/**
 * This SOAPHandler outputs the contents of inbound and outbound messages.
 */
@HandlerChain(file="/handler-chain.xml")
public class BrokerLogHandler implements SOAPHandler<SOAPMessageContext> {

	// Relativo ao getPrivateKeyFromKeystore
		private final String keyStoreFilePath = "UpaBrokerkeystore/UpaBroker.jks";
		private final String keyStorePassword = "ins3cur3";	 
		private final String keyAlias = "upabroker"; 
	    private final String keyPassword = "1ns3cur3"; //    private final String keyPassword = "example";
		
	
	public Set<QName> getHeaders() {
		return null;
	}

	public boolean handleMessage(SOAPMessageContext smc) {
		logToSystemOut(smc);
		return true;
	}

	public boolean handleFault(SOAPMessageContext smc) {
		logToSystemOut(smc);
		return true;
	}

	// nothing to clean up
	public void close(MessageContext messageContext) {
	}

	/**
	 * Check the MESSAGE_OUTBOUND_PROPERTY in the context to see if this is an
	 * outgoing or incoming message. Write a brief message to the print stream
	 * and output the message. The writeTo() method can throw SOAPException or
	 * IOException
	 */
	private void logToSystemOut(SOAPMessageContext smc) {
		Boolean outbound = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		if (outbound) {
			System.out.println("Outbound SOAP message:");
		} else {
			System.out.println("Inbound SOAP message:");
		}


		
		//original
		SOAPMessage message = smc.getMessage();
		
		/*
		// converte messageto bitearray
		SOAPMessage message = smc.getMessage();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		message.writeTo(out);
		
		// testa a ver se foi feito o bitearryok
		String strMsg = new String(out.toByteArray());
		
		
		
		
		System.out.println("Platform Encoding : " + System.getProperty("file.encoding"));
		
		

		
		
		
		DigitalSignatureX509 assinaturaX509 = null;

		PrivateKey chavePrivadaBroker = assinaturaX509.getPrivateKeyFromKeystore(keyStoreFilePath, keyStorePassword.toCharArray(), keyAlias, keyPassword.toCharArray());

		assinaturaX509.makeDigitalSignature(out, chavePrivadaBroker);

		
		*/
		
		
		
		try {
			message.writeTo(System.out);
			System.out.println(); // just to add a newline to output
		} catch (Exception e) {
			System.out.printf("Exception in handler: %s%n", e);
		}
	}

}
