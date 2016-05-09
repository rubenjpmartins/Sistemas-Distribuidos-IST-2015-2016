package pt.upa.broker.ws.handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
@HandlerChain(file = "/handler-chain.xml")
public class BrokerLogHandler implements SOAPHandler<SOAPMessageContext> {

	// Relativo ao getPrivateKeyFromKeystore
	private final String keyStoreFilePath = "UpaBrokerkeystore/UpaBroker.jks";
	private final String keyStorePassword = "ins3cur3";
	private final String keyAlias = "upabroker";
	private final String keyPassword = "1nsecure"; // private final String
													// keyPassword = "example";

	public Set<QName> getHeaders() {
		return null;
	}

	// alterado - adicionado try catch
	public boolean handleMessage(SOAPMessageContext smc) {
		try {
			logToSystemOut(smc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	// alterado - adicionado try catch
	public boolean handleFault(SOAPMessageContext smc) {
		try {
			logToSystemOut(smc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	/// adicionado para trocar a soapmessage to a string
	public String soapMessageToString(SOAPMessage message) {
		String result = null;

		if (message != null) {
			ByteArrayOutputStream baos = null;
			try {
				baos = new ByteArrayOutputStream();
				message.writeTo(baos);
				result = baos.toString();
			} catch (Exception e) {
			} finally {
				if (baos != null) {
					try {
						baos.close();
					} catch (IOException ioe) {
					}
				}
			}
		}
		return result;
	}

	// nothing to clean up
	public void close(MessageContext messageContext) {
	}

	/**
	 * Check the MESSAGE_OUTBOUND_PROPERTY in the context to see if this is an
	 * outgoing or incoming message. Write a brief message to the print stream
	 * and output the message. The writeTo() method can throw SOAPException or
	 * IOException
	 * 
	 * @throws Exception
	 */
	private void logToSystemOut(SOAPMessageContext smc) throws Exception {
		Boolean outbound = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		if (outbound) {
			System.out.println("Outbound SOAP message:");
		} else {
			System.out.println("Inbound SOAP message:");
		}

		System.out.println("Platform Encoding : " + System.getProperty("file.encoding"));
		SOAPMessage message = smc.getMessage();
		// conversão para string
		String soapMessage = soapMessageToString(message);
		System.out.println("\n\n\n\n\n\n\n\n");
		System.out.print(soapMessage);
		System.out.println("\n\n\n\n\n\n\n\n");

		DigitalSignatureX509 assinaturaX509 = null;

		PrivateKey chavePrivadaBroker = assinaturaX509.getPrivateKeyFromKeystore(keyStoreFilePath,
				keyStorePassword.toCharArray(), keyAlias, keyPassword.toCharArray());

		byte[] larilas = assinaturaX509.makeDigitalSignature(soapMessage.getBytes(), chavePrivadaBroker);
		// Impressão
		// System.out.println(javax.xml.bind.DatatypeConverter.printHexBinary(larilas));
		
	
		
		try {
			
		message.writeTo(System.out);
			
			
			System.out.println(); // just to add a newline to output
		} catch (Exception e) {
			System.out.printf("Exception in handler: %s%n", e);
		}
	}
	
}
