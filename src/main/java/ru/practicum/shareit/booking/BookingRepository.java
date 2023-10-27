package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findByBookerIdOrderByStartDesc(Long bookerId, Pageable page);

    Page<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId, Pageable page);

    List<Booking> findByItemId(Long itemId);

    Optional<Booking> findFirstByItemIdAndStartAfterAndStatusNotOrderByStart(Long itemId, LocalDateTime localDateTime, BookingStatus status);

    Optional<Booking> findFirstByItemIdAndStartBeforeAndStatusNotOrderByStartDesc(Long itemId, LocalDateTime localDateTime, BookingStatus status);


}
