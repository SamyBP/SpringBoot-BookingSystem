package com.app.repositories;

import com.app.models.Hotel;
import com.app.models.Room;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HotelRepository {
    private final JdbcTemplate jdbcTemplate;

    public HotelRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Hotel> findWithFilters(double lat, double lon, int radius, LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT h.id, h.name, h.latitude, h.longitude, " +
                "r.id AS room_id, r.room_number, r.type, r.price, " +
                "CASE WHEN COUNT(b.id) = 0 THEN true ELSE false END AS is_available " +
                "FROM hotel h " +
                "INNER JOIN room r ON h.id = r.hotel_id " +
                "LEFT JOIN booked_periods b ON r.id = b.room_id " +
                "AND (? <= b.end_date AND ? >= b.start_date) " +
                "WHERE 6371 * acos(cos(radians(?)) * cos(radians(h.latitude)) * " +
                "cos(radians(h.longitude) - radians(?)) + " +
                "sin(radians(?)) * sin(radians(h.latitude))) <= ? " +
                "GROUP BY h.id, r.id";

        Object[] params = {endDate,startDate, lat, lon, lat, radius};
        List<Hotel> hotels = new ArrayList<>();

        jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            Long hotelId = rs.getLong("id");
            Hotel hotel = hotels.stream()
                    .filter(h -> h.getId().equals(hotelId))
                    .findFirst()
                    .orElseGet(() -> {
                        Hotel newHotel = null;
                        try {
                            newHotel = new Hotel(
                                    hotelId,
                                    rs.getString("name"),
                                    rs.getDouble("latitude"),
                                    rs.getDouble("longitude"),
                                    new ArrayList<>()
                            );
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        hotels.add(newHotel);
                        return newHotel;
                    });


            boolean isRoomAvailable = rs.getBoolean("is_available");
            if (isRoomAvailable) {
                Room room = new Room(
                        rs.getLong("room_id"),
                        rs.getLong("room_number"),
                        rs.getShort("type"),
                        rs.getDouble("price"),
                        true
                );

                hotel.getRooms().add(room);
            }


            return null;
        });

        return hotels;
    }

}
