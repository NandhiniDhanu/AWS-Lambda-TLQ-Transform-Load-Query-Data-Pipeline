# Moving file from local to s3
BUCKETNAME='test.bucket.562f21.abc/'
FILENAME='Records.csv'
aws s3 cp $FILENAME s3://$BUCKETNAME

# JSON object to pass to Lambda Function
json={"\"bucketname\"":\"test.bucket.562f21.abc\"","\"filename\"":\"Records.csv\""}
echo "Invoking Lambda function using API Gateway"
time output=`curl -s -H "Content-Type: application/json" -X POST -d $json https://i5um7nn2xb.execute-api.us-east-2.amazonaws.com/ccproject`
echo “”
echo ""
echo "JSON RESULT:"
echo $output | jq
echo ""
