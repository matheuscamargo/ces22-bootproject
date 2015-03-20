package bootProject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import model.Hyperlink;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

@SpringBootApplication
public class Application implements CommandLineRunner {

    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("Creating tables");
        jdbcTemplate.execute("drop table customers if exists");
        jdbcTemplate.execute("create table customers(" +
                "id serial, first_name varchar(255), last_name varchar(255))");

        String[] fullNames = new String[]{"John Woo", "Jeff Dean", "Josh Bloch", "Josh Long"};
        for (String fullname : fullNames) {
            String[] name = fullname.split(" ");
            System.out.printf("Inserting customer record for %s %s\n", name[0], name[1]);
            jdbcTemplate.update(
                    "INSERT INTO customers(first_name,last_name) values(?,?)",
                    name[0], name[1]);
        }

        System.out.println("Querying for customer records where first_name = 'Josh':");
        List<Hyperlink> results = jdbcTemplate.query(
                "select id, first_name, last_name from customers where first_name = ?", new Object[] { "Josh" },
                new RowMapper<Hyperlink>() {
                    @Override
                    public Hyperlink mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Hyperlink(1, "www.google.com", null);
                    }
                });

        for (Hyperlink customer : results) {
            System.out.println(customer);
        }
    }
}