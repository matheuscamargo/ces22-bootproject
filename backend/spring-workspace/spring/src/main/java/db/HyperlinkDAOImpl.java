package db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import utils.DataBaseIsFullException;

import java.sql.ResultSet;  

import model.Hyperlink;
import model.MetaTag;
import model.Comment;

//class that deals with database access to Hyperlink table
public class HyperlinkDAOImpl implements HyperlinkDAO{
	static final int MAX_HYPERLINKS = 20;
	
	//references to others tables
	private CommentDAO commentDAO;
	private MetaTagDAO metaTagDAO;
	private SimpleJdbcInsert insertHyp;  
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	public HyperlinkDAOImpl (DataSource dataSource, CommentDAO commentDAO,
							MetaTagDAO metaTagDAO) {
		
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	
		this.insertHyp =   
                new SimpleJdbcInsert(dataSource)  
                 .withTableName("Hyperlink")  
                 .usingGeneratedKeyColumns("id");
		
		this.commentDAO = commentDAO;
		this.metaTagDAO = metaTagDAO;
	}
	
    @Override
	public long save (Hyperlink hyperlink) throws DataAccessException {
    	long numberOfHyperlinks = countHyperlinks();

		//security - limit number of hyperlinks in database
		if (numberOfHyperlinks >= MAX_HYPERLINKS) {
			throw new DataBaseIsFullException("Too many hyperlinks in db");
		}
		
    	//hyperlinkId
    	Number hypId = insertHyp.executeAndReturnKey(
    			createHyperlinkParameterSource(hyperlink));
    		
		//sucessfully executed query
		System.out.println("Saved hyperlink");
		
	    //save comments
	    for (Comment comment: hyperlink.getComments()) {
	    	comment.setHyperlinkId(hypId.longValue());
	    	commentDAO.save(comment);
	   	}
	    	
	   	//save metatags
	    for (MetaTag metaTag: hyperlink.getMetaTags()) {
	    	metaTag.setHyperlinkId(hypId.longValue());
	    	metaTagDAO.save(metaTag);
    	}

		return (Long) hypId;
	}
  
    @Override
	public void update (Hyperlink hyperlink) throws DataAccessException {
		String query = "UPDATE Hyperlink SET link=:link, lastEdited=:lastEdited"
				+ " WHERE id=:id";
         
		int out = namedParameterJdbcTemplate.update(query,
				createHyperlinkParameterSource(hyperlink));
		
		//update Comment
		commentDAO.deleteByHyperlinkId(hyperlink.getId());
		for (Comment comment : hyperlink.getComments()) {
			comment.setHyperlinkId(hyperlink.getId()); //hack
			commentDAO.save(comment);
		}
		
		//update Metatags
		metaTagDAO.deleteByHyperlinkId(hyperlink.getId());
		for (MetaTag metaTag : hyperlink.getMetaTags()) {
			metaTag.setHyperlinkId(hyperlink.getId()); //hack
			metaTagDAO.save(metaTag);
		}
	}
	
	private MapSqlParameterSource createHyperlinkParameterSource(Hyperlink hyperlink) {
		return new MapSqlParameterSource()
				.addValue("id", hyperlink.getId())
				.addValue("link", hyperlink.getLink())
				.addValue("created", new Date())
				.addValue("lastEdited", new Date());
	}
	
	@Override
	public boolean deleteById (long id) throws DataAccessException {
        String query = "DELETE FROM Hyperlink WHERE id=:id";
         
        Map<String, Object> params = new HashMap<String, Object>();
    	params.put("id", id);
        
        metaTagDAO.deleteByHyperlinkId(id);
        commentDAO.deleteByHyperlinkId(id);
        
        int out = namedParameterJdbcTemplate.update(query, params);
        
        if(out !=0){
        	//Delete succedded
        	return true;
        }
        else {
        	return false;
        }
	}
	
	@Override
	public long countHyperlinks() throws DataAccessException {
		String query = "SELECT COUNT(*) AS count FROM Hyperlink";
		long numberOfHyperlinks;
		
		Map<String, Object> rs = namedParameterJdbcTemplate.queryForMap(query,
				new HashMap<String, Object>());
		
		numberOfHyperlinks =  (Long)rs.get("count");
		
		return numberOfHyperlinks;
	}
	
	@Override
	public Hyperlink getById(long id) throws DataAccessException {
		 Hyperlink hyp;
		 
		 try {
		 String query = "SELECT * FROM Hyperlink WHERE id = :id";
		 Map<String, Object> params = new HashMap<String, Object>();
		 params.put("id", id);
		 
		 hyp = (Hyperlink) namedParameterJdbcTemplate
				 .queryForObject(query, params, new HyperlinkMapper());
		 
		 }
		 //user not found
		 catch (EmptyResultDataAccessException ex) {
			 //add to logger
			 throw ex;
		 }
		 //inserting comments and metatags
		 hyp.setComments(commentDAO.getByHyperLinkId(id));
		 hyp.setMetaTags(metaTagDAO.getByHyperLinkId(id));
		 
		 return hyp;
	}
	
	@Override
	public List<Hyperlink> getAllWithTag(MetaTag mtag) throws DataAccessException {
		 String query = "SELECT h.id, h.link, h.created, h.lastEdited,"
	        		+ " 0 AS type, mt.tag AS field, mt.id AS cid FROM "
	        		+ " (SELECT h.id, h.link, h.created, h.lastEdited FROM Hyperlink h"
	        		+ " LEFT JOIN MetaTag mt ON h.id = mt.hyperlinkId WHERE mt.tag LIKE :tag) h"
	        		+ " LEFT JOIN MetaTag mt ON h.id = mt.hyperlinkId"
	        		+ " UNION"
	        		+ " SELECT h.id, h.link, h.created, h.lastEdited,"
	        		+ " 1 AS type, c.comment AS field, c.id AS cid FROM"
	        		+ " (SELECT h.id, h.link, h.created, h.lastEdited FROM Hyperlink h"
	        		+ " LEFT JOIN MetaTag mt ON h.id = mt.hyperlinkId WHERE mt.tag LIKE :tag) h"
	        		+ " LEFT JOIN Comment c ON h.id = c.hyperlinkId";
	        		 
	        List<Hyperlink> hypList;
	        
	        Map<String, Object> params = new HashMap<String, Object>();
	      //matches all patterns with link
	    	params.put("tag", "%" + mtag.getTag() + "%");
	        
	        List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(query, params);
	        
	        hypList = extractData(rows);	
			return hypList;
	}
	
	@Override
	public List<Hyperlink> getAllWithLink(String link) throws DataAccessException {
		 String query = "SELECT h.id, h.link, h.created, h.lastEdited,"
	        		+ " 0 AS type, mt.tag AS field, mt.id AS cid  FROM  Hyperlink h"
	        		+ " LEFT JOIN MetaTag mt ON h.id = mt.hyperlinkId WHERE h.link LIKE :link"
	        		+ " UNION"
	        		+ " SELECT h.id, h.link, h.created, h.lastEdited,"
	        		+ " 1 AS type, c.comment AS field, c.id AS cid FROM  Hyperlink h"
	        		+ " LEFT JOIN Comment c ON h.id = c.hyperlinkId WHERE h.link LIKE :link";
		 
	        List<Hyperlink> hypList;
	        
	        Map<String, Object> params = new HashMap<String, Object>();
	        //matches all patterns with link
	    	params.put("link", "%" + link + "%");

	        List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(query, params);
	        
	        hypList = extractData(rows);
			return hypList;
	}
	
	@Override
	public List<Hyperlink> getAll() throws DataAccessException {
        String query = "SELECT h.id, h.link, h.created, h.lastEdited,"
        		+ " 0 AS type, mt.tag AS field, mt.id AS cid  FROM  Hyperlink h"
        		+ " LEFT JOIN MetaTag mt ON h.id = mt.hyperlinkId"
        		+ " UNION"
        		+ " SELECT h.id, h.link, h.created, h.lastEdited,"
        		+ " 1 AS type, c.comment as field, c.id as cid FROM  Hyperlink h"
        		+ " LEFT JOIN Comment c ON h.id = c.hyperlinkId";
        		 
        List<Hyperlink> hypList;
 
        List<Map<String, Object>> rows = namedParameterJdbcTemplate
        			.queryForList(query, new HashMap<String, Object>());
        
        hypList = extractData(rows);
         			
		return hypList;
	}
	
	//helper method for extracting data
	List<Hyperlink> extractData (List<Map<String, Object>> results) {
		
		Map<Long, Hyperlink> hypMap = new HashMap<Long, Hyperlink>();
		
		for (Map<String,Object> rs : results) {
			//New hyperlink
			Hyperlink hyp;
			long id = new Long(((Integer)rs.get("id")).intValue()); //strange cast
			System.out.println(id);
			if (!hypMap.containsKey(id)) {
				hyp = new Hyperlink(id, (String)rs.get("link"));
				hyp.setCreatedAt((Date)rs.get("created"));
				hyp.setLastEditedAt((Date)rs.get("lastEdited"));
				hyp.setMetaTags(new ArrayList<MetaTag>());
				hyp.setComments(new ArrayList<Comment>());
			}
			else hyp = hypMap.get(id);
			
			//MetaTag row
			if (rs.get("field") != null && ((Long)rs.get("type")).intValue() == 0) {
				MetaTag mt = new MetaTag((Integer)rs.get("cid"),
										(String)rs.get("field"),
										 hyp.getId());
				hyp.getMetaTags().add(mt);
			}
			//Comments row
			else if (rs.get("field") != null){
				Comment com = new Comment((Integer)rs.get("cid"),
										(String)rs.get("field"),
											hyp.getId());
				hyp.getComments().add(com);
			}

			hypMap.put(hyp.getId(), hyp);
		}
		return new ArrayList<Hyperlink>(hypMap.values());	
	}
}

class HyperlinkMapper implements RowMapper {    
	 public Hyperlink mapRow(ResultSet rs, int rowNum) throws SQLException {    
	  Hyperlink hyperlink = new Hyperlink();    
	  hyperlink.setId(rs.getInt("id"));    
	  hyperlink.setLink(rs.getString("link"));
	  hyperlink.setCreatedAt(rs.getDate("created"));
	  hyperlink.setLastEditedAt(rs.getDate("lastEdited"));
	  return hyperlink;    
	 }
}    