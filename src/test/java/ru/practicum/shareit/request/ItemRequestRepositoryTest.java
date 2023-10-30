package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRequestRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;


    @Test
    @DirtiesContext
    void findByRequesterId() {
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

        itemRepository.save(ItemMapper.mapToItem(itemDto, owner));

        List<ItemRequest> itemRequestList = itemRequestRepository.findByRequesterId(2L);
        assertEquals(itemRequestList.size(), 1);
    }
}