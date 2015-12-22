# CustomHandler
Sample Custom Handler for WSO2 APIM manger


Installation
============
Please read the official documentation for details.
https://docs.wso2.com/display/AM180/Writing+Custom+Handlers#WritingCustomHandlers-Writingacustomhandler


Sample Synapse Configuration
============================

<api name="admin--hello" context="/world" version="v1" version-type="url">
        <resource methods="POST GET OPTIONS DELETE PUT" url-mapping="/*">
            <inSequence>
                <property name="POST_TO_URI" value="true" scope="axis2"/>
                <filter source="$ctx:AM_KEY_TYPE" regex="PRODUCTION">
                    <then>
                        <loopback/>
                    </then>
                    <else>
                        <sequence key="_sandbox_key_error_"/>
                    </else>
                </filter>
            </inSequence>
            <outSequence>
                <log level="full"/>
                <send/>
            </outSequence>
        </resource>
        <handlers>
            <handler class="org.wso2.carbon.apimgt.gateway.handlers.security.APIAuthenticationHandler"/>
            <handler class="org.wso2.carbon.apimgt.gateway.handlers.throttling.APIThrottleHandler">
                <property name="id" value="A"/>
                <property name="policyKey" value="gov:/apimgt/applicationdata/tiers.xml"/>
            </handler>
            <handler class="org.wso2.carbon.apimgt.usage.publisher.APIMgtUsageHandler"/>
            <handler class="org.wso2.carbon.apimgt.usage.publisher.APIMgtGoogleAnalyticsTrackingHandler"/>
            <handler class="org.wso2.carbon.test.gateway.CustomHandler"/><!--Changed this according to your Custom Handler-->
            <handler class="org.wso2.carbon.apimgt.gateway.handlers.ext.APIManagerExtensionHandler"/>
        </handlers>
    </api>

Sample Request
======================

curl -XPOST -H 'Accept: application/soap+xml' -H 'myOwnHeader: mindyourheader' -d '{"sample":{"hello":"world"}}' 'http://10.249.134.81:8280/world/v1'

Expected Response
======================
Custom Header will be removed and Depend on the Accept header response payload will build.


