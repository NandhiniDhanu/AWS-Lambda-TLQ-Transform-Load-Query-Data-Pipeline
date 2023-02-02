package lambda;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;

import java.util.Scanner;
import java.util.Set;
import com.amazonaws.services.lambda.runtime.Context;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import java.io.File;
import java.io.FileNotFoundException;

import lambda.Response;

public class Service1 implements RequestHandler<Request, String>{ 	
    public String handleRequest(Request request, Context context) {   
        
        String srcBucket = request.getBucketname();
        String srcKey = request.getFilename();
        
        Scanner scanner = null;
        File inputFile = new File("/tmp/"+ srcKey);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().build();
        
        try {
            scanner = new Scanner(inputFile);
        } catch (FileNotFoundException ex) {
            //get object file using source bucket and srcKey name
            S3Object s3Object = s3Client.getObject(new GetObjectRequest(srcBucket, srcKey));
            //get content of the file
            InputStream objectData = s3Object.getObjectContent();
            //scanning data line by line
            scanner = new Scanner(objectData);
        }
        

        Set<Long> uniqueOrderId = new HashSet<>();
        HashMap<String,String> priorities=new HashMap<>();
        priorities.put("L", "Low");
        priorities.put("H", "High");
        priorities.put("M", "Medium");
        priorities.put("C", "Critical");
        
        StringWriter sw = new StringWriter();
        String nextline = scanner.nextLine();
        nextline += ",Order Processing Time,Gross Margin\n";
        sw.append(nextline);
        while(scanner.hasNext()) {
        	nextline= scanner.nextLine();
        	 String[] row = nextline.split(",");
        	 SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        	 int days_difference =0;
        	
             long orderId = Long.parseLong(row[6]);
             if(uniqueOrderId.contains(orderId)) {
            	 continue;
             }
             String orderDate=row[5];
             String shipDate=row[7];
             uniqueOrderId.add(orderId);
             try {
                 Date orderDateRow = dateFormat.parse(orderDate);
                 Date shipDateRow = dateFormat.parse(shipDate);
                 long time_difference = shipDateRow.getTime() - orderDateRow.getTime();
                 days_difference = (int)(time_difference / (1000*60*60*24)) % 365;
             } catch (ParseException ex) {
                 
             }
             // Calculating profit , revenue 
             double profit = Double.parseDouble(row[row.length -1]);
             double revenue = Double.parseDouble(row[row.length -3]);
             double grossMargin = profit / revenue;
       
             String priority = row[4];
             row[4] = priorities.get(priority);
             nextline = String.join(",", row);
            
			String lastcolumn= String.format(",%d,%.2f\n", days_difference , grossMargin);
             nextline += lastcolumn;
             sw.append(nextline);
        	
        	
        }
        
        scanner.close();
           
        byte[] bytes = sw.toString().getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(bytes);
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(bytes.length);
        meta.setContentType("text/plain");
        String filename2 = "Transformed_Record_Data.csv";
        s3Client.putObject(srcBucket, filename2, is, meta);
        Response response = new Response();
        response.setValue("Bucket: " + srcBucket + " filename: " + srcKey + " processed.");
        return response.getValue();

    }
    
}