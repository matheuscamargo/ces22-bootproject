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

import model.MetaTag;

//class that deals with database access to MetaTag table
public class MetaTagDAOImpl implements MetaTagDAO{
	private JdbcTemplate jdbcTemplate;
	
    @Autowired
    public MetaTagDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
    @Override
	public void save (MetaTag metaTag) throws DataAccessException {
		String query = "insert into MetaTag (tag, hyperlinkid) values (?,?)";
	
        Object[] args = new Object[] {metaTag.getTag(),
        							  metaTag.getHyperlinkId()};
         
        int out = jdbcTemplate.update(query, args);
         
        if(out !=0){
            System.out.println("MetaTag saved with id="+metaTag.getId());
        }else System.out.println("MetaTag saved failed with id="+metaTag.getId());
	}
	
    @Override
	public void update (MetaTag metaTag) throws DataAccessException {
        String query = "update MetaTag set tag=?, hyperlinkid=? where id=?";

        Object[] args = new Object[] {metaTag.getTag(),
        						      metaTag.getHyperlinkId(),
				  metaTag.getId()};
         
        int out = jdbcTemplate.update(query, args);
        if(out !=0){
            System.out.println("MetaTag updated with id = " + metaTag.getId());
        }
        else 
        	System.out.println("No MetaTag found with id = " + metaTag.getId());
    }
    
    @Override
	public void deleteById (long id) throws DataAccessException {
        String query = "delete from MetaTag where id=?";
         
        int out = jdbcTemplate.update(query, id);
        if(out !=0){
            System.out.println("MetaTag deleted with id= " + id);
        }
        else 
        	System.out.println("No MetaTag found with id=" + id);
	}
    
    @Override
	public void deleteByHyperlinkId (long hyperlinkId) throws DataAccessException {
        String query = "delete from MetaTag where hyperlinkId=?";
         
        int out = jdbcTemplate.update(query, hyperlinkId);
        if(out !=0){
            System.out.println("MetaTag deleted with id= " + hyperlinkId);
        }
        else 
        	System.out.println("No MetaTag found with id=" + hyperlinkId);
	}
	
    @Override
	public MetaTag getById(long id) throws DataAccessException {
		 String query = "select id, tag, hyperlinkid from MetaTag where id = ?";
		 
		 MetaTag mtag = jdbcTemplate.queryForObject(query, new Object[]{id},
				 								   new MetaTagMapper());
		 
	   return mtag;
	}
	@Override
	public List<MetaTag> getByHyperLinkId(long hyperLinkId) throws DataAccessException {
		 String query = "select id, tag, hyperlinkid from MetaTag where hyperlinkid = ?";
		 //results
		 List<MetaTag> mtList = new ArrayList<MetaTag>();
		 
		 List<Map<String,Object>> mtRows = jdbcTemplate.queryForList(query, new Object[] {hyperLinkId});
		 
	     for(Map<String,Object> mtRow : mtRows){
	            MetaTag mtag = new MetaTag();
	            mtag.setId(Long.parseLong(String.valueOf(mtRow.get("id"))));
	            mtag.setTag(String.valueOf(mtRow.get("tag")));
	            mtag.setHyperlinkId(Long.parseLong(String.valueOf(mtRow.get("hyperlinkid"))));
	            mtList.add(mtag);
	        }
		 return mtList;
	}
}

class MetaTagMapper implements RowMapper {    
	 public MetaTag mapRow(ResultSet rs, int rowNum) throws SQLException {    
	  MetaTag metaTag = new MetaTag();    
	  metaTag.setId(rs.getLong("id"));    
	  metaTag.setTag(rs.getString("tag"));
	  metaTag.setHyperlinkId(rs.getLong(("hyperlinkid")));
	  return metaTag;    
	 }
}   