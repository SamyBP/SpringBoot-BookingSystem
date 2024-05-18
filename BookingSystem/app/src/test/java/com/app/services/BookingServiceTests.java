package com.app.services;

import com.app.models.BookedPeriods;
import com.app.models.Reservation;
import com.app.models.Room;
import com.app.models.User;
import com.app.repositories.IBookedPeriodsRepository;
import com.app.repositories.IReservationRepository;
import com.app.repositories.IRoomRepository;
import com.app.repositories.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTests {
    @Mock
    private IRoomRepository roomRepository;
    @Mock
    private IBookedPeriodsRepository bookedPeriodsRepository;
    @InjectMocks
    private BookingService bookingService;

    @Test
    void BookingService_BookRoom_VerifiesCorrectRepositories() {
        Room room = new Room();
        room.setAvailable(true);

        LocalDate startDate = LocalDate.of(2024, 5, 20);
        LocalDate endDate = LocalDate.of(2024, 5, 22);

        bookingService.bookRoom(room, startDate, endDate);

        verify(bookedPeriodsRepository, times(1)).save(any(BookedPeriods.class));
        verify(roomRepository, times(1)).save(any(Room.class));
    }
}
