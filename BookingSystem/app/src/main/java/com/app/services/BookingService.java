package com.app.services;

import com.app.models.*;
import com.app.repositories.IBookedPeriodsRepository;
import com.app.repositories.IReservationRepository;
import com.app.repositories.IRoomRepository;
import com.app.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BookingService {
    private final IRoomRepository roomRepository;
    private final IReservationRepository reservationRepository;
    private final IUserRepository userRepository;
    private final IBookedPeriodsRepository bookedPeriodsRepository;

    @Autowired
    public BookingService(
            IRoomRepository roomRepository,
            IReservationRepository reservationRepository,
            IUserRepository userRepository,
            IBookedPeriodsRepository bookedPeriodsRepository
            ) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.bookedPeriodsRepository = bookedPeriodsRepository;
    }

    public void bookRoom(Room room, LocalDate startDate, LocalDate endDate) {
        if (room.isAvailable()) {
            room.setAvailable(false);
        }

        BookedPeriods bookedPeriod = new BookedPeriods();
        bookedPeriod.setRoom(room);
        bookedPeriod.setStartDate(startDate);
        bookedPeriod.setEndDate(endDate);

        bookedPeriodsRepository.save(bookedPeriod);
        roomRepository.save(room);
    }

    public void createReservation(Long userId, LocalDate endDate) {
        User user = userRepository.findById(userId).orElseThrow();

        Reservation reservation = new Reservation(endDate, user);
        reservationRepository.save(reservation);
    }
}
