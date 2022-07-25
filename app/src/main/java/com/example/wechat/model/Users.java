package com.example.wechat.model;

public class Users {
    private String id;
    private String username;
    private String ImageURL;
    private String status;
    private String search;
    private String typing;


    public Users(String id, String username, String imageURL, String status, String search, String typing) {
        this.id = id;
        this.username = username;
        ImageURL = imageURL;
        this.status = status;
        this.search = search;
        this.typing = typing;
    }

    public String getTyping() {
        return typing;
    }

    public void setTyping(String typing) {
        this.typing = typing;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Users(){}


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }
}
