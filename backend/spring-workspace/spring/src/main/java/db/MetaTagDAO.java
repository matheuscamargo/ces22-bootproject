package db;

import java.util.List;

import model.MetaTag;

public interface MetaTagDAO {
	public void save (MetaTag metaTag);
	public void update (MetaTag metaTag);
	public void deleteById (long id);
 	public MetaTag getById(long id);
 	public List<MetaTag> getByHyperLinkId(long hypId);
}
