# Moving file from local to s3
BUCKETNAME='test.bucket.562f21.efg/'
FILENAME='Records.csv'
aws s3 cp $FILENAME s3://$BUCKETNAME

# JSON object to pass to Lambda Function
json={"\"bucketname\"":\"test.bucket.562f21.efg\"","\"filename\"":\"Records.csv\""}
echo "Invoking Lambda function using API Gateway"
time output=`curl -s -H "Content-Type: application/json" -X POST -d $json https://1qvgczwp4d.execute-api.us-east-1.amazonaws.com/processcsv`
echo “”
echo ""
echo "JSON RESULT:"
echo $output | jq
echo ""
