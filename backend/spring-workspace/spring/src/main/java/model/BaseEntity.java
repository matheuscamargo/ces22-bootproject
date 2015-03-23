package model;

public class BaseEntity {
	protected long id;


	public long getId () {
		return id;
	}
	
	public void setId (long id) {
		this.id = id;
	}
	
	public boolean isNew () {
		return true;
	}
}
