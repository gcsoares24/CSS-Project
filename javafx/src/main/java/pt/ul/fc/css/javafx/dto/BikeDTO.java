package pt.ul.fc.css.javafx.dto;


public record BikeDTO(
        Long id,
        String model,
        StateDTO state,      // DISPONIVEL, EM_USO, EM_MANUTENCAO
        Long stationId
) {}
