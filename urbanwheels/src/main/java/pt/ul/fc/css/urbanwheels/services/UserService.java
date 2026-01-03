package pt.ul.fc.css.urbanwheels.services;

import java.util.List;

import org.springframework.stereotype.Service;

import pt.ul.fc.css.urbanwheels.dto.CreateUserDTO;
import pt.ul.fc.css.urbanwheels.dto.UserDTO;
import pt.ul.fc.css.urbanwheels.dto.UserDetailsDTO;
import pt.ul.fc.css.urbanwheels.entities.Admin;
import pt.ul.fc.css.urbanwheels.entities.Client;
import pt.ul.fc.css.urbanwheels.entities.Subscription;
import pt.ul.fc.css.urbanwheels.entities.User;
import pt.ul.fc.css.urbanwheels.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.urbanwheels.mapper.AdminMapper;
import pt.ul.fc.css.urbanwheels.mapper.ClientMapper;
import pt.ul.fc.css.urbanwheels.mapper.MaintenanceMapper;
import pt.ul.fc.css.urbanwheels.mapper.TripMapper;
import pt.ul.fc.css.urbanwheels.mapper.UserMapper;
import pt.ul.fc.css.urbanwheels.repository.SubscriptionRepository;
import pt.ul.fc.css.urbanwheels.repository.UserRepository;



@Service
public class UserService {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    public UserService(UserRepository userRepository, SubscriptionRepository subscriptionRepository) {
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
    }
	
    public List<UserDetailsDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No users found");
        }

        return users.stream()
                    .map(user -> getUserDetails(user.getId()))
                    .toList();
    }

	
	public UserDTO createUser(CreateUserDTO dto) {
	    validateUserDTO(dto);

	    User user = switch (dto.userType().toUpperCase()) {
	        case "CLIENT" -> {
	        	Subscription subscription = findSubscription(dto.subscriptionType());
	            yield ClientMapper.toEntity(dto, subscription);
	        }
	        case "ADMIN" -> AdminMapper.toEntity(dto);
	        default -> throw new IllegalArgumentException("Invalid userType (CLIENT ou ADMIN)");
	    };
	    
	    userExists(dto.email());
	    
	    User saved = userRepository.save(user);
	    return UserMapper.toDTO(saved);
	}

	private void validateUserDTO(CreateUserDTO dto) {
	    if (dto == null) throw new IllegalArgumentException("CreateUserDTO can not be null");

	    if (dto.userType() == null || dto.userType().isBlank()) {
	        throw new IllegalArgumentException("userType is mandatory (CLIENT ou ADMIN)");
	    }

	    if (dto.userType().equalsIgnoreCase("CLIENT") &&
	            (dto.subscriptionType() == null || dto.subscriptionType().isBlank())) {
	        throw new IllegalArgumentException("subscriptionType is mandatory for CLIENT");
	    }
	}
	
	private void userExists(String email) {
		if (userRepository.existsByEmail(email)) {
	        throw new IllegalArgumentException("User with this email already exists: " + email);
	    }}
	
	private Subscription findSubscription(String subscriptionName) {
	    return subscriptionRepository.findByName(subscriptionName)
	            .orElseThrow(() -> new ResourceNotFoundException(
	                    "SubscriptionType inválido: " + subscriptionName));
	}
	
	public UserDetailsDTO getUserDetails(Long id) {
	    User user = findUserById(id);
	    String subscription = extractSubscription(user);
	    List<?> activities = extractActivities(user);

	    return UserMapper.toDetailsDTO(user, subscription, activities);
	}

	private User findUserById(Long id) {
	    return userRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
	}

	private String extractSubscription(User user) {
	    if (user instanceof Client client && client.getSubscription() != null) {
	        return client.getSubscription().getName();
	    }
	    return "ADMIN_ACCOUNT";
	}

	
	private List<?> extractActivities(User user) {
	    if (user instanceof Client client) {
	        return client.getTrips()
	                .stream()
	                .map(TripMapper::toDTO)
	                .toList();
	    }

	    if (user instanceof Admin admin) {
	        return admin.getMaintenances()
	                .stream()
	                .map(MaintenanceMapper::toDTO)
	                .toList();
	    }

	    throw new IllegalStateException("Unknown user type: " + user.getClass().getSimpleName());
	}
	
	public void loginAdmin(String email) {
	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new IllegalArgumentException("Admin not found"));

	    if (!(user instanceof Admin)) {
	        throw new IllegalArgumentException("User is not an admin");
	    }
	}
	
	public String loginClient(String email) {
		User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new IllegalArgumentException("User not found"));
		
		if (!(user instanceof Client)) {
	        throw new IllegalArgumentException("User is not a client");
	    }
		
		String token = String.valueOf(user.getId());
		
		return token;
	}

	
}
