package com.app.services;

import com.app.models.Hotel;
import com.app.repositories.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HotelService {
    private final HotelRepository hotelRepository;

    @Autowired
    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public List<Hotel> findWithFilters(double lat, double lon, int radius, LocalDate startDate, LocalDate endDate) {
        return hotelRepository.findWithFilters(lat, lon, radius, startDate, endDate);
    }
}
