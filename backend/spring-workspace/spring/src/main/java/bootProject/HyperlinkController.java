package bootProject;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class HyperlinkController {
	
	private static final Logger logger = LoggerFactory.getLogger(HyperlinkController.class);
	  
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/add")
    public Hyperlink addHyperlink(@RequestParam(value="name", defaultValue="World") String name) {
        return new Hyperlink(counter.incrementAndGet(),
                            String.format(template, name));
    }
    
    @RequestMapping("/list")
    public Hyperlink greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Hyperlink(counter.incrementAndGet(),
                            String.format(template, name));
    }
}