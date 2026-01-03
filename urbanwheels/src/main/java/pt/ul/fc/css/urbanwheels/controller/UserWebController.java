package pt.ul.fc.css.urbanwheels.controller;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pt.ul.fc.css.urbanwheels.dto.CreateUserDTO;
import pt.ul.fc.css.urbanwheels.dto.SubscriptionDTO;
import pt.ul.fc.css.urbanwheels.dto.UserDetailsDTO;
import pt.ul.fc.css.urbanwheels.services.SubscriptionService;
import pt.ul.fc.css.urbanwheels.services.UserService;

@Controller
@RequestMapping("/web/users")
public class UserWebController {

    private final UserService userService;
    private final SubscriptionService subscriptionService;

    public UserWebController(UserService userService,
                             SubscriptionService subscriptionService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    // LISTAR UTILIZADORES + FORMULÁRIO
    @GetMapping
    public String listUsers(Model model) {

        // Se não houver subscriptions, o service lança ResourceNotFoundException
        List<SubscriptionDTO> subscriptions = subscriptionService.getAllSubscriptions();
        List<UserDetailsDTO> users = userService.getAllUsers()
        		.stream()
                .sorted(Comparator.comparing(UserDetailsDTO::createdAt).reversed()) // mais recente no topo
                .toList();

        model.addAttribute("appName", "UrbanWheels");
        model.addAttribute("users", users);
        model.addAttribute("subscriptions", subscriptions);

        // DTO base do formulário
        model.addAttribute(
            "userForm",
            new CreateUserDTO("", "", "CLIENT", null)
        );

        return "users";
    }
    
    @GetMapping("/test-error")
    public String testError() {
        throw new IllegalArgumentException("TESTE");
    }

    // CRIAR UTILIZADOR (CLIENT ou ADMIN)
    @PostMapping
    public String createUser(@ModelAttribute("userForm") CreateUserDTO userForm) {
        userService.createUser(userForm);
        return "redirect:/web/users";
    }
}
