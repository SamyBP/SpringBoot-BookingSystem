package com.app.controllers;

import com.app.dtos.BookingRequestDto;
import com.app.dtos.Location;
import com.app.models.Hotel;
import com.app.models.Room;
import com.app.models.User;
import com.app.services.BookingService;
import com.app.services.HotelService;
import com.app.services.LocationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("api/hotels")
public class HotelController {
    private final HotelService hotelService;
    private final LocationService locationService;
    private final BookingService bookingService;

    @Autowired
    public HotelController(
            HotelService hotelService,
            LocationService locationService,
            BookingService bookingService
    ) {
        this.hotelService = hotelService;
        this.locationService = locationService;
        this.bookingService = bookingService;
    }

    @GetMapping("/nearme/{radius}")
    public ResponseEntity<List<Hotel>> getAllWithFilters(HttpServletRequest request,
                                                         @PathVariable("radius") int radius,
                                                         @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                         @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Location currentUsersLocation = locationService.getUsersLocation(getClientIpAddr(request));
        List<Hotel> foundHotels = hotelService.findWithFilters(
                currentUsersLocation.getLat(),
                currentUsersLocation.getLon(),
                radius,
                startDate,
                endDate
        );
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(foundHotels);
    }

    @PostMapping("/book")
    public ResponseEntity<HttpStatusCode> bookRooms(@RequestBody BookingRequestDto bookingRequestDto) {

        for (Room room : bookingRequestDto.getRooms()) {
            bookingService.bookRoom(room, bookingRequestDto.getStartDate(), bookingRequestDto.getEndDate());
        }

        bookingService.createReservation(bookingRequestDto.getUserId(), bookingRequestDto.getEndDate());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    private String getClientIpAddr(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader != null && !xForwardedForHeader.isEmpty()) {
            return xForwardedForHeader.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
