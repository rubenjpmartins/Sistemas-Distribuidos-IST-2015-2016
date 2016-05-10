package example.ws.handler;

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
import java.util.Iterator;
import java.util.LinkedList;
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

import static javax.xml.bind.DatatypeConverter.printBase64Binary;
import static javax.xml.bind.DatatypeConverter.printHexBinary;
import static javax.xml.bind.DatatypeConverter.parseHexBinary;
import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

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
public class UpaHeaderHandler implements SOAPHandler<SOAPMessageContext> {

    public static final String CONTEXT_PROPERTY = "my.property";

    //
    // Handler interface methods
    //
    public Set<QName> getHeaders() {
        return null;
    }



    //Lista que contem os UUID
    LinkedList<String> queueUUID = new LinkedList<String>();



    // Relativo ao getPrivateKeyFromKeystore
    private final String keyStoreFilePath = "UpaBrokerkeystore/UpaBroker.jks";
    private final String keyStorePassword = "ins3cur3";
    private final String keyAlias = "upabroker";
    private final String keyPassword = "1nsecure"; 


    //Tranforma a Message para uma String
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
            //
            //
            // MENSAGEM A ENVIAR PARA TRANSPORTER
            // RESUMO CIFRADO COM CHAVE PRIVADA DO BROKER  
            //
            //
            //
            if (outboundElement.booleanValue()) {

                System.out.println("Writing header in outbound SOAP message...");
 
                // get SOAP envelope
                SOAPMessage msg = smc.getMessage();
                SOAPPart sp = msg.getSOAPPart();
                SOAPEnvelope se = sp.getEnvelope();     
                
                // conversao para string
                String soapMessage = soapMessageToString(msg);
                //System.out.println("\n\n\n\n\n\n\n\n");
                //System.out.print(soapMessage);
                //System.out.println("\n\n\n\n\n\n\n\n");

                DigitalSignatureX509 assinaturaX509 = null;
                //CHAVE PRIVADA DO BROKER
                PrivateKey chavePrivadaBroker = assinaturaX509.getPrivateKeyFromKeystore(keyStoreFilePath,
                        keyStorePassword.toCharArray(), keyAlias, keyPassword.toCharArray());
                //Faz a assinatura com a chave pricava do Broker
                byte[]assinaturaByte = assinaturaX509.makeDigitalSignature(soapMessage.getBytes(), chavePrivadaBroker);
                // Converte PARA STRING PARA DEPOIS INSERIR NO HEADER
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
                UUID idOne = UUID.randomUUID();
                elementUUID.addTextNode(idOne.toString());
                System.out.println(idOne);
                
                String soapMessagefinal = soapMessageToString(msg);

                System.out.println("\n\n\n\n\n\n\n\n");
                System.out.print(soapMessagefinal);
                System.out.println("\n\n\n\n\n\n\n\n");








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
                //System.out.println("\n\n\n\n\nSOAP MESSAGEM\n\n");
                //System.out.println(soapMessage);
                //System.out.println("\n\n\n\n\n\n");
        
                
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
                System.out.println("\n\n\n\n "+"UUID :" + valueString+ "\n\n\n");
                byte[] assRecebida = parseBase64Binary(valueString);
                
                
                //get UUID
                Name nameUUID = se.createName("myHeaderUUID", "dUUID", "http://demoUUID");
                Iterator itUUID= sh.getChildElements(nameUUID);
                if (!itUUID.hasNext()) {
                    System.out.println("Header element not found.");
                    return true;
                }
                SOAPElement elementUUID = (SOAPElement) itUUID.next();
                String valueStringUUID= elementUUID.getValue();

                System.out.println("\n\n\n\n "+"UUID :" + valueStringUUID+ "\n\n\n");

                
                //Verifica se o UUID recebido está na lista
                if(queueUUID.contains(valueStringUUID)){
                    //trowsexception
                    //
                    //
                    //
                }
                
                //adiciona na lista
                queueUUID.add(valueStringUUID);
                //remove o ultimo se tiver mais que 20 elementos
                if(queueUUID.size()==20){
                    queueUUID.removeFirst();
                }
                
                
                System.out.println(queueUUID.toString());
                System.out.println("size:");
                System.out.println(queueUUID.size());
                
                //System.out.println("\n\n\n\n\nSOAP MESSAGEM\n\n");
                //System.out.println(soapMessage);
                //System.out.println("\n\n\n\n\n\n");
        
                
                
                
                
                
                
                
                
                //Remove o Header
                sh.removeContents();
                
                String soapMessagefinal = soapMessageToString(msg);

                System.out.println("\n\n\n\n\n\n\n\n");
                System.out.print(soapMessagefinal);
                System.out.println("\n\n\n\n\n\n\n\n");
                
                
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                msg.writeTo(out);
                
                
                
                //Verificação de mensagens
                Boolean X = assinaturaX509.verifyDigitalSignature(assRecebida, out.toByteArray(), publicBrokerKey);
                //if(X){
                //  Tem de retornar uma execepcao
                //  System.out.println("BAD VERIFY - SOAP MESSAGE IS NO EQUAL");
                //}
                System.out.println("\n\nValor: TRUE OU FALSE\n");             
                
                System.out.println(X);

                
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