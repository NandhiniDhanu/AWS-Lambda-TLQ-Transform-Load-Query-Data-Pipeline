package lambda;

import java.util.*;
        
public class Request {
    private String filter;
    private String groupby;
    private String aggregation;
    
    private String bucketname;
    private String filename;


    public Request() {

    }
    
    /**
     * @return the filter
     */
    public String getFilter() {
        return filter;
    }

    /**
     * @param filters the filter to set
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


    /**
     * @return the bucketname
     */
    public String getBucketname() {
        return bucketname;
    }

    /**
     * @param bucketname the bucketname to set
     */
    public void setBucketname(String bucketname) {
        this.bucketname = bucketname;
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }
}
