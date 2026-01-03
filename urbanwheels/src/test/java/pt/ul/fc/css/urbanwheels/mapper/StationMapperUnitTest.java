package pt.ul.fc.css.urbanwheels.mapper;

import org.junit.jupiter.api.Test;
import pt.ul.fc.css.urbanwheels.entities.Station;
import pt.ul.fc.css.urbanwheels.dto.BikeDTO;
import pt.ul.fc.css.urbanwheels.dto.StationDTO;
import pt.ul.fc.css.urbanwheels.dto.StationRequestDTO;
import pt.ul.fc.css.urbanwheels.entities.Bike;
import pt.ul.fc.css.urbanwheels.entities.State;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StationMapperUnitTest {

	@Test
	void testToDTO() {
	    // given
	    Station station = new Station();
	    station.setId(1L);
	    station.setName("Central");
	    station.setLat(40.0);
	    station.setLon(-8.0);
	    station.setMaxDocks(20);

	    State state1 = new State();
	    state1.setId(100); // define id do state
	    state1.setDescription("Available");

	    State state2 = new State();
	    state2.setId(101);
	    state2.setDescription("InUse");

	    Bike bike1 = new Bike();
	    bike1.setId(10L);
	    bike1.setModel("Mountain");
	    bike1.setState(state1);
	    bike1.setStation(station);

	    Bike bike2 = new Bike();
	    bike2.setId(11L);
	    bike2.setModel("City");
	    bike2.setState(state2);
	    bike2.setStation(station);

	    station.setBikes(List.of(bike1, bike2));

	    // when
	    StationDTO dto = StationMapper.toDTO(station);

	    // then
	    assertNotNull(dto);
	    assertEquals(1L, dto.id());
	    assertEquals("Central", dto.name());
	    assertEquals(40.0, dto.lat());
	    assertEquals(-8.0, dto.lon());
	    assertEquals(20, dto.maxDocks());
	    assertNotNull(dto.bikes());
	    assertEquals(2, dto.bikes().size());

	    assertEquals(10L, dto.bikes().get(0).id());
	    assertEquals("Mountain", dto.bikes().get(0).model());
	    assertEquals(100, dto.bikes().get(0).state().id());
	    assertEquals("Available", dto.bikes().get(0).state().description());
	    assertEquals(1L, dto.bikes().get(0).stationId());

	    assertEquals(11L, dto.bikes().get(1).id());
	    assertEquals("City", dto.bikes().get(1).model());
	    assertEquals(101, dto.bikes().get(1).state().id());
	    assertEquals("InUse", dto.bikes().get(1).state().description());
	    assertEquals(1L, dto.bikes().get(1).stationId());
	}


    @Test
    void testToDTO_nullStation() {
        assertNull(StationMapper.toDTO(null));
    }

    @Test
    void testToDTO_emptyBikes() {
        Station station = new Station();
        station.setId(2L);
        station.setName("Empty");
        station.setLat(41.0);
        station.setLon(-7.0);
        station.setMaxDocks(10);
        station.setBikes(null); // bikes null

        StationDTO dto = StationMapper.toDTO(station);

        assertNotNull(dto);
        assertNotNull(dto.bikes());
        assertTrue(dto.bikes().isEmpty());
    }
    
    @Test
    void testToDTO_largeBikeList() {
        // given
        Station station = new Station();
        station.setId(1L);
        station.setName("BigBikeStation");

        int M = 500; // grande número de bikes
        List<Bike> bikes = new ArrayList<>();
        for (int i = 0; i < M; i++) {
            Bike b = new Bike();
            b.setId((long) i);
            b.setModel("BikeModel " + i);

            // criar estado associado
            State state = new State();
            state.setId(i); // pode ser qualquer valor único
            state.setDescription("Available " + i);
            b.setState(state);

            b.setStation(station);
            bikes.add(b);
        }
        station.setBikes(bikes);

        // when
        StationDTO dto = StationMapper.toDTO(station);

        // then
        assertNotNull(dto);
        assertEquals(M, dto.bikes().size());
        for (int i = 0; i < M; i++) {
            assertEquals("BikeModel " + i, dto.bikes().get(i).model());
            assertNotNull(dto.bikes().get(i).state());
            assertEquals(i, dto.bikes().get(i).state().id());
            assertEquals("Available " + i, dto.bikes().get(i).state().description());
            assertEquals(1L, dto.bikes().get(i).stationId());
        }
    }

    @Test
    void testToDTOList() {
        Station s1 = new Station();
        s1.setId(1L);
        s1.setName("S1");
        Station s2 = new Station();
        s2.setId(2L);
        s2.setName("S2");

        List<StationDTO> dtos = StationMapper.toDTOList(List.of(s1, s2), null);

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals("S1", dtos.get(0).name());
        assertEquals("S2", dtos.get(1).name());
    }

    @Test
    void testToDTOList_nullList() {
        List<StationDTO> dtos = StationMapper.toDTOList(null, null);
        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }
    
    @Test
    void testToDTOList_largeList() {
        // given
        int N = 1000; // lista grande de estações
        List<Station> stations = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            Station s = new Station();
            s.setId((long) i);
            s.setName("Station " + i);

            // adiciona algumas bikes a cada estação
            List<Bike> bikes = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                Bike b = new Bike();
                b.setId((long) j);
                b.setModel("Bike " + j);

                // inicializa o estado da bike
                State state = new State();
                state.setId(j);
                state.setDescription("Available " + j);
                b.setState(state);

                b.setStation(s);
                bikes.add(b);
            }
            s.setBikes(bikes);

            stations.add(s);
        }

        // when
        List<StationDTO> dtos = StationMapper.toDTOList(stations, null);

        // then
        assertNotNull(dtos);
        assertEquals(N, dtos.size());

        for (int i = 0; i < N; i++) {
            StationDTO dto = dtos.get(i);
            assertEquals("Station " + i, dto.name());
            assertEquals(10, dto.bikes().size());
            for (int j = 0; j < 10; j++) {
                BikeDTO bikeDTO = dto.bikes().get(j);
                assertEquals("Bike " + j, bikeDTO.model());
                assertNotNull(bikeDTO.state());
                assertEquals(j, bikeDTO.state().id());
                assertEquals("Available " + j, bikeDTO.state().description());
                assertEquals((long) i, bikeDTO.stationId());
            }
        }
    }


    @Test
    void testToEntity() {
        StationRequestDTO dto = new StationRequestDTO("North", 42.0, -9.0, 15);
        Station station = StationMapper.toEntity(dto);

        assertNotNull(station);
        assertEquals("North", station.getName());
        assertEquals(42.0, station.getLat());
        assertEquals(-9.0, station.getLon());
        assertEquals(15, station.getMaxDocks());
    }

    @Test
    void testToEntity_nullDTO() {
        assertNull(StationMapper.toEntity(null));
    }
}
