
package pt.upa.transporter.ws;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.10
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "BadJobFault", targetNamespace = "http://ws.transporter.upa.pt/")
public class BadJobFault_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private BadJobFault faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public BadJobFault_Exception(String message, BadJobFault faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public BadJobFault_Exception(String message, BadJobFault faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: pt.upa.transporter.ws.BadJobFault
     */
    public BadJobFault getFaultInfo() {
        return faultInfo;
    }

}
