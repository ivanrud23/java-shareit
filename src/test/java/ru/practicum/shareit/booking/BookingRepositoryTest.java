package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.ItemRequestDto;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookingRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void findByItemId() {
        UserDto userDto = new UserDto();
        userDto.setEmail("jon@gmail.com");
        userDto.setName("Jon");
        UserDto userDtoRequester = new UserDto();
        userDtoRequester.setEmail("donald@gmail.com");
        userDtoRequester.setName("Donald");
        User owner = userRepository.save(UserMapper.dtoToUSer(userDto));
        User requester = userRepository.save(UserMapper.dtoToUSer(userDtoRequester));

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setCreated(LocalDateTime.of(2023, Month.APRIL, 8, 12, 30));
        itemRequestDto.setDescription("Description");
        itemRequestRepository.save(ItemRequestMapper.mapToItemRequest(itemRequestDto, requester));

        ItemDto itemDto = new ItemDto();
        itemDto.setAvailable(true);
        itemDto.setDescription("item description");
        itemDto.setName("item name");
        itemDto.setRequestId(1L);
        Item item = itemRepository.save(ItemMapper.mapToItem(itemDto, owner));

        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setStart(LocalDateTime.of(2023, Month.DECEMBER, 8, 12, 30));
        bookingRequestDto.setEnd(LocalDateTime.of(2023, Month.DECEMBER, 8, 12, 40));
        bookingRepository.save(BookingMapper.mapToBooking(bookingRequestDto, requester, item));

        List<Booking> bookingList = bookingRepository.findByItemId(1L);
        assertEquals(bookingList.size(), 1);
    }
}