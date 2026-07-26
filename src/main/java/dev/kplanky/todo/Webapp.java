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
import org.apache.catalina.valves.RemoteIpValve;
import org.apache.tomcat.util.descriptor.web.ErrorPage;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.apache.tomcat.util.http.Rfc6265CookieProcessor;

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

        // In production a TLS-terminating proxy sits in front, so the connection Tomcat sees is
        // plain HTTP. Without this valve request.isSecure() is false, the session cookie goes out
        // without the Secure flag, and one http:// navigation leaks JSESSIONID in cleartext.
        // The valve reads X-Forwarded-Proto (which the proxy sets) and fixes both that and the
        // client IP in the logs. protocolHeader has no default, so it must be named explicitly.
        // internalProxies defaults to the private ranges, which covers the docker network the
        // proxy reaches us on, and nothing else can reach this port.
        RemoteIpValve proxyValve = new RemoteIpValve();
        proxyValve.setProtocolHeader("X-Forwarded-Proto");
        tomcat.getEngine().getPipeline().addValve(proxyValve);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        UserRepository userRepository = new UserRepository(dataSource);
        UserService userService = new UserService(userRepository, passwordEncoder);
        SecurityService securityService = new SecurityService(userService);

        TodoRepository todoRepository = new TodoRepository(dataSource);
        TodoService todoService = new TodoService(todoRepository);

        ServletRouter servletRouter = new ServletRouter(securityService, userService, todoService);

        Context ctx = tomcat.addWebapp("", TomcatEnvironment.getDocBase().getAbsolutePath());

        // Lax keeps the session cookie off cross-site POSTs, which is what a CSRF against /add,
        // /edit, /delete or /toggle would need. Chrome defaults to Lax already; Firefox does not,
        // so state it rather than relying on the browser. Tomcat adds Secure and HttpOnly itself
        // once the request is seen as HTTPS, see the RemoteIpValve above.
        Rfc6265CookieProcessor cookieProcessor = new Rfc6265CookieProcessor();
        cookieProcessor.setSameSiteCookies("Lax");
        ctx.setCookieProcessor(cookieProcessor);

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
