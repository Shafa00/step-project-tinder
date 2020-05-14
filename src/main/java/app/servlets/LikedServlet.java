package app.servlets;

import app.dao.LikeDao;
import app.dao.UserDao;
import app.entities.User;
import app.tools.TemplateEngine;
import lombok.SneakyThrows;
import org.eclipse.jetty.servlet.Source;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class LikedServlet extends HttpServlet {
    private final TemplateEngine engine;

    public LikedServlet(TemplateEngine engine) throws SQLException {
        this.engine = engine;
    }

    UserDao userDao = new UserDao();
    LikeDao likeDao = new LikeDao();


    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = userDao.getUserByCookie(req);
        List<User> likedUsers = likeDao.getLikedUsers(currentUser);
        HashMap<String , Object> data = new HashMap<>();
        data.put("likedUsers", likedUsers);
        engine.render("people-list.ftl",data,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String button = req.getParameter("toMessage");
        Cookie cookies = new Cookie("msg", button);
        cookies.setMaxAge(60 * 60);
        resp.addCookie(cookies);
        resp.sendRedirect("/messages");
    }
}
