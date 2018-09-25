package org.wso2.sample.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.rest.AbstractHandler;
import java.util.TreeMap;

import org.wso2.carbon.CarbonConstants;


public class CustomHandler extends AbstractHandler {

    private static final Log AUDIT_LOG = CarbonConstants.AUDIT_LOG;

    public boolean mediate(MessageContext messageContext, String direction) {
        return true;
    }

    public boolean handleRequest(MessageContext messageContext) {
        TreeMap<String, String> headers = (TreeMap<String, String>) ((Axis2MessageContext) messageContext).
                getAxis2MessageContext().getProperty("TRANSPORT_HEADERS");
        AUDIT_LOG.info("SessionId created with custom logging handler : " + headers.get("SessionId"));
        return true;
    }

    public boolean handleResponse(MessageContext messageContext) {
        return false;
    }


}