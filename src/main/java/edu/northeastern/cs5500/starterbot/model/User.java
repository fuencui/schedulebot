package edu.northeastern.cs5500.starterbot.model;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class User implements Model {
    private ObjectId id;
    // private String NUID;
    private String UserName;
    private String NUID;
    private Role userRoles;

    public User(String UserName, String NUID, Role userRoles) {
        this.UserName = UserName;
        this.NUID = NUID;
        this.userRoles = userRoles;
    }
}
