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
import org.springframework.web.bind.annotation.RequestParam;

import service.HyperlinkService;
import utils.DataBaseIsFullException;
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
    		if (hyperlink.getLink().length() < 3) {
        		model.addAttribute("error", 1);
        		model.addAttribute("message", "Error adding: link should have 3 or more characters!");
        		return "add";
    		}
    		long id = hyperlinkService.save(hyperlink);
    		model.addAttribute("hyperlink", hyperlinkService.getById(id));
    		model.addAttribute("exists", true);
        	return "redirect:/show/" + id;
    	}
    	catch (DataBaseIsFullException ex) {
    		model.addAttribute("error", 2);
    		model.addAttribute("message", "Error adding: database is full!");
    		return "add";
    	}
    	catch (DataAccessException ex) {
    		model.addAttribute("error", 2);
    		model.addAttribute("message", "Error adding: link too huge!");
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
    		model.addAttribute("message", "No hyperlink entry with id " + id);
    	}
    	return "addtag";
    }    
    @RequestMapping(value = "/addtag", method = RequestMethod.POST) // OK
    public String addTagSubmit(@ModelAttribute MetaTag tag, Model model) {
    	logger.info("Start addTag POST.");
    	try {
    		if (tag.getTag().length() < 3) {
        		model.addAttribute("error", 2);
        		model.addAttribute("message", "Error adding: tag should have 3 or more characters!");
        		long id = tag.getHyperlinkId();
            	Hyperlink hyperlink = hyperlinkService.getById(id);
        		model.addAttribute("tag", tag);
        		model.addAttribute("hyperlink", hyperlink);
        		return "addtag";
    		}
    		hyperlinkService.addMetaTag(tag);
    		model.addAttribute("hyperlink", hyperlinkService.getById(tag.getHyperlinkId()));
    		model.addAttribute("error", 0);
    		return "redirect:/show/" + tag.getHyperlinkId();
    	}
    	catch (DataBaseIsFullException ex) {
      		model.addAttribute("error", 2);
    		model.addAttribute("message", "Error adding: Database is full!");
    		return "addtag";
    	}    	
    	catch (DataAccessException ex) {
    		model.addAttribute("error", 2);
    		model.addAttribute("message", "Error adding: tag too huge!");
    		long id = tag.getHyperlinkId();
        	Hyperlink hyperlink = hyperlinkService.getById(id);
    		model.addAttribute("tag", tag);
    		model.addAttribute("hyperlink", hyperlink);
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
    		model.addAttribute("message", "No hyperlink entry with id " + id);
    	}
    	return "addcomment";
    }    
    @RequestMapping(value = "/addcomment", method = RequestMethod.POST) // OK
    public String addCommentSubmit(@ModelAttribute Comment comment, Model model) {
    	logger.info("Start addCommand POST.");
    	try {
    		if (comment.getComment().length() < 3) {
        		model.addAttribute("error", 2);
        		model.addAttribute("message", "Error adding: comment should have 3 or more characters!");
        		long id = comment.getHyperlinkId();
        		Hyperlink hyperlink = hyperlinkService.getById(id);
        		model.addAttribute("comment", comment);
        		model.addAttribute("hyperlink", hyperlink);        		
        		return "addcomment";
    		}
    		hyperlinkService.addComment(comment);
    		model.addAttribute("hyperlink", hyperlinkService.getById(comment.getHyperlinkId()));
    		model.addAttribute("error", 0);
    		return "redirect:/show/" + comment.getHyperlinkId();
    	}
    	catch (DataBaseIsFullException ex) {
      		model.addAttribute("error", 2);
    		model.addAttribute("message", "Error adding: Database is full!");
    		return "addcomment";
    	} 
    	catch (DataAccessException ex) {
    		model.addAttribute("error", 2);
    		model.addAttribute("message", "Error adding: comment too huge!");
    		long id = comment.getHyperlinkId();
    		Hyperlink hyperlink = hyperlinkService.getById(id);
    		model.addAttribute("comment", comment);
    		model.addAttribute("hyperlink", hyperlink);        		
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
    		model.addAttribute("message", "No hyperlink entry with id " + id);
    	}
    	return "edit";
    }    
    @RequestMapping(value = "/edit", method = RequestMethod.POST) // OK
    public String editHyperlinkSubmit(@ModelAttribute Hyperlink hyperlink, Model model) {
    	logger.info("Start edit POST.");
    	try {
    		if (hyperlink.getLink().length() < 3) {
        		model.addAttribute("error", 2);
        		model.addAttribute("message", "Error adding: link should have 3 or more characters!");
        		return "edit";
    		}
    		hyperlinkService.update(hyperlink);
    		model.addAttribute("hyperlink", hyperlink);
    		model.addAttribute("exists", true);
    		return "redirect:/show/" + hyperlink.getId();
    	}
    	catch (DataAccessException ex) {
    		model.addAttribute("error", 2);
    		model.addAttribute("message", "Error editing: link too huge!");
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
    		model.addAttribute("message", "No comment entry with id " + id);
    	}
    	return "editcomment";
    }    
    @RequestMapping(value = "/editcomment", method = RequestMethod.POST) // TEST IT!
    public String editCommentSubmit(@ModelAttribute Comment comment, Model model) {
    	logger.info("Start editCommand POST.");
    	try {
    		if (comment.getComment().length() < 3) {
        		model.addAttribute("error", 2);
        		model.addAttribute("message", "Error editing: comment should have 3 or more characters!");
        		long id = comment.getHyperlinkId();
            	Hyperlink hyperlink = hyperlinkService.getById(id);
        		model.addAttribute("comment", comment);
        		model.addAttribute("hyperlink", hyperlink);
        		return "editcomment";
    		}
    		hyperlinkService.editComment(comment);
    		model.addAttribute("hyperlink", hyperlinkService.getById(comment.getHyperlinkId()));
    		model.addAttribute("error", 0);
    		return "redirect:/show/" + comment.getHyperlinkId();
    	}
    	catch (DataAccessException ex) {
    		model.addAttribute("error", 2);
    		model.addAttribute("message", "Error adding: comment too huge!");
    		long id = comment.getHyperlinkId();
        	Hyperlink hyperlink = hyperlinkService.getById(id);
    		model.addAttribute("comment", comment);
    		model.addAttribute("hyperlink", hyperlink);
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
    		model.addAttribute("message", "Hyperlink succesfully deleted!");
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
    		long hyperlinkId = hyperlinkService.getMetaTagById(id).getHyperlinkId();
    		Hyperlink hyperlink = hyperlinkService.getById(hyperlinkId);
    		model.addAttribute("hyperlink", hyperlink);
    		hyperlinkService.deleteMetaTag(id);
    		model.addAttribute("exists", true);
    		model.addAttribute("message", "Tag succesfully deleted!");
    		return "redirect:/show/" + hyperlinkId;
    	}
    	catch (DataAccessException ex) {
    		model.addAttribute("message", "Tag with id " + id + " not found!");
        	return "delete";
    	}
    }   
    @RequestMapping(value = "/deletecomment/{id}", method = RequestMethod.GET) // TEST IT!
    public String deleteComment(@PathVariable("id") long id, Model model) {
    	logger.info("Start deleteComment.");
    	try {
    		long hyperlinkId = hyperlinkService.getCommentById(id).getHyperlinkId();
    		Hyperlink hyperlink = hyperlinkService.getById(hyperlinkId);
    		model.addAttribute("hyperlink", hyperlink);
    		hyperlinkService.deleteComment(id);
    		model.addAttribute("exists", true);
    		model.addAttribute("message", "Comment succesfully deleted!");
    		return "redirect:/show/" + hyperlinkId;
    	}
    	catch (DataAccessException ex) {
    		model.addAttribute("message", "Comment with id " + id + " not found!");
        	return "delete";
    	}
    }   
    @RequestMapping(value = "/", method = RequestMethod.GET) // OK
    public String getAllHyperlinks(@RequestParam(required = false, value="order") String order, Model model) {
    	logger.info("Start index.");
    	model.addAttribute("message", "List of all registered hyperlinks");
    	if (order == null) {
    		model.addAttribute("hyperlinksList", hyperlinkService.getAll(""));
    		model.addAttribute("ascending", true);
    	}
    	else if (order.equals("asce")) {
    		model.addAttribute("ascending", true);
    		model.addAttribute("hyperlinksList", hyperlinkService.getAll("asce"));
    	}
    	else {
    		model.addAttribute("ascending", false);
    		model.addAttribute("hyperlinksList", hyperlinkService.getAll("desc"));
    	}
    	return "index";
    }
    @RequestMapping(value = "/searchlink", method = RequestMethod.GET) // OK
    public String searchHyperlinks(@RequestParam("q") String search, @RequestParam(required = false, value="order") String order, Model model) {
    	logger.info("Start searchHyperlinks.");
    	model.addAttribute("search", search);
    	model.addAttribute("message", "Search results for \"" + search + "\"");
    	model.addAttribute("searchTag", false);
    	if (order == null) {
    		model.addAttribute("hyperlinksList", hyperlinkService.getAllWithLink(search, ""));
    		model.addAttribute("ascending", true);
    	}
    	else if (order.equals("asce")) {
    		model.addAttribute("ascending", true);
    		model.addAttribute("hyperlinksList", hyperlinkService.getAllWithLink(search, "asce"));
    	}
    	else {
    		model.addAttribute("ascending", false);
    		model.addAttribute("hyperlinksList", hyperlinkService.getAllWithLink(search, "desc"));
    	}
    	return "index";
    }
    @RequestMapping(value = "/searchtag", method = RequestMethod.GET) // OK
    public String searchTags(@RequestParam("q") String search, @RequestParam(required = false, value="order") String order, Model model) {
    	logger.info("Start searchTags.");
    	model.addAttribute("search", search);
    	model.addAttribute("message", "Search results for \"" + search + "\"");
    	model.addAttribute("searchTag", true);
    	if (order == null) {
    		model.addAttribute("hyperlinksList", hyperlinkService.getAllWithTag(search, ""));
    		model.addAttribute("ascending", true);
    	}
    	else if (order.equals("asce")) {
    		model.addAttribute("ascending", true);
    		model.addAttribute("hyperlinksList", hyperlinkService.getAllWithTag(search, "asce"));
    	}
    	else {
    		model.addAttribute("ascending", false);
    		model.addAttribute("hyperlinksList", hyperlinkService.getAllWithTag(search, "desc"));
    	}
    	return "index";
    }
}