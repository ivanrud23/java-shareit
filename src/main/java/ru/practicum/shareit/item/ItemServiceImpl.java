package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.exeption.NoDataException;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public ItemDto createItem(ItemDto newItem, Long ownerId) {

        if (newItem.getAvailable() == null) {
            throw new ValidationException("Не задан статус");
        }
        if (newItem.getName().isEmpty()) {
            throw new ValidationException("Не задано имя");
        }
        if (newItem.getDescription() == null) {
            throw new ValidationException("Не задано описание");
        }
        if (userRepository.findAll().stream()
                .noneMatch(user -> user.getId() == ownerId)) {
            throw new NoDataException("Пользователь не существует");
        }
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new NoDataException("Пользователь не существует"));


        return ItemMapper.mapToItemDto(repository.save(ItemMapper.mapToItem(newItem, owner)));
    }

    @Transactional
    @Override
    public ItemResponseDto updateItem(Long itemId, ItemDto newItem, Long userId) {
        Item foundItem = repository.findById(itemId).orElseThrow(() -> new NoDataException("Вещь не существует"));
        if (foundItem.getOwner().getId() != userId) {
            throw new NoDataException("Выбранный пользователь не является владельцем");
        }
        if (newItem.getName() != null) {
            foundItem.setName(newItem.getName());
        }
        if (newItem.getDescription() != null) {
            foundItem.setDescription(newItem.getDescription());
        }
        if (newItem.getAvailable() != null) {
            foundItem.setAvailable(newItem.getAvailable());
        }
        repository.save(foundItem);

        return getItemByIdResponse(itemId, userId);
    }


    @Override
    public List<ItemResponseDto> getAllItemsWithBooking(Long userId) {
        List<Item> itemsList = repository.findByOwnerId(userId);
        return itemsList.stream()
                .map(item -> getItemByIdResponse(item.getId(), userId))
                .collect(Collectors.toList());
    }

    @Override
    public ItemResponseDto getItemByIdResponse(Long itemId, Long userId) {
        Item item = repository.findById(itemId).orElseThrow(() -> new NoDataException("Вещь не существует"));
        Optional<Booking> last = bookingRepository.findFirstByItemIdAndStartBeforeAndStatusNotOrderByStartDesc(itemId, LocalDateTime.now(), BookingStatus.REJECTED);
        Optional<Booking> next = bookingRepository.findFirstByItemIdAndStartAfterAndStatusNotOrderByStart(itemId, LocalDateTime.now(), BookingStatus.REJECTED);
        BookingItemDto lastBooking;
        BookingItemDto nextBooking;
        if (item.getOwner().getId() != userId) {
            lastBooking = null;
            nextBooking = null;
        } else {
            lastBooking = last.map(BookingMapper::mapToBookerItemDto).orElse(null);
            nextBooking = next.map(BookingMapper::mapToBookerItemDto).orElse(null);
        }
        List<CommentDto> commentDtoList = commentRepository.findByItemId(itemId).stream()
                .map(CommentMapper::mapToCommentDto)
                .collect(Collectors.toList());
        return ItemMapper.mapToItemResponseDto(item, lastBooking, nextBooking, commentDtoList);
    }


    @Override
    public List<ItemDto> getAllItemByOwner(Long ownerId) {
        return repository.findByOwnerId(ownerId).stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItem(String request) {
        if (request.isBlank()) {
            return new ArrayList<>();
        }
        return repository.findItemByUserText(request).stream()
                .filter(Item::getAvailable)
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto createComment(Long itemId, CommentDto commentDto, Long userId) {
        if (commentDto.getText().isBlank()) {
            throw new ValidationException("Комментарий не может быть пустым");
        }
        if (bookingRepository.findByItemId(itemId).stream()
                .anyMatch(booking -> booking.getBooker().getId() == userId &&
                        booking.getStart().isBefore(LocalDateTime.now()))) {
            Comment comment = new Comment();
            comment.setText(commentDto.getText());
            comment.setItem(repository.findById(itemId).orElseThrow(() -> new NoDataException("Вещь не существует")));
            comment.setAuthor(userRepository.findById(userId).orElseThrow(() -> new NoDataException("Пользователь не существует")));
            comment.setCreated(LocalDateTime.now());
            return CommentMapper.mapToCommentDto(commentRepository.save(comment));
        } else {
            throw new ValidationException("Пользователь не существует или еще не пользовался вещью");
        }
    }

}
