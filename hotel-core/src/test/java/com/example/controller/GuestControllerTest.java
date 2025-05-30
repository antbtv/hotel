package com.example.controller;

import com.example.dto.CreateGuestDTO;
import com.example.dto.CreateProvidedServiceDTO;
import com.example.dto.GuestDTO;
import com.example.dto.ServiceDTO;
import com.example.entity.Guest;
import com.example.entity.Service;
import com.example.mapper.GuestMapper;
import com.example.mapper.ServiceMapper;
import com.example.service.GuestService;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class GuestControllerTest {

    @Mock
    private GuestService guestService;

    @Mock
    private GuestMapper guestMapper;

    @Mock
    private ServiceMapper serviceMapper;

    @InjectMocks
    private GuestController guestController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(guestController).build();
    }

    @Test
    void testAddGuest() throws Exception {
        //GIVEN
        String guestName = "Ivan Ivanov";
        String email = "ivan@m.ru";
        LocalDate now = LocalDate.now();
        LocalDate nowPlusDays = now.plusDays(10);
        CreateGuestDTO createGuestDTO = new CreateGuestDTO(guestName, email, now, nowPlusDays);
        Guest guest = new Guest(guestName, email, now, nowPlusDays);
        GuestDTO guestDTO = new GuestDTO(1, guestName, email, now, nowPlusDays, 0);

        when(guestService.addNewGuest(any(), any(), any(), any())).thenReturn(guest);
        when(guestMapper.toDTO(guest)).thenReturn(guestDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(createGuestDTO);

        //WHEN
        mockMvc.perform(post("/guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Ivan Ivanov"))
                .andExpect(jsonPath("$.email").value("ivan@m.ru"));

        //THEN
        verify(guestService).addNewGuest(any(), any(), any(), any());
        verify(guestMapper).toDTO(guest);
    }

    @Test
    void testListServicesOfGuest() throws Exception {
        //GIVEN
        Service service = new Service();
        service.setServiceId(1);
        List<Service> services = Collections.singletonList(service);
        List<ServiceDTO> serviceDTOs = Collections.singletonList(new ServiceDTO());

        when(guestService.getServicesOfGuest(1)).thenReturn(services);
        when(serviceMapper.toDTOList(services)).thenReturn(serviceDTOs);

        //WHEN
        mockMvc.perform(get("/guests/1/services"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        //THEN
        verify(guestService).getServicesOfGuest(1);
        verify(serviceMapper).toDTOList(services);
    }

    @Test
    void testQuantityOfGuests() throws Exception {
        //GIVEN
        when(guestService.getCountOfGuests()).thenReturn(5);

        //WHEN
        mockMvc.perform(get("/guests/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(5));

        //THEN
        verify(guestService).getCountOfGuests();
    }

    @Test
    void testGuestInfo() throws Exception {
        //GIVEN
        int guestId = 1;
        String name = "Ivan Ivanov";
        String email = "ivan@m.ru";
        LocalDate now = LocalDate.now();
        LocalDate nowPlusDays = now.plusDays(10);
        Guest guest = new Guest(guestId, name, email, now, nowPlusDays, null);
        GuestDTO guestDTO = new GuestDTO(guestId, name, email, now, nowPlusDays, 0);

        when(guestService.getGuest(1)).thenReturn(guest);
        when(guestMapper.toDTO(guest)).thenReturn(guestDTO);

        //WHEN
        mockMvc.perform(get("/guests/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ivan Ivanov"))
                .andExpect(jsonPath("$.email").value("ivan@m.ru"));

        //THEN
        verify(guestService).getGuest(1);
        verify(guestMapper).toDTO(guest);
    }

    @Test
    void testDeleteGuest() throws Exception {
        //WHEN
        mockMvc.perform(delete("/guests/1"))
                .andExpect(status().isOk());

        //THEN
        verify(guestService).deleteGuest(1);
    }

    @Test
    void testAddServiceForGuest() throws Exception {
        //GIVEN
        CreateProvidedServiceDTO createProvidedServiceDTO = new CreateProvidedServiceDTO();
        createProvidedServiceDTO.setServiceId(1);
        createProvidedServiceDTO.setServiceDate(LocalDate.now());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(createProvidedServiceDTO);

        //WHEN
        mockMvc.perform(post("/guests/1/addService")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        //THEN
        verify(guestService).addServiceForGuest(1, 1, LocalDate.now());
    }
}