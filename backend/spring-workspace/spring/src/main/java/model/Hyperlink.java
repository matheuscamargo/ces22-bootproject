package model;


import java.util.Date;
import java.util.List;

//model class for describing a Hyperlink
public class Hyperlink extends BaseEntity {

	private String link;
	private String[] tags;
	private Date addedAt, lastEditedAt;
	private List<Comment> comments;
	private List<MetaTag> metaTags;
	
	public Hyperlink() {}
	
	public Hyperlink(String link, Date addedAt, String[] tags) {
		this.link = link;
		this.addedAt = addedAt;
		this.lastEditedAt = addedAt;
		this.tags = tags;
	}
	
	public Hyperlink(long id, String link) {
		this.id = id;
		this.link = link;
	}
	
	@Override
	public String toString() {
		return String.format("Hyperlink[id=%d, link=%s]", id, link);
	}
	 
	public void setLink(String link) 			   	{ this.link = link; } 
	public void setTags(String[] tags) 			   	{ this.tags = tags; } 
	public void setLastEditedAt(Date lastEditedAt) 	{ this.lastEditedAt = lastEditedAt; }
	public void setComments(List<Comment> comments) { this.comments = comments; }
	public void setMetaTags(List<MetaTag> metaTags) { this.metaTags = metaTags; }
	
	public String getLink() 		 	{ return link; }
	public String[] getTags() 		 	{ return tags; }
	public Date getLastEditedAt() 		{ return lastEditedAt; }
	public Date getAddedAt() 	 	 	{ return addedAt; }
	public List<Comment> getComments() 	{ return comments; }
	public List<MetaTag> getMetaTags() 	{ return metaTags; }
}
