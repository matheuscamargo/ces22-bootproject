package utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QueryString {
	String query;
	
	QueryString() {}
	QueryString(@JsonProperty("query") String query) {
		this.query = query;
	}
	
	public String getQuery() {
		return query;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}
}
