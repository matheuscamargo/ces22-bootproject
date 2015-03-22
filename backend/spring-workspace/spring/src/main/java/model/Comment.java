package model;

public class Comment {
	private long id;
	private String comment;
	private long hyperlinkId;
	
	public Comment() {} 
	
	public Comment (long id, String comment, long hyperlinkId) {
		this.id = id;
		this.comment = comment;
		this.hyperlinkId = hyperlinkId;
	}
	
	public Comment (long id, long hyperlink_id) {
		this.id = id;
		this.hyperlinkId = hyperlink_id;
	}
	
	public void setId (long id) {
		this.id = id;
	}
	public void setComment (String comment) {
		this.comment = comment;
	}
	public void setHyperLinkId (long hyperlinkId) {
		this.hyperlinkId = hyperlinkId;
	}
	
	public String getComment () {
		return comment;
	}
	
	public long getHyperLinkId() {
		return hyperlinkId;
	}
	
	public long getId () {
		return id;
	}
}
