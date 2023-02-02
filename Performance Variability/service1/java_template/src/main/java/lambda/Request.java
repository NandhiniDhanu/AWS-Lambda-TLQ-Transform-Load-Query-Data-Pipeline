package lambda;


public class Request {

    String name;
    private String Bucketname;
    private String Filename;
    

    public String getName() {
        return name;
    }
    
    public String getNameALLCAPS() {
        return name.toUpperCase();
    }

    public void setName(String name) {
        this.name = name;
    }

    public Request(String name) {
        this.name = name;
    }

    public Request() {

    }
    public String getBucketname() {
        return Bucketname;
    }
    public String getFilename() {
        return Filename;
    }
    
    public void setBucketname(String Bucketname ){
        this.Bucketname=Bucketname;
    }
    public void setFilename(String Filename ){
        this.Filename=Filename;
    }
    

}
