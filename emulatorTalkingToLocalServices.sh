#!/bin/bash
sed -i s/denevell.org:8912/10.0.2.2:8080/ res/values/strings.xml
curl -H "Content-Type: application/json" -X PUT -d '{"username": "aaron", "password":"aaron"}' localhost:8080/Natch-REST-ForAutomatedTests/rest/user/
SESSIONID=`curl -H "Content-Type: application/json" -X POST -d '{"username": "aaron", "password":"aaron"}' localhost:8080/Natch-REST-ForAutomatedTests/rest/user/login | /bin/sed "s#.*authKey.*:\(.*\),.*#\\1#"`
echo $SESSIONID
/bin/sed -i "s#\\(.*<string.*services_session_id.*>\\).*\\(</string>.*\\).*#\1$SESSIONID\2#" res/values/strings.xml
