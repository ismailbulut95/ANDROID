package com.demo.mobilproje.Model;

public class Users {

    private String id;
    private String username;
    private String imageURL;

    public Users(){
    }
    public Users(String id, String username, String imageURL){
        this.setId(id);
        this.setUsername(username);
        this.setImageURL(imageURL);
    }

    //region --> Encapsulation
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
        return imageURL;
    }
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
    //endregion
}
