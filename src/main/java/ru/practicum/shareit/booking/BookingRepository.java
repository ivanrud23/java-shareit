package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerId(Long bookerId);

    List<Booking> findByItemOwnerId(Long ownerId);

    List<Booking> findByItemId(Long itemId);

    Optional<Booking> findFirstByItemIdAndStartAfterAndStatusNotOrderByStart(Long itemId, LocalDateTime localDateTime, BookingStatus status);

    Optional<Booking> findFirstByItemIdAndStartBeforeAndStatusNotOrderByStartDesc(Long itemId, LocalDateTime localDateTime, BookingStatus status);


}
