#!/bin/bash

# JSON object to pass to Lambda Function
json={"\"bucketname\"":"\"test.bucket.562f21.efg\"","\"filename\"":"\"Nehaa/sales.db\"","\"filter\"":"\"Region='Europe'\u0020AND\u0020Order_Priority='Medium'\"","\"aggregation\"":"\"AVG(gross_margin),AVG(Order_Processing_Time)\"","\"groupby\"":"\"Country\""}

echo $json
echo "Invoking Lambda function using API Gateway"
time output=`curl -s -H -v "Content-Type: application/json" -X POST -d  $json https://fx7owtximi.execute-api.us-east-1.amazonaws.com/service3`
echo ""
echo "CURL RESULT:"
echo $output
echo ""
