package com.example.mapper;

import com.example.dto.GuestDTO;
import com.example.entity.Guest;
import com.example.entity.Room;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GuestMapper {

    // ДОБАВИТЬ mapstruct
    public Guest toEntity(GuestDTO guestDTO) {
        Guest guest = new Guest();
        guest.setGuestId(guestDTO.getGuestId());
        guest.setName(guestDTO.getName());
        guest.setEmail(guestDTO.getEmail());
        guest.setCheckInDate(guestDTO.getCheckInDate());
        guest.setCheckOutDate(guestDTO.getCheckOutDate());

        Room room = new Room();
        room.setRoomId(guestDTO.getRoomId());
        guest.setRoom(room);
        return guest;
    }

    public GuestDTO toDTO(Guest guest) {
        GuestDTO guestDTO = new GuestDTO();
        guestDTO.setGuestId(guest.getGuestId());
        guestDTO.setName(guest.getName());
        guestDTO.setEmail(guest.getEmail());
        guestDTO.setCheckInDate(guest.getCheckInDate());
        guestDTO.setCheckOutDate(guest.getCheckOutDate());

        Room room = guest.getRoom();
        guestDTO.setRoomId(room == null ? 0 : room.getRoomId());
        return guestDTO;
    }

    public List<Guest> toEntityList(List<GuestDTO> guestDTOs) {
        return guestDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public List<GuestDTO> toDTOList(List<Guest> guests) {
        return guests.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
