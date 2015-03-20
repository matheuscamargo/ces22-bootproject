package model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

//model class for describing a Hyperlink
public class Hyperlink {
	
	private long id;
	private String link;
	private String[] tags;
	private Date addedAt, lastEditedAt;
	
	@JsonCreator
	public Hyperlink(@JsonProperty("id") long id,
					 @JsonProperty("link") String link,
					 @JsonProperty("tags") String[] tags) {
		this.id = id;
		this.link = link;
		this.tags = tags;
	}
	
	@Override
	public String toString() {
		return String.format("Hyperlink[id=%d, link=%s", id, link);
	}

	public void setId(long id) 					   { this.id = id; }	 
	public void setLink(String link) 			   { this.link = link; } 
	public void setTags(String[] tags) 			   { this.tags = tags; } 
	public void setLastEditedAt(Date lastEditedAt) { this.lastEditedAt = lastEditedAt; }
	public void setAddedAt(Date addedAt) { this.addedAt = addedAt; }
	

	public long 	getId() 		  { return id; }
	public String 	getLink() 		  { return link; }
	public String[] getTags() 		  { return tags; }
	public Date 	getLastEditedAt() { return lastEditedAt; }
	public Date 	getAddedAt() 	  { return addedAt; }
	
}
