package db;

import java.util.List;
import model.Hyperlink;
import model.MetaTag;
import model.Comment;


//CRUD operations
public interface HyperlinkDAO {
	public void save (Hyperlink hyperlink);
	public void update (Hyperlink hyperlink);
	public void deleteById (long id);
	
	public Hyperlink getById(long id);
	public List<Hyperlink> getAllWithTag(MetaTag tag);
	public List<Hyperlink> getAll();
}