package com.example.p4backend.controllers;

import com.example.p4backend.models.Action;
import com.example.p4backend.models.ActionImage;
import com.example.p4backend.models.Vzw;
import com.example.p4backend.models.complete.CompleteAction;
import com.example.p4backend.repositories.ActionImageRepository;
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
import java.util.stream.Collectors;

@CrossOrigin(origins = "", allowedHeaders = "")
@RestController
public class ActionController {

    @Autowired
    private ActionRepository actionRepository;
    @Autowired
    private VzwRepository vzwRepository;
    @Autowired
    private ActionImageRepository actionImageRepository;

    @PostConstruct
    public void fillDB() throws InterruptedException {
        actionRepository.deleteAll();
        if (actionRepository.count() == 0) {
            Action action1 = new Action(
                    "action1",
                    new Decimal128(500),
                    "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
                    "vzw1",
                    new GregorianCalendar(2022, Calendar.FEBRUARY, 18).getTime());
            action1.setId("action1");
            action1.setStartDate(new GregorianCalendar(2022, Calendar.JANUARY, 18).getTime());

            Thread.sleep(2000);

            Action action2 = new Action(
                    "action2",
                    new Decimal128(200),
                    "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
                    "vzw2",
                    new GregorianCalendar(2022, Calendar.MARCH, 18).getTime());
            action2.setId("action2");
            action2.setStartDate(new GregorianCalendar(2022, Calendar.JANUARY, 16).getTime());

            Thread.sleep(2000);

            Action action3 = new Action(
                    "action3",
                    new Decimal128(400),
                    "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
                    "vzw3",
                    new GregorianCalendar(2022, Calendar.APRIL, 28).getTime());
            action3.setId("action3");
            action3.setStartDate(new GregorianCalendar(2022, Calendar.JANUARY, 17).getTime());

            Thread.sleep(2000);

            Action action4 = new Action(
                    "action4",
                    new Decimal128(450),
                    "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
                    "vzw4",
                    new GregorianCalendar(2022, Calendar.FEBRUARY, 28).getTime());
            action4.setId("action4");
            action4.setStartDate(new GregorianCalendar(2021, Calendar.DECEMBER, 2).getTime());

            actionRepository.save(action1);
            actionRepository.save(action2);
            actionRepository.save(action3);
            actionRepository.save(action4);
        }
    }

    @GetMapping("/actions")
    public List<CompleteAction> getAll() throws RelationNotFoundException {
        List<CompleteAction> returnList = new ArrayList<>();
        List<Action> actions = actionRepository.findAll();

        for (Action action : actions) {
            returnList.add(getCompleteAction(action));
        }
        return returnList;
    }

    @GetMapping("/actions/{id}")
    public CompleteAction getActionById(@PathVariable String id) throws RelationNotFoundException {
        Optional<Action> action = actionRepository.findById(id);

        if (action.isPresent()) {
            return getCompleteAction(Objects.requireNonNull(action.get()));
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "The Action with ID " + id + " doesn't exist"
            );
        }
    }

    @GetMapping("/actions/newest")
    public List<CompleteAction> getNewestActions() {
        List<Action> newestActions = actionRepository.findByEndDateAfterOrderByStartDateDesc(new Date());
        List<CompleteAction> completeActions = new ArrayList<>();

        for (Action action : newestActions) {
            completeActions.add(getCompleteAction(action));
        }

        return completeActions.stream().limit(4).collect(Collectors.toList()); // Take first n (number in limit(n)) items and return them.;
    }

    // Get the filled CompleteAction for the given action
    private CompleteAction getCompleteAction(Action action) {
        Optional<Vzw> vzw = vzwRepository.findById(action.getVzwID());
        List<ActionImage> actionImages = actionImageRepository.findActionImagesByActionId(action.getId());
        return new CompleteAction(action, vzw, actionImages);
    }
}
