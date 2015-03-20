package web;

import java.util.concurrent.atomic.AtomicLong;

import model.Hyperlink;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class HyperlinkController {
	
	private static final Logger logger = LoggerFactory.getLogger(HyperlinkController.class);
    
    private Map<Long, Hyperlink> dataBase = new HashMap<Long, Hyperlink>();

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Hyperlink addHyperlink(@RequestBody Hyperlink hyperlink) {
    	logger.info("Start addHyperlink.");
    	// CHECK DATABASE SIZE TO KEEP IT LIMITED AND AVOID ATTACKS
    	hyperlink.setLastEditedAt(hyperlink.getAddedAt());
    	dataBase.put(hyperlink.getId(), hyperlink);
        return hyperlink;
    }
    
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Hyperlink editHyperlink(@RequestBody Hyperlink hyperlink) {
    	logger.info("Start editHyperlink.");
    	long key = hyperlink.getId();
    	if (dataBase.containsKey(key)) {
    		hyperlink.setLastEditedAt(new Date());
    		dataBase.put(key, hyperlink);
    		return hyperlink;
    	}
    	else return null;
    }
    
    @RequestMapping(value = "/show/{id}", method = RequestMethod.GET)
    public Hyperlink show(@PathVariable("id") long id) {
    	logger.info("Start show.");
    	if (dataBase.containsKey(id)) {
    		return dataBase.get(id);
    	}
    	else return null;
    }
    
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.PUT)
    public Hyperlink delete(@PathVariable("id") long id) {
    	logger.info("Start delete.");
    	if (dataBase.containsKey(id)) {
    		Hyperlink temp = dataBase.get(id);
    		dataBase.remove(id);
    		return temp;
    	}
    	else return null;
    }
   
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Hyperlink> getAllHyperlinks() {
    	logger.info("Start getAllHyperliks.");
    	List<Hyperlink> allHyperlinks = new ArrayList<Hyperlink>();
    	for (Map.Entry<Long, Hyperlink> entry : dataBase.entrySet()) {
    		allHyperlinks.add(entry.getValue());
    	}
    	return allHyperlinks;
    }
}