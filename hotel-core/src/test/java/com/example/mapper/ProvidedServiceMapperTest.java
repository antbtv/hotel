package com.example.mapper;

import com.example.dto.GuestDTO;
import com.example.dto.ProvidedServiceDTO;
import com.example.entity.Guest;
import com.example.entity.ProvidedService;
import com.example.entity.Room;
import com.example.entity.Service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProvidedServiceMapperTest {

    @InjectMocks
    private ProvidedServiceMapper providedServiceMapper;

    @Test
    void testToEntity() {
        // GIVEN
        ProvidedServiceDTO providedServiceDTO = new ProvidedServiceDTO(1, 1, 1, LocalDate.now());

        // WHEN
        ProvidedService result = providedServiceMapper.toEntity(providedServiceDTO);

        // THEN
        assertEquals(providedServiceDTO.getId(), result.getId());
        assertEquals(providedServiceDTO.getServiceId(), result.getService().getServiceId());
        assertEquals(providedServiceDTO.getGuestId(), result.getGuest().getGuestId());
        assertEquals(providedServiceDTO.getServiceDate(), result.getDate());
    }

    @Test
    void testToDTO() {
        // GIVEN
        Service service = new Service(1, "Уборка", 100);
        Room room = new Room();
        Guest guest = new Guest(1, "Иван Иванов", "ivan@m.ru", LocalDate.now(),
                LocalDate.now().plusDays(10), room);
        ProvidedService providedService = new ProvidedService(1, guest, service, LocalDate.now());

        // WHEN
        ProvidedServiceDTO result = providedServiceMapper.toDTO(providedService);

        // THEN
        assertEquals(providedService.getId(), result.getId());
        assertEquals(providedService.getService().getServiceId(), result.getServiceId());
        assertEquals(providedService.getGuest().getGuestId(), result.getGuestId());
        assertEquals(providedService.getDate(), result.getServiceDate());
    }

    @Test
    void testToEntityList() {
        // GIVEN
        ProvidedServiceDTO providedServiceDTO1 = new ProvidedServiceDTO(1, 1, 1,
                LocalDate.now());
        ProvidedServiceDTO providedServiceDTO2 = new ProvidedServiceDTO(2, 2, 2,
                LocalDate.now().plusDays(1));

        List<ProvidedServiceDTO> providedServiceDTOS = Arrays.asList(providedServiceDTO1, providedServiceDTO2);

        // WHEN
        List<ProvidedService> result = providedServiceMapper.toEntityList(providedServiceDTOS);

        // THEN
        assertEquals(2, result.size());
        assertEquals(providedServiceDTO1.getId(), result.get(0).getId());
        assertEquals(providedServiceDTO2.getId(), result.get(1).getId());
    }

    @Test
    void testToDTOList() {
        // GIVEN
        Room room1 = new Room();
        room1.setRoomId(1);
        Guest guest1 = new Guest(1, "Иван Иванов", "ivan@m.ru", LocalDate.now(),
                LocalDate.now().plusDays(10), room1);
        Service service1 = new Service(1, "Уборка", 100);
        ProvidedService providedService1 = new ProvidedService(1, guest1, service1, LocalDate.now());

        Room room2 = new Room();
        room2.setRoomId(2);
        Guest guest2 = new Guest(2, "Иван Смирнов", "ivans@m.ru", LocalDate.now(),
                LocalDate.now().plusDays(7), room2);
        Service service2 = new Service(2, "Завтрак", 50);
        ProvidedService providedService2 = new ProvidedService(2, guest2, service2, LocalDate.now());

        List<ProvidedService> providedServiceList = Arrays.asList(providedService1, providedService2);

        // WHEN
        List<ProvidedServiceDTO> result = providedServiceMapper.toDTOList(providedServiceList);

        // THEN
        assertEquals(2, result.size());
        assertEquals(providedService1.getId(), result.get(0).getId());
        assertEquals(providedService2.getId(), result.get(1).getId());
    }
}