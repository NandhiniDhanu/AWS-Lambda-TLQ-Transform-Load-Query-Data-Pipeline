/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lambda;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import faasinspector.register;

import java.io.InputStream;
import java.io.File;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.UUID;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.SourceDataLine;

public class Service2DataLoad implements RequestHandler<Request, Response> {

    //static String CONTAINER_ID = "/tmp/container-id";
    //static Charset CHARSET = Charset.forName("US-ASCII");
    
    public Response handleRequest(Request request, Context context) {
        LambdaLogger logger = context.getLogger();
        register reg = new register(logger);
        Response response = reg.StampContainer();

        String pwd = System.getProperty("user.dir");
        logger.log("pwd=" + pwd);

        logger.log("set pwd to tmp");
        setCurrentDirectory("/tmp");

        pwd = System.getProperty("user.dir");
        logger.log("pwd=" + pwd);

        String bucketname = request.getBucketname();
        String filename = request.getFilename();
        String[] names = filename.split("/");
        
        String dbname = "sales.db";

        logger.log("Creating s3 client");
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().build();
        
        //get object file using source bucket and srcKey name
        logger.log("Reading bucket : " + bucketname);
        S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketname, filename));
        
        InputStream objectData = s3Object.getObjectContent();
        
        Scanner scanner = new Scanner(objectData);
        String rowString = scanner.nextLine(); //Skipping first rowString since it contains the table header
        
        try {
            Class.forName("org.sqlite.JDBC");
            
            Connection con = DriverManager.getConnection("jdbc:sqlite:/tmp/sales.db");
            logger.log("Connection Established");
            
            // Detect if the table 'sales_data' exists in the database
            PreparedStatement ps = con.prepareStatement("SELECT name FROM sqlite_master WHERE type='table' AND name='sales_data'");
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                // 'sales_data' does not exist, and should be created
                logger.log("trying to create table 'sales_data'");
                ps = con.prepareStatement("CREATE TABLE sales_data (Region text,Country text,Item_Type text,Sales_Channel text,Order_Priority text,Order_Date date," +
                                                "Order_ID integer PRIMARY KEY,Ship_Date date,Units_Sold integer,Unit_Price float,Unit_Cost float," + 
                                                "Total_Revenue float,Total_Cost float,Total_Profit float,Order_Processing_Time integer,Gross_Margin float);"
                                        );
                ps.execute();
            }
            rs.close();

            logger.log("Start writing to DB");

            // Insert row by row into sales_data
            while (scanner.hasNext()) {
                rowString = scanner.nextLine();
                
                rowString = rowString.replace("\'","\'\'");
                String rowEntryList[] = rowString.split(",");

                for (int i = 0; i < 6; i++) {
                    rowEntryList[i] = "'" + rowEntryList[i] + "'";
                }
                rowEntryList[7] = "'" + rowEntryList[7] + "'";

                rowString = String.join(",", rowEntryList);
                
                ps = con.prepareStatement("INSERT INTO sales_data values(" + rowString + ");");
                ps.execute();
                ps.close();
            }

            con.close();
            logger.log("Finished writing to DB");
        } catch (SQLException e) {
            logger.log("DB ERROR:" + e.toString());
            e.printStackTrace();
        } catch (Exception e){
            logger.log("ERROR: Exception");
            e.printStackTrace();
        }

        scanner.close();
        
        logger.log("Uploading db to S3 bucket");
        File file = new File("/tmp/sales.db");
        s3Client.putObject("test.bucket.562f21.aaa", "Nehaa/" + dbname, file);
        
        logger.log("Deleting DB from temp folder");
        file.delete();

        String responseString = "Finished loading file:" + filename + " into DB:" + dbname + " and uploaded to S3 bucket:" + bucketname;
//                "Bucket: " + bucketname + " filename: " + filename + " loaded. DBname: " + dbname;
        logger.log(responseString);
        
        response.setValue(responseString);
        return response;
    }

    
    public static boolean setCurrentDirectory(String directory_name)
    {
        boolean result = false;  // Boolean indicating whether directory was set
        File    directory;       // Desired current working directory

        directory = new File(directory_name).getAbsoluteFile();
        if (directory.exists() || directory.mkdirs())
        {
            result = (System.setProperty("user.dir", directory.getAbsolutePath()) != null);
        }

        return result;
    }

    // int main enables testing function from cmd rowString
    public static void main(String[] args) {
        Context c = new Context() {
            @Override
            public String getAwsRequestId() {
                return "";
            }

            @Override
            public String getLogGroupName() {
                return "";
            }

            @Override
            public String getLogStreamName() {
                return "";
            }

            @Override
            public String getFunctionName() {
                return "";
            }

            @Override
            public String getFunctionVersion() {
                return "";
            }

            @Override
            public String getInvokedFunctionArn() {
                return "";
            }

            @Override
            public CognitoIdentity getIdentity() {
                return null;
            }

            @Override
            public ClientContext getClientContext() {
                return null;
            }

            @Override
            public int getRemainingTimeInMillis() {
                return 0;
            }

            @Override
            public int getMemoryLimitInMB() {
                return 0;
            }

            @Override
            public LambdaLogger getLogger() {
                return new LambdaLogger() {
                    @Override
                    public void log(String string) {
                        System.out.println("LOG:" + string);
                    }
                };
            }
        };

        // Create an instance of the class
        Service2DataLoad lt = new Service2DataLoad();
        
        Request req = new Request();
        
//        System.out.println("cmd-rowString param name=" + req.getBucketname());
        
        // Run the function
        Response resp = lt.handleRequest(req, c);
        
        // Print out function result
//        System.out.println("function result:" + resp.toString());
    }
}