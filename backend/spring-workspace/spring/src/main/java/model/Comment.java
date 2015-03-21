package model;

public class Comment extends BaseEntity{
	private String comment;
	
	void setComment (String comment) {
		this.comment = comment;
	}
	
	String getComment () {
		return comment;
	}
}
