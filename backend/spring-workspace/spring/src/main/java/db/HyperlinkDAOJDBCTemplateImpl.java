package db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;  

import model.Hyperlink;
import model.MetaTag;

public class HyperlinkDAOJDBCTemplateImpl implements HyperlinkDAO{
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	//public HyperlinkDAOJDBCTemplateImpl () {}
	
    public void setDataSource(DataSource dataSource) {
    	this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
    
//	//for dependency injection
//	@Autowired
//	public HyperlinkDAOJDBCTemplateImpl(DataSource dataSource, NamedParameterJdbcTemplate
//									  namedParameterJdbcTemplate) {
//		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
//	}
	
    @Override
	public void save (Hyperlink hyperlink) {
    	String query = "insert into Hyperlink (id, link) values (:id, :link)";
    	System.out.println("Testing");
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("id", String.valueOf(hyperlink.getId()));
    	params.put("link", hyperlink.getLink());
		//if (hyperlink.isNew()) {
			int out = namedParameterJdbcTemplate.update(query, params);

			if(out !=0) System.out.println("Saved hyperlink");
			else 		System.out.println("Unable to ave hyperlink");
	//	}
		//else {
	//		System.out.println("Already inside the db");
	//	}
	}
  
	public void update (Hyperlink hyperlink) {
		String query = "update Hyperlink set link=:link, tags=:tags, addedAt=:addedAt where id=:id";
       
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("id", hyperlink.getId());
    	params.put("link", hyperlink.getLink());
    	params.put("tags", hyperlink.getTags());
    	params.put("addedAt", hyperlink.getAddedAt());
         
		int out = namedParameterJdbcTemplate.update(query, params);
		
		if(out !=0) System.out.println("Saved hyperlink");
		else 		System.out.println("Unable to send hyperlink");
	}
	
	public void deleteById (long id) {
        String query = "delete from Hyperlink where id=?";
         
        Map<String, Object> params = new HashMap<String, Object>();
    	params.put("id", id);
        int out = namedParameterJdbcTemplate.update(query, params);
        if(out !=0){
            System.out.println("Employee deleted with id="+id);
        }else System.out.println("No Employee found with id="+id);
	}
	
	public Hyperlink getById(long id) {
		 String query = "select id from Employee where id = :id";
		 Map<String, Object> params = new HashMap<String, Object>();
		 params.put("id", id);
		 
		 Hyperlink hyp = (Hyperlink) namedParameterJdbcTemplate.queryForObject(query, params, new HyperlinkMapper());
		 
//		return new Hyperlink(id, ""); //dummy
		 return hyp;
	}
	public List<Hyperlink> getAllWithTag(MetaTag tag) {
		return new ArrayList<Hyperlink>(); //dummy
	}
	public List<Hyperlink> getAll() {
        String query = "select * from Hyperlink";
        List<Hyperlink> hypList = (List<Hyperlink>) namedParameterJdbcTemplate.query(query, new HyperlinkMapper());
        return hypList;
	}
}


class HyperlinkMapper implements RowMapper {    
	 public Hyperlink mapRow(ResultSet rs, int rowNum) throws SQLException {    
	  Hyperlink hyperlink = new Hyperlink();    
	  hyperlink.setId(rs.getInt("id"));    
//	  hyperlink.setLink(rs.getString("link"));      
	  return hyperlink;    
	 }

	}    
