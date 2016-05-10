
package pt.upa.broker.ws;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.10
 * Generated source version: 2.2
 * 
 */
@WebService(name = "BrokerPortType", targetNamespace = "http://ws.broker.upa.pt/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface BrokerPortType {


    /**
     * 
     * @param name
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "ping", targetNamespace = "http://ws.broker.upa.pt/", className = "pt.upa.broker.ws.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://ws.broker.upa.pt/", className = "pt.upa.broker.ws.PingResponse")
    @Action(input = "http://ws.broker.upa.pt/BrokerPort/pingRequest", output = "http://ws.broker.upa.pt/BrokerPort/pingResponse")
    public String ping(
        @WebParam(name = "name", targetNamespace = "")
        String name);

    /**
     * 
     * @param name
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "pingToBroker", targetNamespace = "http://ws.broker.upa.pt/", className = "pt.upa.broker.ws.PingToBroker")
    @ResponseWrapper(localName = "pingToBrokerResponse", targetNamespace = "http://ws.broker.upa.pt/", className = "pt.upa.broker.ws.PingToBrokerResponse")
    @Action(input = "http://ws.broker.upa.pt/BrokerPort/pingToBrokerRequest", output = "http://ws.broker.upa.pt/BrokerPort/pingToBrokerResponse")
    public String pingToBroker(
        @WebParam(name = "name", targetNamespace = "")
        String name);

    /**
     * 
     * @param price
     * @param origin
     * @param destination
     * @return
     *     returns java.lang.String
     * @throws UnavailableTransportFault_Exception
     * @throws UnknownLocationFault_Exception
     * @throws UnavailableTransportPriceFault_Exception
     * @throws InvalidPriceFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "requestTransport", targetNamespace = "http://ws.broker.upa.pt/", className = "pt.upa.broker.ws.RequestTransport")
    @ResponseWrapper(localName = "requestTransportResponse", targetNamespace = "http://ws.broker.upa.pt/", className = "pt.upa.broker.ws.RequestTransportResponse")
    @Action(input = "http://ws.broker.upa.pt/BrokerPort/requestTransportRequest", output = "http://ws.broker.upa.pt/BrokerPort/requestTransportResponse", fault = {
        @FaultAction(className = UnknownLocationFault_Exception.class, value = "http://ws.broker.upa.pt/BrokerPort/requestTransport/Fault/UnknownLocationFault"),
        @FaultAction(className = InvalidPriceFault_Exception.class, value = "http://ws.broker.upa.pt/BrokerPort/requestTransport/Fault/InvalidPriceFault"),
        @FaultAction(className = UnavailableTransportFault_Exception.class, value = "http://ws.broker.upa.pt/BrokerPort/requestTransport/Fault/UnavailableTransportFault"),
        @FaultAction(className = UnavailableTransportPriceFault_Exception.class, value = "http://ws.broker.upa.pt/BrokerPort/requestTransport/Fault/UnavailableTransportPriceFault")
    })
    public String requestTransport(
        @WebParam(name = "origin", targetNamespace = "")
        String origin,
        @WebParam(name = "destination", targetNamespace = "")
        String destination,
        @WebParam(name = "price", targetNamespace = "")
        int price)
        throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception
    ;

    /**
     * 
     * @param id
     * @return
     *     returns pt.upa.broker.ws.TransportView
     * @throws UnknownTransportFault_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "viewTransport", targetNamespace = "http://ws.broker.upa.pt/", className = "pt.upa.broker.ws.ViewTransport")
    @ResponseWrapper(localName = "viewTransportResponse", targetNamespace = "http://ws.broker.upa.pt/", className = "pt.upa.broker.ws.ViewTransportResponse")
    @Action(input = "http://ws.broker.upa.pt/BrokerPort/viewTransportRequest", output = "http://ws.broker.upa.pt/BrokerPort/viewTransportResponse", fault = {
        @FaultAction(className = UnknownTransportFault_Exception.class, value = "http://ws.broker.upa.pt/BrokerPort/viewTransport/Fault/UnknownTransportFault")
    })
    public TransportView viewTransport(
        @WebParam(name = "id", targetNamespace = "")
        String id)
        throws UnknownTransportFault_Exception
    ;

    /**
     * 
     * @return
     *     returns java.util.List<pt.upa.broker.ws.TransportView>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "listTransports", targetNamespace = "http://ws.broker.upa.pt/", className = "pt.upa.broker.ws.ListTransports")
    @ResponseWrapper(localName = "listTransportsResponse", targetNamespace = "http://ws.broker.upa.pt/", className = "pt.upa.broker.ws.ListTransportsResponse")
    @Action(input = "http://ws.broker.upa.pt/BrokerPort/listTransportsRequest", output = "http://ws.broker.upa.pt/BrokerPort/listTransportsResponse")
    public List<TransportView> listTransports();

    /**
     * 
     */
    @WebMethod
    @RequestWrapper(localName = "clearTransports", targetNamespace = "http://ws.broker.upa.pt/", className = "pt.upa.broker.ws.ClearTransports")
    @ResponseWrapper(localName = "clearTransportsResponse", targetNamespace = "http://ws.broker.upa.pt/", className = "pt.upa.broker.ws.ClearTransportsResponse")
    @Action(input = "http://ws.broker.upa.pt/BrokerPort/clearTransportsRequest", output = "http://ws.broker.upa.pt/BrokerPort/clearTransportsResponse")
    public void clearTransports();

}
