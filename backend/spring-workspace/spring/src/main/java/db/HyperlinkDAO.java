package db;

import java.util.List;

import org.springframework.dao.DataAccessException;

import model.Hyperlink;
import model.MetaTag;
import model.Comment;

//CRUD operations
public interface HyperlinkDAO {
	public long save (Hyperlink hyperlink) throws DataAccessException;
	public void update (Hyperlink hyperlink) throws DataAccessException;
	public boolean deleteById (long id) throws DataAccessException;
	
	public long countHyperlinks() throws DataAccessException;
	
	public Hyperlink getById(long idz) throws DataAccessException;
	public List<Hyperlink> getAllWithTag(MetaTag tag) throws DataAccessException;
	public List<Hyperlink> getAllWithLink(String link) throws DataAccessException;
	public List<Hyperlink> getAll(String order) throws DataAccessException;
}