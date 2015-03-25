package bootProject;

import java.util.concurrent.atomic.AtomicLong;

import model.Hyperlink;
import model.MetaTag;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    	logger.info("Start addHyperlink.");
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
    	logger.info("Start addTag.");
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
    	logger.info("Start addTagPost.");
    	// CHECK DATABASE SIZE TO KEEP IT LIMITED AND AVOID ATTACKS
    	long id = tag.getHyperlinkId();
    	if (dataBase.containsKey(id)) {
    		model.addAttribute("error", 0);
    		Hyperlink hyperlink = dataBase.get(tag.getHyperlinkId());
    		hyperlink.addTag(tag);
    		model.addAttribute("hyperlink",	dataBase.get(tag.getHyperlinkId()));
    		model.addAttribute("exists", true);
    		return "redirect:/show/{id}";
    	}
    	else {
        	return "index";
    	}
    }
    
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editHyperlinkForm(@PathVariable("id") long id, Model model) {
    	logger.info("Start editHyperlink.");
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
    	logger.info("Start editHyperlink.");
    	hyperlink.setLastEditedAt(new Date());
		dataBase.put(hyperlink.getId(), hyperlink);
		model.addAttribute("hyperlink", hyperlink);
		model.addAttribute("exists", true);
    	return "show";
    }
    
    @RequestMapping(value = "/show/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") long id, Model model) {
    	logger.info("Start show.");
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
    	logger.info("Start delete.");
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
    	logger.info("Start getAllHyperliks.");    	
    	if (firstTime) {
    		firstTime = false;
//	    	dataBase.put((long)0, new Hyperlink(0, "www.google.com", Arrays.asList("one", "two", "three")));
//	    	dataBase.put((long)1, new Hyperlink(1, "www.facebook.com", Arrays.asList("one", "two", "three")));
//	    	dataBase.put((long)2, new Hyperlink(2, "www.gmail.com", Arrays.asList("one", "two", "three")));
    	}
    	List<Hyperlink> allHyperlinks = new ArrayList<Hyperlink>();  
    	for (Map.Entry<Long, Hyperlink> entry : dataBase.entrySet()) {
    		allHyperlinks.add(entry.getValue());
    	}    	
    	model.addAttribute("hyperlinksList", allHyperlinks);    	
    	return "index";
    }
}