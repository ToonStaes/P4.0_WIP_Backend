package com.example.p4backend.models.complete;

import com.example.p4backend.models.Action;
import com.example.p4backend.models.Vzw;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.Decimal128;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ActionWithVzw {
    private String id;
    private String name;
    private Decimal128 goal;
    private String description;
    private Date startDate;
    private Date endDate;
    private Vzw vzw;

    public ActionWithVzw(Action action, Vzw vzw) {
        this.id = action.getId();
        this.name = action.getName();
        this.goal = action.getGoal();
        this.description = action.getDescription();
        this.startDate = action.getStartDate();
        this.endDate = action.getEndDate();
        this.vzw = vzw;
    }
}
