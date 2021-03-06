package example.ws.handler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.MessageContext.Scope;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.LinkedList;
import pt.upa.cripto.DigitalSignatureX509;
import pt.upa.ws.cli.CertificateClient;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;
import static javax.xml.bind.DatatypeConverter.printHexBinary;
import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import java.util.UUID;
import java.security.PrivateKey;

/**
 * This SOAPHandler shows how to set/get values from headers in inbound/outbound
 * SOAP messages.
 *
 * A header is created in an outbound message and is read on an inbound message.
 *
 * The value that is read from the header is placed in a SOAP message context
 * property that can be accessed by other handlers or by the application.
 */
public class UpaHeaderHandler implements SOAPHandler<SOAPMessageContext> {

	private static HashMap<String, Certificate> certificateCache = new HashMap<String, Certificate>();

	public static final String CONTEXT_PROPERTY = "my.property";

	//
	// Handler interface methods
	//
	public Set<QName> getHeaders() {
		return null;
	}

	// Lista que contem os UUID
	LinkedList<String> queueUUID = new LinkedList<String>();

	public static final String CLASS_NAME = UpaHeaderHandler.class.getSimpleName();

	// Tranforma a Message para uma String
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

	public boolean handleMessage(SOAPMessageContext smc) {

		System.out.println("\n\n----------------------------------");
		System.out.println("AddHeaderHandler: Handling message.");

		Boolean outboundElement = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		String propertyValue = (String) smc.get(CONTEXT_PROPERTY);
		System.out.printf("%s received '%s'%n", CLASS_NAME, propertyValue);
		if (propertyValue == "UpaBroker1") {
			propertyValue = "UpaBroker";
		}
		if (propertyValue == "UpaBroker2") {
			propertyValue = "UpaBroker";
		}

		String keyStoreFilePath = null;
		String keyStorePassword = null;
		String keyAlias = null;
		String keyPassword = null;

		try {
			//
			//
			//
			// MENSAGEM A ENVIAR
			// RESUMO CIFRADO COM CHAVE PRIVADA
			//
			//
			//
			if (outboundElement.booleanValue()) {

				System.out.println("Writing header in outbound SOAP message... - MENSAGEM A ENVIAR");
				System.out.println("----------------------------------");

				// verifica se recebe a entidade certa e atribui o filepath
				// certo
				if (propertyValue.equals("UpaTransporter1")) {
					// Relativo ao getPrivateKeyFromKeystore
					keyStoreFilePath = "UpaTransporter1keystore/UpaTransporter1.jks";
					keyStorePassword = "ins3cur3";
					keyAlias = "UpaTransporter1";
					keyPassword = "1nsecure";

				} else if (propertyValue.equals("UpaTransporter2")) {
					// Relativo ao getPrivateKeyFromKeystore
					keyStoreFilePath = "UpaTransporter2keystore/UpaTransporter2.jks";
					keyStorePassword = "ins3cur3";
					keyAlias = "UpaTransporter2";
					keyPassword = "1nsecure";

				} else if (propertyValue.equals("UpaBroker")) {
					// Relativo ao getPrivateKeyFromKeystore
					keyStoreFilePath = "UpaBrokerkeystore/UpaBroker.jks";
					keyStorePassword = "ins3cur3";
					keyAlias = "upabroker";
					keyPassword = "1nsecure";

				} else {
					// throws exception
					//
					System.out.print("Error - No name received in MESSAGE_OUTBOUND_PROPERTY");
					return false;
					//
					//
				}

				// get SOAP envelope
				SOAPMessage msg = smc.getMessage();
				SOAPPart sp = msg.getSOAPPart();
				SOAPEnvelope se = sp.getEnvelope();

				// conversao para string
				String soapMessage = soapMessageToString(msg);
				// System.out.println("\n\n\n\n\n\n\n\n");
				// System.out.print(soapMessage);
				// System.out.println("\n\n\n\n\n\n\n\n");

				DigitalSignatureX509 assinaturaX509 = null;
				// CHAVE PRIVADA DO BROKER
				PrivateKey chavePrivada = assinaturaX509.getPrivateKeyFromKeystore(keyStoreFilePath,
						keyStorePassword.toCharArray(), keyAlias, keyPassword.toCharArray());
				// Faz a assinatura com a chave privada do Broker
				byte[] assinaturaByte = assinaturaX509.makeDigitalSignature(soapMessage.getBytes(), chavePrivada);
				// Converte PARA STRING PARA DEPOIS INSERIR NO HEADER
				
				
				
				
				
				/*
				
				// --------------------------- DEMONSTRACAO ------------------------- //
				
				// simula alteracao da mensagem ( teste de integridade )
				assinaturaByte[3] = 12;
				
				System.out.println(printHexBinary(assinaturaByte));

				// ------------------------------------------------------------------ //
		
				
				*/
			
				
				
		
				String bytesAsString = printBase64Binary(assinaturaByte);

				// add header
				SOAPHeader sh = se.getHeader();
				if (sh == null)
					sh = se.addHeader();

				// add header element (name, namespace prefix, namespace)
				// add RESUMO
				Name name = se.createName("myHeader", "d", "http://demo");
				SOAPHeaderElement element = sh.addHeaderElement(name);
				element.addTextNode(bytesAsString);

				// add UUID para garantir frescura
				Name nameUUID = se.createName("myHeaderUUID", "dUUID", "http://demoUUID");
				SOAPHeaderElement elementUUID = sh.addHeaderElement(nameUUID);
				
				
				
				
				
				
				
				// --------------------------- DEMONSTRACAO ------------------------- //

				// trocar na demostraçao
				UUID idOne = UUID.randomUUID();
				elementUUID.addTextNode(idOne.toString());
				
				
				
				
				
				
				/*
				
				// --------------------------- DEMONSTRACAO ------------------------- //

				//adicionado uuid conhecido para testar a frescura das mensagens  -- trocar acima
				String idOne = "553c3eaf-44da-445e-a6ed-17e032ae14dd";
				elementUUID.addTextNode(idOne);

				// System.out.println(idOne);

				// ------------------------------------------------------------------ //

				*/
				
				
				
				

				// Faz a assinatura com a chave privada do Broker do UUID
				byte[] assinaturaByteUUID = assinaturaX509.makeDigitalSignature(idOne.toString().getBytes(),
						chavePrivada);
				// Converte PARA STRING PARA DEPOIS INSERIR NO HEADER
				String bytesAsStringUUID = printBase64Binary(assinaturaByteUUID);
				// add UUID para garantir frescura
				Name nameUUIDassinado = se.createName("myHeaderUUIDassinado", "dUUIDassinado",
						"http://demoUUIDassinado");
				SOAPHeaderElement elementUUIDassinado = sh.addHeaderElement(nameUUIDassinado);
				elementUUIDassinado.addTextNode(bytesAsStringUUID);

				// add header
				// SOAPHeader sh2 = se.getHeader();
				// if (sh2 == null)
				// sh2 = se.addHeader();

				// add header element (name, namespace prefix, namespace)
				Name name2 = se.createName("nome", "n", "http://nome");
				SOAPHeaderElement element2 = sh.addHeaderElement(name2);
				// add header element value
				element2.addTextNode(propertyValue);
				// System.out.println("-------------------");
				// System.out.println(propertyValue);
				// System.out.println("-------------------");

				// String soapMessagefinal = soapMessageToString(msg);
				// System.out.println("\n\n\n\n\n\n\n\n");
				// System.out.print(soapMessagefinal);
				// System.out.println("\n\n\n\n\n\n\n\n");

			} else {
				//
				//
				//
				// MENSAGEM RECEBIDA
				//
				//
				//
				System.out.println("Reading header in inbound SOAP message... - MENSAGEM RECEBIDA");
				System.out.println("----------------------------------");

				
				
				
				
				
				
				/*
		
			
				// --------------------------- DEMONSTRACAO ------------------------- //

				//adicionado uuid conhecido para testar a frescura das mensagens
				String idOne = "553c3eaf-44da-445e-a6ed-17e032ae14dd";
				queueUUID.add(idOne);

				// System.out.println(idOne);

				// ------------------------------------------------------------------ //
				
				
				
				 */
				
				
				
				
				
				// get SOAP envelope header
				SOAPMessage msg = smc.getMessage();
				SOAPPart sp = msg.getSOAPPart();
				SOAPEnvelope se = sp.getEnvelope();
				SOAPHeader sh = se.getHeader();

				// System.out.println("\n\n\n\n\nSOAP MESSAGEM\n\n");
				// System.out.println(soapMessage);
				// System.out.println("\n\n\n\n\n\n");

				// check header
				if (sh == null) {
					System.out.println("Header not found.");
					return true;
				}

				// get first header element
				Name name = se.createName("myHeader", "d", "http://demo");
				Iterator it = sh.getChildElements(name);
				// check header element
				if (!it.hasNext()) {
					System.out.println("Header element not found.");
					return true;
				}

				SOAPElement element = (SOAPElement) it.next();
				// get header element value
				String valueString = element.getValue();
				// System.out.println("\n\n\n\n "+"UUID :" + valueString+
				// "\n\n\n");
				byte[] assRecebida = parseBase64Binary(valueString);

				// get NOME DO UPA
				Name name2 = se.createName("nome", "n", "http://nome");
				Iterator it2 = sh.getChildElements(name2);
				if (!it2.hasNext()) {
					System.out.println("Header element not found.");
					return true;
				}
				SOAPElement element2 = (SOAPElement) it2.next();
				String valueStringNomeUPA = element2.getValue();
				// System.out.println("------RECEBE NULL?-------------");
				// System.out.println(valueStringNomeUPA);
				// System.out.println("-------------------");
				// String valueStringNomeUPA= valueStringNomeUPA.toString();

				// Vai verificar se o certificado esta na cache
				Certificate certUpaBroker;

				if (!certificateCache.containsKey(valueStringNomeUPA)) {

					// System.out.print("certificado não esta em cache cache");
					// Se não tiver - Vai buscar o certificado do broker
					// liga ao servidor de CA e vai buscar as chaves públicas
					CertificateClient badjoras = new CertificateClient("http://localhost:9090",
							"CertificateFileInterface");
					// saca o certificado/com chave pública do emissor
					certUpaBroker = badjoras.serverConnect(valueStringNomeUPA);
					// adiciona na cache
					certificateCache.put(valueStringNomeUPA, certUpaBroker);

				} else {
					// System.out.print("certificado em cache");
					certUpaBroker = certificateCache.get(valueStringNomeUPA);

				}

				// testa
				// System.out.println(cert.toString());
				DigitalSignatureX509 assinaturaX509 = null;
				// Public Key Broker
				PublicKey publicUpaKey = assinaturaX509.getPublicKeyFromCertificate(certUpaBroker);

				// get UUID
				Name nameUUID = se.createName("myHeaderUUID", "dUUID", "http://demoUUID");
				Iterator itUUID = sh.getChildElements(nameUUID);
				if (!itUUID.hasNext()) {
					System.out.println("Header element not found.");
					return true;
				}
				SOAPElement elementUUID = (SOAPElement) itUUID.next();
				String valueStringUUID = elementUUID.getValue();
				// System.out.println("\n\n\n\n "+"UUID :" + valueStringUUID+
				// "\n\n\n");

				// get UUID assinatura e verifica se o UUDI mantem a integridade
				Name nameUUIDassinado = se.createName("myHeaderUUIDassinado", "dUUIDassinado",
						"http://demoUUIDassinado");

				Iterator itUUIDassinado = sh.getChildElements(nameUUIDassinado);
				if (!itUUIDassinado.hasNext()) {
					System.out.println("Header element not found.");
					return true;
				}
				SOAPElement elementUUIDassinado = (SOAPElement) itUUIDassinado.next();
				String valueStringUUIDassinado = elementUUIDassinado.getValue();

				// ByteArrayOutputStream outUUDI = new ByteArrayOutputStream();
				// valueStringUUID.writeTo(outUUDI);

				// Verificação de UUID
				Boolean XUUID = assinaturaX509.verifyDigitalSignature(parseBase64Binary(valueStringUUIDassinado),
						valueStringUUID.getBytes(), publicUpaKey);
				if (!XUUID) {
					// Tem de retornar uma execepcao
					System.out.println("BAD UUDI VERIFY - UUDI MESSAGE IS NO EQUAL");
					return false;
				}
				System.out.println("\nVerificacao UUDI TRUE OU FALSE: " + XUUID + " \n");

				// Verifica se o UUID recebido está na lista
				if (queueUUID.contains(valueStringUUID)) {
					// trowsexception
					//
					System.out.println("Bad UUID");
					return false;
					//
					//
				}

				// adiciona na lista
				queueUUID.add(valueStringUUID);
				// remove o ultimo se tiver mais que 200 elementos
				if (queueUUID.size() == 200) {
					queueUUID.removeFirst();
				}

				// System.out.println(queueUUID.toString());
				// System.out.println("size:");
				// System.out.println(queueUUID.size());

				// System.out.println("\n\n\n\n\nSOAP MESSAGEM\n\n");
				// System.out.println(soapMessage);
				// System.out.println("\n\n\n\n\n\n");

				// Remove o Header
				sh.removeContents();

				String soapMessagefinal = soapMessageToString(msg);

				// System.out.println("\n\n\n\n\n\n\n\n");
				// System.out.print(soapMessagefinal);
				// System.out.println("\n\n\n\n\n\n\n\n");

				ByteArrayOutputStream out = new ByteArrayOutputStream();
				msg.writeTo(out);

				// Verificação de mensagens
				Boolean X = assinaturaX509.verifyDigitalSignature(assRecebida, out.toByteArray(), publicUpaKey);
				if (!X) {
					// Tem de retornar uma execepcao
					System.out.println("BAD VERIFY - SOAP MESSAGE IS NO EQUAL");
					return false;
				}
				System.out.println("Verificacao certificado TRUE OU FALSE: " + X + " \n");

				// put header in a property context
				// smc.put(CONTEXT_PROPERTY, value);
				smc.put(CONTEXT_PROPERTY, valueString);

				// set property scope to application client/server class can
				// access it
				smc.setScope(CONTEXT_PROPERTY, Scope.APPLICATION);

			}
		} catch (Exception e) {
			System.out.print("Caught exception in handleMessage: ");
			System.out.println(e);
			System.out.println("Continue normal processing...");
		}

		return true;
	}

	public boolean handleFault(SOAPMessageContext smc) {
		System.out.println("Ignoring fault message...");
		return true;
	}

	public void close(MessageContext messageContext) {
	}

}