package pt.ul.fc.css.urbanwheels.mapper;

import org.junit.jupiter.api.Test;
import pt.ul.fc.css.urbanwheels.entities.State;
import pt.ul.fc.css.urbanwheels.dto.StateDTO;

import static org.junit.jupiter.api.Assertions.*;

class StateMapperUnitTest {

    @Test
    void testToDTO() {
        // given
        State state = new State();
        state.setId(5);
        state.setDescription("DISPONIVEL");

        // when
        StateDTO dto = StateMapper.toDTO(state);

        // then
        assertNotNull(dto);
        assertEquals(5L, dto.id());
        assertEquals("DISPONIVEL", dto.description());
    }

    @Test
    void testToDTO_null() {
        // garante que lida com entrada null
        assertNull(StateMapper.toDTO(null));
    }

    @Test
    void testToEntity() {
        // given
        StateDTO dto = new StateDTO(7L, "EM_USO");

        // when
        State state = StateMapper.toEntity(dto);

        // then
        assertNotNull(state);
        assertEquals(7, state.getId());
        assertEquals("EM_USO", state.getDescription());
    }

    @Test
    void testToEntity_null() {
        // garante que lida com entrada null
        assertNull(StateMapper.toEntity(null));
    }

    @Test
    void testGetStateFromDesc() {
        // given
        String description = "EM_MANUTENCAO";

        // when
        StateDTO dto = StateMapper.getStateFromDesc(description);

        // then
        assertNotNull(dto);
        assertNull(dto.id()); // sempre null
        assertEquals("EM_MANUTENCAO", dto.description());
    }

    @Test
    void testGetStateFromDesc_null() {
        // garante que retorna null se a descrição for null
        assertNull(StateMapper.getStateFromDesc(null));
    }
}

