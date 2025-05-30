package com.example.mapper;

import com.example.dto.GuestDTO;
import com.example.entity.Guest;
import com.example.entity.Room;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GuestMapperTest {

    @InjectMocks
    private GuestMapper guestMapper;

    @Test
    void testToEntity() {
        // GIVEN
        GuestDTO guestDTO = new GuestDTO(1, "Иван Иванов", "ivan@m.ru", LocalDate.now(),
                LocalDate.now().plusDays(10), 1);

        // WHEN
        Guest result = guestMapper.toEntity(guestDTO);

        // THEN
        assertEquals(guestDTO.getGuestId(), result.getGuestId());
        assertEquals(guestDTO.getName(), result.getName());
        assertEquals(guestDTO.getEmail(), result.getEmail());
        assertEquals(guestDTO.getCheckInDate(), result.getCheckInDate());
        assertEquals(guestDTO.getCheckOutDate(), result.getCheckOutDate());
        assertEquals(guestDTO.getRoomId(), result.getRoom().getRoomId());
    }

    @Test
    void testToDTO() {
        // GIVEN
        Room room = new Room();
        room.setRoomId(1);
        Guest guest = new Guest(1, "Иван Иванов", "ivan@m.ru", LocalDate.now(),
                LocalDate.now().plusDays(10), room);

        // WHEN
        GuestDTO result = guestMapper.toDTO(guest);

        // THEN
        assertEquals(guest.getGuestId(), result.getGuestId());
        assertEquals(guest.getName(), result.getName());
        assertEquals(guest.getEmail(), result.getEmail());
        assertEquals(guest.getCheckInDate(), result.getCheckInDate());
        assertEquals(guest.getCheckOutDate(), result.getCheckOutDate());
        assertEquals(room.getRoomId(), result.getRoomId());
    }

    @Test
    void testToEntityList() {
        // GIVEN
        GuestDTO guestDTO1 = new GuestDTO(1, "Иван Иванов", "ivan@m.ru", LocalDate.now(),
                LocalDate.now().plusDays(10), 1);

        GuestDTO guestDTO2 = new GuestDTO(2, "Иван Смирнов", "ivans@m.ru", LocalDate.now(),
                LocalDate.now().plusDays(7), 2);

        List<GuestDTO> guestDTOs = Arrays.asList(guestDTO1, guestDTO2);

        // WHEN
        List<Guest> result = guestMapper.toEntityList(guestDTOs);

        // THEN
        assertEquals(2, result.size());
        assertEquals(guestDTO1.getGuestId(), result.get(0).getGuestId());
        assertEquals(guestDTO2.getGuestId(), result.get(1).getGuestId());
    }


    @Test
    void testToDTOList() {
        // GIVEN
        Room room1 = new Room();
        room1.setRoomId(1);
        Guest guest1 = new Guest(1, "Иван Иванов", "ivan@m.ru", LocalDate.now(),
                LocalDate.now().plusDays(10), room1);

        Room room2 = new Room();
        room2.setRoomId(2);
        Guest guest2 = new Guest(2, "Иван Смирнов", "ivans@m.ru", LocalDate.now(),
                LocalDate.now().plusDays(7), room2);

        List<Guest> guests = Arrays.asList(guest1, guest2);

        // WHEN
        List<GuestDTO> result = guestMapper.toDTOList(guests);

        // THEN
        assertEquals(2, result.size());
        assertEquals(guest1.getGuestId(), result.get(0).getGuestId());
        assertEquals(guest2.getGuestId(), result.get(1).getGuestId());
        assertEquals(guest1.getRoom().getRoomId(), result.get(0).getRoomId());
        assertEquals(guest2.getRoom().getRoomId(), result.get(1).getRoomId());
    }
}
