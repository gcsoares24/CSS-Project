package pt.ul.fc.css.urbanwheels.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;

import pt.ul.fc.css.urbanwheels.dto.StationRequestDTO;
import pt.ul.fc.css.urbanwheels.entities.Station;
import pt.ul.fc.css.urbanwheels.mapper.StationMapper;
import pt.ul.fc.css.urbanwheels.services.StationService;

@Controller
@RequestMapping("/web/stations")
public class StationWebController {

    private final StationService stationService;

    public StationWebController(StationService stationService){
        this.stationService = stationService;
    }

    // Use Case C
    @GetMapping
    public String listStations (Model model) {
        model.addAttribute("stations", stationService.listAllStations());
        return "stations";
    }

    //Use Case B

    //FORMULARIO PARA NOVA ESTACAO
    @GetMapping("/new")
    public String newStationForm(Model model){
        model.addAttribute("stationForm", new StationRequestDTO(null, 0.0, 0.0, 0));
        return "newStation";
    }

    //CRIAR ESTACAO
    @PostMapping("/new")
    public String createStation(@ModelAttribute StationRequestDTO stationForm){
        Station newStation = StationMapper.toEntity(stationForm);

        stationService.createStation(newStation);

        return "redirect:/web/stations";
    }

    //Use Case D
    @GetMapping("/{id}")
    public String stationDetails(@PathVariable Long id, Model model) {
        Station station = stationService.getStationDetails(id);

        if (station.getBikes() != null) {
            station.getBikes().size();
        }

        model.addAttribute("station", station);
        
        return "stationDetails";
    }
}