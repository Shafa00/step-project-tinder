package app.dao;

import app.entities.Message;
import app.entities.User;
import app.tools.ConnectionTool;

import java.sql.SQLException;
import java.util.List;

public class MessageDao {

    ConnectionTool con = new ConnectionTool();

    public List<Message> getMessages(User sender, User receiver) throws SQLException {
        return con.getMessages(sender, receiver);
    }

    public void addMessage(User sender, User receiver, String context) throws SQLException {
        con.addMessage(sender, receiver, context);
    }
}
