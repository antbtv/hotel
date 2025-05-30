package com.example.mapper;

import com.example.dto.RoomDTO;
import com.example.entity.Room;
import com.example.enums.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RoomMapperTest {
    @InjectMocks
    private RoomMapper roomMapper;

    @Test
    void testToEntity() {
        // GIVEN
        RoomDTO roomDTO = new RoomDTO(1, 1, Status.AVAILABLE, 3000, 2, 4);

        // WHEN
        Room result = roomMapper.toEntity(roomDTO);

        // THEN
        assertEquals(roomDTO.getRoomId(), result.getRoomId());
        assertEquals(roomDTO.getNumber(), result.getNumber());
        assertEquals(roomDTO.getStatus(), result.getStatus());
        assertEquals(roomDTO.getPrice(), result.getPrice());
        assertEquals(roomDTO.getCapacity(), result.getCapacity());
        assertEquals(roomDTO.getStars(), result.getStars());
    }

    @Test
    void testToDTO() {
        // GIVEN
        Room room = new Room(1, 101, Status.AVAILABLE, 3000, 2, 4, null);

        // WHEN
        RoomDTO result = roomMapper.toDTO(room);

        // THEN
        assertEquals(room.getRoomId(), result.getRoomId());
        assertEquals(room.getNumber(), result.getNumber());
        assertEquals(room.getStatus(), result.getStatus());
        assertEquals(room.getPrice(), result.getPrice());
        assertEquals(room.getCapacity(), result.getCapacity());
        assertEquals(room.getStars(), result.getStars());
    }

    @Test
    void testToEntityList() {
        // GIVEN
        RoomDTO roomDTO1 = new RoomDTO(1, 101, Status.AVAILABLE, 3000, 2, 4);
        RoomDTO roomDTO2 = new RoomDTO(2, 102, Status.UNAVAILABLE, 4000, 3, 5);

        List<RoomDTO> roomDTOs = Arrays.asList(roomDTO1, roomDTO2);

        // WHEN
        List<Room> result = roomMapper.toEntityList(roomDTOs);

        // THEN
        assertEquals(2, result.size());
        assertEquals(roomDTO1.getRoomId(), result.get(0).getRoomId());
        assertEquals(roomDTO2.getRoomId(), result.get(1).getRoomId());
    }

    @Test
    void testToDTOList() {
        // GIVEN
        Room room1 = new Room(1, 101, Status.AVAILABLE, 3000, 2, 4, null);
        Room room2 = new Room(2, 102, Status.AVAILABLE, 4000, 3, 5, null);

        List<Room> rooms = Arrays.asList(room1, room2);

        // WHEN
        List<RoomDTO> result = roomMapper.toDTOList(rooms);

        // THEN
        assertEquals(2, result.size());
        assertEquals(room1.getRoomId(), result.get(0).getRoomId());
        assertEquals(room2.getRoomId(), result.get(1).getRoomId());
        assertEquals(room1.getNumber(), result.get(0).getNumber());
        assertEquals(room2.getNumber(), result.get(1).getNumber());
    }
}