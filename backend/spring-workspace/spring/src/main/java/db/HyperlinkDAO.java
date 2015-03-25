package db;

import java.util.List;

import org.springframework.dao.DataAccessException;

import model.Hyperlink;
import model.MetaTag;
import model.Comment;

//CRUD operations
public interface HyperlinkDAO {
	public void save (Hyperlink hyperlink) throws DataAccessException;
	public void update (Hyperlink hyperlink) throws DataAccessException;
	public void deleteById (long id) throws DataAccessException;
	
	public Hyperlink getById(long id) throws DataAccessException;
	public List<Hyperlink> getAllWithTag(MetaTag tag) throws DataAccessException;
	public List<Hyperlink> getAllWithLink(String link) throws DataAccessException;
	public List<Hyperlink> getAll() throws DataAccessException;
}