# CustomHandler
Sample Custom Logging Handler for WSO2 APIM manger


Installation
============
Please read the official documentation for details.
https://docs.wso2.com/display/AM250/Writing+Custom+Handlers


Sample Synapse Configuration
============================
```
<?xml version="1.0" encoding="UTF-8"?>
<api xmlns="http://ws.apache.org/ns/synapse"
     name="admin--loggingAPI"
     context="/logging/v1"
     version="v1"
     version-type="context">
   <resource methods="POST GET" url-mapping="/*" faultSequence="fault">
      <inSequence>
         <property name="api.ut.backendRequestTime"
                   expression="get-property('SYSTEM_TIME')"/>
         <filter source="$ctx:AM_KEY_TYPE" regex="PRODUCTION">
            <then>
               <send>
                  <endpoint key="loggingAPI--vv1_APIproductionEndpoint"/>
               </send>
            </then>
            <else>
               <send>
                  <endpoint key="loggingAPI--vv1_APIsandboxEndpoint"/>
               </send>
            </else>
         </filter>
      </inSequence>
      <outSequence>
         <class name="org.wso2.carbon.apimgt.gateway.handlers.analytics.APIMgtResponseHandler"/>
         <send/>
      </outSequence>
   </resource>
   <handlers>
      <handler class="org.wso2.sample.handler.CustomHandler"/>
      <handler class="org.wso2.carbon.apimgt.gateway.handlers.common.APIMgtLatencyStatsHandler"/>
      <handler class="org.wso2.carbon.apimgt.gateway.handlers.security.CORSRequestHandler">
         <property name="apiImplementationType" value="ENDPOINT"/>
      </handler>
      <handler class="org.wso2.carbon.apimgt.gateway.handlers.security.APIAuthenticationHandler">
         <property name="RemoveOAuthHeadersFromOutMessage" value="true"/>
      </handler>
      <handler class="org.wso2.carbon.apimgt.gateway.handlers.throttling.ThrottleHandler"/>
      <handler class="org.wso2.carbon.apimgt.gateway.handlers.analytics.APIMgtUsageHandler"/>
      <handler class="org.wso2.carbon.apimgt.gateway.handlers.analytics.APIMgtGoogleAnalyticsTrackingHandler">
         <property name="configKey" value="gov:/apimgt/statistics/ga-config.xml"/>
      </handler>
      <handler class="org.wso2.carbon.apimgt.gateway.handlers.ext.APIManagerExtensionHandler"/>
   </handlers>
</api>
```


Sample Request
======================

curl -v -H "SessionId: 123213213" -H "Authorization: Bearer 4c394433-bcb9-30d3-b68a-603877ca6cdc"  http://localhost:8280/logging/v1

Expected Result
======================
You should see the following log printed in the APIM_HOME/repository/logs/audit.log file

[2018-09-25 17:47:05,713]  INFO -  SessionId created with custom logging handler : 123213213 

