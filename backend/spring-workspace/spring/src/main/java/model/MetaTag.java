package model;

public class MetaTag {
	
	private String name;
	private long id;
	private long linkId;
	
	public MetaTag() {
		name = null;
		id = 99;
		linkId = 99;
	}
	
	public String getName() { return name; }
	public long getId() { return id; }
	public long getLinkId() { return linkId; }
	
	public void setName(String name) { this.name = name; }
	public void setId(long id) { this.id = id; }
	public void setLinkId(long linkId) { this.linkId = linkId; }

}
