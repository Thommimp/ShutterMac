package com.example.realshit.Model;

public class User {

    private String email;
    private String username;
    private String birthday;
    private String bio;
    private String imageUrl;
    private String id;

    public User() {
    }

    public User(String email, String username, String birthday, String bio, String imageUrl, String id) {
        this.email = email;
        this.username = username;
        this.birthday = birthday;
        this.bio = bio;
        this.imageUrl = imageUrl;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
