# CustomHandler
Sample Custom Handler for WSO2 APIM manger


Installation
============
Please read the official documentation for details.
https://docs.wso2.com/display/AM180/Writing+Custom+Handlers#WritingCustomHandlers-Writingacustomhandler


Sample Synapse Configuration
============================

&lt;api name=&quot;admin--hello&quot; context=&quot;/world&quot; version=&quot;v1&quot; version-type=&quot;url&quot;&gt;
        &lt;resource methods=&quot;POST GET OPTIONS DELETE PUT&quot; url-mapping=&quot;/*&quot;&gt;
            &lt;inSequence&gt;
                &lt;property name=&quot;POST_TO_URI&quot; value=&quot;true&quot; scope=&quot;axis2&quot;/&gt;
                &lt;filter source=&quot;$ctx:AM_KEY_TYPE&quot; regex=&quot;PRODUCTION&quot;&gt;
                    &lt;then&gt;
                        &lt;loopback/&gt;
                    &lt;/then&gt;
                    &lt;else&gt;
                        &lt;sequence key=&quot;_sandbox_key_error_&quot;/&gt;
                    &lt;/else&gt;
                &lt;/filter&gt;
            &lt;/inSequence&gt;
            &lt;outSequence&gt;
                &lt;log level=&quot;full&quot;/&gt;
                &lt;send/&gt;
            &lt;/outSequence&gt;
        &lt;/resource&gt;
        &lt;handlers&gt;
            &lt;handler class=&quot;org.wso2.carbon.apimgt.gateway.handlers.security.APIAuthenticationHandler&quot;/&gt;
            &lt;handler class=&quot;org.wso2.carbon.apimgt.gateway.handlers.throttling.APIThrottleHandler&quot;&gt;
                &lt;property name=&quot;id&quot; value=&quot;A&quot;/&gt;
                &lt;property name=&quot;policyKey&quot; value=&quot;gov:/apimgt/applicationdata/tiers.xml&quot;/&gt;
            &lt;/handler&gt;
            &lt;handler class=&quot;org.wso2.carbon.apimgt.usage.publisher.APIMgtUsageHandler&quot;/&gt;
            &lt;handler class=&quot;org.wso2.carbon.apimgt.usage.publisher.APIMgtGoogleAnalyticsTrackingHandler&quot;/&gt;
            &lt;handler class=&quot;org.wso2.carbon.test.gateway.CustomHandler&quot;/&gt;&lt;!--Changed this according to your Custom Handler--&gt;
            &lt;handler class=&quot;org.wso2.carbon.apimgt.gateway.handlers.ext.APIManagerExtensionHandler&quot;/&gt;
        &lt;/handlers&gt;
    &lt;/api&gt;

Sample Request
======================

curl -XPOST -H 'Accept: application/soap+xml' -H 'myOwnHeader: mindyourheader' -d '{"sample":{"hello":"world"}}' 'http://localhost:8280/world/v1'

Expected Response
======================
Custom Header will be removed and Depend on the Accept header response payload will build.


