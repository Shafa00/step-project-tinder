package app;

import app.servlets.*;
import app.tools.CookieFilter;
import app.tools.TemplateEngine;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class TinderApp {
    public static void main(String[] args) throws Exception {
        Server server = new Server(9000);
        ServletContextHandler handler = new ServletContextHandler();
        TemplateEngine engine = TemplateEngine.folder("content");

        handler.addServlet(new ServletHolder(new MainServlet()), "/*");
        handler.addServlet(new ServletHolder(new LinkServlet("css")), "/css/*");
        handler.addServlet(new ServletHolder(new LinkServlet("img")), "/img/*");
        handler.addServlet(new ServletHolder(new LoginServlet(engine)), "/login");
        handler.addServlet(new ServletHolder(new RegistrationServlet(engine)), "/reg");
        handler.addServlet(new ServletHolder(new UserServlet(engine)), "/users");
        handler.addServlet(new ServletHolder(new LikedServlet(engine)), "/liked");
        handler.addServlet(new ServletHolder(new MessageServlet(engine)), "/messages");

        handler.addFilter(new FilterHolder(new CookieFilter()), "/users", EnumSet.of(DispatcherType.REQUEST));
        handler.addFilter(new FilterHolder(new CookieFilter()), "/liked", EnumSet.of(DispatcherType.REQUEST));
        handler.addFilter(new FilterHolder(new CookieFilter()), "/messages", EnumSet.of(DispatcherType.REQUEST));

        server.setHandler(handler);
        server.start();
        server.join();
    }
}
