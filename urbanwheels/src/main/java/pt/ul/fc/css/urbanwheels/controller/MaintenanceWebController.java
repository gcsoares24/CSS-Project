package pt.ul.fc.css.urbanwheels.controller.web;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pt.ul.fc.css.urbanwheels.dto.MaintenanceDTO;
import pt.ul.fc.css.urbanwheels.entities.User;
import pt.ul.fc.css.urbanwheels.repository.UserRepository;
import pt.ul.fc.css.urbanwheels.services.BikeService;

import java.time.LocalDateTime;
import java.util.Collections;

@Controller
@RequestMapping("/web/maintenance")
public class MaintenanceWebController {

    private final BikeService bikeService;
    private final UserRepository userRepository;

    public MaintenanceWebController(BikeService bikeService, UserRepository userRepository) {
        this.bikeService = bikeService;
        this.userRepository = userRepository;
    }


    @GetMapping
    public String showForm(Model model) {
        loadAvailableBikes(model);
        model.addAttribute("formData", new MaintenanceForm());
        return "maintenanceform";
    }

    @PostMapping
    public String registerMaintenance(@ModelAttribute MaintenanceForm form, 
                                      HttpSession session, 
                                      Model model) {
        try {
            String email = (String) session.getAttribute("adminEmail");
            if (email == null) return "redirect:/web/login";

            User admin = userRepository.findByEmail(email)
                    .orElseThrow(() -> new Exception("Admin not found"));

            MaintenanceDTO dto = new MaintenanceDTO(
                null,
                form.getBikeId(),
                admin.getId(),
                LocalDateTime.now(),
                form.getDescription(),
                form.getCost()  
            );

            bikeService.registerMaintenance(dto);

            bikeService.changeBikeState(form.getBikeId(), "MAINTENANCE"); 

            model.addAttribute("success", "Success! Maintenance registered and bike status updated.");
            
            model.addAttribute("formData", new MaintenanceForm());
            loadAvailableBikes(model);

            return "maintenanceform";
            
        } catch (Exception e) {
            model.addAttribute("error", "Error: " + e.getMessage());
            loadAvailableBikes(model);
            return "maintenanceform";
        }
    }

    private void loadAvailableBikes(Model model) {
        try {
            model.addAttribute("bikes", bikeService.getBikesByState("AVAILABLE"));
        } catch (Exception e) {
            model.addAttribute("bikes", Collections.emptyList());
        }
    }

    public static class MaintenanceForm {
        private Long bikeId;
        private String description;
        private Double cost; 
        
        public Long getBikeId() { return bikeId; }
        public void setBikeId(Long bikeId) { this.bikeId = bikeId; }
        
        public String getDescription() { return description; }
        public void setDescription(String d) { this.description = d; }

        public Double getCost() { return cost; }
        public void setCost(Double cost) { this.cost = cost; }
    }
}