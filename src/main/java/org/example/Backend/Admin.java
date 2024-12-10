package org.example.Backend;

public class Admin extends User{
    public Admin(String user_id, String user_name, String user_password,String mail, String admin_level) {
        super(user_id, user_name, user_password,mail);
    }
}
