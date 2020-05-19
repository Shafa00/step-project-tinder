package app.servlets;

import app.dao.LikeDao;
import app.dao.UserDao;
import app.entities.User;
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
import java.util.List;

public class DislikeServlet extends HttpServlet {
    private final TemplateEngine engine;

    public DislikeServlet(TemplateEngine engine) throws SQLException {
        this.engine = engine;
    }

    UserDao userDao = new UserDao();
    LikeDao likeDao = new LikeDao();

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = userDao.getUserByCookie(req);
        List<User> likedUsers = likeDao.getDislikedUsers(currentUser);

        Cookie[] cookies = req.getCookies();
        Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("msg"))
                .forEach(cookie -> {
            cookie.setMaxAge(0);
            resp.addCookie(cookie);
        });

        HashMap<String , Object> data = new HashMap<>();
        data.put("likedUsers", likedUsers);
        data.put("like", "no");
        userDao.updateLastLogin(currentUser);
        engine.render("people-list.ftl",data,resp);
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String button = req.getParameter("toMessage");
        Cookie cookies = new Cookie("msg", button);
        cookies.setMaxAge(60 * 60);
        resp.addCookie(cookies);
        userDao.updateLastLogin(userDao.getUserByCookie(req));
        resp.sendRedirect("/messages");
    }
}
