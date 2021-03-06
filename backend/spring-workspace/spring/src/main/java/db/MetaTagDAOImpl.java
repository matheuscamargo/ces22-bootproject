package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import utils.DataBaseIsFullException;
import model.MetaTag;

//class that deals with database access to MetaTag table
public class MetaTagDAOImpl implements MetaTagDAO{
	static final int MAX_METATAGS = 20;
	
	private JdbcTemplate jdbcTemplate;
	
    @Autowired
    public MetaTagDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
    @Override
	public void save (MetaTag metaTag) throws DataAccessException {
		String query = "INSERT INTO MetaTag (tag, hyperlinkid)"
				+ " SELECT * FROM (select ?, ?) AS tmp"
				+ " WHERE NOT EXISTS ( SELECT tag FROM MetaTag"
				+ " WHERE hyperlinkId = ? AND tag = ?) LIMIT 1";
	
        Object[] args = new Object[] {metaTag.getTag(),
        							  metaTag.getHyperlinkId(),
        							  metaTag.getHyperlinkId(),
        							  metaTag.getTag()};
        
        
		int numberOfMetaTags = countMetaTagsByHyperlinkId(metaTag.getHyperlinkId());

		//security - limit number of metatags per hyperlink
		if (numberOfMetaTags >= MAX_METATAGS) {
			throw new DataBaseIsFullException("Too many metatags");
		}

        int out = jdbcTemplate.update(query, args);

        if(out !=0){
            System.out.println("MetaTag saved with id="+metaTag.getId());
        }
        else {
        	throw new DataIntegrityViolationException("Could not insert in db");
        }
	}
	
    @Override
	public void update (MetaTag metaTag) throws DataAccessException {
        String query = "UPDATE MetaTag SET tag=?, hyperlinkid=? WHERE id=?";

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
	public boolean deleteById (long id) throws DataAccessException {
        String query = "DELETE FROM MetaTag WHERE id=?";
         
        int out = jdbcTemplate.update(query, id);
        if(out !=0){
        	//Successfully deleted MetaTag
        	return true;
        }
        else {
        	//Could not delete MetaTag
        	return false;
        }
	}
    
    @Override
	public void deleteByHyperlinkId (long hyperlinkId) throws DataAccessException {
        String query = "DELETE FROM MetaTag WHERE hyperlinkId=?";
         
        int out = jdbcTemplate.update(query, hyperlinkId);
        if(out !=0){
            System.out.println("MetaTag deleted with id= " + hyperlinkId);
        }
        else 
        	System.out.println("No MetaTag found with id=" + hyperlinkId);
	}
	
    @Override
    public int countMetaTagsByHyperlinkId (long hypId) throws DataAccessException {
    	String query = "SELECT COUNT(*) AS count FROM MetaTag"
    				+ " WHERE hyperlinkId = ?";
    	long numberOfMetaTags;
        
        Map<String,Object> rs = jdbcTemplate.queryForMap(query, new Object[] {hypId});
        numberOfMetaTags = (Long)rs.get("count");
        
        return (int)numberOfMetaTags;  
    }
    
    @SuppressWarnings("unchecked")
	@Override
	public MetaTag getById(long id) throws DataAccessException {
		 String query = "select id, tag, hyperlinkid from MetaTag where id = ?";
		 
		 MetaTag mtag = jdbcTemplate.queryForObject(query, new Object[]{id},
				 								   new MetaTagMapper());
		 
	   return mtag;
	}
	@Override
	public List<MetaTag> getByHyperLinkId(long hyperLinkId) throws DataAccessException {
		 String query = "SELECT id, tag, hyperlinkid FROM MetaTag WHERE hyperlinkid = ?";
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

@SuppressWarnings("rawtypes")
class MetaTagMapper implements RowMapper {    
	 public MetaTag mapRow(ResultSet rs, int rowNum) throws SQLException {    
	  MetaTag metaTag = new MetaTag();    
	  metaTag.setId(rs.getLong("id"));    
	  metaTag.setTag(rs.getString("tag"));
	  metaTag.setHyperlinkId(rs.getLong(("hyperlinkid")));
	  return metaTag;    
	 }
}   