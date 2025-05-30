package com.example.controller;

import com.example.comparator.ComparatorFactory;
import com.example.dto.CreateGuestDTO;
import com.example.dto.CreateProvidedServiceDTO;
import com.example.dto.GuestDTO;
import com.example.dto.ResultDTO;
import com.example.dto.ServiceDTO;
import com.example.entity.Guest;
import com.example.entity.Service;
import com.example.enums.SortOption;
import com.example.mapper.GuestMapper;
import com.example.mapper.ServiceMapper;
import com.example.service.GuestService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/guests")
public class GuestController {

    private final GuestService guestService;
    private final GuestMapper guestMapper;
    private final ServiceMapper serviceMapper;

    public GuestController(GuestService guestService, GuestMapper guestMapper, ServiceMapper serviceMapper) {
        this.guestService = guestService;
        this.guestMapper = guestMapper;
        this.serviceMapper = serviceMapper;
    }

    @PostMapping
    public ResponseEntity<GuestDTO> addGuest(@RequestBody CreateGuestDTO createGuestDTO) throws Exception {
        try {
            Guest createdGuest = guestService.addNewGuest(
                    createGuestDTO.getName(), createGuestDTO.getEmail(),
                    createGuestDTO.getCheckInDate(), createGuestDTO.getCheckOutDate());
            GuestDTO createdGuestDTO = guestMapper.toDTO(createdGuest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGuestDTO);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(MessageSources.INVALID_INPUT, e);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @GetMapping("/{guestId}/services")
    public ResponseEntity<List<ServiceDTO>> listServicesOfGuest(@PathVariable int guestId) {
        List<Service> services = guestService.getServicesOfGuest(guestId);
        List<ServiceDTO> serviceDTOs = serviceMapper.toDTOList(services);
        return ResponseEntity.ok(serviceDTOs);
    }

    @GetMapping("/count")
    public ResponseEntity<ResultDTO> quantityOfGuests() {
        int countOfGuests = guestService.getCountOfGuests();
        ResultDTO resultDTO = new ResultDTO(countOfGuests);
        return ResponseEntity.ok(resultDTO);
    }

    @GetMapping("/{guestId}/payment")
    public ResponseEntity<ResultDTO> paymentOfGuest(@PathVariable int guestId) {
        int payment = guestService.calculateRoomPrice(guestId);
        ResultDTO resultDTO = new ResultDTO(payment);
        return ResponseEntity.ok(resultDTO);
    }

    @GetMapping
    public ResponseEntity<List<GuestDTO>> listOfGuests(
            @RequestParam(required = false) SortOption sortOption) {
        List<Guest> guests = guestService.getGuests();

        if (sortOption != null) {
            guests = guestService.sortGuestsBy(guests,
                    ComparatorFactory.getGuestComparator(sortOption));
        }

        List<GuestDTO> guestDTOs = guestMapper.toDTOList(guests);
        return ResponseEntity.ok(guestDTOs);
    }

    @GetMapping("/{guestId}")
    public ResponseEntity<GuestDTO> guestInfo(@PathVariable int guestId) {
        Guest guest = guestService.getGuest(guestId);
        if (guest == null) {
            return ResponseEntity.notFound().build();
        }
        GuestDTO guestDTO = guestMapper.toDTO(guest);
        return ResponseEntity.ok(guestDTO);
    }

    @PostMapping("/{guestId}/addService")
    public ResponseEntity<Void> addServiceForGuest(
            @PathVariable int guestId,
            @RequestBody CreateProvidedServiceDTO createProvidedServiceDTO) {

        guestService.addServiceForGuest(createProvidedServiceDTO.getServiceId(),
                guestId, createProvidedServiceDTO.getServiceDate());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/import")
    public ResponseEntity<Void> importGuests(@RequestParam("file") MultipartFile file) {
        try {
            guestService.importGuests(file);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/export")
    public ResponseEntity<Resource> exportGuests() {
        try {
            File file = guestService.exportGuests();
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
    public ResponseEntity<Void> deleteGuest(@PathVariable int id) {
        guestService.deleteGuest(id);
        return ResponseEntity.ok().build();
    }
}
