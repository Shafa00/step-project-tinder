package app.tools;

import app.entities.Like;
import app.entities.Message;
import app.entities.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public User getUserById(int id) throws SQLException {
        return getUsers().stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .get();
    }

    public Optional<User> getUnvisitedUser(User user) throws SQLException {
        List<User> users = getUsers();
        List<User> visited = new ArrayList<>();

        Connection connection = DriverManager.getConnection(URL, USER, PASS);
        String SQL = "select * from likes where sender_id =?";
        PreparedStatement ps = connection.prepareStatement(SQL);
        ps.setInt(1, user.getId());
        ResultSet resultSet = ps.executeQuery();

        while(resultSet.next()) {
            int receiverId = resultSet.getInt("receiver_id");

            User current = users.stream()
                    .filter(u -> u.getId() == receiverId)
                    .findFirst().get();

            visited.add(current);
        }

        users.removeAll(visited);
        User me = users.stream().filter(u -> u.getId() == user.getId()).findFirst().get();
        users.remove(me);
        List<User> unvisitedUsers = users;

        if (unvisitedUsers.isEmpty()) {
            return Optional.empty();
        } else {
            Random random = new Random();
            int r = random.nextInt(unvisitedUsers.size());
            return Optional.of(unvisitedUsers.get(r));
        }
    }

    public void addAction(User sender, User receiver, String action) throws SQLException {
        Connection con = DriverManager.getConnection(URL, USER, PASS);
        String Sql = "insert into likes (sender_id, receiver_id, action) values (?,?,?)";
        PreparedStatement ps = con.prepareStatement(Sql);
        ps.setInt(1, sender.getId());
        ps.setInt(2, receiver.getId());
        ps.setString(3, action);
        ps.execute();
        con.close();
    }

    public List<User> getLikedUsers(User sender) throws SQLException {
        List<User> likedUsers = new ArrayList<>();
        Connection con = DriverManager.getConnection(URL, USER, PASS);
        String Sql = "select * from likes where sender_id = ? and action=?";
        PreparedStatement ps = con.prepareStatement(Sql);
        ps.setInt(1, sender.getId());
        ps.setString(2, "yes");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int receiverId = rs.getInt("receiver_id");
            likedUsers.add(getUserById(receiverId));
        }
        return likedUsers;
    }

    public List<User> getDislikedUsers(User sender) throws SQLException {
        List<User> likedUsers = new ArrayList<>();
        Connection con = DriverManager.getConnection(URL, USER, PASS);
        String Sql = "select * from likes where sender_id = ? and action=?";
        PreparedStatement ps = con.prepareStatement(Sql);
        ps.setInt(1, sender.getId());
        ps.setString(2, "no");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int receiverId = rs.getInt("receiver_id");
            likedUsers.add(getUserById(receiverId));
        }
        return likedUsers;
    }

    public List<Message> getMessages(User sender, User receiver) throws SQLException {
        List<Message> messages = new ArrayList<>();
        Connection con = DriverManager.getConnection(URL, USER, PASS);
        String Sql = "select * from messages where sender_id = ? and receiver_id = ?" +
                " order by message_date desc limit 10";
        PreparedStatement ps = con.prepareStatement(Sql);

        ps.setInt(1, sender.getId());
        ps.setInt(2, receiver.getId());

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            int senderId = rs.getInt("sender_id");
            int receiverId = rs.getInt("receiver_id");
            String context = rs.getString("context");
            String messageDate = rs.getString("message_date");
            messages.add(new Message(id, senderId, receiverId, context, messageDate));
        }
        con.close();
        return messages;
    }

    public void addMessage(User sender, User receiver, String context) throws SQLException {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String formatDateTime = now.format(format);

        if (context.matches("^\\s*$")) context = "no context";

        Connection con = DriverManager.getConnection(URL, USER, PASS);
        String Sql = "insert into messages (sender_id, receiver_id, context, message_date) values (?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(Sql);
        ps.setInt(1, sender.getId());
        ps.setInt(2, receiver.getId());
        ps.setString(3, context);
        ps.setString(4, formatDateTime);

        ps.execute();
        con.close();
    }

    public void addUser(String fullname, String email, String password, String image) throws SQLException {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String lastLogin = now.format(format);

        Connection con = DriverManager.getConnection(URL, USER, PASS);
        String Sql = "insert into users (fullname, email, password, last_login, image) values (?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(Sql);

        ps.setString(1, fullname);
        ps.setString(2, email);
        ps.setString(3, password);
        ps.setString(4, lastLogin);
        ps.setString(5, image);

        ps.execute();
        con.close();


    }

    public void updateLastLogin(User user) throws SQLException {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String formatDateTime = now.format(format);

        Connection con = DriverManager.getConnection(URL, USER, PASS);
        String Sql = "update users set last_login =? where id=?";
        PreparedStatement ps = con.prepareStatement(Sql);
        ps.setString(1, formatDateTime);
        ps.setInt(2, user.getId());

        ps.execute();
        con.close();
    }
}
