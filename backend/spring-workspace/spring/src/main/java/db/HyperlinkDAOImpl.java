package db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
    	String query = "insert into Hyperlink (link) value (link)";
    	
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("link", hyperlink.getLink());
    	
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
		String query = "update Hyperlink set link=:link where id=:id";
       
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("link", hyperlink.getLink());
    	params.put("id", hyperlink.getId());
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
        int out = namedParameterJdbcTemplate.update(query, params);
        
        metaTagDAO.deleteByHyperlinkId(id);
        commentDAO.deleteByHyperlinkId(id);
        
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
	public List<Hyperlink> getAllWithTag(MetaTag tag) {
		return new ArrayList<Hyperlink>(); //dummy
	}
	
	public List<Hyperlink> getAllWithLink(String link) {
		return new ArrayList<Hyperlink>(); //dummy
	}
	
	public List<Hyperlink> getAll() {
//        String query = "select * from  Hyperlink h"
//        		+ " inner join MetaTag mt"
//        		+ " on h.id = mt.hyperlinkId"
//        		+ " inner join Comment c"
//        		+ " on h.id = c.hyperlinkId";
		String query = "select * from Hyperlink";
		
		List<Hyperlink> hypList = (List<Hyperlink>) namedParameterJdbcTemplate
									.query(query, new HyperlinkMapper());
		
		for (Hyperlink hyp : hypList) {
			 hyp.setComments(commentDAO.getByHyperLinkId(hyp.getId()));
			 hyp.setMetaTags(metaTagDAO.getByHyperLinkId(hyp.getId()));
		}
		
		return hypList;
	}
}


class HyperlinkMapper implements RowMapper {    
	 public Hyperlink mapRow(ResultSet rs, int rowNum) throws SQLException {    
	  Hyperlink hyperlink = new Hyperlink();    
	  hyperlink.setId(rs.getInt("id"));    
	  hyperlink.setLink(rs.getString("link"));      
	  return hyperlink;    
	 }
}    