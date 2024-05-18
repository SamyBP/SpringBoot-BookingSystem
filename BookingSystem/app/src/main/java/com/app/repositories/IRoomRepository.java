package com.app.repositories;

import com.app.models.Hotel;
import com.app.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoomRepository extends JpaRepository<Room, Long> {
}
