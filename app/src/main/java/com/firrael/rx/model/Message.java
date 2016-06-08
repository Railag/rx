package com.firrael.rx.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Railag on 03.06.2016.
 */
public class Message {
    @SerializedName("group_id")
    private long groupId;
    @SerializedName("user_id")
    private long userId;
    @SerializedName("user_login")
    private String userLogin;
    @SerializedName("user_image_url")
    private String userImageUrl;
    @SerializedName("text")
    private String message;
    private Date date;

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }
}
