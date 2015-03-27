package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import utils.DataBaseIsFullException;
import model.Comment;

//class that deals with database access to Comment table
public class CommentDAOImpl implements CommentDAO {
	static final int MAX_COMMENTS = 20;
	
	private JdbcTemplate jdbcTemplate;
	
    @Autowired
    public CommentDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
    @Override
	public void save (Comment comment)  throws DataAccessException{
    	
		String query = "INSERT INTO Comment (comment, hyperlinkid) VALUES (?,?)";
	
        Object[] args = new Object[] {comment.getComment(),
        							  comment.getHyperlinkId()};
        
		int numberOfMetaTags = countCommentsByHyperlinkId(comment.getHyperlinkId());
		
		//security - limit number of metatags per hyperlink
		if (numberOfMetaTags >= MAX_COMMENTS) {
			throw new DataBaseIsFullException("Too many comments");
		}
		
        int out = jdbcTemplate.update(query, args);
         
        if(out !=0){
            System.out.println("Comment saved with id="+comment.getId());
        }else System.out.println("Comment saved failed with id="+comment.getId());
	}
	
    @Override
	public boolean update (Comment comment)  throws DataAccessException{
        String query = "UPDATE Comment SET comment=?, hyperlinkid=? WHERE id=?";

        Object[] args = new Object[] {comment.getComment(),
				  comment.getHyperlinkId(),
				  comment.getId()};
         
        int out = jdbcTemplate.update(query, args);
        if(out !=0){
        	//successfully updated
        	return true;
        }
        else {
        	//no elements with id
        	return false;
        }
    }
    
    @Override
	public boolean deleteById (long id)  throws DataAccessException{
        String query = "DELETE FROM Comment WHERE id=?";
         
        int out = jdbcTemplate.update(query, id);
        
        if (out != 0) {
        	//successfully deleted
        	return true;
        }
        
        else {
        	//no elements with id
        	return false;
        }
	}
    
    public void deleteByHyperlinkId (long hyperlinkId)  throws DataAccessException{
    	 String query = "delete FROM Comment WHERE hyperlinkId=?";
         
         int out = jdbcTemplate.update(query, hyperlinkId);
         if(out !=0){
             System.out.println("Comment deleted with hypid= " + hyperlinkId);
         }
         else 
         	System.out.println("No Comment found with hypid=" + hyperlinkId);
    }
    
    @Override
    public int countCommentsByHyperlinkId (long hypId) throws DataAccessException {
    	String query = "SELECT COUNT(*) AS count From Comment"
    				+ " WHERE hyperlinkId=:hyperlinkId";
    	long numberOfComments;
        
        Map<String,Object> rs = jdbcTemplate.queryForMap(query, new Object[] {hypId});
        numberOfComments = (Long)rs.get("count");
        
        return (int)numberOfComments;  
    }
	
    @SuppressWarnings("unchecked")
	@Override
	public Comment getById(long id) throws DataAccessException {
		 String query = "SELECT id, comment, hyperlinkid FROM Comment WHERE id = ?";
		 
		 Comment com = jdbcTemplate.queryForObject(query, new Object[]{id},
				 								   new CommentMapper());
		 
	   return com;
	}
	@Override
	public List<Comment> getByHyperLinkId(long hyperLinkId) throws DataAccessException {
		 String query = "SELECT id, comment, hyperlinkid FROM Comment WHERE hyperlinkid = ?";
		 //results
		 List<Comment> comList = new ArrayList<Comment>();
		 
		 List<Map<String,Object>> comRows = jdbcTemplate.queryForList(query, new Object[] {hyperLinkId});
		 
	     for(Map<String,Object> comRow : comRows){
	            Comment com = new Comment();
	            com.setId(Long.parseLong(String.valueOf(comRow.get("id"))));
	            com.setComment(String.valueOf(comRow.get("comment")));
	            com.setHyperlinkId(Long.parseLong(String.valueOf(comRow.get("hyperlinkid"))));
	            comList.add(com);
	        }
		 return comList;
	}
}

@SuppressWarnings("rawtypes")
class CommentMapper implements RowMapper {    
	 public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {    
	  Comment comment = new Comment();    
	  comment.setId(rs.getLong("id"));    
	  comment.setComment(rs.getString("comment"));
	  comment.setHyperlinkId(rs.getLong(("hyperlinkid")));
	  return comment;    
	 }

}   