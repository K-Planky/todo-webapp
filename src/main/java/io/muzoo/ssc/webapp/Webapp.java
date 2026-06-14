package io.muzoo.ssc.webapp;

import io.muzoo.ssc.assignment.tracker.SscAssignment;
import io.muzoo.ssc.webapp.db.Database;
import io.muzoo.ssc.webapp.repository.DuplicateUsernameException;
import io.muzoo.ssc.webapp.repository.UserRepository;
import io.muzoo.ssc.webapp.service.BCryptPasswordEncoder;
import io.muzoo.ssc.webapp.service.PasswordEncoder;
import io.muzoo.ssc.webapp.service.SecurityService;
import io.muzoo.ssc.webapp.service.UserService;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Webapp extends SscAssignment {

    public static void main(String[] args) {

        DataSource dataSource = Database.createDataSource();
        try (Connection c = dataSource.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT 1")) {
            rs.next();
            System.out.println("Database connection OK");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot reach the database at startup", e);
        }


        TomcatEnvironment.init();
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(TomcatEnvironment.getWorkDir().getAbsolutePath());
        tomcat.setPort(8082);
        tomcat.getConnector();

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        UserRepository userRepository = new UserRepository(dataSource);
        UserService userService = new UserService(userRepository, passwordEncoder);
        SecurityService securityService = new SecurityService(userService);

        try {
            userService.register("alice", "password123");
        } catch (DuplicateUsernameException ignored) {
        }

        ServletRouter servletRouter = new ServletRouter(securityService, userService);

        Context ctx = tomcat.addWebapp("", TomcatEnvironment.getDocBase().getAbsolutePath());

        servletRouter.init(ctx);

        try {
            tomcat.start();
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }
        tomcat.getServer().await();
    }

}
