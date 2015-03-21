package model;

public class MetaTag extends BaseEntity{
	
	private String tag;
	
	void setTag (String tag) {
		this.tag = tag;
	}
	
	String getTag () {
		return this.tag;
	}
}
