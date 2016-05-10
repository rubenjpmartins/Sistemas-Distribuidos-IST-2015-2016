package pt.upa.transporter.ws.cli.handler;


import java.util.Base64;
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
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import pt.upa.cripto.DigitalSignatureX509;



/**
 *  This SOAPHandler shows how to set/get values from headers in
 *  inbound/outbound SOAP messages.
 *
 *  A header is created in an outbound message and is read on an
 *  inbound message.
 *
 *  The value that is read from the header
 *  is placed in a SOAP message context property
 *  that can be accessed by other handlers or by the application.
 */
public class TransporterBrokerHeaderHandler implements SOAPHandler<SOAPMessageContext> {
	
	// Relativo ao getPrivateKeyFromKeystore
    private final String keyStoreFilePath = "UpaBrokerkeystore/UpaBroker.jks";
    private final String keyStorePassword = "ins3cur3";
    private final String keyAlias = "upabroker";
    private final String keyPassword = "1nsecure"; 
    
    

    public static final String CONTEXT_PROPERTY = "my.property";
    //
    // Handler interface methods
    //
    public Set<QName> getHeaders() {
        return null;
    }
    //
    // adicionado para trocar a soapmessage to a string
    //
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
        System.out.println("AddHeaderHandler: Handling message.");
        

        Boolean outboundElement = (Boolean) smc
                .get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        try {
            //
            // MENSAGEM A ENVIAR!
        	//
            if (outboundElement.booleanValue()) {
                System.out.println("Writing header in outbound SOAP message...");

                
                // get SOAP envelope
                SOAPMessage msg = smc.getMessage();
                SOAPPart sp = msg.getSOAPPart();
                SOAPEnvelope se = sp.getEnvelope();     
                
                
                // conversão para string
                String soapMessage = soapMessageToString(msg);
                //System.out.println("\n\n\n\n\n\n\n\n");
                //System.out.print(soapMessage);
                //System.out.println("\n\n\n\n\n\n\n\n");

                DigitalSignatureX509 assinaturaX509 = null;

                PrivateKey chavePrivadaBroker = assinaturaX509.getPrivateKeyFromKeystore(keyStoreFilePath,
                        keyStorePassword.toCharArray(), keyAlias, keyPassword.toCharArray());

                byte[]assinaturaByte = assinaturaX509.makeDigitalSignature(soapMessage.getBytes(), chavePrivadaBroker);
                // Impressão
                // System.out.println(javax.xml.bind.DatatypeConverter.printHexBinary(larilas));
                
                //String assinatura = new String(assinaturaByte,"UTF-8");
                //System.out.println("assinatura");
         
                
                
                String bytesAsString = new String(assinaturaByte, StandardCharsets.UTF_8);

                
                
                // add header
                SOAPHeader sh = se.getHeader();
                if (sh == null)
                    sh = se.addHeader();

                // add header element (name, namespace prefix, namespace)
                Name name = se.createName("myHeader", "d", "http://demo");
                SOAPHeaderElement element = sh.addHeaderElement(name);

                
                // add header element value
                //int value = 22;
                //String valueString = Integer.toString(value);
                //element.addTextNode(valueString);
                element.addTextNode(bytesAsString);
                
                
                
                
                
                
                
                
                
                //System.out.print("\n\n\n\n\n\n\n\n\n");
                //msg.writeTo(System.out);
                //System.out.print("\n\n\n\n\n\n\n\n\n");
                
                
                
            } else {
                System.out.println("Reading header in inbound SOAP message...");

                // get SOAP envelope header
                SOAPMessage msg = smc.getMessage();
                SOAPPart sp = msg.getSOAPPart();
                SOAPEnvelope se = sp.getEnvelope();
                SOAPHeader sh = se.getHeader();

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
                int value = Integer.parseInt(valueString);

                // print received header
                System.out.println("Header value is " + value);

                
                
                // put header in a property context
                smc.put(CONTEXT_PROPERTY, value);
                // set property scope to application client/server class can access it
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





















