package model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

//model class for describing a Hyperlink
public class Hyperlink {
	
	private long id;
	private String link;
	private List<String> tags;
	private Date addedAt, lastEditedAt;
	
	public Hyperlink(@JsonProperty("id") long id,
					 @JsonProperty("link") String link,
					 @JsonProperty("tags") List<String> tags) {
		this.id = id;
		this.link = link;
		this.tags = tags;
	}
	
	public Hyperlink() {
		id = 98;
		link = null;
		tags = null;
		addedAt = new Date();
		lastEditedAt = new Date();
	}
	
	@Override
	public String toString() {
		return String.format("Hyperlink[id=%d, link=%s", id, link);
	}
	
	public void addTag(String tag) {
		tags.add(tag);
	}

	public void setId(long id) 					   { this.id = id; }	 
	public void setLink(String link) 			   { this.link = link; } 
	public void setTags(List<String> tags) 		   { this.tags = tags; } 
	public void setLastEditedAt(Date lastEditedAt) { this.lastEditedAt = lastEditedAt; }
	public void setAddedAt(Date addedAt) 		   { this.addedAt = addedAt; }
	

	public long 		getId() 		  { return id; }
	public String 		getLink() 		  { return link; }
	public List<String> getTags() 		  { return tags; }
	public Date 		getLastEditedAt() { return lastEditedAt; }
	public Date 		getAddedAt() 	  { return addedAt; }
	
}
