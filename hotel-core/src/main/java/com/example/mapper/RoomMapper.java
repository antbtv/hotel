package com.example.mapper;

import com.example.dto.RoomDTO;
import com.example.entity.Room;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomMapper {

    public Room toEntity(RoomDTO roomDTO) {
        Room room = new Room();
        room.setRoomId(roomDTO.getRoomId());
        room.setNumber(roomDTO.getNumber());
        room.setStatus(roomDTO.getStatus());
        room.setPrice(roomDTO.getPrice());
        room.setCapacity(roomDTO.getCapacity());
        room.setStars(roomDTO.getStars());
        return room;
    }

    public RoomDTO toDTO(Room room) {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setRoomId(room.getRoomId());
        roomDTO.setNumber(room.getNumber());
        roomDTO.setStatus(room.getStatus());
        roomDTO.setPrice(room.getPrice());
        roomDTO.setCapacity(room.getCapacity());
        roomDTO.setStars(room.getStars());
        return roomDTO;
    }

    public List<Room> toEntityList(List<RoomDTO> roomDTOs) {
        return roomDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public List<RoomDTO> toDTOList(List<Room> rooms) {
        return rooms.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
