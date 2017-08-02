package org.wso2.carbon.test.gateway;

import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.rest.AbstractHandler;
import java.util.TreeMap;

import org.wso2.carbon.apimgt.gateway.handlers.Utils;

import org.apache.synapse.*;

import org.apache.http.HttpStatus;
import org.apache.synapse.transport.passthru.util.RelayUtils;
import org.apache.axis2.Constants;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;


public class CustomHandler extends AbstractHandler {

    private static final String DIRECTION_OUT = "Out";
    private static final String APPLICATION_JSON = "application/json";
    private static final Log log = LogFactory.getLog(CustomHandler.class);

    public boolean mediate(MessageContext messageContext, String direction) {
        log.info("====================mediate===========================================================");
        return true;
    }

    public boolean handleRequest(MessageContext messageContext) {
        log.info("===================handleRequest============================================================");
        // Set the Payload
        sendBadRequestTrace(messageContext, "Missing CSRF Token");
    
        TreeMap<String, String> headers = (TreeMap<String, String>) ((Axis2MessageContext) messageContext).
                getAxis2MessageContext().getProperty("TRANSPORT_HEADERS");
        //Break the flow
        return false;
    }

    public boolean handleResponse(MessageContext messageContext) {
        log.info("==================== Mediate OUT =========================================================");
        return mediate(messageContext, DIRECTION_OUT);
    }

    private OMElement getFaultPayload(MessageContext messageContext) {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace ns = fac.createOMNamespace("http://wso2.org/apimanager/security", "ams");
        OMElement payload = fac.createOMElement("fault", ns);
        OMElement errorCode = fac.createOMElement("code", ns);
        errorCode.setText("Code");
        OMElement errorMessage = fac.createOMElement("message", ns);
        errorMessage.setText("Message");
        OMElement errorDetail = fac.createOMElement("description", ns);
        errorDetail.setText("description");
        payload.addChild(errorCode);
        payload.addChild(errorMessage);
        payload.addChild(errorDetail);
        return payload;
    }

    private void sendBadRequestTrace(MessageContext messageContext,
                                     String errorMessage) {
        messageContext.setProperty(SynapseConstants.ERROR_MESSAGE, errorMessage);
        org.apache.axis2.context.MessageContext axis2MC = ((Axis2MessageContext) messageContext).
                getAxis2MessageContext();
        try {
            RelayUtils.buildMessage(axis2MC);
        } catch (Exception ex) {
            log.error("Error occurred while building the message", ex);
        }
        TreeMap<String, String> headers = (TreeMap<String, String>) ((Axis2MessageContext) messageContext).
                getAxis2MessageContext().getProperty("TRANSPORT_HEADERS");
        headers.remove("myOwnHeader");// you can move to your common method
        if (headers != null && headers.get(HTTPConstants.HEADER_ACCEPT) != null && !headers.get(HTTPConstants.HEADER_ACCEPT).equals("*/*")) {
            axis2MC.setProperty(Constants.Configuration.MESSAGE_TYPE,headers.get(HTTPConstants.HEADER_ACCEPT));
        } else {
            axis2MC.setProperty(Constants.Configuration.MESSAGE_TYPE, CustomHandler.APPLICATION_JSON);
        }
        Utils.setSOAPFault(messageContext, "Client", "Authentication Failure", "No Message thank god!");
        Utils.sendFault(messageContext, HttpStatus.SC_UNAUTHORIZED);
    }
}
