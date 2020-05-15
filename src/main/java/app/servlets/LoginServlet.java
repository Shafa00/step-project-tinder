package app.servlets;

import app.dao.UserDao;
import app.tools.CookieFilter;
import app.tools.TemplateEngine;
import lombok.SneakyThrows;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;

public class LoginServlet extends HttpServlet {
    private final TemplateEngine engine;

    public LoginServlet(TemplateEngine engine) throws SQLException {
        this.engine = engine;
    }

    UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HashMap<String, Object> data = new HashMap<>();
        CookieFilter cookieFilter = new CookieFilter();
        if (!cookieFilter.isLogged(req)) {
            data.put("error", "noerror");
            engine.render("login.ftl", data, resp);
        } else resp.sendRedirect("/users");
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HashMap<String, Object> data = new HashMap<>();

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (userDao.checkUser(email, password)) {
            Cookie cookie = new Cookie("login", email);
            cookie.setMaxAge(60 * 60 * 24);
            resp.addCookie(cookie);
            resp.sendRedirect("/users");
        } else {
            data.put("error", "noMatch");
            engine.render("login.ftl", data, resp);
        }
    }
}
