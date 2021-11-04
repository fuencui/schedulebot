package edu.northeastern.cs5500.starterbot.model;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Session implements Model {
    private ObjectId id;
    private ObjectId userId;
    
    
    @Override
    public ObjectId getId() {
        return this.id;
    }

    @Override
    public void setId(ObjectId id) {
        this.id = id;
        
    }

    public ObjectId getuserId() {
        return this.userId;
    }


    public void setuserId(ObjectId id) {
        this.userId = id;
        
    }
}
