package io.muzoo.ssc.webapp;

import io.muzoo.ssc.webapp.service.SecurityService;
import io.muzoo.ssc.webapp.service.UserService;
import io.muzoo.ssc.webapp.service.UserServiceAware;
import io.muzoo.ssc.webapp.servlet.HomeServlet;
import io.muzoo.ssc.webapp.servlet.LoginServlet;
import io.muzoo.ssc.webapp.servlet.RegisterServlet;
import jakarta.servlet.http.HttpServlet;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import java.util.ArrayList;
import java.util.List;

public class ServletRouter {

    private static final List<Class<? extends Routable>> routables = new ArrayList<>();

    static {
        routables.add(HomeServlet.class);
        routables.add(LoginServlet.class);
        routables.add(RegisterServlet.class);
    }

    private final SecurityService securityService;
    private final UserService userService;

    public ServletRouter(SecurityService securityService, UserService userService) {
        this.securityService = securityService;
        this.userService = userService;
    }

    public void init(Context ctx) {
        for (Class<? extends Routable> routableClass : routables) {
            try {
                Routable routable = routableClass.getDeclaredConstructor().newInstance();
                routable.setSecurityService(securityService);
                if (routable instanceof UserServiceAware aware) {
                    aware.setUserService(userService);
                }
                String name = routable.getClass().getSimpleName();
                Tomcat.addServlet(ctx, name, (HttpServlet) routable);
                ctx.addServletMappingDecoded(routable.getMapping(), name);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }
    }

}
