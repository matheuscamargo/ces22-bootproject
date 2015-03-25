package model;

public class Comment {
	
	private String name;
	private long id;
	private long linkId;
	
	public Comment() {
		name = null;
		id = 99;
		linkId = 99;
	}
		
	@Override
	public String toString() { return String.format("Comment[id=%d linkId=%d name= %s]", id, linkId, name); }
	
	public String getName() { return name; }
	public long getId() { return id; }
	public long getLinkId() { return linkId; }
	
	public void setName(String name) { this.name = name; }
	public void setId(long id) { this.id = id; }
	public void setLinkId(long linkId) { this.linkId = linkId; }

}
