package com.firrael.rx;

/**
 * Created by Railag on 01.06.2016.
 */
public class User { // TODO groups info
    private static User user;

    public static User get() { // TODO save fields to preferences
        if (user == null)
            user = new User();

        return user;
    }

    private String login;
    private String email;
    private String token;
    private String profileImageUrl;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}