package web;

import java.util.concurrent.atomic.AtomicLong;

import model.Hyperlink;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class HyperlinkController {
	
	private static final Logger logger = LoggerFactory.getLogger(HyperlinkController.class);
	  
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(value = "/add", method = RequestMethod.GET) //POST
    public Hyperlink addHyperlink(@RequestParam(value="name", defaultValue="World") String name) {
    	logger.info("Start addHyperlink.");
        return new Hyperlink("www.google.com");
    }
    
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public Hyperlink queryFor(@RequestParam(value="name", defaultValue="World") String name) {
    	logger.info("Start queryFor.");
        return new Hyperlink(counter.incrementAndGet(),
                            String.format(template, name));
    }
   
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Hyperlink> getAllHyperlinks() {
    	logger.info("Start getAllHyperliks.");
    	List<Hyperlink> allHyperlinks = new ArrayList<Hyperlink>();
    	//query for all elements db
    	return allHyperlinks;
    }
}