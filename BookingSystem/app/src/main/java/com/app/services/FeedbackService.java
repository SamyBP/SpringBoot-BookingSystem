package com.app.services;

import com.app.models.Feedback;
import com.app.models.Reservation;
import com.app.models.User;
import com.app.repositories.IFeedbackRepository;
import com.app.repositories.IReservationRepository;
import com.app.repositories.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {
    private final IFeedbackRepository feedbackRepository;
    private final IUserRepository userRepository;
    private final IReservationRepository reservationRepository;

    @Autowired
    public FeedbackService(
            IFeedbackRepository feedbackRepository,
            IUserRepository userRepository,
            IReservationRepository reservationRepository) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
    }

    public Feedback leaveFeedback(Long userId, String information, Long reservationId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Reservation doesn't exists"));


        Feedback feedback = feedbackRepository.save(new Feedback(information, user, reservation));
        reservation.setFeedback(feedback);
        reservationRepository.save(reservation);

        return feedback;
    }
}
