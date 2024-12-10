package org.example.Backend;
public class User {
    private String username;
    private String userNumber;
    private String password;
    private String email;
    private String[] friend_id;

    public User(String username, String userNumber, String password, String email ) {
        this.username = username;
        this.userNumber = userNumber;
        this.password = password;
        this.email = email;
    }

    // Getter ve Setter metodları
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String[] getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String[] friend_id) {
        this.friend_id = friend_id;
    }

}

