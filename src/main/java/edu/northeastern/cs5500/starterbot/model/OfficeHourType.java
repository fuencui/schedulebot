package edu.northeastern.cs5500.starterbot.model;

import javax.annotation.Nonnull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;

@Data
@RequiredArgsConstructor
public class OfficeHourType {
    ObjectId id;
    @Nonnull private String typeName; // remote, in-person
}
