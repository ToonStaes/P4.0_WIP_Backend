package com.example.p4backend.controllers;

import com.example.p4backend.models.Action;
import com.example.p4backend.models.ActionImage;
import com.example.p4backend.repositories.ActionImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class ActionImageController {
    @Autowired
    private ActionImageRepository actionImageRepository;

    @PostConstruct
    public void fillDB(){
        if (actionImageRepository.count() == 0){
            ActionImage image1 = new ActionImage("https://http.cat/400.jpg", "action1");
            ActionImage image2 = new ActionImage("https://http.cat/401.jpg", "action1");
            ActionImage image3 = new ActionImage("https://http.cat/402.jpg", "action2");
            ActionImage image4 = new ActionImage("https://http.cat/403.jpg", "action2");
            ActionImage image5 = new ActionImage("https://http.cat/404.jpg", "action2");
            ActionImage image6 = new ActionImage("https://http.cat/405.jpg", "action3");
            ActionImage image7 = new ActionImage("https://http.cat/406.jpg", "action4");

            actionImageRepository.save(image1);
            actionImageRepository.save(image2);
            actionImageRepository.save(image3);
            actionImageRepository.save(image4);
            actionImageRepository.save(image5);
            actionImageRepository.save(image6);
            actionImageRepository.save(image7);
        }
    }

    @GetMapping("/actionimages")
    public List<ActionImage> getAll() {
        return actionImageRepository.findAll();
    }

    @GetMapping("/actionimages/{id}")
    public ActionImage getActionImageByID(@PathVariable String id){
        Optional<ActionImage> image = actionImageRepository.findById(id);

        if (image.isPresent()) {
            return image.get();
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "The ActionImage with ID " + id + " doesn't exist"
            );
        }
    }

    @GetMapping("/actionimages/actionID/{id}")
    public List<ActionImage> getActionImagesByActionID(@PathVariable String id){
        return actionImageRepository.findActionImagesByActionId(id);
    }
}
