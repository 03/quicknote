package com.wind.quicknote.system;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.Node;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http://blog.csdn.net/unei66/article/details/12291051
 */
public class SoapSecurityHandler implements SOAPHandler<SOAPMessageContext> {
	
	private static Logger log = LoggerFactory.getLogger(SoapSecurityHandler.class);

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		logDetails(context);
		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		logDetails(context);
		return true;
	}

	@Override
	public void close(MessageContext context) {
	}

	@Override
	public Set<QName> getHeaders() {
		return Collections.emptySet();
	}

	private void logDetails(SOAPMessageContext smc) {
		
		Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (outboundProperty.booleanValue()) {
			log.debug("\nResponse:");
			
			/*ByteArrayOutputStream baos = new ByteArrayOutputStream();
			soapMsg.writeTo(baos);
			log.debug(baos.toString());*/
			
		} else {
			log.debug("\nRequest:");

			try {
				SOAPMessage soapMsg = smc.getMessage();
				SOAPEnvelope soapEnv = soapMsg.getSOAPPart().getEnvelope();
				SOAPHeader soapHeader = soapEnv.getHeader();

				// if no header, add one and throw exception
				if (soapHeader == null) {
					soapHeader = soapEnv.addHeader();
					generateSoapError(soapMsg, "SOAP header is missing!");
				}

				// Get
				Iterator<?> it = soapHeader.extractHeaderElements(SOAPConstants.URI_SOAP_ACTOR_NEXT);
				if (it == null || !it.hasNext()) {
					generateSoapError(soapMsg, "No header block exists!");
				}

				Node a = (Node) it.next();
				log.debug("Node (MAC): " + a.getValue());

				// do some other check here
				// ...
				
			} catch (SOAPException e) {
				e.printStackTrace();
			}
		}

	}
	
	private void generateSoapError(SOAPMessage msg, String reason)
			throws SOAPException {
		
		SOAPBody soapBody = msg.getSOAPPart().getEnvelope().getBody();
		SOAPFault soapFault = soapBody.addFault();
		soapFault.setFaultString(reason);
		throw new SOAPFaultException(soapFault);
		
	}

}
