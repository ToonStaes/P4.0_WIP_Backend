package com.example.p4backend.controllers;

import com.example.p4backend.models.Action;
import com.example.p4backend.models.Vzw;
import com.example.p4backend.models.complete.ActionWithVzw;
import com.example.p4backend.repositories.ActionRepository;
import com.example.p4backend.repositories.VzwRepository;
import org.bson.types.Decimal128;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import javax.management.relation.RelationNotFoundException;
import java.util.*;

@CrossOrigin(origins = "", allowedHeaders = "")
@RestController
public class ActionController {

    @Autowired
    private ActionRepository actionRepository;
    @Autowired
    private VzwRepository vzwRepository;

    @PostConstruct
    public void fillDB() {
        if (actionRepository.count() == 0) {
            Action action1 = new Action(
                    "action1",
                    new Decimal128(500),
                    "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
                    "vzw1",
                    new GregorianCalendar(2022, Calendar.FEBRUARY, 18).getTime());
            action1.setId("action1");

            Action action2 = new Action(
                    "action2",
                    new Decimal128(200),
                    "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
                    "vzw2",
                    new GregorianCalendar(2022, Calendar.MARCH, 18).getTime());
            action2.setId("action2");

            Action action3 = new Action(
                    "action3",
                    new Decimal128(400),
                    "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
                    "vzw3",
                    new GregorianCalendar(2022, Calendar.APRIL, 28).getTime());
            action3.setId("action3");

            Action action4 = new Action(
                    "action4",
                    new Decimal128(450),
                    "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
                    "vzw4",
                    new GregorianCalendar(2022, Calendar.FEBRUARY, 28).getTime());
            action4.setId("action4");

            actionRepository.save(action1);
            actionRepository.save(action2);
            actionRepository.save(action3);
            actionRepository.save(action4);
        }
    }

    @GetMapping("/actions")
    public List<ActionWithVzw> getAll() throws RelationNotFoundException {
        List<ActionWithVzw> returnList = new ArrayList<>();
        List<Action> actions = actionRepository.findAll();

        for (Action action : actions) {
            ActionWithVzw actionToAdd = getActionWithVzw(action);
            returnList.add(getActionWithVzw(action));
        }
        return returnList;
    }

    @GetMapping("/actions/{id}")
    public ActionWithVzw getActionById(@PathVariable String id) throws RelationNotFoundException {
        Optional<Action> action = actionRepository.findById(id);

        if (action.isPresent()) {
            return getActionWithVzw(Objects.requireNonNull(action.get()));
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "The Action with ID " + id + " doesn't exist"
        );
    }

    // Get the filled ActionWithVzw for the given action
    private ActionWithVzw getActionWithVzw(Action action) {
        Optional<Vzw> vzw = vzwRepository.findById(action.getVzwID());
        return new ActionWithVzw(action, vzw);
    }
}
