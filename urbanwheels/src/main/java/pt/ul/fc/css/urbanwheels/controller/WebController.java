package pt.ul.fc.css.urbanwheels.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;

import pt.ul.fc.css.urbanwheels.dto.AddBikeToFleetDTO;
import pt.ul.fc.css.urbanwheels.dto.BikeDTO;
import pt.ul.fc.css.urbanwheels.dto.LoginDTO;
import pt.ul.fc.css.urbanwheels.dto.StateDTO;
import pt.ul.fc.css.urbanwheels.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.urbanwheels.mapper.BikeMapper;
import pt.ul.fc.css.urbanwheels.services.BikeService;
import pt.ul.fc.css.urbanwheels.services.StateService;
import pt.ul.fc.css.urbanwheels.services.StationService;
import pt.ul.fc.css.urbanwheels.services.UserService;

@Controller
@RequestMapping("/web")
public class WebController {

    private final StationService stationService;
    private final BikeService bikeService;
    private final UserService userService;

    public WebController(StationService stationService, BikeService bikeService, UserService userService) {
        this.stationService = stationService;
        this.bikeService = bikeService;
        this.userService = userService;
    }

    // Página de login
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("appName", "UrbanWheels");
        model.addAttribute("loginForm", new LoginDTO(null, null));
        return "index"; // templates/index.html
    }

    // Processar login
    @PostMapping("/login")
    public String loginSubmit(LoginDTO loginDTO, Model model, HttpSession session) {
        try {
            // Valida se o email existe e é de um Admin
            userService.loginAdmin(loginDTO.email());

            // Aqui podes adicionar verificação de password se tiveres
            // Exemplo simplificado: assumimos password correta
            session.setAttribute("adminEmail", loginDTO.email());
            return "redirect:/web/dashboard";

        } catch (IllegalArgumentException e) {
            // Se o email não existir ou não for admin
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("appName", "UrbanWheels");
            return "index";
        }
    }



    // Dashboard (apenas acessível se estiver logado)
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        int stationsCount = stationService.listAllStations().size();
        int bikesCount = bikeService.getAllBikes().size();
        int usersCount = userService.getAllUsers().size();
        String adminEmail = (String) session.getAttribute("adminEmail");

        model.addAttribute("appName", "UrbanWheels");
        model.addAttribute("stationsCount", stationsCount);
        model.addAttribute("bikesCount", bikesCount);
        model.addAttribute("usersCount", usersCount);
        model.addAttribute("weatherStatus", "OK");
        
        model.addAttribute("username", adminEmail);
        
        return "dashboard";
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/web/login";
    }
}
