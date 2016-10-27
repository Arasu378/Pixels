package com.kyrostechnologies.thirunavukkarasu.pixels.modelclass;

/**
 * Created by Thirunavukkarasu on 25-10-2016.
 */

public class User {
    public String Email;
    public String UserName;
    public String UserId;
    public String ProfilePicture;
    public User(){

    }
    public User(String Email,String UserName,String UserId,String ProfilePicture){
        this.Email=Email;
        this.UserName=UserName;
        this.UserId=UserId;
        this.ProfilePicture=ProfilePicture;
    }
}
