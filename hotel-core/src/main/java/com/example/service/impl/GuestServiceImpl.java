package com.example.service.impl;

import com.example.dao.GuestDAO;
import com.example.dao.ProvidedServicesDAO;
import com.example.dao.ServiceDAO;
import com.example.entity.Guest;
import com.example.entity.Room;
import com.example.service.GuestService;
import com.example.utils.MessageSources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

@Service
public class GuestServiceImpl implements GuestService {

    private final GuestDAO guestDAO;
    private final ServiceDAO serviceDAO;
    private final ProvidedServicesDAO providedServicesDAO;

    private static final Logger logger = LogManager.getLogger(GuestServiceImpl.class);

    @Autowired
    public GuestServiceImpl(GuestDAO guestDAO,
                            ServiceDAO serviceDAO,
                            ProvidedServicesDAO providedServicesDAO) {
        this.guestDAO = guestDAO;
        this.serviceDAO = serviceDAO;
        this.providedServicesDAO = providedServicesDAO;
    }

    @Override
    @Transactional(readOnly = true)
    public Guest getGuest(int id) {
        return guestDAO.read(id);
    }

    @Override
    @Transactional
    public Guest addNewGuest(String guestName, String email, LocalDate checkInDate,
                             LocalDate checkOutDate) {
        Guest guest = new Guest(guestName, email, checkInDate, checkOutDate);
        guestDAO.create(guest);
        return guest;
    }

    @Override
    @Transactional
    public void addServiceForGuest(int serviceId, int guestId, LocalDate date) {
        com.example.entity.Service service = serviceDAO.read(serviceId);
        Guest guest = getGuest(guestId);
        if (service != null && guest != null) {
            try {
                serviceDAO.addServiceForGuest(guest, service, date);
            } catch (Exception e) {
                logger.error("Ошибка в синхронизации транзакций", e);
            }
        } else {
            logger.error(MessageSources.NOT_EXIST);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Guest> getGuests() {
        return guestDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Guest> getGustsByIds(List<Integer> guestsIds) {
        return guestDAO.findByIds(guestsIds);
    }

    @Override
    public String getGuestInfo(Guest guest) {
        return guest.toString();
    }

    @Override
    public List<Guest> sortGuestsBy(List<Guest> guests, Comparator<Guest> comparator) {
        guests.sort(comparator);
        return guests;
    }

    @Override
    @Transactional(readOnly = true)
    public List<com.example.entity.Service> getServicesOfGuest(int guestId) {
        return providedServicesDAO.getServicesForGuest(guestId);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountOfGuests() {
        return guestDAO.findAll().size();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Guest> getLastThreeGuests(int id) {
        return guestDAO.getLastThreeGuests(id);
    }

    @Override
    @Transactional(readOnly = true)
    public int calculateRoomPrice(int guestId) {
        Guest guest = getGuest(guestId);
        LocalDate checkInDate = guest.getCheckInDate();
        LocalDate checkOutDate = guest.getCheckOutDate();

        Room room = guest.getRoom();
        if (room != null) {
            long days = Math.abs(ChronoUnit.DAYS.between(checkOutDate, checkInDate));
            return (int) (days * room.getPrice());
        } else {
            System.out.println("Гость ещё не заселился");
            return 0;
        }
    }

    @Override
    @Transactional
    public void importGuests(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String name = data[0];
                String email = data[1];
                LocalDate checkInDate = LocalDate.parse(data[2]);
                LocalDate checkOutDate = LocalDate.parse(data[3]);
                addNewGuest(name, email, checkInDate, checkOutDate);
            }
        } catch (IOException e) {
            logger.error(MessageSources.FAILURE_FILE_READ);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public File exportGuests() {
        List<Guest> guests = guestDAO.findAll();
        File file = new File("guests_export.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Guest guest : guests) {
                String guestInfo = getGuestInfo(guest);
                writer.write(guestInfo);
                writer.newLine();
            }
        } catch (IOException e) {
            logger.error(MessageSources.FAILURE_WRITE);
        }

        return file;
    }

    @Override
    @Transactional
    public void deleteGuest(int guestId) {
        guestDAO.delete(guestId);
    }
}
