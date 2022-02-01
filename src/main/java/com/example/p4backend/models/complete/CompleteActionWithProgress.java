package com.example.p4backend.models.complete;

import com.example.p4backend.models.Action;
import com.example.p4backend.models.ActionImage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CompleteActionWithProgress extends CompleteAction{
    private double progress;

    public CompleteActionWithProgress(Action action, CompleteVzw vzw, double progress) {
        super(action, vzw);
        this.progress = progress;
    }
}
