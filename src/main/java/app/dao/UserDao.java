package app.dao;

import app.entities.User;
import app.tools.ConnectionTool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UserDao {
    ConnectionTool con = new ConnectionTool();
    List<User> users = getUsers();

    public UserDao() throws SQLException {
    }

    public List<User> getUsers() throws SQLException {
        return con.getUsers();
    }

    public boolean checkUser(String email, String password) throws SQLException {
        return con.getUsers().stream().anyMatch(u ->u.getEmail().equals(email) && u.getPassword().equals(password));
    }

    public User getUserByCookie(HttpServletRequest req) throws SQLException {
        Cookie[] cookies = req.getCookies();
        String email = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("login"))
                .findFirst()
                .get()
                .getValue();

        return getUsers().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .get();

    }

    public Optional<User> getUnLikedUser(User user) throws SQLException {
        return con.getUnLikedUser(user);
    }

    public User getUserById(int id) throws SQLException {
        return con.getUserById(id);
    }

    public boolean checkDuplicate(String email) throws SQLException {
        return getUsers().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    public void addUser(String fullname, String email, String password, String image) throws SQLException {
        con.addUser(fullname, email, password, image);
    }

    public void updateLastLogin(User user) throws SQLException {
        con.updateLastLogin(user);
    }
}
