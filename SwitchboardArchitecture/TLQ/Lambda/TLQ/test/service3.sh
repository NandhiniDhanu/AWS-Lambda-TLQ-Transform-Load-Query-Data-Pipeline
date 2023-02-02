#!/bin/bash

# JSON object to pass to Lambda Function
json={"\"bucketname\"":"\"test.bucket.562f21.abc\"","\"filename\"":"\"sales.db\"","\"filter\"":"\"Region='Europe'\u0020AND\u0020Order_Priority='Medium'\"","\"aggregation\"":"\"AVG(gross_margin),AVG(Order_Processing_Time)\"","\"groupby\"":"\"Country\"","\"servicename\"":"\"service3\""}

echo $json
echo "Invoking Lambda function using API Gateway"
time output=`curl -s -H -v "Content-Type: application/json" -X POST -d  $json https://i5um7nn2xb.execute-api.us-east-2.amazonaws.com/ccproject`
echo ""
echo "CURL RESULT:"
echo $output
echo ""
time
