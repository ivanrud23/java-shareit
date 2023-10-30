package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
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
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Test
    @DirtiesContext
    void findByOwnerIdOrderById() throws Exception {
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

        PageRequest page = PageRequest.of(1 / 10, 10);

        List<Item> itemList = itemRepository.findByOwnerIdOrderById(1L, page);
        assertEquals(itemList.size(), 1);
    }

    @Test
    @DirtiesContext
    void findItemByUserText() {
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
        itemDto.setDescription("item description text");
        itemDto.setName("item name");
        itemDto.setRequestId(1L);

        itemRepository.save(ItemMapper.mapToItem(itemDto, owner));

        PageRequest page = PageRequest.of(1 / 10, 10);

        List<Item> itemList = itemRepository.findItemByUserText("tExT");
        assertEquals(itemList.size(), 1);
    }


}