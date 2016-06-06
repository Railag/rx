package com.firrael.rx.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.firrael.rx.Utils;

/**
 * Created by Railag on 01.06.2016.
 */
public class User { // TODO groups info
    private static User user;

    private final static String LOGIN_KEY = "login";
    private final static String EMAIL_KEY = "email";
    private final static String TOKEN_KEY = "token";
    private final static String IMAGE_URL_KEY = "imageUrl";

    public static User get(Context context) {
        if (user == null)
            user = loadUser(context);

        return user;
    }

    public static void save(UserResult result, Context context) {
        if (result == null)
            return;


        User user = get(context);
        user.login = result.login;
        user.email = result.email;
        user.token = result.token;
        user.profileImageUrl = result.profileImageUrl;

        Utils.prefs(context)
                .edit()
                .putString(LOGIN_KEY, user.login)
                .putString(EMAIL_KEY, user.email)
                .putString(TOKEN_KEY, user.token)
                .putString(IMAGE_URL_KEY, user.profileImageUrl)
                .commit();
    }

    public static void logout(Context context) {
        user = null;
        Utils.prefs(context).edit().clear().commit();
    }

    private static User loadUser(Context context) {
        SharedPreferences prefs = Utils.prefs(context);
        String token = prefs.getString(TOKEN_KEY, "");
        if (TextUtils.isEmpty(token)) {
            return new User();
        } else {
            User user = new User();
            user.login = prefs.getString(LOGIN_KEY, "");
            user.email = prefs.getString(EMAIL_KEY, "");
            user.token = prefs.getString(TOKEN_KEY, "");
            user.profileImageUrl = prefs.getString(IMAGE_URL_KEY, "");
            return user;
        }
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