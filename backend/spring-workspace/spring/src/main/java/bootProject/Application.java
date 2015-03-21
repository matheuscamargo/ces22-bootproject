package bootProject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Random;

import model.Hyperlink;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import db.HyperlinkDAO;

@SpringBootApplication
public class Application implements CommandLineRunner {

    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
    	
  
    	//Test
    	 ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
         
         //Get the HyperlinkDAO Bean
         HyperlinkDAO HyperlinkDAO = ctx.getBean("hyperlinkDAO", HyperlinkDAO.class);
         System.out.println("Created");
         
        // HyperlinkDAO.create();
         
         //Run some tests for JDBC CRUD operations
         Hyperlink hyp = new Hyperlink();
         int rand = new Random().nextInt(1000);
         hyp.setId(rand);
         hyp.setLink("www.google.com");
         hyp.setLastEditedAt(new Date());
          
         //Create
         HyperlinkDAO.save(hyp);
         System.out.println("Saved");
//          
         //Read
         Hyperlink hyp1 = HyperlinkDAO.getById(rand);
         System.out.println("Hyperlink Retrieved::"+hyp1);
//          
//         //Update
//         hyp.setLink("CEO");
//         HyperlinkDAO.update(hyp);
//          
         
         //Get All
//         List<Hyperlink> empList = HyperlinkDAO.getAll();
//         System.out.println(empList);
          
//         //Delete
//         HyperlinkDAO.deleteById(rand);
//          
//         //Close Spring Context
//         ctx.close();
//          
//         System.out.println("DONE");
   }
}