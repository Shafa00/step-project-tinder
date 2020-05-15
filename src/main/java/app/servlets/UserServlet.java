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
import java.util.Optional;

public class UserServlet extends HttpServlet {
    private final TemplateEngine engine;

    public UserServlet(TemplateEngine engine) throws SQLException {
        this.engine = engine;
    }

    UserDao userDao = new UserDao();
    LikeDao likeDao = new LikeDao();

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HashMap<String, Object> data = new HashMap<>();
        User currentUser = userDao.getUserByCookie(req);
        userDao.updateLastLogin(currentUser);
        printUnlikedUser(resp, data, currentUser);
    }


    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String button = req.getParameter("option");
        User currentUser = userDao.getUserByCookie(req);
        HashMap<String, Object> data = new HashMap<>();
        Cookie[] cookies = req.getCookies();

        String email = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("like"))
                .findFirst()
                .get()
                .getValue();

        User likedUser = userDao.getUsers().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst().get();

        if (button.equals("yes")) {
            likeDao.addLike(currentUser, likedUser);
        }
        userDao.updateLastLogin(currentUser);
        printUnlikedUser(resp, data, currentUser);
    }

    private void printUnlikedUser(HttpServletResponse resp, HashMap<String, Object> data, User currentUser) throws SQLException, IOException {
        Optional<User> unLikedUser = userDao.getUnLikedUser(currentUser);


        if (unLikedUser.equals(Optional.empty())) {
            resp.sendRedirect("/liked");
        } else {
            Cookie cookie = new Cookie("like", unLikedUser.get().getEmail());
            resp.addCookie(cookie);
            data.put("user", unLikedUser.get());
            engine.render("like-page.ftl", data, resp);
        }
    }
}
