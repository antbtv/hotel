package com.example.menu.actions.room;

import com.example.menu.actions.Action;
import com.example.entity.Room;
import com.example.service.HotelManager;
import com.example.utils.MessageSources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class RoomInfoToFileAction  implements Action {

    private final Scanner scanner;
    private static final Logger logger = LogManager.getLogger(RoomInfoToFileAction.class);

    public RoomInfoToFileAction(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute(HotelManager hotelManager) {
        System.out.println(MessageSources.ENTER_ROOM_ID);

        int id = scanner.nextInt();

        Room room = hotelManager.getRoom(id);
        if (room == null) {
            logger.error(MessageSources.NOT_EXIST);
            return;
        }

        String info = hotelManager.getRoomInfo(room);

        String fileName = "roomOutput.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(info);
            logger.info(MessageSources.SUCCESS_WRITE);
        } catch (IOException e) {
            logger.error(MessageSources.FAILURE_WRITE);
        }
    }
}
