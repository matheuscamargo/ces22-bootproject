package service;

import java.util.List;

import model.Comment;
import model.Hyperlink;
import model.MetaTag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import db.CommentDAO;
import db.HyperlinkDAO;
import db.MetaTagDAO;

public class HyperlinkServiceImpl implements HyperlinkService {
	
	//references to others tables
	private CommentDAO commentDAO;
	private MetaTagDAO metaTagDAO;
	private HyperlinkDAO hyperlinkDAO;
	
	@Autowired
	public HyperlinkServiceImpl (CommentDAO commentDAO, MetaTagDAO metaTagDAO, HyperlinkDAO hyperlinkDAO) {
		this.commentDAO = commentDAO;
		this.metaTagDAO = metaTagDAO;
		this.hyperlinkDAO = hyperlinkDAO;
	}
	
	@Override
	public long save (Hyperlink hyperlink) throws DataAccessException {
			return hyperlinkDAO.save(hyperlink);
	}
	
	@Override
	public void update (Hyperlink hyperlink) throws DataAccessException {
		hyperlinkDAO.update(hyperlink);
	}
	
	@Override
	public boolean deleteById (long id) throws DataAccessException {
		return hyperlinkDAO.deleteById(id);
	}
	
	@Override
	public void addMetaTag (MetaTag tag) throws DataAccessException {
			metaTagDAO.save(tag);
	}
	
	@Override
	public MetaTag getMetaTagById (long id) throws DataAccessException {
		return metaTagDAO.getById(id);
	}
	
	@Override
	public boolean deleteMetaTag (long id) throws DataAccessException {
		return metaTagDAO.deleteById(id);
	}
	
	@Override
	public void addComment (Comment comment) throws DataAccessException {
		commentDAO.save(comment);
	}
	
	@Override
	public Comment getCommentById (long id) throws DataAccessException {
		return commentDAO.getById(id);
	}
	
	@Override
	public boolean editComment (Comment comment) throws DataAccessException {
		
		return commentDAO.update(comment);	
	}
	
	@Override
	public boolean deleteComment (long id) throws DataAccessException {
		return commentDAO.deleteById(id);
	}
	
	@Override
	public Hyperlink getById(long id) throws DataAccessException {
		return hyperlinkDAO.getById(id);
	}
	
	@Override
	public List<Hyperlink> getAllWithTag(String tag, String order) throws DataAccessException {
		return hyperlinkDAO.getAllWithTag(tag, order);
	}
	
	@Override
	public List<Hyperlink> getAllWithLink(String link, String order) throws DataAccessException {
		return hyperlinkDAO.getAllWithLink(link, order);
	}
	
	@Override
	public List<Hyperlink> getAll(String order) throws DataAccessException {
		return hyperlinkDAO.getAll(order);
	}
}
