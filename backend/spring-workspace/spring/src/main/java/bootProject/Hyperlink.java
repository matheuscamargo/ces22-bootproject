package bootProject;

import java.util.Date;

public class Hyperlink {
	private String link;
	private String[] tags;
	private Date addedAt, lastEditedAt;
	
	public Hyperlink(String link, Date addedAt, String[] tags) {
		this.link = link;
		this.addedAt = addedAt;
		this.lastEditedAt = addedAt;
		this.tags = tags;
	}	

	public void setLink(String link) 			   { this.link = link; } 
	public void setTags(String[] tags) 			   { this.tags = tags; } 
	public void setLastEditedAt(Date lastEditedAt) { this.lastEditedAt = lastEditedAt; }
	
	public String getLink() 		 { return link; }
	public String[] getTags() 		 { return tags; }
	public Date getLastEditedAt() 	 { return lastEditedAt; }
	public Date getAddedAt() 	 	 { return addedAt; }
	
}
