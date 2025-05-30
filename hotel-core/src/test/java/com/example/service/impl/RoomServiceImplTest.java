package com.example.service.impl;

import com.example.dao.RoomDAO;
import com.example.entity.Guest;
import com.example.entity.Room;
import com.example.enums.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Mock
    private RoomDAO roomDAO;

    @InjectMocks
    private RoomServiceImpl roomService;

    @Test
    void testCheckIn() {
        // GIVEN
        Room room = new Room(101, 100, 2, 3);
        room.setRoomId(1);
        List<Guest> guests = List.of(new Guest("Ivan Ivanov", "ivan@m.ru", LocalDate.now(),
                LocalDate.now().plusDays(10)));

        // WHEN
        roomService.checkIn(room, guests);

        // THEN
        assertEquals(Status.UNAVAILABLE, room.getStatus());
        assertEquals(guests, room.getGuests());
        for (Guest guest : guests) {
            assertEquals(room, guest.getRoom());
        }
    }

    @Test
    void testCheckOut() {
        // GIVEN
        Room room = new Room(101, 100, 2, 3);
        room.setStatus(Status.UNAVAILABLE);
        room.setRoomId(1);
        List<Guest> guests = new ArrayList<>();
        guests.add(new Guest("Ivan Ivanov", "ivan@m.ru", LocalDate.now(),
                LocalDate.now().plusDays(10)));
        room.setGuests(guests);

        // WHEN
        roomService.checkOut(room, guests);

        // THEN
        assertEquals(Status.AVAILABLE, room.getStatus());
        assertTrue(room.getGuests().isEmpty());
        for (Guest guest : guests) {
            assertNull(guest.getRoom());
        }
    }

    @Test
    void testAddNewRoom() {
        // GIVEN
        int roomNumber = 102;
        int price = 150;
        int capacity = 3;
        int stars = 4;
        Room room = new Room(roomNumber, price, capacity, stars);

        // WHEN
        Room result = roomService.addNewRoom(roomNumber, price, capacity, stars);

        // THEN
        assertEquals(room, result);
        verify(roomDAO).create(room);
    }

    @Test
    void testGetRoom() {
        // GIVEN
        Room room = new Room(101, 100, 2, 3);
        room.setRoomId(1);
        when(roomDAO.read(1)).thenReturn(room);

        // WHEN
        Room result = roomService.getRoom(1);

        // THEN
        assertEquals(room, result);
        verify(roomDAO).read(1);
    }

    @Test
    void testGetRooms() {
        // GIVEN
        Room room1 = new Room(101, 100, 2, 3);
        Room room2 = new Room(102, 150, 3, 4);
        when(roomDAO.findAll()).thenReturn(Arrays.asList(room1, room2));

        // WHEN
        List<Room> result = roomService.getRooms();

        // THEN
        assertEquals(2, result.size());
        verify(roomDAO).findAll();
    }

    @Test
    void testImportRooms() throws IOException {
        // GIVEN
        String csvData = "101,100,2,3\n102,150,3,4";
        InputStream inputStream = new ByteArrayInputStream(csvData.getBytes());
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenReturn(inputStream);

        // WHEN
        roomService.importRooms(file);

        // THEN
        verify(roomDAO, times(2)).create(any(Room.class));
    }

    @Test
    void testExportRooms() throws IOException {
        // GIVEN
        Room room = new Room(101, 100, 2, 3);
        when(roomDAO.findAll()).thenReturn(List.of(room));

        // WHEN
        File resultFile = roomService.exportRooms();

        // THEN
        assertTrue(resultFile.exists());
        assertTrue(resultFile.length() > 0);
    }

    @Test
    void testDeleteRoom() {
        // GIVEN
        int roomId = 101;

        // WHEN
        roomService.deleteRoom(roomId);

        // THEN
        verify(roomDAO).delete(roomId);
    }
}