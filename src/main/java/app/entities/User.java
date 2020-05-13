package app.entities;

public class User {
    private final int id;
    private final String fullname;
    private final String email;
    private final String password;
    private final String lastLogin;
    private final String image;

    public User(int id, String fullname, String email, String password, String lastLogin,String image) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.lastLogin = lastLogin;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public String getImage() {
        return image;
    }

    @Override
    public String toString() {
        return String.format("User{id=%d, fullname='%s', email='%s', password='%s', lastLogin='%s'}", id, fullname, email, password, lastLogin);
    }
}
