package com.example.controller;

import com.example.dto.CreateRoomDTO;
import com.example.dto.RoomDTO;
import com.example.entity.Guest;
import com.example.entity.Room;
import com.example.enums.Status;
import com.example.mapper.RoomMapper;
import com.example.service.GuestService;
import com.example.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RoomControllerTest {

    @Mock
    private RoomService roomService;

    @Mock
    private GuestService guestService;

    @Mock
    private RoomMapper roomMapper;

    @InjectMocks
    private RoomController roomController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(roomController).build();
    }

    @Test
    void testAddRoom() throws Exception {
        //GIVEN
        int number = 101;
        int price = 200;
        int capacity = 2;
        int stars = 3;
        CreateRoomDTO createRoomDTO = new CreateRoomDTO(number, price, capacity, stars);

        Room room = new Room();
        RoomDTO roomDTO = new RoomDTO(1, number, Status.AVAILABLE, price, capacity, stars);

        when(roomService.addNewRoom(number, price, capacity, stars)).thenReturn(room);
        when(roomMapper.toDTO(room)).thenReturn(roomDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(createRoomDTO);

        //WHEN
        mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.number").value(101))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(200));

        //THEN
        verify(roomService).addNewRoom(number, price, capacity, stars);
        verify(roomMapper).toDTO(room);
    }

    @Test
    void testQuantityOfRooms() throws Exception {
        //GIVEN
        when(roomService.getCountOfAvailableRooms()).thenReturn(10);

        //WHEN
        mockMvc.perform(get("/rooms/count"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(10));

        //THEN
        verify(roomService).getCountOfAvailableRooms();
    }

    @Test
    void testUpdateRoomPrice() throws Exception {
        //GIVEN
        int newPrice = 150;

        //WHEN
        mockMvc.perform(put("/rooms/1/price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(newPrice)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //THEN
        verify(roomService).setNewRoomPrice(1, newPrice);
    }

    @Test
    void testCheckIn() throws Exception {
        //GIVEN
        List<Integer> guestIds = Collections.singletonList(1);

        when(guestService.getGustsByIds(guestIds)).thenReturn(Collections.singletonList(new Guest()));

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(guestIds);

        //WHEN
        mockMvc.perform(post("/rooms/1/checkin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //THEN
        verify(guestService).getGustsByIds(guestIds);
        verify(roomService).checkInRoom(any(), eq(1));
    }

    @Test
    void testCheckOut() throws Exception {
        //GIVEN
        List<Integer> guestIds = Collections.singletonList(1);

        when(guestService.getGustsByIds(guestIds)).thenReturn(Collections.singletonList(new Guest()));

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(guestIds);

        //WHEN
        mockMvc.perform(post("/rooms/1/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //THEN
        verify(guestService).getGustsByIds(guestIds);
        verify(roomService).checkOutRoom(any(), eq(1));
    }

    @Test
    void testUpdateRoomStatus() throws Exception {
        //GIVEN
        Status status = Status.AVAILABLE;

        //WHEN
        mockMvc.perform(put("/rooms/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(status)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //THEN
        verify(roomService).setNewStatus(1, status);
    }

    @Test
    void testListOfRooms() throws Exception {
        //GIVEN
        int number = 101;
        int price = 200;
        int capacity = 2;
        int stars = 3;

        List<Room> rooms = Collections.singletonList(new Room(number, price, capacity, stars));
        List<RoomDTO> roomDTOs = Collections.singletonList(new RoomDTO(1, number,
                Status.AVAILABLE, price, capacity, stars));

        when(roomService.getRooms()).thenReturn(rooms);
        when(roomMapper.toDTOList(rooms)).thenReturn(roomDTOs);

        //WHEN
        mockMvc.perform(get("/rooms"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].number").value(101))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price").value(200));

        //THEN
        verify(roomService).getRooms();
        verify(roomMapper).toDTOList(rooms);
    }

    @Test
    void testRoomInfo() throws Exception {
        //GIVEN
        int number = 101;
        int price = 200;
        int capacity = 2;
        int stars = 3;
        Room room = new Room(number, price, capacity, stars);
        RoomDTO roomDTO = new RoomDTO(1, number, Status.AVAILABLE, price, capacity, stars);

        when(roomService.getRoom(1)).thenReturn(room);
        when(roomMapper.toDTO(room)).thenReturn(roomDTO);

        //WHEN
        mockMvc.perform(get("/rooms/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.number").value(101))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(200));

        //THEN
        verify(roomService).getRoom(1);
        verify(roomMapper).toDTO(room);
    }

    @Test
    void testDeleteRoom() throws Exception {
        //WHEN
        mockMvc.perform(delete("/rooms/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //THEN
        verify(roomService).deleteRoom(1);
    }
}