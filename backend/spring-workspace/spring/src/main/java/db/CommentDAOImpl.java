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

import model.Comment;

//class that deals with database access to Comment table
public class CommentDAOImpl implements CommentDAO {
	private JdbcTemplate jdbcTemplate;
	
    @Autowired
    public CommentDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
    @Override
	public void save (Comment comment)  throws DataAccessException{
		String query = "insert into Comment (comment, hyperlinkid) values (?,?)";
	
        Object[] args = new Object[] {comment.getComment(),
        							  comment.getHyperlinkId()};
         
        int out = jdbcTemplate.update(query, args);
         
        if(out !=0){
            System.out.println("Comment saved with id="+comment.getId());
        }else System.out.println("Comment saved failed with id="+comment.getId());
	}
	
    @Override
	public void update (Comment comment)  throws DataAccessException{
        String query = "update Comment set comment=?, hyperlinkid=? where id=?";

        Object[] args = new Object[] {comment.getComment(),
				  comment.getHyperlinkId(),
				  comment.getId()};
         
        int out = jdbcTemplate.update(query, args);
        if(out !=0){
            System.out.println("Comment updated with id = " + comment.getId());
        }
        else 
        	System.out.println("No Comment found with id = " + comment.getId());
    }
    
    @Override
	public void deleteById (long id)  throws DataAccessException{
        String query = "delete from Comment where id=?";
         
        int out = jdbcTemplate.update(query, id);
        if(out !=0){
            System.out.println("Comment deleted with id= " + id);
        }
        else 
        	System.out.println("No Comment found with id=" + id);
	}
    
    public void deleteByHyperlinkId (long hyperlinkId)  throws DataAccessException{
    	 String query = "delete from Comment where hyperlinkId=?";
         
         int out = jdbcTemplate.update(query, hyperlinkId);
         if(out !=0){
             System.out.println("Comment deleted with hypid= " + hyperlinkId);
         }
         else 
         	System.out.println("No Comment found with hypid=" + hyperlinkId);
    }
	
    @Override
	public Comment getById(long id) throws DataAccessException {
		 String query = "select id, comment, hyperlinkid from Comment where id = ?";
		 
		 Comment com = jdbcTemplate.queryForObject(query, new Object[]{id},
				 								   new CommentMapper());
		 
	   return com;
	}
	@Override
	public List<Comment> getByHyperLinkId(long hyperLinkId) throws DataAccessException {
		 String query = "select id, comment, hyperlinkid from Comment where hyperlinkid = ?";
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

class CommentMapper implements RowMapper {    
	 public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {    
	  Comment comment = new Comment();    
	  comment.setId(rs.getLong("id"));    
	  comment.setComment(rs.getString("comment"));
	  comment.setHyperlinkId(rs.getLong(("hyperlinkid")));
	  return comment;    
	 }

}   