package service;

import java.util.List;

import model.Comment;
import model.Hyperlink;
import model.MetaTag;

import org.springframework.dao.DataAccessException;

//Abstraction for all database objects
public interface HyperlinkService {
	public long save (Hyperlink hyperlink) throws DataAccessException;
	public void update (Hyperlink hyperlink) throws DataAccessException;
	public boolean deleteById (long id) throws DataAccessException;
	
	public boolean addMetaTag (MetaTag tag) throws DataAccessException;
	public MetaTag getMetaTagById (long id) throws DataAccessException;
	public boolean deleteMetaTag (long id) throws DataAccessException;
	
	public boolean addComment (Comment comment) throws DataAccessException;
	public Comment getCommentById (long id) throws DataAccessException;
	public boolean editComment (Comment comment) throws DataAccessException;
	public boolean deleteComment (long id) throws DataAccessException;
	
	public Hyperlink getById(long id) throws DataAccessException;
	public List<Hyperlink> getAllWithTag(MetaTag tag) throws DataAccessException;
	public List<Hyperlink> getAllWithLink(String link) throws DataAccessException;
	public List<Hyperlink> getAll() throws DataAccessException;
}