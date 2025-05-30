package com.example.mapper;

import com.example.dto.ProvidedServiceDTO;
import com.example.entity.Guest;
import com.example.entity.ProvidedService;
import com.example.entity.Service;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProvidedServiceMapper {

    public ProvidedService toEntity(ProvidedServiceDTO providedServiceDTO) {
        ProvidedService providedService = new ProvidedService();

        providedService.setId(providedServiceDTO.getId());

        Service service = new Service();
        service.setServiceId(providedServiceDTO.getServiceId());
        providedService.setService(service);

        Guest guest = new Guest();
        guest.setGuestId(providedServiceDTO.getGuestId());
        providedService.setGuest(guest);

        providedService.setDate(providedServiceDTO.getServiceDate());
        return providedService;
    }

    public ProvidedServiceDTO toDTO(ProvidedService providedService) {
        ProvidedServiceDTO providedServiceDTO = new ProvidedServiceDTO();

        providedServiceDTO.setId(providedService.getId());
        providedServiceDTO.setServiceId(providedService.getService().getServiceId());
        providedServiceDTO.setGuestId(providedService.getGuest().getGuestId());
        providedServiceDTO.setServiceDate(providedService.getDate());
        return providedServiceDTO;
    }

    public List<ProvidedService> toEntityList(List<ProvidedServiceDTO> providedServiceDTOs) {
        return providedServiceDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public List<ProvidedServiceDTO> toDTOList(List<ProvidedService> providedServices) {
        return providedServices.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
