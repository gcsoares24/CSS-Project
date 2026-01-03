package pt.ul.fc.css.urbanwheels.mapper;

import org.junit.jupiter.api.Test;

import pt.ul.fc.css.urbanwheels.dto.SubscriptionDTO;
import pt.ul.fc.css.urbanwheels.entities.Subscription;

import static org.junit.jupiter.api.Assertions.*;

class SubscriptionMapperUnitTest {

    @Test
    void testToDTO_validEntity() {
        Subscription entity = new Subscription("OCASIONAL");
        entity.setId(42L);

        SubscriptionDTO dto = SubscriptionMapper.toDTO(entity);

        assertNotNull(dto);
        assertEquals(42L, dto.id());
        assertEquals("OCASIONAL", dto.name());
    }

    @Test
    void testToDTO_entityWithoutId() {
        Subscription entity = new Subscription("OCASIONAL");
        SubscriptionDTO dto = SubscriptionMapper.toDTO(entity);

        assertNotNull(dto);
        assertNull(dto.id());
        assertEquals("OCASIONAL", dto.name());
    }

    @Test
    void testToDTO_entityWithEmptyName() {
        Subscription entity = new Subscription("");
        SubscriptionDTO dto = SubscriptionMapper.toDTO(entity);

        assertNotNull(dto);
        assertEquals("", dto.name());
    }

    @Test
    void testToDTO_entityWithNullName() {
        Subscription entity = new Subscription();
        entity.setId(10L);
        entity.setName(null);

        SubscriptionDTO dto = SubscriptionMapper.toDTO(entity);

        assertNotNull(dto);
        assertEquals(10L, dto.id());
        assertNull(dto.name());
    }
    
    @Test
    void testToEntity_validDTO() {
        SubscriptionDTO dto = new SubscriptionDTO(null, "ANUAL");
        Subscription entity = SubscriptionMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals("ANUAL", entity.getName());
        assertNull(entity.getId());
    }

    @Test
    void testToEntity_emptyName() {
        SubscriptionDTO dto = new SubscriptionDTO(null, "");
        Subscription entity = SubscriptionMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals("", entity.getName());
    }

    @Test
    void testToEntity_nullName() {
        SubscriptionDTO dto = new SubscriptionDTO(null, null);
        Subscription entity = SubscriptionMapper.toEntity(dto);

        assertNotNull(entity);
        assertNull(entity.getName());
    }
}
