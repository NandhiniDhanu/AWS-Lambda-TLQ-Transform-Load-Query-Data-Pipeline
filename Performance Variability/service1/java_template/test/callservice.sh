#!/bin/bash
# JSON object to pass to Lambda Function
json={"\"row\"":50,"\"col\"":10,"\"bucketname\"":\"test.bucket.562f21.aaa\"","\"filename\"":\"test.csv\""}
echo "Invoking Lambda function using API Gateway"
time output=`curl -s -H "Content-Type: application/json" -X POST -d $json  https://70tbmia2h0.execute-api.us-east-2.amazonaws.com/CreateCSV`
echo “”
echo ""
echo "JSON RESULT:"
echo $output | jq
echo ""
