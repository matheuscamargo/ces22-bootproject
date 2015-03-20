package model;


import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

//model class for describing a Hyperlink
public class Hyperlink extends BaseEntity {
	
	private long id;
	private String link;
	private Date createdAt, lastEditedAt;
	private List<Comment> comments;
	private List<MetaTag> metaTags;
	
	public Hyperlink() {}
	
	public Hyperlink(String link) {
		this.link = link;
	}
	
	@JsonCreator
	public Hyperlink(@JsonProperty("id") long id,
					 @JsonProperty("link") String link) {
		this.id = id;
		this.link = link;
	}
	
	@Override
	public String toString() {
		return String.format("Hyperlink[id=%d, link=%s]", id, link);
	}
	 
	public void setLink(String link) 			   	{ this.link = link; } 
	public void setLastEditedAt(Date lastEditedAt) 	{ this.lastEditedAt = lastEditedAt; }
	public void setCreatedAt (Date createdAt) 		{ this.createdAt = createdAt; }
	public void setComments(List<Comment> comments) { this.comments = comments; }
	public void setMetaTags(List<MetaTag> metaTags) { this.metaTags = metaTags; }

	
	public String getLink() 		 	{ return link; }
	public Date getLastEditedAt() 		{ return lastEditedAt; }
	public Date getAddedAt() 	 	 	{ return createdAt; }
	public List<Comment> getComments() 	{ return comments; }
	public List<MetaTag> getMetaTags() 	{ return metaTags; }
}
