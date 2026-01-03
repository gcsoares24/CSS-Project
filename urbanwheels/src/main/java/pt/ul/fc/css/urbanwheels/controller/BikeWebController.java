package pt.ul.fc.css.urbanwheels.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import pt.ul.fc.css.urbanwheels.dto.AddBikeToFleetDTO;
import pt.ul.fc.css.urbanwheels.dto.BikeDTO;
import pt.ul.fc.css.urbanwheels.dto.StateDTO;
import pt.ul.fc.css.urbanwheels.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.urbanwheels.mapper.BikeMapper;
import pt.ul.fc.css.urbanwheels.services.BikeService;
import pt.ul.fc.css.urbanwheels.services.StateService;
import pt.ul.fc.css.urbanwheels.services.StationService;
import pt.ul.fc.css.urbanwheels.services.UserService;

@Controller
@RequestMapping("/web")
public class BikeWebController {

    private final StationService stationService;
    private final BikeService bikeService;
    private final StateService stateService;

    public BikeWebController(StationService stationService, BikeService bikeService, StateService stateService) {
        this.stationService = stationService;
        this.bikeService = bikeService;
        this.stateService = stateService;
    }

    /*
     * 
     * BIKES
     * 
     * */
    // LISTAR BIKES
    @GetMapping("/bikes")
    public String listBikes(@RequestParam(required = false) String state, Model model) {
    	 System.out.println("Received state param: '" + state + "'"); // <--- print do state

    	
    	List<BikeDTO> bikes;
        try {
            if (state != null && !state.trim().isEmpty() && state != "null") {
                bikes = bikeService.getBikesByState(state);
            } else {
                bikes = bikeService.getAllBikes();
            }
        } catch (ResourceNotFoundException e) {
            bikes = List.of(); // lista vazia se o service lançar exceção
        }

        model.addAttribute("bikes", bikes);
        model.addAttribute("states", stateService.getAllStates());
        model.addAttribute("stateFilter", state != null ? state : "");

        return "bikes"; // nome do template Thymeleaf
    }



    // FORMULÁRIO PARA NOVA BIKE
    @GetMapping("/bikes/new")
    public String newBikeForm(Model model) {
        model.addAttribute("bikeForm", new AddBikeToFleetDTO(null, null));
        model.addAttribute("stations", stationService.listAllStations());
        return "newBike"; // templates/newBike.html
    }

    // CRIAR BIKE
    @PostMapping("/bikes/new")
    public String createBike(@ModelAttribute AddBikeToFleetDTO bikeForm) {
        BikeDTO dto = BikeMapper.ABTFtoDTO(bikeForm);
        bikeService.addBikeToStation(dto);
        return "redirect:/web/bikes";
    }
    
}
