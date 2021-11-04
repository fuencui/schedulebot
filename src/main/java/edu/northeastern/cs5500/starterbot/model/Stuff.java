package edu.northeastern.cs5500.starterbot.model;

//import com.fasterxml.jackson.annotation.JsonIgnore;

import org.bson.types.ObjectId;

import lombok.Data;

@Data
public class Stuff implements Model {
    private ObjectId id;
    private String title;
    //private String description;

    /** @return true if this Stuff is valid */
    //@JsonIgnore
    public boolean isValid() {
        return title != null && !title.isEmpty();
    }

    @Override
    public ObjectId getId() {
        return this.id;
    }

    @Override
    public void setId(ObjectId id) {
        this.id = id;
        
    }
}
