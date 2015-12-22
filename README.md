# CustomHandler
Sample Custom Handler for WSO2 APIM manger

Sample Request
======================

curl -XPOST -H 'Accept: application/soap+xml' -H 'myOwnHeader: mindyourheader' -d '{"sample":{"hello":"world"}}' 'http://10.249.134.81:8280/world/v1'


Expected Response
======================
Custom Header will be removed and Depend on the Accept header response payload will build.


