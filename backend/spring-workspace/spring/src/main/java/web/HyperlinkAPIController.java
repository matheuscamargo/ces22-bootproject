package web;

import model.Comment;
import model.Hyperlink;
import model.MetaTag;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

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
    public Hyperlink addMetaTag(@RequestBody MetaTag tag) {
    	logger.info("Start addMetaTag.");
    	try {
    		hyperlinkDAO.getById(tag.getHyperlinkId());
    		metaTagDAO.save(tag);
        	return hyperlinkDAO.getById(tag.getHyperlinkId());
    	}
    	catch (DataAccessException ex) {
    		return null;
    	}
    }    
    @RequestMapping(value = "/api/addcomment", method = RequestMethod.POST)
    public Hyperlink addComment(@RequestBody Comment comment) {
    	logger.info("Start addComment.");
    	try {
    		hyperlinkDAO.getById(comment.getHyperlinkId());
    		commentDAO.save(comment);
        	return hyperlinkDAO.getById(comment.getHyperlinkId());
    	}
    	catch (DataAccessException ex) {
    		return null;
    	}
    }
    @RequestMapping(value = "/api/edit", method = RequestMethod.POST) // OK
    public Hyperlink editHyperlink(@RequestBody Hyperlink hyperlink) {
    	logger.info("Start editHyperlink.");
    	try {
    		hyperlinkDAO.getById(hyperlink.getId());
    		hyperlinkDAO.update(hyperlink);
    		return hyperlinkDAO.getById(hyperlink.getId());
    	}
    	catch (DataAccessException ex) {
    		return null;
    	}
    }
    @RequestMapping(value = "/api/editcomment", method = RequestMethod.POST)
    public Hyperlink editComment(@RequestBody Comment comment) {
    	logger.info("Start editComment.");
    	try {
    		hyperlinkDAO.getById(comment.getHyperlinkId());
    		commentDAO.update(comment);
        	return hyperlinkDAO.getById(comment.getHyperlinkId());
    	}
    	catch (DataAccessException ex) {
    		return null;
    	}
    }
    @RequestMapping(value = "/api", method = RequestMethod.GET) // OK
    public List<Hyperlink> getAllHyperlinks() {
    	logger.info("Start getAllHyperliks.");
    	return hyperlinkDAO.getAll();
    }
    @RequestMapping(value = "/api/show/{id}", method = RequestMethod.GET) // OK
    public Hyperlink show(@PathVariable("id") long id) {
    	logger.info("Start show.");
    	try {
    		return hyperlinkDAO.getById(id);
    	}
    	catch (EmptyResultDataAccessException ex) {
    		return null;
    	}
    }
    @RequestMapping(value = "/api/getallwithtag", method = RequestMethod.POST) // OK
    public List<Hyperlink> getAllWithTag(@RequestBody MetaTag tag) {
    	logger.info("Start getAllWithTag.");
    	try {
        	return hyperlinkDAO.getAllWithTag(tag);
    	}
    	catch (DataAccessException ex) {
    		return null;
    	}
    }    
    @RequestMapping(value = "/api/getallwithhyperlink", method = RequestMethod.POST) // OK
    public List<Hyperlink> getAllWithHyperlink(@RequestBody Hyperlink hyperlink) {
    	logger.info("Start getAllWithHyperlink.");
    	try {
        	return hyperlinkDAO.getAllWithLink(hyperlink.getLink());
    	}
    	catch (DataAccessException ex) {
    		// throw ex;
    		return null;
    	}
    }        
    @RequestMapping(value = "/api/delete/{id}", method = RequestMethod.PUT) // OK
    public Hyperlink delete(@PathVariable("id") long id) {
    	logger.info("Start delete.");
    	try {
    		Hyperlink hyperlink = hyperlinkDAO.getById(id);
    		hyperlinkDAO.deleteById(id);
    		return hyperlink;
    	}
    	catch (DataAccessException ex) {
    		return null;
    	}
    }
    @RequestMapping(value = "/api/deletetag/{id}", method = RequestMethod.PUT)
    public Hyperlink deleteMetaTag(@PathVariable("id") long id) {
    	logger.info("Start deleteMetaTag.");
    	try {
    		long hyperlinkId = metaTagDAO.getById(id).getHyperlinkId();
    		hyperlinkDAO.getById(hyperlinkId);
    		metaTagDAO.getById(id);
    		metaTagDAO.deleteById(id);
    		return hyperlinkDAO.getById(hyperlinkId);
    	}
    	catch (DataAccessException ex) {
    		return null;
    	}
    }
}