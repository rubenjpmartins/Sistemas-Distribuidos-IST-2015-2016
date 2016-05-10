package pt.upa.transporter.ws.handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
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

import pt.upa.cripto.DigitalSignatureX509;
import pt.upa.ws.cli.CertificateClient;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;
import static javax.xml.bind.DatatypeConverter.printHexBinary;
import static javax.xml.bind.DatatypeConverter.parseHexBinary;
import static javax.xml.bind.DatatypeConverter.parseBase64Binary;


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

public class TransporterServerHeaderHandler implements SOAPHandler<SOAPMessageContext> {
	
    public static final String CONTEXT_PROPERTY = "my.property";
    //
    // Handler interface methods
    //
    public Set<QName> getHeaders() {
        return null;
    }
    
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
            if (outboundElement.booleanValue()){
            	
            	
              	//
            	//
                //
                // MENSAGEM A ENVIAR PARA BROKER
            	//
            	//
            	//
            	
                System.out.println("Writing header in outbound SOAP message...");

                // get SOAP envelope
                SOAPMessage msg = smc.getMessage();
                SOAPPart sp = msg.getSOAPPart();
                SOAPEnvelope se = sp.getEnvelope();

                // add header
                SOAPHeader sh = se.getHeader();
                if (sh == null)
                    sh = se.addHeader();

                // add header element (name, namespace prefix, namespace)
                Name name = se.createName("myHeader", "d", "http://demo");
                SOAPHeaderElement element = sh.addHeaderElement(name);

                // add header element value
                int value = 26666;
                String valueString = Integer.toString(value);
                element.addTextNode(valueString);

                
                
            } else {
            	
            	//
            	//
                //
                // MENSAGEM RECEBIDA DO BROKER
            	//
            	//
            	//
            	

            	System.out.println("Reading header in inbound SOAP message...");
            	// get SOAP envelope header
                SOAPMessage msg = smc.getMessage();
                SOAPPart sp = msg.getSOAPPart();
                SOAPEnvelope se = sp.getEnvelope();
                SOAPHeader sh = se.getHeader();
                

                String soapMessage = soapMessageToString(msg);
                System.out.println("\n\n\n\n\nSOAP MESSAGEM\n\n");
                System.out.println(soapMessage);
                System.out.println("\n\n\n\n\n\n");
        
                
                // check header
                if (sh == null) {
                    System.out.println("Header not found.");
                    return true;
                }
                

                //Vai buscar o certificado do broker
    			//liga ao servidor de CA e vai buscar as chaves públicas do broker
    			CertificateClient badjoras = new CertificateClient("http://localhost:9090","CertificateFileInterface");
    			//saca o certificado/com chave pública do emissor
    			Certificate certUpaBroker = badjoras.serverConnect("UpaBroker");
    			//testa 
    			//System.out.println(cert.toString());
    			DigitalSignatureX509 assinaturaX509 = null;
    			//Public Key Broker
    			// FALTA GUARDAR PARA NÂO ESTAR SEMPRE A IR BUSCAR A CERTIFICATE
                PublicKey publicBrokerKey = assinaturaX509.getPublicKeyFromCertificate( certUpaBroker );
                
    			
                
    		
                
    			
    	
                
                
                
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
                byte[] assRecebida = parseBase64Binary(valueString);
                
                
                //int value = Integer.parseInt(valueString);
                // print received header
                //System.out.println("Header value is " + value);
                System.out.println("\n\n\n\n " + valueString);
            	
    			
                
                
                //Remove o Header
                sh.removeContents();
                
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                msg.writeTo(out);
                
                //Verificação de mensagens
                
                Boolean X = assinaturaX509.verifyDigitalSignature(assRecebida, out.toByteArray(), publicBrokerKey);
                
                System.out.println("\n\nValor: TRUE OU FALSE\n");

             
                
                System.out.println(X);

                //if(){
                //	Tem de retornar uma execepcao
                //	System.out.println("BAD VERIFY - SOAP MESSAGE IS NO EQUAL");
                //}
                

                

                
                
                // put header in a property context
                //smc.put(CONTEXT_PROPERTY, value);
                smc.put(CONTEXT_PROPERTY, valueString);
                
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
