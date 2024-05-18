package com.app.repositories;

import com.app.models.BookedPeriods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBookedPeriodsRepository extends JpaRepository<BookedPeriods, Long> {
}
