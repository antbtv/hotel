package com.example.mapper;

import com.example.dto.ServiceDTO;
import com.example.entity.Service;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ServiceMapper {

    public Service toEntity(ServiceDTO serviceDTO) {
        Service service = new Service();
        service.setServiceId(serviceDTO.getServiceId());
        service.setName(serviceDTO.getName());
        service.setPrice(serviceDTO.getPrice());
        return service;
    }

    public ServiceDTO toDTO(Service service) {
        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setServiceId(service.getServiceId());
        serviceDTO.setName(service.getName());
        serviceDTO.setPrice(service.getPrice());
        return serviceDTO;
    }

    public List<Service> toEntityList(List<ServiceDTO> serviceDTOs) {
        return serviceDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public List<ServiceDTO> toDTOList(List<Service> services) {
        return services.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
