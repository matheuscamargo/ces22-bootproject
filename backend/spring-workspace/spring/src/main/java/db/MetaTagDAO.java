package db;

import java.util.List;

import org.springframework.dao.DataAccessException;

import model.MetaTag;

public interface MetaTagDAO {
	public void save (MetaTag metaTag) throws DataAccessException;
	public void update (MetaTag metaTag) throws DataAccessException;
	public boolean deleteById (long id) throws DataAccessException;
	public void deleteByHyperlinkId (long hyperlinkId) throws DataAccessException;
	public int countMetaTagsByHyperlinkId (long hypId) throws DataAccessException;
 	public MetaTag getById(long id) throws DataAccessException;
 	public List<MetaTag> getByHyperLinkId(long hypId) throws DataAccessException;
}
