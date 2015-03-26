package model;

<<<<<<< HEAD
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
		return String.format("Tag[id=%d linkId=%d tag= %s]", id, hyperlinkId, tag);
	}
	
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
=======
public class MetaTag {
	
	private String name;
	private long id;
	private long linkId;
	
	public MetaTag() {
		name = null;
		id = 99;
		linkId = 99;
	}
		
	@Override
	public String toString() { return String.format("Tag[id=%d linkId=%d name= %s]", id, linkId, name); }
	
	public String getName() { return name; }
	public long getId() { return id; }
	public long getLinkId() { return linkId; }
	
	public void setName(String name) { this.name = name; }
	public void setId(long id) { this.id = id; }
	public void setLinkId(long linkId) { this.linkId = linkId; }

>>>>>>> 85a705d256f392954a635a4cbb1b62ba363e81e7
}
