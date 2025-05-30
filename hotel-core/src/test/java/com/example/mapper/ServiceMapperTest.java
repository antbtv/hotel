package com.example.mapper;

import com.example.dto.ServiceDTO;
import com.example.entity.Service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ServiceMapperTest {

    @InjectMocks
    private ServiceMapper serviceMapper;

    @Test
    void testToEntity() {
        // GIVEN
        ServiceDTO serviceDTO = new ServiceDTO(1, "Уборка", 100);

        // WHEN
        Service result = serviceMapper.toEntity(serviceDTO);

        // THEN
        assertEquals(serviceDTO.getServiceId(), result.getServiceId());
        assertEquals(serviceDTO.getName(), result.getName());
        assertEquals(serviceDTO.getPrice(), result.getPrice());
    }

    @Test
    void testToDTO() {
        // GIVEN
        Service service = new Service(1, "Уборка", 100);

        // WHEN
        ServiceDTO result = serviceMapper.toDTO(service);

        // THEN
        assertEquals(service.getServiceId(), result.getServiceId());
        assertEquals(service.getName(), result.getName());
        assertEquals(service.getPrice(), result.getPrice());
    }

    @Test
    void testToEntityList() {
        // GIVEN
        ServiceDTO serviceDTO1 = new ServiceDTO(1, "Уборка", 100);
        ServiceDTO serviceDTO2 = new ServiceDTO(2, "Завтрак", 50);
        List<ServiceDTO> serviceDTOs = Arrays.asList(serviceDTO1, serviceDTO2);

        // WHEN
        List<Service> result = serviceMapper.toEntityList(serviceDTOs);

        // THEN
        assertEquals(2, result.size());
        assertEquals(serviceDTO1.getServiceId(), result.get(0).getServiceId());
        assertEquals(serviceDTO2.getServiceId(), result.get(1).getServiceId());
    }

    @Test
    void testToDTOList() {
        // GIVEN
        Service service1 = new Service(1, "Уборка", 100);
        Service service2 = new Service(2, "Завтрак", 50);


        List<Service> services = Arrays.asList(service1, service2);

        // WHEN
        List<ServiceDTO> result = serviceMapper.toDTOList(services);

        // THEN
        assertEquals(2, result.size());
        assertEquals(service1.getServiceId(), result.get(0).getServiceId());
        assertEquals(service2.getServiceId(), result.get(1).getServiceId());
        assertEquals(service1.getName(), result.get(0).getName());
        assertEquals(service2.getName(), result.get(1).getName());
        assertEquals(service1.getPrice(), result.get(0).getPrice());
        assertEquals(service2.getPrice(), result.get(1).getPrice());
    }
}