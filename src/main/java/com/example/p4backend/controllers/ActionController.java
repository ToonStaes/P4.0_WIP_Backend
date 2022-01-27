package com.example.p4backend.controllers;

import com.example.p4backend.models.*;
import com.example.p4backend.models.complete.CompleteAction;
import com.example.p4backend.models.complete.CompleteActionWithProgress;
import com.example.p4backend.models.complete.CompleteVzw;
import com.example.p4backend.repositories.*;
import org.bson.types.Decimal128;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class ActionController {

    @Autowired
    private ActionRepository actionRepository;
    @Autowired
    private VzwRepository vzwRepository;
    @Autowired
    private ActionImageRepository actionImageRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private AddressRepository addressRepository;

    @PostConstruct
    public void fillDB() throws InterruptedException {
        if (actionRepository.count() == 0) {
            Action action1 = new Action(
                    "action1",
                    new Decimal128(500),
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec sed mi quis elit vulputate porttitor. Integer ex diam, maximus in mi ac, vulputate varius lacus. Ut aliquam eros ac est sagittis pellentesque. Suspendisse feugiat nibh nec lectus consectetur facilisis. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Sed vulputate nisi lacus, quis tincidunt diam pharetra ac. Ut dictum magna vel sem sollicitudin efficitur. Sed accumsan, lacus sit amet tempus commodo, metus quam faucibus tortor,",
                    "vzw1",
                    new GregorianCalendar(2022, Calendar.FEBRUARY, 18).getTime());
            action1.setId("action1");
            action1.setStartDate(new GregorianCalendar(2022, Calendar.JANUARY, 18).getTime());

            Thread.sleep(2000);

            Action action2 = new Action(
                    "action2",
                    new Decimal128(200),
                    "quis dictum dui odio sed odio. Sed sed mattis enim. Curabitur fringilla lorem at mauris tempor, sit amet tincidunt odio scelerisque. Morbi ac leo sed nisi hendrerit vulputate. Vivamus id blandit libero, sit amet aliquet diam. Morbi sit amet nibh a turpis ultricies accumsan quis eu massa. Suspendisse potenti. Integer dapibus, nibh quis sollicitudin vestibulum, urna nulla dignissim sem, eget semper nisl neque non nisi.",
                    "vzw2",
                    new GregorianCalendar(2022, Calendar.MARCH, 18).getTime());
            action2.setId("action2");
            action2.setStartDate(new GregorianCalendar(2022, Calendar.JANUARY, 16).getTime());

            Thread.sleep(2000);

            Action action3 = new Action(
                    "action3",
                    new Decimal128(400),
                    "Sed ante elit, scelerisque in egestas facilisis, eleifend at eros. Morbi congue ornare orci, a pharetra urna. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Praesent sed lorem sit amet enim ornare scelerisque. Nam sit amet eleifend est. Proin a sagittis lorem, at tincidunt metus.",
                    "vzw3",
                    new GregorianCalendar(2022, Calendar.APRIL, 28).getTime());
            action3.setId("action3");
            action3.setStartDate(new GregorianCalendar(2022, Calendar.JANUARY, 17).getTime());

            Thread.sleep(2000);

            Action action4 = new Action(
                    "action4",
                    new Decimal128(450),
                    "Duis at posuere felis. Etiam pellentesque euismod purus. Fusce turpis lorem, rhoncus sed nulla sagittis, finibus venenatis dui. Ut ut scelerisque nulla. Quisque eu orci pharetra, dictum turpis vel, suscipit sem. Vivamus quis rutrum est. Phasellus ut magna vitae tellus pharetra eleifend et quis nulla. Integer eu erat erat. Vivamus non sapien augue. Morbi consequat ante ac nibh feugiat fringilla.",
                    "vzw4",
                    new GregorianCalendar(2022, Calendar.FEBRUARY, 28).getTime());
            action4.setId("action4");
            action4.setStartDate(new GregorianCalendar(2021, Calendar.DECEMBER, 2).getTime());

            Action action5 = new Action(
                    "action5",
                    new Decimal128(500),
                    "Mauris vitae risus ut nulla vestibulum scelerisque at eu nulla.\n" + "\n" + "Sed tincidunt massa sed mattis porttitor. Fusce at euismod dui. Aliquam sed lorem accumsan, porta orci nec, efficitur leo. Curabitur iaculis lorem tincidunt risus vestibulum, eget posuere urna vehicula. Morbi et felis posuere, blandit massa quis, sagittis nisl. Fusce eu metus nunc. Phasellus id dui sit amet augue tincidunt gravida in in orci.",
                    "vzw1",
                    new GregorianCalendar(2022, Calendar.FEBRUARY, 1).getTime());
            action5.setId("action5");
            action5.setStartDate(new GregorianCalendar(2022, Calendar.JANUARY, 18).getTime());

            actionRepository.save(action1);
            actionRepository.save(action2);
            actionRepository.save(action3);
            actionRepository.save(action4);
            actionRepository.save(action5);
        }
    }

    @GetMapping("/actions")
    public List<CompleteAction> getAll(@RequestParam(defaultValue = "false") boolean progress) {
        List<CompleteAction> returnList = new ArrayList<>();
        List<Action> actions = actionRepository.findAll();

        for (Action action : actions) {
            if (progress) {
                double actionProgress = getProgress(action);
                returnList.add(getCompleteActionWithProgress(action, actionProgress));
            } else {
                returnList.add(getCompleteAction(action));
            }
        }
        return returnList;
    }

    @GetMapping("/actions/{id}")
    public CompleteAction getActionById(@PathVariable String id, @RequestParam(defaultValue = "false") boolean progress) {
        Optional<Action> action = actionRepository.findById(id);

        if (action.isPresent()) {
            Action actionValue = Objects.requireNonNull(action.get());
            if (progress) {
                double progressValue = getProgress(Objects.requireNonNull(action.get()));
                return getCompleteActionWithProgress(Objects.requireNonNull(action.get()), progressValue);
            } else {
                return getCompleteAction(actionValue);
            }
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "The Action with ID " + id + " doesn't exist"
            );
        }
    }

    @GetMapping("/actions/newest")
    public List<CompleteAction> getNewestActions(@RequestParam(defaultValue = "false") boolean progress) {
        List<Action> newestActions = actionRepository.findByEndDateAfterOrderByStartDateDesc(new Date());
        List<CompleteAction> completeActions = new ArrayList<>();

        for (Action action : newestActions) {
            if (progress) {
                double actionProgress = getProgress(action);
                completeActions.add(getCompleteActionWithProgress(action, actionProgress));
            } else {
                completeActions.add(getCompleteAction(action));
            }
        }

        return completeActions.stream().limit(4).collect(Collectors.toList()); // Take first n (number in limit(n)) items and return them.
    }

    @GetMapping("/actions/random")
    public List<CompleteAction> getRandomActions(@RequestParam(defaultValue = "false") boolean progress){
        List<Action> actions = actionRepository.findAll();
        Collections.shuffle(actions);
        List<Action> selectedActions = actions.stream().limit(6).collect(Collectors.toList()); // Take first n (number in limit(n)) items and return them.;

        List<CompleteAction> completeActions = new ArrayList<>();

        for (Action action : selectedActions) {
            if (progress) {
                double actionProgress = getProgress(action);
                completeActions.add(getCompleteActionWithProgress(action, actionProgress));
            } else {
                completeActions.add(getCompleteAction(action));
            }
        }

        return completeActions;
    }

    @GetMapping("/actions/deadline")
    public List<CompleteAction> getDeadlineActions(@RequestParam(defaultValue = "false") boolean progress) {
        // Generate the current date
        GregorianCalendar currentCallender = new GregorianCalendar();
        Date currentDate = currentCallender.getTime();

        // Generate the date a month in the future from the current date
        currentCallender.add(Calendar.WEEK_OF_YEAR, 2);
        Date futureDate = currentCallender.getTime();

        List<Action> deadlineActions = actionRepository.findActionsByEndDateBetweenAndStartDateBeforeOrderByEndDateDesc(currentDate, futureDate, currentDate);
        List<CompleteAction> completeActions = new ArrayList<>();

        for (Action action : deadlineActions) {
            if (progress) {
                double actionProgress = getProgress(action);
                completeActions.add(getCompleteActionWithProgress(action, actionProgress));
            } else {
                completeActions.add(getCompleteAction(action));
            }
        }

        return completeActions.stream().limit(5).collect(Collectors.toList()); // Take first n (number in limit(n)) items and return them.
    }
  
    @GetMapping("/actions/vzw/{vzwId}")
    public List<CompleteAction> getActionsByVzwId(@PathVariable String vzwId, @RequestParam(defaultValue = "false") boolean progress) {
        List<CompleteAction> returnList = new ArrayList<>();
        List<Action> actions = actionRepository.findActionsByVzwID(vzwId);

        for (Action action : actions) {
            if (progress) {
                double actionProgress = getProgress(action);
                returnList.add(getCompleteActionWithProgress(action, actionProgress));
            } else {
                returnList.add(getCompleteAction(action));
            }
        }
        return returnList;
    }

    // Get the filled CompleteAction for the given action
    private CompleteAction getCompleteAction(Action action) {
        Optional<Vzw> vzw = vzwRepository.findById(action.getVzwID());
        CompleteVzw completeVzw = getCompleteVzw(vzw);
        List<ActionImage> actionImages = actionImageRepository.findActionImagesByActionId(action.getId());
        return new CompleteAction(action, completeVzw, actionImages);
    }

    // Get the filled CompleteActionWithProgress for the given action and progress
    private CompleteActionWithProgress getCompleteActionWithProgress(Action action, double progress) {
        Optional<Vzw> vzw = vzwRepository.findById(action.getVzwID());
        CompleteVzw completeVzw = getCompleteVzw(vzw);
        List<ActionImage> actionImages = actionImageRepository.findActionImagesByActionId(action.getId());
        return new CompleteActionWithProgress(action, completeVzw, actionImages, progress);
    }

    // Calculate the progress percentage of a given action
    private double getProgress(Action action) {
        Decimal128 actionGoal = action.getGoal();
        List<Product> products = productRepository.findProductsByActionId(action.getId());

        BigDecimal actionPurchased = new BigDecimal(0);
        for (Product product : products) {
            List<Purchase> purchases = purchaseRepository.findPurchasesByProductId(product.getId());
            int sum = purchases.stream().mapToInt(Purchase::getAmount).sum(); // Sum of all the amounts a product has been purchased
            actionPurchased = actionPurchased.add(BigDecimal.valueOf(sum * product.getCost().doubleValue())); // Add the total value of this product purchased to the total value action purchased
        }

        return actionPurchased.doubleValue() / Objects.requireNonNull(actionGoal).doubleValue() * 100; // calculate progress percentage (total value purchased / goal * 100)
    }


    @GetMapping(value="/actions/search")
    public List<CompleteAction> searchActionsEmpty(@RequestParam(defaultValue = "false") boolean progress){
        return getAll(progress);
    }

    @GetMapping(value="/actions/search/{terms}")
    public List<CompleteAction> searchActionsByNameContaining(@PathVariable String terms, @RequestParam(defaultValue = "false") boolean progress){
        Set<Action> actions = new HashSet<>(actionRepository.findActionsByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(terms, terms)); // HashSet to prevent duplicate actions
        List<Vzw> vzws = vzwRepository.findVzwsByNameContainingIgnoreCase(terms);
        List<CompleteAction> returnList = new ArrayList<>();

        // Add the actions from the vzw whose name also matched the search terms
        for (Vzw vzw : vzws) {
            actions.addAll(actionRepository.findActionsByVzwID(vzw.getId()));
        }

        for (Action action : actions) {
            if (progress) {
                double actionProgress = getProgress(action);
                returnList.add(getCompleteActionWithProgress(action, actionProgress));
            } else {
                returnList.add(getCompleteAction(action));
            }
        }
        return returnList;
    }

    // Generate Complete vzw to include address
    private CompleteVzw getCompleteVzw(Optional<Vzw> vzw){
        if (vzw.isPresent()){
            Optional<Address> address = addressRepository.findById(vzw.get().getAddressID());
            return new CompleteVzw(vzw.get(), address);
        }
        return null;
    }
}
