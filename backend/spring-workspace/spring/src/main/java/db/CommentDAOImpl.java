package db;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import model.Comment;
import model.Hyperlink;
import model.MetaTag;

public class CommentDAOImpl {
//	private JdbcTemplate jdbcTemplate;
//	
//	//helper class for inserting in db
//	private SimpleJdbcInsert insertComment;
//	
//    @Autowired
//    public CommentDAOImpl(DataSource dataSource) {
//        this.jdbcTemplate = new JdbcTemplate(dataSource);
//
//        this.insertComment = new SimpleJdbcInsert(dataSource)
//                .withTableName("comment")
//                .usingGeneratedKeyColumns("id");
//    }
//	
//	public void save (Comment comment) {
//		// if (visit.isNew()) {
//	            Number newKey = this.insertComment.executeAndReturnKey(
//	                    createVisitParameterSource(comment));
//	            visit.setId(newKey.intValue());
////	        } else {
////	            throw new UnsupportedOperationException("Visit update not supported");
////	     }
//	}
//	public void update (Hyperlink hyperlink) {
//		
//	}
//	public void deleteById (long id) {
//		
//	}
//	
//	public Hyperlink getById(long id) {
//		
//	}
//	public List<Hyperlink> getAllWithTag(MetaTag tag) {
//		
//	}
//	public List<Hyperlink> getAll() {
//		
//	}
}
