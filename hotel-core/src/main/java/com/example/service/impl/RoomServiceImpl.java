package com.example.service.impl;

import com.example.dao.GuestDAO;
import com.example.dao.RoomDAO;
import com.example.entity.Guest;
import com.example.entity.Room;
import com.example.enums.Status;
import com.example.service.RoomService;
import com.example.utils.MessageSources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    @Value("${room.status.change.enabled}")
    private boolean changeStatus;

    private final RoomDAO roomDAO;
    private final GuestDAO guestDAO;

    private static final Logger logger = LogManager.getLogger(RoomServiceImpl.class);

    @Autowired
    public RoomServiceImpl(RoomDAO roomDAO,
                           GuestDAO guestDAO) {
        this.roomDAO = roomDAO;
        this.guestDAO = guestDAO;
    }

    @Override
    @Transactional
    public void checkIn(Room room, List<Guest> guests) {
        int capacity = room.getCapacity();
        Status status = room.getStatus();

        if (guests.size() > capacity) {
            logger.error("Заселение невозможно, вместимость номера меньше количества гостей");
            return;
        }

        if (status == Status.AVAILABLE) {
            room.setStatus(Status.UNAVAILABLE);
            room.setGuests(guests);
            for (Guest guest : guests) {
                guest.setRoom(room);
            }

            logger.info("Заселение прошло успешно");
        } else {
            logger.error("Номер " + room.getNumber() + ": недоступен для заселения");
        }
    }

    @Override
    @Transactional
    public void checkOut(Room room, List<Guest> guests) {
        Status status = room.getStatus();
        List<Guest> checkOutGuests = room.getGuests();

        if (status == Status.UNAVAILABLE) {

            for (Guest guest : guests) {
                guest.setRoom(null);
            }

            while (checkOutGuests.size() > room.getMaxHistory()) {
                checkOutGuests.remove(0);
            }

            guests = new ArrayList<>();
            room.setGuests(guests);

            room.setStatus(Status.AVAILABLE);
            logger.info("Выселение прошло успешно");
        } else {
            logger.error("Номер " + room.getNumber() + ": выселение недоступно, номер свободен");
        }
    }

    @Override
    public boolean canChangeStatus() {
        return changeStatus;
    }

    @Override
    @Transactional(readOnly = true)
    public Room getRoom(int id) {
        return roomDAO.read(id);
    }

    @Override
    @Transactional
    public Room addNewRoom(int roomNumber, int price, int capacity, int stars) {
        Room room = new Room(roomNumber, price, capacity, stars);
        roomDAO.create(room);
        return room;
    }

    @Override
    @Transactional
    public void checkInRoom(List<Guest> guests, int roomId) {
        Room room = getRoom(roomId);
        if (room != null) {
            checkIn(room, guests);
            roomDAO.update(room);
            for (Guest guest : guests) {
                guestDAO.update(guest);
            }
        } else {
            logger.error(MessageSources.NOT_EXIST);
        }
    }

    @Override
    @Transactional
    public void checkOutRoom(List<Guest> guests, int roomId) {
        Room room = getRoom(roomId);
        if (room != null) {
            checkOut(room, guests);
            roomDAO.update(room);
            for (Guest guest : guests) {
                guestDAO.update(guest);
            }
        } else {
            logger.error(MessageSources.NOT_EXIST);
        }
    }

    @Override
    @Transactional
    public void setNewStatus(int id, Status status) {
        if (!canChangeStatus()) {
            logger.error(MessageSources.ROOM_CONFIG_ERROR);
        }

        Room room = getRoom(id);
        if (room != null) {
            room.setStatus(status);
            roomDAO.update(room);
        } else {
            logger.error(MessageSources.NOT_EXIST);
        }
    }

    @Override
    @Transactional
    public void setNewRoomPrice(int id, int newPrice) {
        Room room = getRoom(id);
        if (room != null) {
            room.setPrice(newPrice);
            roomDAO.update(room);
        } else {
            logger.error(MessageSources.NOT_EXIST);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Room> getRooms() {
        return roomDAO.findAll();
    }

    @Override
    public String getRoomInfo(Room room) {
        return room.toString();
    }

    @Override
    public List<Room> sortRoomsBy(List<Room> rooms, Comparator<Room> comparator) {
        rooms.sort(comparator);
        return rooms;
    }

    @Override
    public List<Room> sortAvailableRoomsBy(List<Room> rooms, Comparator<Room> comparator) {
        List<Room> availableRooms = new ArrayList<>();

        for (Room room : rooms) {
            if (room.getStatus() == Status.AVAILABLE) {
                availableRooms.add(room);
            }
        }

        availableRooms.sort(comparator);
        return availableRooms;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Room> getAvailableRoomsOn(LocalDate date) {
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : roomDAO.findAll()) {
            if (room.isAvailableOn(date)) {
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountOfAvailableRooms() {
        List<Room> availableRooms = new ArrayList<>();
        List<Room> rooms = roomDAO.findAll();
        for (Room room : rooms) {
            if (room.getStatus() == Status.AVAILABLE) {
                availableRooms.add(room);
            }
        }

        return availableRooms.size();
    }

    @Override
    @Transactional
    public void importRooms(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                int roomNumber = Integer.parseInt(data[0]);
                int price = Integer.parseInt(data[1]);
                int capacity = Integer.parseInt(data[2]);
                int stars = Integer.parseInt(data[3]);

                addNewRoom(roomNumber, price, capacity, stars);
            }
        } catch (IOException e) {
            logger.error(MessageSources.FAILURE_FILE_READ);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public File exportRooms() {
        List<Room> rooms = roomDAO.findAll();
        File file = new File("rooms_export.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Room room : rooms) {
                String roomInfo = getRoomInfo(room);
                writer.write(roomInfo);
                writer.newLine();
            }
        } catch (IOException e) {
            logger.error(MessageSources.FAILURE_WRITE);
        }

        return file;
    }

    @Override
    @Transactional
    public void deleteRoom(int roomId) {
        roomDAO.delete(roomId);
    }
}
