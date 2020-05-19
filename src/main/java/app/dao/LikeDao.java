package app.dao;

import app.entities.User;
import app.tools.ConnectionTool;

import java.sql.SQLException;
import java.util.List;

public class LikeDao {

    ConnectionTool con = new ConnectionTool();

    public void addAction(User sender, User receiver, String action) throws SQLException {
        con.addAction(sender, receiver, action);
    }

    public List<User> getLikedUsers(User sender) throws SQLException {
        return con.getLikedUsers(sender);
    }
    public List<User> getDislikedUsers(User sender) throws SQLException {
        return con.getDislikedUsers(sender);
    }
}
