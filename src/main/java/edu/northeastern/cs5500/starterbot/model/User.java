package edu.northeastern.cs5500.starterbot.model;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class User implements Model {
    private ObjectId id;
    // private String NUID;
    private String UserName;

    public User(String UserName) {
        this.UserName = UserName;
    }
}
