package bootProject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import model.Comment;
import model.Hyperlink;
import model.MetaTag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import db.CommentDAO;
import db.HyperlinkDAO;
import db.MetaTagDAO;

@SpringBootApplication
public class Application implements CommandLineRunner {

    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
    	HyperlinkTest();
    	//CommentTest();
    	//MetaTagTest();
   }
    
    //test methods
	void MetaTagTest() {
	    System.out.println("Starting Meta Test");
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-meta.xml");
		System.out.println("Done loading ctx");
	    //Get the MetaTagDAO Bean
	    MetaTagDAO metaTagDAO = ctx.getBean("metaTagDAO", MetaTagDAO.class);
	    System.out.println("Created");
	    
	    //Run some tests for JDBC CRUD operations
	    MetaTag mtag = new MetaTag();
	    int rand = new Random().nextInt(1000);
	    mtag.setId(rand);
	    mtag.setTag("Tag do hu3");
	    
	    //Create
	    metaTagDAO.save(mtag);
	    System.out.println("Saved MetaTag");
	    
	     //Update
	     mtag.setTag("Mat eh gay");
	     metaTagDAO.update(mtag);
	    
	    //Read
	    List<MetaTag> com1 = metaTagDAO.getByHyperLinkId(0);
	    System.out.println("MetaTags Retrieved::"+ com1.size());
	}
	
    void CommentTest() {
        System.out.println("Starting Comment Test");
    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-comment.xml");
    	System.out.println("Done loading ctx");
        //Get the CommentDAO Bean
        CommentDAO commentDAO = ctx.getBean("commentDAO", CommentDAO.class);
        System.out.println("Created");
        
        //Run some tests for JDBC CRUD operations
        Comment com = new Comment();
        int rand = new Random().nextInt(1000);
        com.setId(rand);
        com.setComment("comentario do hu3");
        
        //Create
        commentDAO.save(com);
        System.out.println("Saved Comment");
        
         //Update
	     com.setComment("Mat eh gay");
	     commentDAO.update(com);
        
        //Read
        List<Comment> com1 = commentDAO.getByHyperLinkId(0);
        System.out.println("Comments Retrieved::"+ com1.size());
       
    }
    
    void HyperlinkTest() {
    	//Test
   	 ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-hyper.xml");
        
        //Get the HyperlinkDAO Bean
        HyperlinkDAO HyperlinkDAO = ctx.getBean("hyperlinkDAO", HyperlinkDAO.class);
        System.out.println("Created");
        
       List<Comment> comList = new ArrayList<Comment>();
       comList.add(new Comment("Testing comment 1"));
       comList.add(new Comment("Testing comment 2"));
       
       List<MetaTag> mtList = new ArrayList<MetaTag>();
       mtList.add(new MetaTag("meta tag"));
       mtList.add(new MetaTag("meta tag 2"));
       
        //Run some tests for JDBC CRUD operations
        Hyperlink hyp = new Hyperlink();
        hyp.setLink("www.googletesting.com");
        hyp.setComments(comList);
        hyp.setMetaTags(mtList);
         
        //Create
        HyperlinkDAO.save(hyp);
        System.out.println("Saved");
//         
        //Read
        Hyperlink hyp1 = HyperlinkDAO.getById(11);
        System.out.println("Hyperlink Retrieved::"+hyp1);
//         
//      //Update
        hyp.setId(11);
        hyp.setLink("www.ocu.com");
        List<MetaTag> mtList2 = new ArrayList<MetaTag>();
        mtList2.add(new MetaTag("meta tag UPDATE"));
        mtList2.add(new MetaTag("meta tag UPDATE 2"));
        hyp.setMetaTags(mtList2);
        HyperlinkDAO.update(hyp);
         
        
        //Get All
//        List<Hyperlink> hypList = HyperlinkDAO.getAll();
//        System.out.println("Number of results = " + hypList.size());
//        for (Hyperlink hype : hypList) {
//        	System.out.println(hype);
//        	for (Comment com: hype.getComments()) {
//        		System.out.println("\t" + com.getComment());
//        	}
//        	for (MetaTag com: hype.getMetaTags()) {
//        		System.out.println("\t" + com.getTag());
//        	}
//        }
         
//        //Delete
        HyperlinkDAO.deleteById(124);
//         
//        //Close Spring Context
        ctx.close();
//         
        System.out.println("DONE");
    }
}