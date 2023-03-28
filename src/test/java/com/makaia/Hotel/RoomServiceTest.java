package com.makaia.Hotel;

import com.makaia.Hotel.Exceptions.HandlerResponseException;
import com.makaia.Hotel.modules.Customer;
import com.makaia.Hotel.modules.Room;
import com.makaia.Hotel.repositories.CustomerRepository;
import com.makaia.Hotel.repositories.RoomRepository;
import com.makaia.Hotel.services.CustomerService;
import com.makaia.Hotel.services.RoomService;
import org.junit.Test;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RoomServiceTest {
    RoomRepository roomRepository;
    RoomService roomService;

    @Before
    public void setUp(){
        this.roomRepository = mock(RoomRepository.class);
        this.roomService = new RoomService(roomRepository);
    }

    @Test
    public void validateRoom() {
        Room room = new Room(1, "basic", 100.00);
        Room room2 = roomService.create(room);

        assertNotNull(room2);
    }

    @Test
    public void testResearchByIdSuccessRoom(){
        // Arrange
        int numberRoom = 75;
        Room room = new Room(numberRoom,"Basic",300.00);
        when(roomRepository.findById(numberRoom)).thenReturn(Optional.of(room));

        // Act
        Optional<Room> result = roomService.researchById(numberRoom);
        // Assert
        assertTrue(result.isPresent());
        assertEquals(numberRoom, result.get().getNumberRoom().intValue());
        assertEquals(room.getRoomType(), result.get().getRoomType());
        assertEquals(room.getPrice(), result.get().getPrice());
    }

    @Test
    public void testResearchAllSuccess() {
        // Arrange
        Room room1 = new Room(3,"Premium",100.50);
        Room room2 = new Room(7,"Basic",25.50);
        List<Room> roomList = Arrays.asList(room1, room2);
        when(roomRepository.findAll()).thenReturn(roomList);

        // Act
        List<Room> result = roomService.researchAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals(room1, result.get(0));
        assertEquals(room2, result.get(1));
    }

    @Test
    public void testResearchAllWithRoomsAvailable() {
        // Arrange
        List<Room> roomList = Arrays.asList(
                new Room(1, "Basic", 300.00),
                new Room(2, "Standard", 400.00),
                new Room(3, "Deluxe", 500.00)
        );
        when(roomRepository.findAll()).thenReturn(roomList);

        // Act
        List<Room> result = roomService.researchAll();

        // Assert
        assertEquals(roomList, result);
    }
    @Test
    public void testResearchByIdWithRoomAvailable() {
        // Arrange
        int roomId = 1;
        Room room = new Room(roomId, "Basic", 300.00);
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));

        // Act
        Optional<Room> result = roomService.researchById(roomId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(room, result.get());
    }

    @Test
    public void testResearchByIdWithoutRoomAvailable() {
        // Arrange
        int roomId = 1;
        Optional<Room> room = Optional.empty();
        when(roomRepository.findById(roomId)).thenReturn(room);

        // Act and Assert
        HandlerResponseException exception = assertThrows(HandlerResponseException.class, () -> {
            roomService.researchById(roomId);
        });
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        assertEquals("The room " + roomId + " is doesn't available.", exception.getReason());
    }

    @Test
    public void testResearchAllWithoutRoomsAvailable() {
        // Arrange
        List<Room> roomList = Collections.emptyList();
        when(roomRepository.findAll()).thenReturn(roomList);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            roomService.researchAll();
        });

        assertAll(
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus()),
                () -> assertEquals("There aren't Rooms available now.", exception.getReason())
        );


    }
}
