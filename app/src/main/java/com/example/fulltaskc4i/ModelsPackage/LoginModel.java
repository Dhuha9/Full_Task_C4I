package com.example.fulltaskc4i.ModelsPackage;

public class LoginModel {
    private int id;
    private String email, password;

    public LoginModel(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public LoginModel(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
