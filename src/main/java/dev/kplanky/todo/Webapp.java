package dev.kplanky.todo;

import dev.kplanky.todo.db.Database;
import dev.kplanky.todo.db.Schema;
import dev.kplanky.todo.filter.AuthFilter;
import dev.kplanky.todo.repository.TodoRepository;
import dev.kplanky.todo.repository.UserRepository;
import dev.kplanky.todo.service.*;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ErrorPage;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Webapp {

    private static final int DEFAULT_PORT = 8080;

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

        Schema.apply(dataSource);

        TomcatEnvironment.init();
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(TomcatEnvironment.getWorkDir().getAbsolutePath());
        tomcat.setPort(port());
        tomcat.getConnector();

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        UserRepository userRepository = new UserRepository(dataSource);
        UserService userService = new UserService(userRepository, passwordEncoder);
        SecurityService securityService = new SecurityService(userService);

        TodoRepository todoRepository = new TodoRepository(dataSource);
        TodoService todoService = new TodoService(todoRepository);

        ServletRouter servletRouter = new ServletRouter(securityService, userService, todoService);

        Context ctx = tomcat.addWebapp("", TomcatEnvironment.getDocBase().getAbsolutePath());
        servletRouter.init(ctx);

        AuthFilter authFilter = new AuthFilter(securityService);

        FilterDef filterDef = new FilterDef();
        filterDef.setFilterName("AuthFilter");
        filterDef.setFilter(authFilter);
        ctx.addFilterDef(filterDef);

        FilterMap filterMap = new FilterMap();
        filterMap.setFilterName("AuthFilter");
        filterMap.addURLPattern("/*");
        ctx.addFilterMap(filterMap);

        ErrorPage notFoundPage = new ErrorPage();
        notFoundPage.setErrorCode(404);
        notFoundPage.setLocation("/notFound");
        ctx.addErrorPage(notFoundPage);

        try {
            tomcat.start();
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }
        tomcat.getServer().await();
    }

    private static int port() {
        String value = System.getenv("PORT");
        if (value == null || value.isBlank()) {
            return DEFAULT_PORT;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalStateException("PORT is not a number: " + value, e);
        }
    }

}
