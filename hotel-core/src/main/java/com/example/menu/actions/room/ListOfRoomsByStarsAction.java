package com.example.menu.actions.room;

import com.example.entity.Room;
import com.example.menu.actions.Action;
import com.example.enums.SortOption;
import com.example.service.HotelManager;
import com.example.comparator.ComparatorFactory;

import java.util.List;

public class ListOfRoomsByStarsAction implements Action {

    public ListOfRoomsByStarsAction() { }

    @Override
    public void execute(HotelManager hotelManager) {
        List<Room> rooms = hotelManager.sortRoomsBy(hotelManager.getRooms(),
                ComparatorFactory.getRoomComparator(SortOption.STARS));

        System.out.println("Список номеров:");
        for (Room room : rooms) {
            System.out.println(room);
        }
    }
}
