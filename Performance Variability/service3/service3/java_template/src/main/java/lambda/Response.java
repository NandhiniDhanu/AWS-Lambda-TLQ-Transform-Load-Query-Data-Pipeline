package lambda;

import java.util.List;

class QueryResult {
    private String query;
    private double value;

    public QueryResult() {
    }

    public QueryResult(String query) {
        this();
        this.query = query;
    }

    public QueryResult(String query, double value) {
        this(query);
        this.value = value;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
public class Response extends saaf.Response {
    
    private List<List<QueryResult>> results;

    public List<List<QueryResult>>  getResults()
    {
        return results;
    }
    public void setResults(List<List<QueryResult>>  results)
    {
        this.results = results;
    }

    @Override
    public String toString()
    {
        return "value=" + this.getResults() + super.toString(); 
    }
}
