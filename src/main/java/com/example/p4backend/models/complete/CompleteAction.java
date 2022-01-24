package com.example.p4backend.models.complete;

import com.example.p4backend.models.Action;
import com.example.p4backend.models.ActionImage;
import com.example.p4backend.models.Vzw;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.Decimal128;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    private List<ActionImage> actionImages = new ArrayList<>();

    public CompleteAction(Action action, CompleteVzw vzw, List<ActionImage> actionImages) {
        this.id = action.getId();
        this.name = action.getName();
        this.goal = action.getGoal();
        this.description = action.getDescription();
        this.startDate = action.getStartDate();
        this.endDate = action.getEndDate();
        this.vzw = vzw;
        this.actionImages.addAll(actionImages); // Add all images from parameters to the list of action images
    }
}
