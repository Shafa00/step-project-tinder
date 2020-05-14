package app.servlets;

import app.dao.MessageDao;
import app.dao.UserDao;
import app.entities.Message;
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
import java.util.*;

public class MessageServlet extends HttpServlet {

    private final TemplateEngine engine;

    public MessageServlet(TemplateEngine engine) throws SQLException {
        this.engine = engine;
    }

    UserDao userDao = new UserDao();
    MessageDao messageDao = new MessageDao();

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HashMap<String, Object> data = new HashMap<>();

        Optional<Cookie> oppositeUserCk = Arrays.stream(req.getCookies())
                .filter(cookie -> cookie.getName().equals("msg"))
                .findFirst();

        if (oppositeUserCk.equals(Optional.empty())) resp.sendRedirect("/liked");
        else {
            User currentUser = userDao.getUserByCookie(req);
            int oppositeId = Integer.parseInt(oppositeUserCk.get().getValue());
            User oppositeUser = userDao.getUserById(oppositeId);

            List<Message> sent = messageDao.getMessages(currentUser, oppositeUser);
            List<Message> received = messageDao.getMessages(oppositeUser, currentUser);
            List<Message> allMessages = new ArrayList<>();

            allMessages.addAll(sent);
            allMessages.addAll(received);

            Comparator<Message> compareById = Comparator.comparing(Message::getId);
            allMessages.sort(compareById);

            data.put("messages", allMessages);
            data.put("currentUser", currentUser);
            data.put("oppositeUser", oppositeUser);
            engine.render("chat.ftl", data, resp);
        }


    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String button = req.getParameter("send");
        String context = req.getParameter("context");

        HashMap<String, Object> data = new HashMap<>();
        User sender = userDao.getUserByCookie(req);
        Optional<Cookie> receiverCk = Arrays.stream(req.getCookies()).filter(cookie -> cookie.getName().equals("msg"))
                .findFirst();
        if (receiverCk.equals(Optional.empty())) {
            resp.sendRedirect("/liked");
        } else {
            int receiverId = Integer.parseInt(receiverCk.get().getValue());
            User receiver = userDao.getUserById(receiverId);

            if (button.equals("send")) {
                messageDao.addMessage(sender, receiver, context);
                List<Message> sent = messageDao.getMessages(sender, receiver);
                List<Message> received = messageDao.getMessages(receiver, sender);
                List<Message> allMessages = new ArrayList<>();

                allMessages.addAll(sent);
                allMessages.addAll(received);

                Comparator<Message> compareById = Comparator.comparing(Message::getId);
                allMessages.sort(compareById);

                data.put("messages", allMessages);
                data.put("currentUser", sender);
                data.put("oppositeUser", receiver);
                engine.render("chat.ftl", data, resp);            }
        }


    }
}

