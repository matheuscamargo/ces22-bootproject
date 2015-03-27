package web;

import model.Comment;
import model.Hyperlink;
import model.MetaTag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import service.HyperlinkService;
import db.CommentDAO;
import db.HyperlinkDAO;
import db.MetaTagDAO;

//@RestController
@Controller
public class HyperlinkWebController{
	
	private static final Logger logger = LoggerFactory.getLogger(HyperlinkWebController.class);
        
  	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring/spring-hyper.xml");
     
    //Get DAO beans
    HyperlinkService hyperlinkService = ctx.getBean("hyperlinkService", HyperlinkService.class);

    @RequestMapping(value = "/add", method = RequestMethod.GET) // OK
    public String addHyperlinkForm(Model model) {
    	logger.info("Start addHyperlink GET.");
    	model.addAttribute("hyperlink", new Hyperlink());
    	model.addAttribute("error", 0);
        return "add";
    }    
    @RequestMapping(value = "/add", method = RequestMethod.POST) // OK
    public String addHyperlinkSubmit(@ModelAttribute Hyperlink hyperlink, Model model) {
    	logger.info("Start addHyperlink POST.");
    	try {
    		long id = hyperlinkService.save(hyperlink);
    		model.addAttribute("hyperlink", hyperlinkService.getById(id));
    		model.addAttribute("exists", true);
        	return "redirect:/show/" + id;
    	}
    	catch (DataAccessException ex) {
    		model.addAttribute("error", 1);
    		return "add";
    	}
    }    
    @RequestMapping(value = "/addtag/{id}", method = RequestMethod.GET) // OK
    public String addTagForm(@PathVariable("id") long id, Model model) {
    	logger.info("Start addTag GET.");    	
    	try {
        	Hyperlink hyperlink = hyperlinkService.getById(id);
        	MetaTag tag = new MetaTag();
        	tag.setHyperlinkId(id);
    		model.addAttribute("tag", tag);
    		model.addAttribute("hyperlink", hyperlink);
        	model.addAttribute("error", 0);    		
    	}
    	catch (EmptyResultDataAccessException ex) {
    		model.addAttribute("error", 1);
    	}
    	return "addtag";
    }    
    @RequestMapping(value = "/addtag", method = RequestMethod.POST) // OK
    public String addTagSubmit(@ModelAttribute MetaTag tag, Model model) {
    	logger.info("Start addTag POST.");
    	// CHECK DATABASE SIZE TO KEEP IT LIMITED AND AVOID ATTACKS
    	try {
    		logger.info("Starting debug");
    		hyperlinkService.addMetaTag(tag);
    		model.addAttribute("hyperlink", hyperlinkService.getById(tag.getHyperlinkId()));
    		model.addAttribute("error", 0);
    		return "redirect:/show/" + tag.getHyperlinkId();
    	}
    	catch (DataAccessException ex) {
    		model.addAttribute("error", 2);
    		return "addtag";
    	}
    }    
    @RequestMapping(value = "/addcomment/{id}", method = RequestMethod.GET) // OK
    public String addCommentForm(@PathVariable("id") long id, Model model) {
    	logger.info("Start addCommand GET.");
    	try {
        	Hyperlink hyperlink = hyperlinkService.getById(id);
        	Comment comment = new Comment();
        	comment.setHyperlinkId(id);
    		model.addAttribute("comment", comment);
    		model.addAttribute("hyperlink", hyperlink);
        	model.addAttribute("error", 0);    		
    	}
    	catch (EmptyResultDataAccessException ex) {
    		model.addAttribute("error", 1);
    	}
    	return "addcomment";
    }    
    @RequestMapping(value = "/addcomment", method = RequestMethod.POST) // OK
    public String addCommentSubmit(@ModelAttribute Comment comment, Model model) {
    	logger.info("Start addCommand POST.");
    	// CHECK DATABASE SIZE TO KEEP IT LIMITED AND AVOID ATTACKS
    	try {
    		hyperlinkService.addComment(comment);
    		model.addAttribute("hyperlink", hyperlinkService.getById(comment.getHyperlinkId()));
    		model.addAttribute("error", 0);
    		return "redirect:/show/" + comment.getHyperlinkId();
    	}
    	catch (DataAccessException ex) {
    		model.addAttribute("error", 2);
    		return "addcomment";
    	}
    }    
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET) // OK
    public String editHyperlinkForm(@PathVariable("id") long id, Model model) {
    	logger.info("Start edit GET.");
    	try {
        	Hyperlink hyperlink = hyperlinkService.getById(id);
    		model.addAttribute("hyperlink", hyperlink);
        	model.addAttribute("error", 0);    		
    	}
    	catch (EmptyResultDataAccessException ex) {
    		model.addAttribute("error", 1);
    	}
    	return "edit";
    }    
    @RequestMapping(value = "/edit", method = RequestMethod.POST) // OK
    public String editHyperlinkSubmit(@ModelAttribute Hyperlink hyperlink, Model model) {
    	logger.info("Start edit POST.");
    	try {
    		hyperlinkService.update(hyperlink);
    		model.addAttribute("hyperlink", hyperlink);
    		model.addAttribute("exists", true);
    		return "redirect:/show/" + hyperlink.getId();
    	}
    	catch (DataAccessException ex) {
    		model.addAttribute("error", 2);
    		return "edit";
    	}
    }    
    @RequestMapping(value = "/editcomment/{id}", method = RequestMethod.GET) // TEST IT!
    public String editCommentForm(@PathVariable("id") long id, Model model) {
    	logger.info("Start editCommand GET.");
    	try {
        	Comment comment = hyperlinkService.getCommentById(id);
        	Hyperlink hyperlink = hyperlinkService.getById(comment.getHyperlinkId());
    		model.addAttribute("comment", comment);
    		model.addAttribute("hyperlink", hyperlink);
        	model.addAttribute("error", 0);    		
    	}
    	catch (EmptyResultDataAccessException ex) {
    		model.addAttribute("error", 1);
    	}
    	return "editcomment";
    }    
    @RequestMapping(value = "/editcomment", method = RequestMethod.POST) // TEST IT!
    public String editCommentSubmit(@ModelAttribute Comment comment, Model model) {
    	logger.info("Start editCommand POST.");
    	// CHECK DATABASE SIZE TO KEEP IT LIMITED AND AVOID ATTACKS
    	try {
    		hyperlinkService.editComment(comment);
    		model.addAttribute("hyperlink", hyperlinkService.getById(comment.getHyperlinkId()));
    		model.addAttribute("error", 0);
    		return "redirect:/show/" + comment.getHyperlinkId();
    	}
    	catch (DataAccessException ex) {
    		model.addAttribute("error", 2);
    		return "editcomment";
    	}
    }    
    @RequestMapping(value = "/show/{id}", method = RequestMethod.GET) // OK
    public String show(@PathVariable("id") long id, Model model) {
    	logger.info("Start show.");
    	try {
    		model.addAttribute("hyperlink",	hyperlinkService.getById(id));
    		model.addAttribute("exists", true);
    	}
    	catch (EmptyResultDataAccessException ex) {
    		model.addAttribute("exists", false);
    	}
    	return "show";
    }    
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET) // TEST IT!
    public String delete(@PathVariable("id") long id, Model model) {
    	logger.info("Start delete.");
    	try {
    		hyperlinkService.deleteById(id);
    		model.addAttribute("message", "Hyperlink with id " + id + " succesfully deleted!");
    	}
    	catch (DataAccessException ex) {
    		model.addAttribute("message", "Hyperlink with id " + id + " not found!");
    	}
    	return "delete";
    }   
    @RequestMapping(value = "/deletetag/{id}", method = RequestMethod.GET) // TEST IT!
    public String deleteTag(@PathVariable("id") long id, Model model) {
    	logger.info("Start deleteComment.");
    	try {
    		hyperlinkService.deleteMetaTag(id);
    		model.addAttribute("message", "Tag with id " + id + " succesfully deleted!");
    	}
    	catch (DataAccessException ex) {
    		model.addAttribute("message", "Tag with id " + id + " not found!");
    	}
    	return "delete";
    }   
    @RequestMapping(value = "/deletecomment/{id}", method = RequestMethod.GET) // TEST IT!
    public String deleteComment(@PathVariable("id") long id, Model model) {
    	logger.info("Start deleteComment.");
    	try {
    		hyperlinkService.deleteMetaTag(id);
    		model.addAttribute("message", "Comment with id " + id + " succesfully deleted!");
    	}
    	catch (DataAccessException ex) {
    		model.addAttribute("message", "Comment with id " + id + " not found!");
    	}
    	return "delete";
    }   
    @RequestMapping(value = "/", method = RequestMethod.GET) // OK
    public String getAllHyperlinks(Model model) {
    	logger.info("Start index.");
    	logger.info("List size: " + hyperlinkService.getAll().size()); 
    	model.addAttribute("hyperlinksList", hyperlinkService.getAll());
    	return "index";
    }
}