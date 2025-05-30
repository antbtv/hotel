package com.example.controller;

import com.example.comparator.ComparatorFactory;
import com.example.dto.CreateRoomDTO;
import com.example.dto.ResultDTO;
import com.example.dto.RoomDTO;
import com.example.entity.Guest;
import com.example.entity.Room;
import com.example.enums.SortOption;
import com.example.enums.Status;
import com.example.mapper.RoomMapper;
import com.example.service.GuestService;
import com.example.service.RoomService;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;
    private final GuestService guestService;
    private final RoomMapper roomMapper;

    public RoomController(RoomService roomService, GuestService guestService, RoomMapper roomMapper) {
        this.roomService = roomService;
        this.guestService = guestService;
        this.roomMapper = roomMapper;
    }

    @PostMapping
    public ResponseEntity<RoomDTO> addRoom(@RequestBody CreateRoomDTO createRoomDTO) throws Exception {
        try {
            Room createdRoom = roomService.addNewRoom(createRoomDTO.getNumber(), createRoomDTO.getPrice(),
                    createRoomDTO.getCapacity(), createRoomDTO.getStars());
            RoomDTO createdRoomDTO = roomMapper.toDTO(createdRoom);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRoomDTO);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(MessageSources.INVALID_INPUT, e);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<ResultDTO> quantityOfRooms() {
        int countOfRooms = roomService.getCountOfAvailableRooms();
        ResultDTO resultDTO = new ResultDTO(countOfRooms);
        return ResponseEntity.ok(resultDTO);
    }

    @PutMapping("/{roomId}/price")
    public ResponseEntity<Void> updateRoomPrice(
            @PathVariable int roomId,
            @RequestBody int price) {
        try {
            roomService.setNewRoomPrice(roomId, price);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(MessageSources.INVALID_INPUT, e);
        }
    }

    @PostMapping("/{roomId}/checkin")
    public ResponseEntity<Void> checkIn(
            @PathVariable int roomId,
            @RequestBody List<Integer> guestIds) {

        List<Guest> guests = guestService.getGustsByIds(guestIds);
        roomService.checkInRoom(guests, roomId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{roomId}/checkout")
    public ResponseEntity<Void> checkOut(
            @PathVariable int roomId,
            @RequestBody List<Integer> guestIds) {

        List<Guest> guests = guestService.getGustsByIds(guestIds);
        roomService.checkOutRoom(guests, roomId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{roomId}/status")
    public ResponseEntity<Void> updateRoomStatus(
            @PathVariable int roomId,
            @RequestBody Status status) {

        try {
            roomService.setNewStatus(roomId, status);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(MessageSources.INVALID_INPUT, e);
        }
    }

    @GetMapping
    public ResponseEntity<List<RoomDTO>> listOfRooms(
            @RequestParam(required = false) SortOption sortOption) {

        List<Room> rooms = roomService.getRooms();

        if (sortOption != null) {
            rooms = roomService.sortRoomsBy(rooms,
                    ComparatorFactory.getRoomComparator(sortOption));
        }

        List<RoomDTO> roomDTOS = roomMapper.toDTOList(rooms);
        return ResponseEntity.ok(roomDTOS);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDTO> roomInfo(@PathVariable int roomId) {
        Room room = roomService.getRoom(roomId);
        if (room == null) {
            return ResponseEntity.notFound().build();
        }
        RoomDTO roomDTO = roomMapper.toDTO(room);
        return ResponseEntity.ok(roomDTO);
    }

    @PostMapping("/import")
    public ResponseEntity<Void> importRooms(@RequestParam("file") MultipartFile file) {
        try {
            roomService.importRooms(file);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/export")
    public ResponseEntity<Resource> exportRooms() {
        try {
            File file = roomService.exportRooms();
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
    public ResponseEntity<Void> deleteRoom(@PathVariable int id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok().build();
    }
}
