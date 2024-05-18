package com.app.services;

import com.app.models.Reservation;
import com.app.models.User;
import com.app.repositories.IReservationRepository;
import com.app.repositories.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {
    private final IUserRepository userRepository;
    private final IReservationRepository reservationRepository;

    @Autowired
    public ReservationService(IReservationRepository reservationRepository, IUserRepository userRepository) {
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> findAvailableForFeedbackReservations(Long userId, LocalDate currentDate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return reservationRepository.findAllByUserAndEndOfReservationIsLessThanEqual(user, currentDate);
    }
}
