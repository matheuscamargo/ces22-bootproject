package db;

import java.util.List;

import model.Comment;
import model.Hyperlink;
import model.MetaTag;

public interface CommentDAO {
	public void save (Comment comment);
	public void update (Comment comment);
	public void deleteById (long id);
	
	public Comment getById(long id);
	public List<Comment> getAll();
}
