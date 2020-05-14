package app.dao;

import app.entities.User;
import app.tools.ConnectionTool;

import java.sql.SQLException;
import java.util.List;

public class LikeDao {

    ConnectionTool con = new ConnectionTool();

    public void addLike(User sender, User receiver) throws SQLException {
        con.addLike(sender, receiver);
    }

    public List<User> getLikedUsers(User sender) throws SQLException {
        return con.getLikedUsers(sender);
    }
}
