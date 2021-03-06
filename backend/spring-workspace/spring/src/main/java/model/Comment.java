package model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Comment extends BaseEntity {
	private String comment;
	private long hyperlinkId;
	
	public Comment () {}
	
	public Comment (@JsonProperty("comment") String comment) {
		this.comment = comment;
	}
	
	public Comment (long id, String comment, long hyperlinkId) {
		this.id = id;
		this.comment = comment;
		this.hyperlinkId = hyperlinkId;
	}
	
	public void setComment (String comment) {
		this.comment = comment;
	}
	public void setHyperlinkId (long hyperlinkId) {
		this.hyperlinkId = hyperlinkId;
	}
	
	public String getComment () {
		return comment;
	}
	
	public long getHyperlinkId() {
		return hyperlinkId;
	}
}
