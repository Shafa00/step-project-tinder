package app.dao;

import app.entities.User;
import app.tools.ConnectionTool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.ArrayList;
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

    public boolean checkUser(String email, String password) {
        return users.stream().anyMatch(u ->u.getEmail().equals(email) && u.getPassword().equals(password));
    }

    public User getUserByCookie(HttpServletRequest req){
        Cookie[] cookies = req.getCookies();
        String email = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("login"))
                .findFirst()
                .get()
                .getValue();

        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .get();

    }

    public Optional<User> getUnLikedUser(User user) throws SQLException {
        return con.getUnLikedUser(user);
    }
}
