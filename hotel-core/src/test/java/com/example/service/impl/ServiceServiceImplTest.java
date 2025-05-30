package com.example.service.impl;

import com.example.dao.ProvidedServicesDAO;
import com.example.dao.ServiceDAO;
import com.example.entity.Guest;
import com.example.entity.Service;
import com.example.entity.ProvidedService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceServiceImplTest {

    @Mock
    private ServiceDAO serviceDAO;

    @Mock
    private ProvidedServicesDAO providedServicesDAO;

    @InjectMocks
    private ServiceServiceImpl serviceService;

    @Test
    void testGetService() {
        // GIVEN
        Service service = new Service("Уборка", 100);
        when(serviceDAO.read(1)).thenReturn(service);

        // WHEN
        Service result = serviceService.getService(1);

        // THEN
        assertEquals(service, result);
        verify(serviceDAO).read(1);
    }

    @Test
    void testSetNewServicePrice() {
        // GIVEN
        Service service = new Service("Уборка", 100);
        when(serviceDAO.read(1)).thenReturn(service);
        int newPrice = 120;

        // WHEN
        serviceService.setNewServicePrice(1, newPrice);

        // THEN
        assertEquals(newPrice, service.getPrice());
        verify(serviceDAO).update(service);
    }

    @Test
    void testAddNewService() {
        // GIVEN
        String serviceName = "Уборка";
        int price = 100;
        Service service = new Service(serviceName, price);

        // WHEN
        Service result = serviceService.addNewService(serviceName, price);

        // THEN
        assertEquals(service, result);
        verify(serviceDAO).create(service);
    }

    @Test
    void testGetServices() {
        // GIVEN
        Service service1 = new Service("Уборка", 100);
        Service service2 = new Service("Завтрак", 50);
        when(serviceDAO.findAll()).thenReturn(Arrays.asList(service1, service2));

        // WHEN
        List<Service> result = serviceService.getServices();

        // THEN
        assertEquals(2, result.size());
        verify(serviceDAO).findAll();
    }

    @Test
    void testGetProvidedServices() {
        // GIVEN
        Guest guest = new Guest("Ivan Ivanov", "ivan@m.ru", LocalDate.now(),
                LocalDate.now().plusDays(10));
        guest.setGuestId(1);

        Service service1 = new Service("Уборка", 100);
        service1.setServiceId(1);

        Service service2 = new Service("Завтрак", 50);
        service2.setServiceId(2);

        ProvidedService providedService1 = new ProvidedService(guest, service1, LocalDate.now());
        ProvidedService providedService2 = new ProvidedService(guest, service2, LocalDate.now());

        when(providedServicesDAO.findAll()).thenReturn(Arrays.asList(providedService1, providedService2));

        // WHEN
        List<ProvidedService> result = serviceService.getProvidedServices();

        // THEN
        assertEquals(2, result.size());
        verify(providedServicesDAO).findAll();
    }

    @Test
    void testImportServices() throws IOException {
        // GIVEN
        String csvData = "Уборка,100\nЗавтрак,50";
        InputStream inputStream = new ByteArrayInputStream(csvData.getBytes());
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenReturn(inputStream);

        // WHEN
        serviceService.importServices(file);

        // THEN
        verify(serviceDAO, times(2)).create(any(Service.class));
    }

    @Test
    void testExportServices() {
        // GIVEN
        Service service = new Service("Уборка", 100);
        when(serviceDAO.findAll()).thenReturn(List.of(service));

        // WHEN
        File resultFile = serviceService.exportServices();

        // THEN
        assertTrue(resultFile.exists());
        assertTrue(resultFile.length() > 0);
    }

    @Test
    void testDeleteService() {
        // GIVEN
        int serviceId = 1;

        // WHEN
        serviceService.deleteService(serviceId);

        // THEN
        verify(serviceDAO).delete(serviceId);
    }
}