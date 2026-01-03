package pt.ul.fc.css.urbanwheels.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiOperation;
import pt.ul.fc.css.urbanwheels.dto.SubscriptionDTO;
import pt.ul.fc.css.urbanwheels.services.SubscriptionService;

import java.util.List;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    // List all available subscriptions
    @GetMapping
    @ApiOperation(value = "List all subscriptions", notes = "Retrieve all valid subscriptions from the system")
    public ResponseEntity<List<SubscriptionDTO>> listSubscriptions() {
        List<SubscriptionDTO> subscriptions = subscriptionService.getAllSubscriptions();
        return ResponseEntity.ok(subscriptions);
    }

    // Add a new subscription
    @PostMapping("/create")
    @ApiOperation(value = "Add a new subscription", notes = "Creates a new subscription that can be assigned to clients")
    public ResponseEntity<SubscriptionDTO> createSubscription(@RequestParam String subscriptionName) {
        SubscriptionDTO newSubscription = subscriptionService.createSubscription(subscriptionName);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSubscription);
    }
}
