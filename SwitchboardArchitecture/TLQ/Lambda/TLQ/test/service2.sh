#!/bin/bash

# JSON object to pass to Lambda Function
json={"\"bucketname\"":"\"test.bucket.562f21.abc\"","\"filename\"":"\"Transformed_Record_Data.csv\"","\"servicename\"":"\"service2\""}

echo $json
echo "Invoking Lambda function using API Gateway"
time output=`curl -s -H -v "Content-Type: application/json" -X POST -d  $json  https://i5um7nn2xb.execute-api.us-east-2.amazonaws.com/ccproject`
echo ""
echo "CURL RESULT:"
echo $output
echo ""
time
