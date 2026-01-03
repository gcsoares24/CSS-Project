package pt.ul.fc.css.urbanwheels.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import pt.ul.fc.css.urbanwheels.dto.SubscriptionDTO;
import pt.ul.fc.css.urbanwheels.entities.Subscription;
import pt.ul.fc.css.urbanwheels.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.urbanwheels.mapper.SubscriptionMapper;
import pt.ul.fc.css.urbanwheels.repository.SubscriptionRepository;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public List<SubscriptionDTO> getAllSubscriptions() {
        List<Subscription> subs = subscriptionRepository.findAll();

        if (subs.isEmpty()) {
            throw new ResourceNotFoundException("No subscriptions found");
        }

        return subs.stream()
                   .map(SubscriptionMapper::toDTO)
                   .collect(Collectors.toList());
    }


    public SubscriptionDTO createSubscription(String name) {

        // Validate input
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Subscription name cannot be empty");
        }

        // Check if subscription already exists
        Subscription subscription = subscriptionRepository.findByName(name)
                .orElseGet(() -> {
                    Subscription newSub = new Subscription();
                    newSub.setName(name);
                    return subscriptionRepository.save(newSub);
                });

        return SubscriptionMapper.toDTO(subscription);
    }


}
