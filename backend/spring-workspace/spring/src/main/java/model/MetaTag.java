package model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MetaTag extends BaseEntity{
	
	private String tag;
	private long hyperlinkId;
	
	public MetaTag () {
		tag = null;
		id = 99;
		hyperlinkId = 99;
	}
		
	@Override
	public String toString() { 
		return String.format("Tag[id=%d hyperlinklinkId=%d tag=%s]", id, hyperlinkId, tag);
	}
	
	public MetaTag (@JsonProperty("tag") String tag) {
		this.tag = tag; 
	}
	public MetaTag (long id, String tag, long hyperlinkId) {
		this.id = id;
		this.tag = tag;
		this.setHyperlinkId(hyperlinkId);
	}
	
	public void setTag (String tag) {
		this.tag = tag;
	}
	
	public String getTag () {
		return this.tag;
	}
	
	public long getHyperlinkId () {
		return hyperlinkId;
	}
	public void setHyperlinkId (long hyperlinkId) {
		this.hyperlinkId = hyperlinkId;
	}
}
