package com.example.p4backend.models.complete;

import com.example.p4backend.models.Action;
import com.example.p4backend.models.ActionImage;
import com.example.p4backend.models.Vzw;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class CompleteActionWithProgress extends CompleteAction{
    private double progress;

    public CompleteActionWithProgress(Action action, Optional<Vzw> vzw, List<ActionImage> actionImages, double progress) {
        super(action, vzw, actionImages);
        this.progress = progress;
    }
}
