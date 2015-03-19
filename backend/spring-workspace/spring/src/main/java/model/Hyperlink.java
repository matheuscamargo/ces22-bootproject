package model;

import java.util.Date;

//model class for describing a Hyperlink
public class Hyperlink {
	
	private long id;
	private String link;
	private String[] tags;
	private Date addedAt, lastEditedAt;
	
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
		return String.format("Hyperlink[id=%d, link=%s", id, link);
	}

	public long getId() 			{ return id; }
	public void setId(long id) 	{ this.id = id; }
	 
	public void setLink(String link) 			   { this.link = link; } 
	public void setTags(String[] tags) 			   { this.tags = tags; } 
	public void setLastEditedAt(Date lastEditedAt) { this.lastEditedAt = lastEditedAt; }
	
	public String getLink() 		 { return link; }
	public String[] getTags() 		 { return tags; }
	public Date getLastEditedAt() 	 { return lastEditedAt; }
	public Date getAddedAt() 	 	 { return addedAt; }
	
}
