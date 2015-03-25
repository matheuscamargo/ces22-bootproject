package db;

import java.util.List;

import org.springframework.dao.DataAccessException;

import model.Comment;
import model.Hyperlink;
import model.MetaTag;

public interface CommentDAO {
	public void save (Comment comment) throws DataAccessException;
	public void update (Comment comment) throws DataAccessException;
	public void deleteById (long id) throws DataAccessException;
	public void deleteByHyperlinkId (long id) throws DataAccessException;
 	public Comment getById(long id) throws DataAccessException;
 	public List<Comment> getByHyperLinkId(long hyperLinkId) throws DataAccessException;
}