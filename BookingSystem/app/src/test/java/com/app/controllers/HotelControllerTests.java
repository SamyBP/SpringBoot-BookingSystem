package com.app.controllers;

import com.app.dtos.Location;
import com.app.models.Hotel;
import com.app.models.Room;
import com.app.services.BookingService;
import com.app.services.HotelService;
import com.app.services.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = HotelController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class HotelControllerTests {
    @MockBean
    private HotelService hotelService;
    @MockBean
    private LocationService locationService;
    @MockBean
    private BookingService bookingService;
    @Autowired
    private MockMvc mockMvc;

    private List<Hotel> hotels;
    private List<Room> rooms;

    @BeforeEach
    public void init() {
        Hotel hotel = Hotel.builder().id(1L).longitude(49.764654252624204).latitude(49.764654252624204).name("Hotel").build();
        Room room = Room.builder().roomNumber(21L).hotel(hotel).bookedPeriods(null).isAvailable(true).price(150).type((short) 1).build();
        rooms = new ArrayList<>();
        rooms.add(room);
    }

    @Test
    public void HotelController_GetAllWithFilters_StatusOk() throws Exception {

        when(hotelService.findWithFilters(anyDouble(), anyDouble(), anyInt(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.emptyList());

        when(locationService.getUsersLocation(anyString()))
                .thenReturn(new Location(49.764654252624204, 48.764654252624204));

        LocalDate startDate = LocalDate.of(2024, 5, 20);
        LocalDate endDate = LocalDate.of(2024, 5, 22);
        int radius = 10;

        ResultActions resultActions = mockMvc.perform(get("/api/hotels/nearme/{radius}", radius)
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString()));

        resultActions.andExpect(status().isOk());

        verify(hotelService, times(1)).findWithFilters(anyDouble(), anyDouble(), anyInt(), any(LocalDate.class), any(LocalDate.class));
    }
}
