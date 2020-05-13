package app.tools;

import app.entities.Like;
import app.entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

public class ConnectionTool {
    private final static String URL = "jdbc:postgresql://localhost:5432/Tinder";
    private final static String USER = "postgres";
    private final static String PASS = "secret";

    public List<User> getUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        Connection con = DriverManager.getConnection(URL, USER, PASS);
        String Sql = "select * from users";
        PreparedStatement statement = con.prepareStatement(Sql);

        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id");
            String fullname = rs.getString("fullname");
            String email = rs.getString("email");
            String password = rs.getString("password");
            String lastLogin = rs.getString("last_login");
            String image = rs.getString("image");
            users.add(new User(id, fullname, email, password, lastLogin, image));
        }

        con.close();
        return users;
    }

    public Optional<User> getUnLikedUser(User user) throws SQLException {
        List<User> users = getUsers();
        List<User> likedUsers = new ArrayList<>();

        Connection con = DriverManager.getConnection(URL, USER, PASS);
        String Sql = "select * from likes where sender_id = ?";
        PreparedStatement statement = con.prepareStatement(Sql);
        statement.setInt(1, user.getId());

        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            int senderId = rs.getInt("sender_id");
            int receiverId = rs.getInt("receiver_id");

            User likedUser = users.stream()
                    .filter(u -> u.getId() == receiverId)
                    .findFirst().get();

            likedUsers.add(likedUser);
        }

        users.removeAll(likedUsers);
        User me = users.stream().filter(u -> u.getId() == user.getId()).findFirst().get();
        users.remove(me);
        List<User> unLikedUsers = users;

        if (unLikedUsers.isEmpty()) {
            con.close();
            return Optional.empty();
        } else {
            Random random = new Random();
            int r = random.nextInt(unLikedUsers.size());
            con.close();
            return Optional.of(unLikedUsers.get(r));
        }

    }
}
