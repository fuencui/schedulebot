package edu.northeastern.cs5500.starterbot.model;

//import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

@Data
public class registerList implements Model {
    private ObjectId id;
    private List<String> nameList = new ArrayList<>();

    public void addNameToList(String name) {
        this.nameList.add(name);
    }

    @Override
    public ObjectId getId() {
        return this.id;
    }

    @Override
    public void setId(ObjectId id) {
        this.id = id;
        
    }

    public void setNameList(List<String> nameList){
        this.nameList = nameList;
    }

    public List<String> getNameList() {
        return this.nameList;
    }
}