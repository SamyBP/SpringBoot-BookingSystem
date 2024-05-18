package com.app.controllers;

import com.app.dtos.FeedbackRequestDto;
import com.app.models.Feedback;
import com.app.models.Reservation;
import com.app.services.FeedbackService;
import com.app.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/api/feedback")
public class FeedbackController {
    private final ReservationService reservationService;
    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(ReservationService reservationService, FeedbackService feedbackService) {
        this.reservationService = reservationService;
        this.feedbackService = feedbackService;
    }

    @GetMapping("/notification/{id}")
    public ResponseEntity<List<Reservation>> getReservationsToLeaveFeedback(@PathVariable(name = "id") Long userId) {
        List<Reservation> availableForFeedbackReservations = reservationService.findAvailableForFeedbackReservations(
                userId, LocalDate.now()
        );
        return ResponseEntity.ok(availableForFeedbackReservations);
    }

    @PostMapping("/addfeedback")
    public ResponseEntity<Feedback> addFeedback(@RequestBody FeedbackRequestDto feedbackRequestDto) {
        return ResponseEntity.ok(feedbackService.leaveFeedback(
                feedbackRequestDto.getUserId(),
                feedbackRequestDto.getInformation(),
                feedbackRequestDto.getReservationId()
        ));
    }
}
