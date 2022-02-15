package com.example.p4backend.controllers;

import com.example.p4backend.models.*;
import com.example.p4backend.models.complete.CompleteAction;
import com.example.p4backend.models.complete.CompleteActionWithProgress;
import com.example.p4backend.models.complete.CompleteVzw;
import com.example.p4backend.models.dto.ActionDTO;
import com.example.p4backend.repositories.*;
import lombok.Getter;
import org.bson.types.Decimal128;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"http://www.wip-shop.be","http://wip-shop.be"}, allowedHeaders = "*")
@Getter
@RestController
public class ActionController {
    private final ActionRepository actionRepository;
    private final VzwRepository vzwRepository;
    private final ProductRepository productRepository;
    private final PurchaseRepository purchaseRepository;
    private final AddressRepository addressRepository;
    private final ProductController productController;

    public ActionController(ActionRepository actionRepository, VzwRepository vzwRepository, ProductRepository productRepository, PurchaseRepository purchaseRepository, AddressRepository addressRepository, ProductController productController) {
        this.actionRepository = actionRepository;
        this.vzwRepository = vzwRepository;
        this.productRepository = productRepository;
        this.purchaseRepository = purchaseRepository;
        this.addressRepository = addressRepository;
        this.productController = productController;
    }

    @PostConstruct
    public void fillDB() {
        if (getActionRepository().count() == 0) {
            // Action 1
            Action action1 = new Action(
                    "Koekenverkoop",
                    new Decimal128(new BigDecimal("500.0")),
                    "Roze koeken, cupcakes of een gewone cake, dit alles kan je bestellen bij chiro Kasterlee.\n" +
                            "Dit jaar verkopen we al deze lekkernijen om ons zomerkamp te sponsoren. Deze zelfgebakken dessertjes kan je nu aan een schappelijke prijs kopen en je zou er ons een groot plezier mee doen.\n" +
                            "De kindjes en chiro danken u. ",
                    "vzw1",
                    new GregorianCalendar(2022, Calendar.FEBRUARY, 18).getTime());

            GregorianCalendar action1StartCallender = new GregorianCalendar();
            GregorianCalendar action1EndCallender = new GregorianCalendar();
            action1StartCallender.add(Calendar.DATE, -5);
            action1EndCallender.add(Calendar.YEAR, 1);
            action1.setId("action1");
            action1.setStartDate(action1StartCallender.getTime());
            action1.setEndDate(action1EndCallender.getTime());

            // Action 2
            Action action2 = new Action(
                    "Taartenverkoop",
                    new Decimal128(new BigDecimal("200.0")),
                    "Movements biedt dit jaar een lekkere appeltaarten aan ten voordelen van een buitenlands show. \n" +
                            "Het team is geselecteerd voor een unieke ervaring in het buitenland waar alle grote dansploegen voor worden uitgenodigd.\n" +
                            "We willen graag dat iedereen deel kan uitmaken van deze ervaring en verkopen daarom taarten om dit te sponsoren. Twijfel dus niet en koop een lekkere zelfgemaakte taart of 2.",
                    "vzw2",
                    new GregorianCalendar(2022, Calendar.MARCH, 18).getTime());

            GregorianCalendar action2StartCallender = new GregorianCalendar();
            GregorianCalendar action2EndCallender = new GregorianCalendar();
            action2StartCallender.add(Calendar.DATE, -4);
            action2EndCallender.add(Calendar.DATE, 61);
            action2.setId("action2");
            action2.setStartDate(action2StartCallender.getTime());
            action2.setEndDate(action2EndCallender.getTime());
            // Action 3
            Action action3 = new Action(
                    "Appelgebak verkoop",
                    new Decimal128(new BigDecimal("400.0")),
                    "Vlaamse volksbeweging verkoopt dit jaar lekkere, zelfgemaakte appelgebakken. Deze taarten zijn ten voordelen van onze visie en doelen. Wij vinden dat we het bij het rechte eind hebben en willen hier ook graag promotie voor maken.\n" +
                            "Ga jij akkoord met onze visie en wil je andere ook overtuigen. Twijfel dan niet en bestel onze lekkere taarten zodat wij onze visie verder kunnen promoten. ",
                    "vzw3",
                    new GregorianCalendar(2022, Calendar.APRIL, 28).getTime());
            GregorianCalendar action3StartCallender = new GregorianCalendar();
            GregorianCalendar action3EndCallender = new GregorianCalendar();
            action3StartCallender.add(Calendar.DATE, -3);
            action3EndCallender.add(Calendar.DATE, 30);
            action3.setId("action3");
            action3.setStartDate(action3StartCallender.getTime());
            action3.setEndDate(action3EndCallender.getTime());

            // Action 4
            Action action4 = new Action(
                    "Inzamelactie stoet",
                    new Decimal128(new BigDecimal("450.0")),
                    "Onze kleine bloemenstoet wil graag een inzamelactie doen ten voordelen van onze stoet. Dit jaar niet aan de hand van een product maar van een vrijwillige donatie die ons enorm zou helpen.",
                    "vzw4",
                    new GregorianCalendar(2022, Calendar.FEBRUARY, 28).getTime());
            GregorianCalendar action4StartCallender = new GregorianCalendar();
            GregorianCalendar action4EndCallender = new GregorianCalendar();
            action4StartCallender.add(Calendar.DATE, -2);
            action4EndCallender.add(Calendar.DATE, 5);
            action4.setId("action4");
            action4.setStartDate(action4StartCallender.getTime());
            action4.setEndDate(action4EndCallender.getTime());

            // Action 5
            Action action5 = new Action(
                    "Inzamelactie renovatie lokaal",
                    new Decimal128(new BigDecimal("500.0")),
                    "Chiro Kasterlee heeft met het slechte weer enkele problemen ondervonden met het chirolokaal. Om onze kindjes zondagnamiddag op te kunnen vangen is het van belang om onze lokalen dus te renoveren zodat deze helemaal terug in orde zijn.\n" +
                            "De veiligheid staat natuurlijk centraal waardoor we een bedrag van €500 willen inzamelen om alles te renoveren. Alvast bedankt!",
                    "vzw1",
                    new GregorianCalendar(2022, Calendar.FEBRUARY, 1).getTime());
            GregorianCalendar action5StartCallender = new GregorianCalendar();
            GregorianCalendar action5EndCallender = new GregorianCalendar();
            action5StartCallender.add(Calendar.DATE, 5);
            action5EndCallender.add(Calendar.DATE, 155);
            action5.setId("action5");
            action5.setStartDate(action5StartCallender.getTime());
            action5.setEndDate(action5EndCallender.getTime());

            getActionRepository().saveAll(List.of(action1, action2, action3, action4, action5));
        }
    }

    @GetMapping("/actions")
    public List<CompleteAction> getAll(@RequestParam(defaultValue = "false") boolean progress) {
        List<CompleteAction> returnList = new ArrayList<>();
        List<Action> actions = getActionRepository().findActionsByIsActiveTrueAndEndDateAfter(new Date());

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
        Action action = getActionRepository().findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The Action with ID " + id + " doesn't exist"));

        if (progress) {
            double progressValue = getProgress(action);
            return getCompleteActionWithProgress(action, progressValue);
        } else {
            return getCompleteAction(action);
        }
    }

    @GetMapping("/actions/newest")
    public List<CompleteAction> getNewestActions(@RequestParam(defaultValue = "false") boolean progress) {
        List<Action> newestActions = getActionRepository().findActionsByIsActiveTrueAndEndDateAfterOrderByStartDateDesc(new Date());
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
    public List<CompleteAction> getRandomActions(@RequestParam(defaultValue = "false") boolean progress) {
        List<Action> actions = getActionRepository().findActionsByIsActiveTrueAndEndDateAfter(new Date());
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

        List<Action> deadlineActions = getActionRepository().findActionsByIsActiveTrueAndEndDateBetweenAndStartDateBeforeOrderByEndDateDesc(currentDate, futureDate, currentDate);
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
        List<Action> actions = getActionRepository().findActionsByIsActiveTrueAndVzwIDAndEndDateAfter(vzwId, new Date());

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
        List<Product> products = getProductRepository().findProductsByActionIdAndIsActiveTrue(action.getId());
        List<String> images = new ArrayList<>();
        for (Product product : products) {
            if (!product.getImage().isEmpty()) {
                images.add(product.getImage());
            }
        }
        Optional<Vzw> vzw = getVzwRepository().findById(action.getVzwID());
        CompleteVzw completeVzw = getCompleteVzw(vzw);
        return new CompleteAction(action, completeVzw, images);
    }

    // Get the filled CompleteActionWithProgress for the given action and progress
    private CompleteActionWithProgress getCompleteActionWithProgress(Action action, double progress) {
        List<Product> products = getProductRepository().findProductsByActionIdAndIsActiveTrue(action.getId());
        List<String> images = new ArrayList<>();
        for (Product product : products) {
            if (!product.getImage().isEmpty()) {
                images.add(product.getImage());
            }
        }
        Optional<Vzw> vzw = getVzwRepository().findById(action.getVzwID());
        CompleteVzw completeVzw = getCompleteVzw(vzw);
        return new CompleteActionWithProgress(action, completeVzw, progress, images);
    }

    // Calculate the progress percentage of a given action
    private double getProgress(Action action) {
        Decimal128 actionGoal = action.getGoal();
        List<Product> products = getProductRepository().findProductsByActionId(action.getId());

        BigDecimal actionPurchased = new BigDecimal(0);
        for (Product product : products) {
            List<Purchase> purchases = getPurchaseRepository().findPurchasesByProductId(product.getId());
            int sum = purchases.stream().mapToInt(Purchase::getAmount).sum(); // Sum of all the amounts a product has been purchased
            actionPurchased = actionPurchased.add(BigDecimal.valueOf(sum * product.getCost().doubleValue())); // Add the total value of this product purchased to the total value action purchased
        }

        return actionPurchased.doubleValue() / Objects.requireNonNull(actionGoal).doubleValue() * 100; // calculate progress percentage (total value purchased / goal * 100)
    }


    @GetMapping(value = "/actions/search")
    public List<CompleteAction> searchActionsEmpty(@RequestParam(defaultValue = "false") boolean progress) {
        return getAll(progress);
    }

    @GetMapping(value = "/actions/search/{terms}")
    public List<CompleteAction> searchActionsByNameContaining(@PathVariable String terms, @RequestParam(defaultValue = "false") boolean progress) {
        Set<Action> actions = new HashSet<>(getActionRepository().findActionsByIsActiveTrueAndNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndEndDateAfter(terms, terms, new Date())); // HashSet to prevent duplicate actions
        List<Vzw> vzws = getVzwRepository().findVzwsByNameContainingIgnoreCase(terms);
        List<CompleteAction> returnList = new ArrayList<>();

        // Add the actions from the vzw whose name also matched the search terms
        for (Vzw vzw : vzws) {
            actions.addAll(getActionRepository().findActionsByIsActiveTrueAndVzwIDAndEndDateAfter(vzw.getId(), new Date()));
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

    @PostMapping("/action")
    public CompleteAction postAction(@RequestBody ActionDTO actionDTO) {
        Action action = new Action(actionDTO);
        getActionRepository().save(action);
        return getCompleteAction(action);
    }

    @PutMapping("/action/{id}")
    public CompleteAction updateAction(@RequestBody ActionDTO updateAction, @PathVariable String id) {
        Action action = getActionRepository().findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The Action with ID " + id + " doesn't exist"));

        action.setName(updateAction.getName());
        action.setGoal(updateAction.getGoal());
        action.setDescription(updateAction.getDescription());
        action.setVzwID(updateAction.getVzwID());
        action.setEndDate(updateAction.getEndDate());
        getActionRepository().save(action);
        return getCompleteAction(action);
    }

    // Set action and linked products to inActive
    @DeleteMapping("/action/{id}")
    public Action deleteAction(@PathVariable String id) {
        Action action = getActionRepository().findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The Action with ID " + id + " doesn't exist"));

        // Set products of deleted action to inActive
        List<Product> productList = getProductRepository().findProductsByActionId(id);
        for (Product product : productList) {
            getProductController().deleteProduct(product.getId());
        }

        // Set deleted action to inActive
        action.setActive(false);
        getActionRepository().save(action);
        return action;
    }

    // Generate Complete vzw to include address
    private CompleteVzw getCompleteVzw(Optional<Vzw> vzw) {
        if (vzw.isPresent()) {
            Optional<Address> address = getAddressRepository().findById(vzw.get().getAddressID());
            return new CompleteVzw(vzw.get(), address);
        }
        return null;
    }
}
