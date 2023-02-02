#!/bin/bash

# JSON object to pass to Lambda Function
json={"\"bucketname\"":"\"service1.div\"","\"filename\"":"\"Nehaa/sales.db\"","\"filter\"":"\"Region='Europe'\u0020AND\u0020Order_Priority='Medium'\"","\"aggregation\"":"\"AVG(gross_margin),AVG(Order_Processing_Time)\"","\"groupby\"":"\"Country\""}

echo $json
echo "Invoking Lambda function using API Gateway"
time output=`curl -s -H -v "Content-Type: application/json" -X POST -d  $json https://h5oltxf7l1.execute-api.us-east-2.amazonaws.com/service3`
echo ""
echo "CURL RESULT:"
echo $output
echo ""
