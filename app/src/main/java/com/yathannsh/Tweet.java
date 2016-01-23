package com.yathannsh;

/**
 * Created by yathannsh on 1/20/2016.
 */
public class Tweet {
    String username;
    String profile_url;
    String message;
    java.util.Date created_at;

    public Tweet(String username, String profile_url, String message, java.util.Date created_at) {
        this.username = username;
        this.profile_url = profile_url;
        this.message = message;
        this.created_at = created_at;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public java.util.Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(java.util.Date created_at) {
        this.created_at = created_at;
    }
}
