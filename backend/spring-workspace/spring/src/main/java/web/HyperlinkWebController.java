package web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import model.Comment;
import model.Hyperlink;
import model.MetaTag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//@RestController
@Controller
public class HyperlinkWebController{
	
	private static final Logger logger = LoggerFactory.getLogger(HyperlinkWebController.class);
    
    private Map<Long, Hyperlink> dataBase = new HashMap<Long, Hyperlink>();
    
    private boolean firstTime = true; // GAMBIARRA

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addHyperlinkForm(Model model) {
    	logger.info("Start addHyperlink.");
    	model.addAttribute("hyperlink", new Hyperlink());
    	model.addAttribute("error", 0);
        return "add";
    }
    
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addHyperlinkSubmit(@ModelAttribute Hyperlink hyperlink, Model model) {
    	// CHECK DATABASE SIZE TO KEEP IT LIMITED AND AVOID ATTACKS
    	if (dataBase.containsKey(hyperlink.getId())) {
    		model.addAttribute("error", 1);
    		return "add";
    	}
    	else {
    		hyperlink.setLastEditedAt(new Date());
    		dataBase.put(hyperlink.getId(), hyperlink);
    		model.addAttribute("hyperlink", hyperlink);
    		model.addAttribute("exists", true);
        	return "show";
    	}
    }
    
    @RequestMapping(value = "/addtag/{id}", method = RequestMethod.GET)
    public String addTagForm(@PathVariable("id") long id, Model model) {
    	if (dataBase.containsKey(id)) {
        	Hyperlink hyperlink = dataBase.get(id);
    		model.addAttribute("hyperlink", hyperlink);
    		MetaTag tag = new MetaTag();
    		tag.setId(0);
    		tag.setHyperlinkId(hyperlink.getId());
    		model.addAttribute("tag", tag);
        	model.addAttribute("error", 0);
    		return "addtag";
    	}
    	else {
    		model.addAttribute("error", 1);
        	return "addtag";
    	}
    }
    
    @RequestMapping(value = "/addtag", method = RequestMethod.POST)
    public String addTagSubmit(@ModelAttribute MetaTag tag, Model model) {
    	// CHECK DATABASE SIZE TO KEEP IT LIMITED AND AVOID ATTACKS
    	long id = tag.getHyperlinkId();
    	if (dataBase.containsKey(id)) {
    		model.addAttribute("error", 0);
    		Hyperlink hyperlink = dataBase.get(tag.getHyperlinkId());
    		hyperlink.addTag(tag);
    		model.addAttribute("hyperlink",	dataBase.get(tag.getHyperlinkId()));
    		model.addAttribute("exists", true);
    		return "redirect:/show/" + id;
    	}
    	else {
        	return "index";
    	}
    }
    
    @RequestMapping(value = "/addcomment/{id}", method = RequestMethod.GET)
    public String addCommentForm(@PathVariable("id") long id, Model model) {
    	if (dataBase.containsKey(id)) {
        	Hyperlink hyperlink = dataBase.get(id);
    		model.addAttribute("hyperlink", hyperlink);
    		Comment comment = new Comment();
    		comment.setId(0);
    		comment.setHyperlinkId(hyperlink.getId());
    		model.addAttribute("comment", comment);
        	model.addAttribute("error", 0);
    		return "addcomment";
    	}
    	else {
    		model.addAttribute("error", 1);
        	return "addcomment";
    	}
    }
    
    @RequestMapping(value = "/addcomment", method = RequestMethod.POST)
    public String addCommentSubmit(@ModelAttribute Comment comment, Model model) {
    	// CHECK DATABASE SIZE TO KEEP IT LIMITED AND AVOID ATTACKS
    	long id = comment.getHyperlinkId();
    	if (dataBase.containsKey(id)) {
    		model.addAttribute("error", 0);
    		Hyperlink hyperlink = dataBase.get(comment.getHyperlinkId());
    		//hyperlink.addComment(comment.getComment());
    		dataBase.put(comment.getHyperlinkId(), hyperlink);
    		model.addAttribute("hyperlink",	dataBase.get(comment.getHyperlinkId()));
    		model.addAttribute("exists", true);
    		return "redirect:/show/" + id;
    	}
    	else {
        	return "index";
    	}
    }
    
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editHyperlinkForm(@PathVariable("id") long id, Model model) {
    	if (dataBase.containsKey(id)) {
        	Hyperlink hyperlink = dataBase.get(id);
    		model.addAttribute("hyperlink", hyperlink);
        	model.addAttribute("error", 0);
    		return "edit";
    	}
    	else {
    		model.addAttribute("error", 1);
        	return "edit";
    	}
    }
    
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editHyperlinkSubmit(@ModelAttribute Hyperlink hyperlink, Model model) {
    	hyperlink.setLastEditedAt(new Date());
		dataBase.put(hyperlink.getId(), hyperlink);
		model.addAttribute("hyperlink", hyperlink);
		model.addAttribute("exists", true);
		return "redirect:/show/" + hyperlink.getId();
    }
    
    @RequestMapping(value = "/show/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") long id, Model model) {
    	boolean exists = false;
    	if (dataBase.containsKey(id)) {
    		exists = true;
    		model.addAttribute("hyperlink",	dataBase.get(id));
    	}
    	model.addAttribute("exists", exists);
    	return "show";
    }
    
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") long id, Model model) {
    	boolean deleted = false;
    	if (dataBase.containsKey(id)) {
    		deleted = true;
    		dataBase.remove(id);
    	}
    	model.addAttribute("deleted", deleted);
    	model.addAttribute("id", id);
    	return "delete";
    }
   
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getAllHyperlinks(Model model) {
//    	if (firstTime) {
//    		firstTime = false;
//
//    		LinkedList<String> tags1 = new LinkedList<String>();
//    		tags1.add("one");
//    		tags1.add("two");
//    		tags1.add("three");
//    		LinkedList<String> tags2 = new LinkedList<String>();
//    		tags2.add("one");
//    		tags2.add("two");
//    		tags2.add("four");
//    		LinkedList<String> tags3 = new LinkedList<String>();
//    		tags3.add("four");
//    		tags3.add("two");
//    		tags3.add("three");
//    		LinkedList<String> comments1 = new LinkedList<String>();
//    		comments1.add("Comentário 1");
//    		comments1.add("Comentário 2");
//	    	dataBase.put((long)0, new Hyperlink(0, "www.google.com", tags1, comments1));
//	    	dataBase.put((long)1, new Hyperlink(1, "www.facebook.com", tags2, comments1));
//	    	dataBase.put((long)2, new Hyperlink(2, "www.gmail.com", tags3, comments1));
//    	}
    	List<Hyperlink> allHyperlinks = new ArrayList<Hyperlink>();  
    	for (Map.Entry<Long, Hyperlink> entry : dataBase.entrySet()) {
    		allHyperlinks.add(entry.getValue());
    	}    	
    	model.addAttribute("hyperlinksList", allHyperlinks);    	
    	return "index";
    }
}