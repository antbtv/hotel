package com.example.menu.actions.service;

import com.example.menu.actions.Action;
import com.example.entity.Service;
import com.example.service.HotelManager;
import com.example.utils.MessageSources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class ServiceInfoAction implements Action {

    private final Scanner scanner;
    private static final Logger logger = LogManager.getLogger(ServiceInfoAction.class);

    public ServiceInfoAction(Scanner scanner) {
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
        System.out.println(info);
    }
}
