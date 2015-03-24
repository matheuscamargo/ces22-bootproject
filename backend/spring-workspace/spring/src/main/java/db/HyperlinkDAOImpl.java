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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.ResultSet;  

import model.Hyperlink;
import model.MetaTag;
import model.Comment;

//class that deals with database access to Hyperlink table
public class HyperlinkDAOImpl implements HyperlinkDAO{
	
	//references to others tables
	private CommentDAO commentDAO;
	private MetaTagDAO metaTagDAO;
	private SimpleJdbcInsert insertHyp;  
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	public HyperlinkDAOImpl (DataSource dataSource, CommentDAO commentDAO,
							MetaTagDAO metaTagDAO) {
		
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		
		this.commentDAO = commentDAO;
		this.metaTagDAO = metaTagDAO;
		this.insertHyp =   
                new SimpleJdbcInsert(dataSource)  
                 .withTableName("Hyperlink")  
                 .usingGeneratedKeyColumns("id");  
	}
	
    @Override
	public void save (Hyperlink hyperlink) {
    	String query = "insert into Hyperlink (link, created, lastEdited)"
    			+ " values (:link, :created, :lastEdited)";
    	
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("link", hyperlink.getLink());
    	params.put("created", hyperlink.getAddedAt());
    	params.put("lastEdited", hyperlink.getLastEditedAt());
    	
    	//hyperlinkId
    	Number hypId = insertHyp.executeAndReturnKey(params);
    		
		//sucessfully executed query
		System.out.println("Saved hyperlink");
		
	    //save comments
		if (hyperlink.getComments().size() > 0) {
		    for (Comment comment: hyperlink.getComments()) {
		    	comment.setHyperlinkId(hypId.longValue());
		    	commentDAO.save(comment);
		   	}
		}
	    	
	   	//save metatags
		if (hyperlink.getMetaTags().size() > 0) {
		    for (MetaTag metaTag: hyperlink.getMetaTags()) {
		    	metaTag.setHyperlinkId(hypId.longValue());
		    	metaTagDAO.save(metaTag);
	    	}
		}
	    
//		else 	{
//			System.out.println("Unable to Save hyperlink");
//		}
		
		//TODO
		//else {
	//		System.out.println("Already inside the db");
	//	}
	}
  
	public void update (Hyperlink hyperlink) {
		String query = "update Hyperlink set link=:link, lastEdited=:lastEdited"
				+ " where id=:id";
       
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("link", hyperlink.getLink());
    	params.put("id", hyperlink.getId());
    	params.put("lastEdited", new Date());
    	//params.put("addedAt", hyperlink.getAddedAt());
         
		int out = namedParameterJdbcTemplate.update(query, params);
		
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

		if(out !=0) System.out.println("Saved hyperlink");
		else 		System.out.println("Unable to save hyperlink");
	}
	
	public void deleteById (long id) {
        String query = "delete from Hyperlink where id=:id";
         
        Map<String, Object> params = new HashMap<String, Object>();
    	params.put("id", id);
        
        metaTagDAO.deleteByHyperlinkId(id);
        commentDAO.deleteByHyperlinkId(id);
        
        int out = namedParameterJdbcTemplate.update(query, params);
        
        if(out !=0){
            System.out.println("Employee deleted with id="+id);
        }else System.out.println("No Employee found with id="+id);
	}
	
	public Hyperlink getById(long id) {
		 String query = "select * from Hyperlink where id = :id";
		 Map<String, Object> params = new HashMap<String, Object>();
		 params.put("id", id);
		 
		 Hyperlink hyp = (Hyperlink) namedParameterJdbcTemplate
				 .queryForObject(query, params, new HyperlinkMapper());
		 
		 //inserting comments and metatags
		 hyp.setComments(commentDAO.getByHyperLinkId(id));
		 hyp.setMetaTags(metaTagDAO.getByHyperLinkId(id));
		 
		 return hyp;
	}
	public List<Hyperlink> getAllWithTag(MetaTag mtag) {
		 String query = "select h.id, h.link, h.created, h.lastEdited,"
	        		+ " 0 as type, mt.tag as field, mt.id as cid from "
	        		+ " (select h.id, h.link, h.created, h.lastEdited from Hyperlink h"
	        		+ " inner join MetaTag mt on h.id = mt.hyperlinkId where mt.tag=:tag) h"
	        		+ " inner join MetaTag mt on h.id = mt.hyperlinkId"
	        		+ " UNION"
	        		+ " select h.id, h.link, h.created, h.lastEdited,"
	        		+ " 1 as type, c.comment as field, c.id as cid from"
	        		+ " (select h.id, h.link, h.created, h.lastEdited from Hyperlink h"
	        		+ " inner join MetaTag mt on h.id = mt.hyperlinkId where mt.tag=:tag) h"
	        		+ " inner join Comment c on h.id = c.hyperlinkId";
	        		 
	        List<Hyperlink> hypList;
	        
	        Map<String, Object> params = new HashMap<String, Object>();
	    	params.put("tag", mtag.getTag());
	        
	        List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(query, params);
	        
	        hypList = extractData(rows);
	         			
			return hypList;
	}
	
	public List<Hyperlink> getAllWithLink(String link) {
		 String query = "select h.id, h.link, h.created, h.lastEdited,"
	        		+ " 0 as type, mt.tag as field, mt.id as cid  from  Hyperlink h"
	        		+ " inner join MetaTag mt on h.id = mt.hyperlinkId where h.link=:link"
	        		+ " UNION"
	        		+ " select h.id, h.link, h.created, h.lastEdited,"
	        		+ " 1 as type, c.comment as field, c.id as cid from  Hyperlink h"
	        		+ " inner join Comment c on h.id = c.hyperlinkId where h.link=:link";

	        List<Hyperlink> hypList;
	        
	        Map<String, Object> params = new HashMap<String, Object>();
	    	params.put("link", link);

	        List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(query, params);
	        
	        hypList = extractData(rows);
	         			
			return hypList;
	}
	
	public List<Hyperlink> getAll() {
        String query = "select h.id, h.link, h.created, h.lastEdited,"
        		+ " 0 as type, mt.tag as field, mt.id as cid  from  Hyperlink h"
        		+ " inner join MetaTag mt on h.id = mt.hyperlinkId"
        		+ " UNION"
        		+ " select h.id, h.link, h.created, h.lastEdited,"
        		+ " 1 as type, c.comment as field, c.id as cid from  Hyperlink h"
        		+ " inner join Comment c on h.id = c.hyperlinkId";
        		 
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
			if (((Long)rs.get("type")).intValue() == 0) {
				MetaTag mt = new MetaTag((Integer)rs.get("cid"),
										(String)rs.get("field"),
										 hyp.getId());
				hyp.getMetaTags().add(mt);
			}
			//Comments row
			else {
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