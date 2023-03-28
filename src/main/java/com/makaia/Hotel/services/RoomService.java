package com.makaia.Hotel.services;


import com.makaia.Hotel.Exceptions.HandlerResponseException;
import com.makaia.Hotel.modules.Room;
import com.makaia.Hotel.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> researchAll()throws HandlerResponseException{
        List<Room> roomsAvailables = (List<Room>) roomRepository.findAll();
        if(roomsAvailables.isEmpty()){

            throw new HandlerResponseException(HttpStatus.INTERNAL_SERVER_ERROR,"There aren't Rooms available now.");
        }
        return roomsAvailables;
    }

    public Optional<Room> researchById(int id){
        Optional<Room> roomsAvailables = roomRepository.findById(id);
        if(roomsAvailables.isEmpty()){

            throw new HandlerResponseException(HttpStatus.INTERNAL_SERVER_ERROR,"The room " + id + " is doesn't available.");
        }
        return roomsAvailables;
    }
    public Room create(Room room){
        if(room.getNumberRoom() != null ){
            Optional<Room> tempRoom = this.roomRepository.findById(room.getNumberRoom());
            if(tempRoom.isPresent()){
                throw new HandlerResponseException(HttpStatus.INTERNAL_SERVER_ERROR,"Room rejected database.");
            }
        }
        this.roomRepository.save(room);
        return room;
    }
}