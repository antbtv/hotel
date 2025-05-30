package com.example.service.impl;

import com.example.dao.GuestDAO;
import com.example.dao.ServiceDAO;
import com.example.entity.Guest;
import com.example.entity.Service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuestServiceImplTest {

    @Mock
    private GuestDAO guestDAO;

    @Mock
    private ServiceDAO serviceDAO;

    @InjectMocks
    private GuestServiceImpl guestService;

    @Test
    void testGetGuest() {
        //GIVEN
        Guest guest = new Guest("Ivan Ivanov", "ivan@m.ru", LocalDate.now(),
                LocalDate.now().plusDays(10));
        when(guestDAO.read(1)).thenReturn(guest);
        //WHEN
        Guest result = guestService.getGuest(1);
        //THEN
        assertEquals(guest, result);
        verify(guestDAO).read(1);
    }

    @Test
    void testAddNewGuest() {
        //GIVEN
        String name = "Ivan Ivanov";
        String email = "ivan@m.ru";
        LocalDate now = LocalDate.now();
        LocalDate nowPlusDays = now.plusDays(10);
        Guest guest = new Guest(name, email, now, nowPlusDays);
        //WHEN
        Guest result = guestService.addNewGuest(name, email, now, nowPlusDays);
        //THEN
        assertEquals(guest, result);
        verify(guestDAO).create(guest);
    }

    @Test
    void testAddServiceForGuest() {
        // GIVEN
        Service service = new Service();
        service.setServiceId(1);
        Guest guest = new Guest("Ivan Ivanov", "ivan@m.ru", LocalDate.now(),
                LocalDate.now().plusDays(10));
        when(serviceDAO.read(1)).thenReturn(service);
        when(guestDAO.read(1)).thenReturn(guest);

        // WHEN
        guestService.addServiceForGuest(1, 1, LocalDate.now());

        // THEN
        verify(serviceDAO).addServiceForGuest(guest, service, LocalDate.now());
    }

    @Test
    void testGetGuests() {
        // GIVEN
        Guest guest1 = new Guest("Ivan Ivanov", "ivan@m.ru", LocalDate.now(),
                LocalDate.now().plusDays(10));
        Guest guest2 = new Guest("Ivan Smirnov", "ivans@m.ru", LocalDate.now(),
                LocalDate.now().plusDays(7));
        when(guestDAO.findAll()).thenReturn(Arrays.asList(guest1, guest2));

        // WHEN
        List<Guest> result = guestService.getGuests();

        // THEN
        assertEquals(2, result.size());
        verify(guestDAO).findAll();
    }

    @Test
    void testImportGuests() throws IOException {
        // GIVEN
        String csvData = "Иван Иванов,ivan@m.ru,2025-01-01,2023-01-05\n" +
                "Семён Смирнов,semen@m.ru,2025-01-02,2025-01-06";
        InputStream inputStream = new ByteArrayInputStream(csvData.getBytes());
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenReturn(inputStream);

        // WHEN
        guestService.importGuests(file);

        // THEN
        verify(guestDAO, times(2)).create(any(Guest.class));
    }

    @Test
    void testExportGuests() {
        // GIVEN
        Guest guest = new Guest("Иван Ивано", "ivan@m.ru",
                LocalDate.now(), LocalDate.now().plusDays(10));
        when(guestDAO.findAll()).thenReturn(List.of(guest));

        // WHEN
        File resultFile = guestService.exportGuests();

        // THEN
        assertTrue(resultFile.exists());
        assertTrue(resultFile.length() > 0);
    }

    @Test
    void testDeleteGuest() {
        // GIVEN
        int guestId = 1;

        // WHEN
        guestService.deleteGuest(guestId);

        // THEN
        verify(guestDAO).delete(guestId);
    }
}