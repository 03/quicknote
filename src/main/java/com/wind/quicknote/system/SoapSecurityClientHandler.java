package com.wind.quicknote.system;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.xml.internal.ws.wsdl.parser.SOAPConstants;

public class SoapSecurityClientHandler implements SOAPHandler<SOAPMessageContext> {
	
	private static Logger log = LoggerFactory.getLogger(SoapSecurityClientHandler.class);

	@Override
	public void close(MessageContext arg0) {

	}

	@Override
	public boolean handleFault(SOAPMessageContext arg0) {
		return false;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		
		Boolean isRequest = (Boolean) context.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY);

		if (isRequest) {
			log.debug("request...");
			
			SOAPMessage soapMsg = context.getMessage();
			SOAPEnvelope soapEnv;
			try {
				soapEnv = soapMsg.getSOAPPart().getEnvelope();
				SOAPHeader soapHeader = soapEnv.getHeader();

				if (soapHeader == null) {
					soapHeader = soapEnv.addHeader();
				}

				QName qname = new QName(SOAPConstants.URI_SOAP_TRANSPORT_HTTP, "macAddress");
				SOAPHeaderElement soapHeaderElement = soapHeader.addHeaderElement(qname);

				// actor/role/...
				/*soapHeaderElement.setActor(SOAPConstants.URI_SOAP_ACTOR_NEXT);
				soapHeaderElement.setRole(SOAPConstants.URI_SOAP_1_2_ROLE_NEXT);
				soapHeaderElement.setRelay(true);*/
				
				soapHeaderElement.addTextNode(getMACAddress());
				soapMsg.saveChanges();

				// tracking
				soapMsg.writeTo(System.out);
				
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			log.debug("response...");
		}
		return true;
	}

	@Override
	public Set<QName> getHeaders() {
		return null;
	}

	private String getMACAddress() throws Exception {
		
		StringBuilder sb = new StringBuilder();
		InetAddress ip = InetAddress.getLocalHost();
		NetworkInterface network = NetworkInterface.getByInetAddress(ip);
		byte[] mac = network.getHardwareAddress();
		for (int i = 0; i < mac.length; i++) {
			sb.append(String.format("%02X%s", mac[i],
					(i < mac.length - 1) ? "-" : ""));
		}

		log.debug("MAC Address:" + sb.toString());
		return sb.toString();
	}
}
