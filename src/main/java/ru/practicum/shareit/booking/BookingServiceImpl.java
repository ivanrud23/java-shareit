package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exeption.NoDataException;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public BookingResponseDto createBooking(BookingRequestDto bookingDto, Long booker) {
        User bookerUser = userRepository.findById(booker).orElseThrow(() -> new NoDataException("Нет такого владельца"));
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() -> new NoDataException("Нет такого вещи"));
        Long itemOwnerId = item.getOwner().getId();

        if (booker == itemOwnerId) {
            throw new NoDataException("Букер не может быть владельцем");
        }
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь недоступна");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())
                || bookingDto.getEnd().isBefore(LocalDateTime.now())
                || bookingDto.getStart().isBefore(LocalDateTime.now())
                || bookingDto.getEnd().isEqual(bookingDto.getStart())
        ) {
            throw new ValidationException("Дата не верна");
        }

        return BookingMapper.mapToBookingResponseDto(bookingRepository.save(BookingMapper.mapToBooking(bookingDto, bookerUser, item)));
    }

    @Transactional
    @Override
    public BookingResponseDto updateBookingApprove(Long bookingId, Boolean approved, Long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NoDataException("Бронирование не существует"));
        if (booking.getItem().getOwner().getId() != userId) {
            throw new NoDataException("Только владелец может обновлять статус бронирования");
        }
        if (approved) {
            if (booking.getStatus().equals(BookingStatus.APPROVED)) {
                throw new ValidationException("Статус бронирование уже подтверждён");
            }
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return BookingMapper.mapToBookingResponseDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto getBookingById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NoDataException("Бронирование не существует"));
        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            throw new NoDataException("Только владелец или букер могут получить данные бронирования");
        }
        return BookingMapper.mapToBookingResponseDto(bookingRepository.findById(bookingId).orElseThrow(() -> new NoDataException("Бронирование не существует")));
    }

    @Override
    public List<BookingResponseDto> getBookingByItemId(Long itemId, Long userId) {
        return bookingRepository.findByItemId(itemId).stream()
                .map(BookingMapper::mapToBookingResponseDto)
                .collect(Collectors.toList());

    }

    @Override
    public List<BookingResponseDto> getAllBookingByBookerId(String stateStr, Long bookerId) {
        BookingState state;
        try {
            state = BookingState.valueOf(stateStr);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }

        if (userRepository.findAll().stream()
                .noneMatch(user -> user.getId() == bookerId)) {
            throw new NoDataException("Пользователь не сделал ни одной брони");
        }
        switch (state) {
            case ALL:
                return bookingRepository.findByBookerId(bookerId).stream()
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .map(BookingMapper::mapToBookingResponseDto)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findByBookerId(bookerId).stream()
                        .filter(booking -> booking.getStart().isBefore(LocalDateTime.now())
                                && booking.getEnd().isAfter(LocalDateTime.now()))
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .map(BookingMapper::mapToBookingResponseDto)
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.findByBookerId(bookerId).stream()
                        .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .map(BookingMapper::mapToBookingResponseDto)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findByBookerId(bookerId).stream()
                        .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .map(BookingMapper::mapToBookingResponseDto)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findByBookerId(bookerId).stream()
                        .filter(booking -> booking.getStatus().equals(BookingStatus.WAITING))
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .map(BookingMapper::mapToBookingResponseDto)
                        .collect(Collectors.toList());
            default:
                return bookingRepository.findByBookerId(bookerId).stream()
                        .filter(booking -> booking.getStatus().equals(BookingStatus.REJECTED))
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .map(BookingMapper::mapToBookingResponseDto)
                        .collect(Collectors.toList());
        }

    }

    @Override
    public List<BookingResponseDto> getAllBookingByOwnerId(String stateStr, Long ownerId) {
        BookingState state;
        try {
            state = BookingState.valueOf(stateStr);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
        if (userRepository.findAll().stream()
                .noneMatch(user -> user.getId() == ownerId)) {
            throw new NoDataException("Только владелец может получить данные бронирования");
        }

        switch (state) {
            case ALL:
                return bookingRepository.findByItemOwnerId(ownerId).stream()
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .map(BookingMapper::mapToBookingResponseDto)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findByItemOwnerId(ownerId).stream()
                        .filter(booking -> booking.getStart().isBefore(LocalDateTime.now())
                                && booking.getEnd().isAfter(LocalDateTime.now()))
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .map(BookingMapper::mapToBookingResponseDto)
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.findByItemOwnerId(ownerId).stream()
                        .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .map(BookingMapper::mapToBookingResponseDto)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findByItemOwnerId(ownerId).stream()
                        .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .map(BookingMapper::mapToBookingResponseDto)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findByItemOwnerId(ownerId).stream()
                        .filter(booking -> booking.getStatus().equals(BookingStatus.WAITING))
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .map(BookingMapper::mapToBookingResponseDto)
                        .collect(Collectors.toList());
            default:
                return bookingRepository.findByItemOwnerId(ownerId).stream()
                        .filter(booking -> booking.getStatus().equals(BookingStatus.REJECTED))
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .map(BookingMapper::mapToBookingResponseDto)
                        .collect(Collectors.toList());

        }

    }

}