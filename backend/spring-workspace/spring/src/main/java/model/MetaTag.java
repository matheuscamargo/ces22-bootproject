package model;

public class MetaTag extends BaseEntity{
	
	private String tag;
	private long hyperlinkId;
	
	public MetaTag () {}
	
	public MetaTag (String tag) {
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
