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
	
	static final int MAX_COMMENTS = 20;
	static final int MAX_METATAGS = 20;
	static final int MAX_HYPERLINKS = 100;
	
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
	public void deleteById (long id) throws DataAccessException {
		hyperlinkDAO.deleteById(id);
	}
	
	@Override
	public boolean addMetaTag (MetaTag tag) throws DataAccessException {
		int numberOfMetaTags = metaTagDAO.countMetaTagsByHyperlinkId
							  (tag.getHyperlinkId());
		
		//security - limit number of metatags per hyperlink
		if (numberOfMetaTags < MAX_METATAGS) {
			metaTagDAO.save(tag);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean deleteMetaTag (MetaTag tag) throws DataAccessException {
		return metaTagDAO.deleteById(tag.getId());
	}
	
	@Override
	public boolean addComment (Comment comment) throws DataAccessException {
		int numberOfComments = metaTagDAO.countMetaTagsByHyperlinkId
				  (comment.getHyperlinkId());

		//security - limit number of metatags per hyperlink
		if (numberOfComments < MAX_COMMENTS) {
			commentDAO.save(comment);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean editComment (Comment comment) throws DataAccessException {
		
		return commentDAO.update(comment);	
	}
	
	@Override
	public boolean deleteComment (Comment comment) throws DataAccessException {
		return commentDAO.deleteById(comment.getId());
	}
	
	@Override
	public Hyperlink getById(long id) throws DataAccessException {
		return hyperlinkDAO.getById(id);
	}
	
	@Override
	public List<Hyperlink> getAllWithTag(MetaTag tag) throws DataAccessException {
		return hyperlinkDAO.getAllWithTag(tag);
	}
	
	@Override
	public List<Hyperlink> getAllWithLink(String link) throws DataAccessException {
		return hyperlinkDAO.getAllWithLink(link);
	}
	
	@Override
	public List<Hyperlink> getAll() throws DataAccessException {
		return hyperlinkDAO.getAll();
	}
}
