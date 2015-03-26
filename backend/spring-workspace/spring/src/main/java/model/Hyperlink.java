package model;


import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

//model class for describing a Hyperlink
public class Hyperlink extends BaseEntity {
	
	private long id;
	private String link;
<<<<<<< HEAD

	private Date createdAt, lastEditedAt;
	private List<Comment> comments;
	private List<MetaTag> metaTags;
	
	public Hyperlink() {
		id = 98;
		link = null;
		metaTags = null;
		createdAt = new Date();
		lastEditedAt = new Date();
	}
	
	public Hyperlink(String link) {
		this.link = link;
	}
	
	public Hyperlink(@JsonProperty("id") long id,
					 @JsonProperty("link") String link) {
		this.id = id;
		this.link = link;
=======
	private List<String> tags;
	private List<String> comments;
	private Date addedAt, lastEditedAt;
	
	public Hyperlink(@JsonProperty("id") long id,
					 @JsonProperty("link") String link,
					 @JsonProperty("tags") List<String> tags,
					 @JsonProperty("tags") List<String> comments) {
		this.id = id;
		this.link = link;
		this.tags = tags;
		this.comments = comments;
	}
	
	public Hyperlink() {
		id = 98;
		link = null;
		tags = new LinkedList<String>();;
		comments = new LinkedList<String>();
		addedAt = new Date();
		lastEditedAt = new Date();
>>>>>>> 85a705d256f392954a635a4cbb1b62ba363e81e7
	}
	
	@Override
	public String toString() {
		return String.format("Hyperlink[id=%d, link=%s]", id, link);
<<<<<<< HEAD
	}
	 
	public void setLink(String link) 			   	{ this.link = link; } 
	public void setLastEditedAt(Date lastEditedAt) 	{ this.lastEditedAt = lastEditedAt; }
	public void setCreatedAt (Date createdAt) 		{ this.createdAt = createdAt; }
	public void setComments(List<Comment> comments) { this.comments = comments; }
	public void setMetaTags(List<MetaTag> metaTags) { this.metaTags = metaTags; }
	
	public void addTag(MetaTag tag) { metaTags.add(tag); }
=======
	}
	
	public void addTag(String tag) {
		tags.add(tag);
	}
	
	public void addComment(String comment) {
		comments.add(comment);
	}

	public void setId(long id) 					   { this.id = id; }	 
	public void setLink(String link) 			   { this.link = link; } 
	public void setTags(List<String> tags) 		   { this.tags = tags; } 
	public void setComments(List<String> comments) { this.comments = comments; } 
	public void setLastEditedAt(Date lastEditedAt) { this.lastEditedAt = lastEditedAt; }
	public void setAddedAt(Date addedAt) 		   { this.addedAt = addedAt; }
	

	public long 		getId() 		  { return id; }
	public String 		getLink() 		  { return link; }
	public List<String> getTags() 		  { return tags; }
	public List<String> getComments() 	  { return comments; }
	public Date 		getLastEditedAt() { return lastEditedAt; }
	public Date 		getAddedAt() 	  { return addedAt; }
>>>>>>> 85a705d256f392954a635a4cbb1b62ba363e81e7
	
	public String getLink() 		 	{ return link; }
	public Date getLastEditedAt() 		{ return lastEditedAt; }
	public Date getAddedAt() 	 	 	{ return createdAt; }
	public List<Comment> getComments() 	{ return comments; }
	public List<MetaTag> getMetaTags() 	{ return metaTags; }
}
