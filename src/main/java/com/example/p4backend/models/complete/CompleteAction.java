package com.example.p4backend.models.complete;

import com.example.p4backend.models.Action;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.Decimal128;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CompleteAction {
    private String id;
    private String name;
    private Decimal128 goal;
    private String description;
    private Date startDate;
    private Date endDate;
    private CompleteVzw vzw;
    private Set<String> images = new HashSet<>(); // Set to remove dupe images
    private boolean isActive;

    public CompleteAction(Action action, CompleteVzw vzw, List<String> images) {
        this.id = action.getId();
        this.name = action.getName();
        this.goal = action.getGoal();
        this.description = action.getDescription();
        this.startDate = action.getStartDate();
        this.endDate = action.getEndDate();
        this.vzw = vzw;
        this.images.addAll(images);
        this.isActive = action.isActive();
    }
}
