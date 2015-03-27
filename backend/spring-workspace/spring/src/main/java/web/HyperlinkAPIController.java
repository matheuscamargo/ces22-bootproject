package web;

import model.Comment;
import model.Hyperlink;
import model.MetaTag;

import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import service.HyperlinkService;
import db.CommentDAO;
import db.HyperlinkDAO;
import db.MetaTagDAO;

//@RestController
@RestController
public class HyperlinkAPIController {
	
	private static final Logger logger = LoggerFactory.getLogger(HyperlinkAPIController.class);
    	 
  	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring/spring-hyper.xml");
    
    //Get DAO beans
    HyperlinkDAO hyperlinkDAO = ctx.getBean("hyperlinkDAO", HyperlinkDAO.class);
    MetaTagDAO metaTagDAO = ctx.getBean("metaTagDAO", MetaTagDAO.class);
    CommentDAO commentDAO = ctx.getBean("commentDAO", CommentDAO.class);
    HyperlinkService hyperlinkService = ctx.getBean("hyperlinkService", HyperlinkService.class);

    @RequestMapping(value = "/api/add", method = RequestMethod.POST) // OK
    public Hyperlink addHyperlink(@RequestBody Hyperlink hyperlink) {
    	logger.info("Start addHyperlink.");
    	try {
    		long id = hyperlinkService.save(hyperlink);
        	return hyperlinkService.getById(id);
    	}
    	catch (DataAccessException ex) {
    		return null;
    	}
    }    
    @RequestMapping(value = "/api/addtag", method = RequestMethod.POST)
    public boolean addMetaTag(@RequestBody MetaTag tag) {
    	logger.info("Start addMetaTag.");
    	try {
    		return hyperlinkService.addMetaTag(tag);
    	}
    	catch (DataAccessException ex) {
    		return false;
    	}
    }    
    @RequestMapping(value = "/api/addcomment", method = RequestMethod.POST)
    public boolean addComment(@RequestBody Comment comment) {
    	logger.info("Start addComment.");
    	try {
    		return hyperlinkService.addComment(comment);
    	}
    	catch (DataAccessException ex) {
    		return false;
    	}
    }
    @RequestMapping(value = "/api/edit", method = RequestMethod.POST) // OK
    public Hyperlink editHyperlink(@RequestBody Hyperlink hyperlink) {
    	logger.info("Start editHyperlink.");
    	try {
    		hyperlinkService.getById(hyperlink.getId());
    		hyperlinkService.update(hyperlink);
    		return hyperlinkService.getById(hyperlink.getId());
    	}
    	catch (DataAccessException ex) {
    		return null;
    	}
    }
    @RequestMapping(value = "/api/editcomment", method = RequestMethod.POST)
    public Hyperlink editComment(@RequestBody Comment comment) {
    	logger.info("Start editComment.");
    	try {
    		hyperlinkService.getById(comment.getHyperlinkId());
    		hyperlinkService.editComment(comment);
        	return hyperlinkService.getById(comment.getHyperlinkId());
    	}
    	catch (DataAccessException ex) {
    		return null;
    	}
    }
    @RequestMapping(value = "/api", method = RequestMethod.GET) // OK
    public List<Hyperlink> getAllHyperlinks() {
    	logger.info("Start getAllHyperliks.");
    	return hyperlinkService.getAll();
    }
    @RequestMapping(value = "/api/show/{id}", method = RequestMethod.GET) // OK
    public Hyperlink show(@PathVariable("id") long id) {
    	logger.info("Start show.");
    	try {
    		return hyperlinkService.getById(id);
    	}
    	catch (EmptyResultDataAccessException ex) {
    		return null;
    	}
    }
    @RequestMapping(value = "/api/getallwithtag", method = RequestMethod.POST) // OK
    public List<Hyperlink> getAllWithTag(@RequestBody MetaTag tag) {
    	logger.info("Start getAllWithTag.");
    	try {
        	return hyperlinkService.getAllWithTag(tag);
    	}
    	catch (DataAccessException ex) {
    		return null;
    	}
    }    
    @RequestMapping(value = "/api/getallwithhyperlink", method = RequestMethod.POST) // OK
    public List<Hyperlink> getAllWithHyperlink(@RequestBody Hyperlink hyperlink) {
    	logger.info("Start getAllWithHyperlink.");
    	try {
        	return hyperlinkService.getAllWithLink(hyperlink.getLink());
    	}
    	catch (DataAccessException ex) {
    		// throw ex;
    		return null;
    	}
    }        
    @RequestMapping(value = "/api/delete/{id}", method = RequestMethod.PUT) // OK
    public boolean delete(@PathVariable("id") long id) {
    	logger.info("Start delete.");
    	try {
    		return hyperlinkService.deleteById(id);
    	}
    	catch (DataAccessException ex) {
    		return false;
    	}
    }
    @RequestMapping(value = "/api/deletetag/{id}", method = RequestMethod.PUT)
    public boolean deleteMetaTag(@PathVariable("id") long id) {
    	logger.info("Start deleteMetaTag.");
    	try {    		
    		return hyperlinkService.deleteMetaTag(id);
    	}
    	catch (DataAccessException ex) {
    		return false;
    	}
    }
    @RequestMapping(value = "/api/deletecomment/{id}", method = RequestMethod.PUT)
    public boolean deleteComment(@PathVariable("id") long id) {
    	logger.info("Start deleteComment.");
    	try {    		
    		return hyperlinkService.deleteComment(id);
    	}
    	catch (DataAccessException ex) {
    		return false;
    	}
    }
}