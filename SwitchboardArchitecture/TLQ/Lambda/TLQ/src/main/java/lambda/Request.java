package lambda;


public class Request {

    private String bucketname;
    private String filename;
    private String servicename;
    private String filter;
    private String groupby;
    private String aggregation;

    public Request() {

    }
    public String getBucketname() {
        return bucketname;
    }
    
    public void setBucketname(String bucketname ){
        this.bucketname = bucketname;
    }
    
    public String getFilename() {
        return filename;
    }
    
    public void setFilename(String filename ){
        this.filename = filename;
    }
    public String getServicename(){
        return servicename;
    }
    public void setServicename(String servicename ){
        this.servicename = servicename;
    }
    
    /**
     * @return the filter
     */
    public String getFilter() {
        return filter;
    }

    /**
     * @param filter the filter to set
     */
    public void setFilter(String filter) {
        this.filter = filter;
    }

    /**
     * @return the aggregation
     */
    public String getAggregation() {
        return aggregation;
    }

    /**
     * @param aggregation the aggregation to set
     */
    public void setAggregation(String aggregation) {
        this.aggregation = aggregation;
    }
    
    
    /**
     * @return the groupby
     */
    public String getGroupby() {
        return groupby;
    }

    /**
     * @param groupby the groupby to set
     */
    public void setGroupby(String groupby) {
        this.groupby = groupby;
    }

}
