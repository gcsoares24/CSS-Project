package pt.ul.fc.css.urbanwheels.mapper;

import org.junit.jupiter.api.Test;
import pt.ul.fc.css.urbanwheels.dto.AddBikeToFleetDTO;
import pt.ul.fc.css.urbanwheels.dto.BikeDTO;
import pt.ul.fc.css.urbanwheels.dto.StateDTO;
import pt.ul.fc.css.urbanwheels.entities.Bike;
import pt.ul.fc.css.urbanwheels.entities.State;
import pt.ul.fc.css.urbanwheels.entities.Station;

import static org.junit.jupiter.api.Assertions.*;

class BikeMapperUnitTest {

    @Test
    void testToDTO_normalCase() {
        Station station = new Station();
        station.setId(10L);

        State state = new State();
        state.setId(3);
        state.setDescription("AVAILABLE");

        Bike bike = new Bike();
        bike.setId(5L);
        bike.setModel("Mountain");
        bike.setState(state);
        bike.setStation(station);

        BikeDTO dto = BikeMapper.toDTO(bike);

        assertNotNull(dto);
        assertEquals(5L, dto.id());
        assertEquals("Mountain", dto.model());
        assertEquals(10L, dto.stationId());

        assertNotNull(dto.state());
        assertEquals(3, dto.state().id());
        assertEquals("AVAILABLE", dto.state().description());
    }

    @Test
    void testToDTO_NoStation() {
        State state = new State();
        state.setId(3);
        state.setDescription("Maintenance");

        Bike bike = new Bike();
        bike.setId(7L);
        bike.setModel("Road");
        bike.setState(state);

        BikeDTO dto = BikeMapper.toDTO(bike);

        assertNotNull(dto);
        assertNull(dto.stationId());
        assertEquals(3, dto.state().id());
        assertEquals("Maintenance", dto.state().description());
    }

    @Test
    void testToDTO_NullBike() {
        BikeDTO dto = BikeMapper.toDTO(null);
        assertNull(dto);
    }

    @Test
    void testToDTO_NoState() {
        Bike bike = new Bike();
        bike.setId(8L);
        bike.setModel("BMX");

        BikeDTO dto = BikeMapper.toDTO(bike);

        assertNull(dto.state());
    }

    @Test
    void testABTFtoDTO_normalCase() {
        AddBikeToFleetDTO dtoIn = new AddBikeToFleetDTO("Road", 7L);

        BikeDTO dto = BikeMapper.ABTFtoDTO(dtoIn);

        assertNotNull(dto);
        assertNull(dto.id()); // novo, ainda sem ID
        assertEquals("Road", dto.model());
        assertNotNull(dto.state());
        assertEquals("AVAILABLE", dto.state().description());
        assertEquals(7L, dto.stationId());
    }

    @Test
    void testABTFtoDTO_stationIdNull() {
        AddBikeToFleetDTO dtoIn = new AddBikeToFleetDTO("Mountain", null);

        BikeDTO dto = BikeMapper.ABTFtoDTO(dtoIn);

        assertNotNull(dto);
        assertNull(dto.stationId());
        assertEquals("Mountain", dto.model());
        assertNotNull(dto.state());
        assertEquals("AVAILABLE", dto.state().description());
    }

    @Test
    void testABTFtoDTO_dtoNull() {
        BikeDTO dto = BikeMapper.ABTFtoDTO(null);
        assertNull(dto);
    }

    @Test
    void testToEntity() {
        State state = new State();
        state.setId(2);
        state.setDescription("AVAILABLE");

        Station station = new Station();
        station.setId(4L);

        BikeDTO dto = new BikeDTO(3L, "City", new StateDTO(2L, "AVAILABLE"), 4L);

        Bike bike = BikeMapper.toEntity(dto, station, state);

        assertNotNull(bike);
        assertEquals(3L, bike.getId());
        assertEquals("City", bike.getModel());

        assertNotNull(bike.getState());
        assertEquals(2, bike.getState().getId());
        assertEquals("AVAILABLE", bike.getState().getDescription());

        assertNotNull(bike.getStation());
        assertEquals(4L, bike.getStation().getId());
    }

    @Test
    void testToEntity_dtoNull() {
        Bike bike = BikeMapper.toEntity(null, new Station(), new State());
        assertNull(bike);
    }

    @Test
    void testToEntity_stationNull() {
        State state = new State();
        state.setId(2);
        state.setDescription("AVAILABLE");

        BikeDTO dto = new BikeDTO(3L, "City", new StateDTO(2L, "AVAILABLE"), null);

        Bike bike = BikeMapper.toEntity(dto, null, state);

        assertNotNull(bike);
        assertNull(bike.getStation());
        assertNotNull(bike.getState());
        assertEquals(2, bike.getState().getId());
        assertEquals("AVAILABLE", bike.getState().getDescription());
    }

    @Test
    void testToEntity_stateNull() {
        Station station = new Station();
        station.setId(4L);

        BikeDTO dto = new BikeDTO(3L, "City", new StateDTO(2L, "AVAILABLE"), 4L);

        Bike bike = BikeMapper.toEntity(dto, station, null);

        assertNotNull(bike);
        assertNull(bike.getState());
        assertNotNull(bike.getStation());
        assertEquals(4L, bike.getStation().getId());
    }
}
