package com.app.repositories;

import com.app.models.Reservation;
import com.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByUserAndEndOfReservationIsLessThanEqual(User user, LocalDate date);
    Optional<Reservation> findById(Long id);
}
