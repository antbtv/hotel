package com.example.controller;

import com.example.comparator.ComparatorFactory;
import com.example.dto.CreateServiceDTO;
import com.example.dto.ProvidedServiceDTO;
import com.example.dto.ServiceDTO;
import com.example.entity.ProvidedService;
import com.example.entity.Service;
import com.example.enums.SortOption;
import com.example.mapper.ProvidedServiceMapper;
import com.example.mapper.ServiceMapper;
import com.example.service.ServiceService;
import com.example.utils.MessageSources;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/services")
public class ServiceController {

    private final ServiceService serviceService;
    private final ServiceMapper serviceMapper;
    private final ProvidedServiceMapper providedServiceMapper;

    public ServiceController(ServiceService serviceService, ServiceMapper serviceMapper,
                             ProvidedServiceMapper providedServiceMapper) {
        this.serviceService = serviceService;
        this.serviceMapper = serviceMapper;
        this.providedServiceMapper = providedServiceMapper;
    }

    @PostMapping
    public ResponseEntity<ServiceDTO> addService(@RequestBody CreateServiceDTO createServiceDTO) throws Exception {
        try {
            Service createdService = serviceService.addNewService(createServiceDTO.getName(),
                    createServiceDTO.getPrice());
            ServiceDTO createdServiceDTO = serviceMapper.toDTO(createdService);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdServiceDTO);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(MessageSources.INVALID_INPUT, e);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @PutMapping("/{serviceId}/price")
    public ResponseEntity<Void> changePriceOfService(
            @PathVariable int serviceId,
            @RequestBody int price) {
        try {
            serviceService.setNewServicePrice(serviceId, price);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(MessageSources.INVALID_INPUT, e);
        }
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<ServiceDTO>> listOfServices(
            @RequestParam(required = false) SortOption sortOption
    ) {

        List<Service> services = serviceService.getServices();

        if (sortOption != null) {
            services = serviceService.sortServicesBy(services,
                    ComparatorFactory.getServiceComparator(sortOption));
        }

        List<ServiceDTO> serviceDTOS = serviceMapper.toDTOList(services);
        return ResponseEntity.ok(serviceDTOS);
    }

    @GetMapping("/provided")
    @ResponseBody
    public ResponseEntity<List<ProvidedServiceDTO>> listOfProvidedServicesByDate() {
        List<ProvidedService> providedServices = serviceService.sortProvidedServicesBy(
                serviceService.getProvidedServices(),
                ComparatorFactory.getProvidedServicesComparator(SortOption.DATE));

        List<ProvidedServiceDTO> providedServiceDTOS = providedServiceMapper.toDTOList(providedServices);
        return ResponseEntity.ok(providedServiceDTOS);
    }


    @GetMapping("/{serviceId}")
    public ResponseEntity<ServiceDTO> serviceInfo(@PathVariable int serviceId) {
        Service service = serviceService.getService(serviceId);
        if (service == null) {
            return ResponseEntity.notFound().build();
        }
        ServiceDTO serviceDTO = serviceMapper.toDTO(service);
        return ResponseEntity.ok(serviceDTO);
    }

    @PostMapping("/import")
    public ResponseEntity<Void> importServices(@RequestParam("file") MultipartFile file) {
        try {
            serviceService.importServices(file);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/export")
    public ResponseEntity<Resource> exportServices() {
        try {
            File file = serviceService.exportServices();
            Resource resource = new FileSystemResource(file);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + file.getName() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable int id) {
        serviceService.deleteService(id);
        return ResponseEntity.ok().build();
    }
}
