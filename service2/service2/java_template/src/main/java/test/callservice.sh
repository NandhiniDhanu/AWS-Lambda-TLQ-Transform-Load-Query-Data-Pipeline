#!/bin/bash

# JSON object to pass to Lambda Function
json={"\"bucketname\"":"\"test.bucket.562f21.aaa\"","\"filename\"":"\"Transformed_Record_Data.csv\""}

echo $json
echo "Invoking Lambda function using API Gateway"
time output=`curl -s -H -v "Content-Type: application/json" -X POST -d  $json   https://c5asgn5f0i.execute-api.us-east-1.amazonaws.com/s2_stage`
echo ""
echo "CURL RESULT:"
echo $output
echo ""
