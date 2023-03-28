package com.makaia.Hotel.services;

import com.makaia.Hotel.Exceptions.HandlerResponseException;
import com.makaia.Hotel.modules.Customer;
import com.makaia.Hotel.modules.Reservation;
import com.makaia.Hotel.modules.Room;
import com.makaia.Hotel.repositories.CustomerRepository;
import com.makaia.Hotel.repositories.ReservationRepository;
import com.makaia.Hotel.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;


    private RoomRepository roomRepository;


    private CustomerRepository customerRepository;

    public ReservationService(ReservationRepository reservationRepository, RoomRepository roomRepository, CustomerRepository customerRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.customerRepository = customerRepository;
    }

    public List<Reservation> research(){
        List<Reservation> reservationsAvailables = (List<Reservation>) reservationRepository.findAll();
        if(reservationsAvailables.isEmpty()){
            throw new HandlerResponseException(HttpStatus.INTERNAL_SERVER_ERROR,"There aren't Rooms available now.");
        }
        return reservationsAvailables;
    }

//    public List<Room> roomsByType( LocalDate date, String roomType){
//        List<Reservation> reservations = research();
//        List<Room> roomList = new ArrayList<>();
//
//        if(roomType.equals("basic")){
//            List<Reservation> reservationBasic = reservations.stream()
//                    .filter(reservation -> reservation.getRoom() != null && reservation.getRoom().getRoomType().equals("basic") && reservation.getReserveDate().equals(date))
//                    .collect(Collectors.toList());
//            reservationBasic.stream().forEach(reservation -> {
//                Optional<Room> auxRoom = roomRepository.findById(reservation.getRoom().getNumberRoom());
//                roomList.add(auxRoom.get());
//
//            });
//        }else if (roomType.equals("premium")) {
//            List<Reservation> reservationBasic = reservations.stream()
//                    .filter(reservation -> reservation.getRoom() != null && reservation.getRoom().getRoomType().equals("premium") && reservation.getReserveDate().equals(date))
//                    .collect(Collectors.toList());
//            reservationBasic.stream().forEach(reservation -> {
//                Optional<Room> auxRoom = roomRepository.findById(reservation.getRoom().getNumberRoom());
//                roomList.add(auxRoom.get());
//
//            });
//
//        }
//        return roomList;
//    }

    public List<Room> roomsByType(LocalDate date, String roomType) {
        List<Reservation> reservations = research();
        List<Room> roomList = new ArrayList<>();

        if(roomType.equals("basic")){
            List<Reservation> reservationBasic = reservations.stream()
                    .filter(reservation -> reservation.getRoom() != null && reservation.getRoom().getRoomType().equals("basic") && reservation.getReserveDate().equals(date))
                    .collect(Collectors.toList());
            reservationBasic.forEach(reservation -> {
                roomList.add(reservation.getRoom());
            });
        } else if (roomType.equals("premium")) {
            List<Reservation> reservationPremium = reservations.stream()
                    .filter(reservation -> reservation.getRoom() != null && reservation.getRoom().getRoomType().equals("premium") && reservation.getReserveDate().equals(date))
                    .collect(Collectors.toList());
            reservationPremium.forEach(reservation -> {
                roomList.add(reservation.getRoom());
            });
        }
        return roomList;
    }



    public List<Room> roomsByDate(LocalDate date){
        List<Reservation> reservations = research();
        List<Room> roomList = new ArrayList<>();

            List<Reservation> reservationBasic = reservations.stream()
                    .filter(reservation -> reservation.getRoom() != null && reservation.getReserveDate().equals(date))
                    .collect(Collectors.toList());
            reservationBasic.stream().forEach(reservation -> {
                Optional<Room> auxRoom = roomRepository.findById(reservation.getRoom().getNumberRoom());
                roomList.add(auxRoom.get());
            });
        return roomList;
    }


    public Reservation create(Reservation reservation, int idCustomer){
        Optional<Customer> customer = this.customerRepository.findById(idCustomer);
        List<Customer> customerList = (List<Customer>) this.customerRepository.findAll();
        if(reservation.getReserveCode() != null ){
            Optional<Reservation> tempReservation = this.reservationRepository.findById(reservation.getReserveCode());
            if(tempReservation.isPresent()){
                throw new HandlerResponseException(HttpStatus.INTERNAL_SERVER_ERROR,"Reservation isn't available.");
            }
        }

        LocalDate nowDate = LocalDate.now();
        if(reservation.getReserveDate().isAfter(nowDate) &&
                customerList.contains(customer.get()) &&
                reservation.getRoom() != null){
            this.reservationRepository.save(reservation);
            return reservation;
        }   else{
            throw new HandlerResponseException(HttpStatus.INTERNAL_SERVER_ERROR,"Reservation isn't available for " + nowDate);
        }
    }
}