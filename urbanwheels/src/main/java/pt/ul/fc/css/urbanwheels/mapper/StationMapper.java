package pt.ul.fc.css.urbanwheels.mapper;

import pt.ul.fc.css.urbanwheels.dto.BikeDTO;
import pt.ul.fc.css.urbanwheels.dto.WeatherConditionDTO;
import pt.ul.fc.css.urbanwheels.dto.StationDTO;
import pt.ul.fc.css.urbanwheels.dto.StationRequestDTO;
import pt.ul.fc.css.urbanwheels.entities.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class StationMapper {
    public static StationDTO toDTO(Station station) {
        if (station == null) return null;

        List<BikeDTO> bikeDTOs;
        if (station.getBikes() == null) {
            bikeDTOs = new ArrayList<>();
        } else {
            bikeDTOs = station.getBikes()
                    .stream()
                    .map(bike -> BikeMapper.toDTO(bike)) 
                    .toList();
        }

        return new StationDTO(
                station.getId(),
                station.getName(),
                station.getLat(),
                station.getLon(),
                station.getMaxDocks(),
                bikeDTOs,
                null
        );
    }
    public static StationDTO toDTO(Station station, WeatherConditionDTO wc) {
        if (station == null) return null;

        List<BikeDTO> bikeDTOs;
        if (station.getBikes() == null) {
            bikeDTOs = new ArrayList<>();
        } else {
            bikeDTOs = station.getBikes()
                    .stream()
                    .map(bike -> BikeMapper.toDTO(bike)) 
                    .toList();
        }

        return new StationDTO(
                station.getId(),
                station.getName(),
                station.getLat(),
                station.getLon(),
                station.getMaxDocks(),
                bikeDTOs,
                wc
        );
    }

    public static List<StationDTO> toDTOList(
            List<Station> stations,
            List<WeatherConditionDTO> wc
    ) {
        if (stations == null) {
            return new ArrayList<>();
        }

        
        return IntStream.range(0, stations.size())
                .mapToObj(i -> {
                    WeatherConditionDTO weather = (wc != null && i < wc.size()) ? wc.get(i) : null;
                    return StationMapper.toDTO(stations.get(i), weather);
                })
                .toList();
    }


    
    public static Station toEntity(StationRequestDTO dto) {
        if (dto == null) return null;
        Station station = new Station();
        station.setName(dto.name());
        station.setLat(dto.lat());
        station.setLon(dto.lon());
        station.setMaxDocks(dto.maxDocks());
        return station;
    }

}
