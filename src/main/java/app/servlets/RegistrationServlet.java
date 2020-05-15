package app.servlets;

import app.dao.UserDao;
import app.tools.CookieFilter;
import app.tools.TemplateEngine;
import lombok.SneakyThrows;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;

public class RegistrationServlet extends HttpServlet {
    TemplateEngine engine = TemplateEngine.folder("content");

    public RegistrationServlet() throws IOException, SQLException {
    }

    UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HashMap<String, Object> data = new HashMap<>();

        CookieFilter cookieFilter = new CookieFilter();
        if (!cookieFilter.isLogged(req)) {
            data.put("error", "noError");
            engine.render("registration.ftl", data, resp);
        } else {
            resp.sendRedirect("/users");
        }
    }


    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HashMap<String, Object> data = new HashMap<>();

        String email = req.getParameter("email");
        String fullname = req.getParameter("fullname");
        String password = req.getParameter("password");
        String confirmed = req.getParameter("confirmed");
        String img = req.getParameter("img");

        if (!password.equals(confirmed)) {
            data.put("error", "passError");
            engine.render("registration.ftl", data, resp);
        } else if (userDao.checkDuplicate(email)) {
            data.put("error", "duplicate");
            engine.render("registration.ftl", data, resp);
        } else {
            String imageName = uploadImage(req);
            userDao.addUser(fullname, email, password, imageName);
            resp.sendRedirect("/login");
        }
    }

    String uploadImage(HttpServletRequest req) throws IOException, ServletException {
        StringBuilder fileNameBuilder = new StringBuilder();

        Part p = req.getPart("img");
        InputStream partIS = p.getInputStream();
        String fileSubName = p.getSubmittedFileName();
        String filename = String.format("%s", fileSubName);
        Files.copy(partIS, Paths.get("content/img/" + filename),
                StandardCopyOption.REPLACE_EXISTING);
        fileNameBuilder.append("img/").append(filename);

        return fileNameBuilder.toString();
    }
}
