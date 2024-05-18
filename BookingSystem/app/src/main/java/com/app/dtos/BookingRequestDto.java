package com.app.dtos;

import com.app.models.Room;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class BookingRequestDto {
    private List<Room> rooms;
    private Long userId;
    private LocalDate startDate;
    private LocalDate endDate;
}
