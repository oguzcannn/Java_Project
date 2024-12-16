package org.example.Backend;


public interface UserInterface {

    String getUsername();

    void setUsername(String username);

    String getUserNumber();

    void setUserNumber(String userNumber);

    String getPassword();

    void setPassword(String password);

    String getEmail();

    void setEmail(String email);

    String[] getFriend_id();

    void setFriend_id(String[] friendIds);

    void removeFriend(String friendUsername);
}
