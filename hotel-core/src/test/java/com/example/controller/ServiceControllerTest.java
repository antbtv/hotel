package com.example.controller;

import com.example.dto.CreateServiceDTO;
import com.example.dto.ServiceDTO;
import com.example.entity.Service;
import com.example.mapper.ServiceMapper;
import com.example.service.ServiceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ServiceControllerTest {

    @Mock
    private ServiceService serviceService;

    @Mock
    private ServiceMapper serviceMapper;

    @InjectMocks
    private ServiceController serviceController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(serviceController).build();
    }

    @Test
    void testAddService() throws Exception {
        // GIVEN
        String serviceName = "Уборка";
        int servicePrice = 100;
        CreateServiceDTO createServiceDTO = new CreateServiceDTO(serviceName, servicePrice);
        Service createdService = new Service(serviceName, servicePrice);
        ServiceDTO createdServiceDTO = new ServiceDTO(1, serviceName, servicePrice);

        when(serviceService.addNewService(serviceName, servicePrice)).thenReturn(createdService);
        when(serviceMapper.toDTO(createdService)).thenReturn(createdServiceDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(createServiceDTO);

        // WHEN
        mockMvc.perform(post("/services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(serviceName))
                .andExpect(jsonPath("$.price").value(servicePrice));

        // THEN
        verify(serviceService).addNewService(serviceName, servicePrice);
        verify(serviceMapper).toDTO(createdService);
    }

    @Test
    void testChangePriceOfService() throws Exception {
        // GIVEN
        int newPrice = 150;

        // WHEN
        mockMvc.perform(put("/services/1/price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(newPrice)))
                .andExpect(status().isOk());

        // THEN
        verify(serviceService).setNewServicePrice(1, newPrice);
    }

    @Test
    void testListOfServices() throws Exception {
        // GIVEN
        String name1 = "Уборка";
        int price1 = 100;
        String name2 = "Завтрак";
        int price2 = 50;

        List<Service> services = Arrays.asList(new Service(name1, price1), new Service(name2, price2));
        List<ServiceDTO> serviceDTOs = Arrays.asList(new ServiceDTO(1, name1, price1),
                new ServiceDTO(2, name2, price2));

        when(serviceService.getServices()).thenReturn(services);
        when(serviceMapper.toDTOList(services)).thenReturn(serviceDTOs);

        // WHEN
        mockMvc.perform(get("/services"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value(name1))
                .andExpect(jsonPath("$[1].name").value(name2));

        // THEN
        verify(serviceService).getServices();
        verify(serviceMapper).toDTOList(services);
    }

    @Test
    void testServiceInfo() throws Exception {
        // GIVEN
        String name = "Уборка";
        int price = 100;
        Service service = new Service(name, price);
        ServiceDTO serviceDTO = new ServiceDTO(1, name, price);

        when(serviceService.getService(1)).thenReturn(service);
        when(serviceMapper.toDTO(service)).thenReturn(serviceDTO);

        // WHEN
        mockMvc.perform(get("/services/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.price").value(price));

        // THEN
        verify(serviceService).getService(1);
        verify(serviceMapper).toDTO(service);
    }

    @Test
    void testDeleteService() throws Exception {
        // WHEN
        mockMvc.perform(delete("/services/1"))
                .andExpect(status().isOk());

        // THEN
        verify(serviceService).deleteService(1);
    }
}