package com.example.menu.actions.service;

import com.example.menu.actions.Action;
import com.example.entity.Service;
import com.example.service.HotelManager;
import com.example.utils.MessageSources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ServiceInfoToFileAction implements Action {

    private final Scanner scanner;
    private static final Logger logger = LogManager.getLogger(ServiceInfoToFileAction.class);

    public ServiceInfoToFileAction(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute(HotelManager hotelManager) {
        System.out.println(MessageSources.ENTER_SERVICE_ID);
        int id = scanner.nextInt();

        Service service = hotelManager.getService(id);
        if (service == null) {
            logger.error(MessageSources.NOT_EXIST);
            return;
        }

        String info = hotelManager.getServiceInfo(service);

        String fileName = "serviceOutput.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(info);
            System.out.println(MessageSources.SUCCESS_WRITE);
        } catch (IOException e) {
            System.out.println(MessageSources.FAILURE_WRITE);
        }
    }
}
