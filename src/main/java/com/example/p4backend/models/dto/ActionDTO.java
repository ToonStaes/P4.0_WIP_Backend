package com.example.p4backend.models.dto;

import com.mongodb.lang.Nullable;
import lombok.Getter;
import org.bson.types.Decimal128;

import java.util.Date;

@Getter
public class ActionDTO {
    private String name;
    private Decimal128 goal;
    private String description;
    private String vzwID;
    private Date endDate;
}
