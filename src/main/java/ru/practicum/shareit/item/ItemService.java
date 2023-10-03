package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.NoDataException;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemStorageImpl itemStorage;
    private final UserStorage userStorage;
    private final ItemMapper itemMapper;

    public ItemDto createItem(ItemDto newItem, Long ownerId) {
        if (userStorage.getUser(ownerId) == null) {
            throw new NoDataException("Пользователь не существует");
        }
        if (newItem.getAvailable() == null) {
            throw new ValidationException("Не задан статус");
        }
        if (newItem.getName().isEmpty()) {
            throw new ValidationException("Не задано имя");
        }
        if (newItem.getDescription() == null) {
            throw new ValidationException("Не задано описание");
        }
        return itemMapper.itemToItemDto(itemStorage.createItem(itemMapper.itemDtoToItem(newItem, ownerId)));
    }

    public ItemDto updateItem(Long itemId, ItemDto newItem, Long ownerId) {
        Item foundItem = itemStorage.getItemById(itemId);
        if (userStorage.getUser(ownerId) == null) {
            throw new NoDataException("Пользователь не существует");
        }
        if (itemStorage.getItemById(itemId).getOwnerId() != ownerId) {
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

        return itemMapper.itemToItemDto(foundItem);
    }

    public ItemDto getItemById(Long itemId) {
        return itemMapper.itemToItemDto(itemStorage.getItemById(itemId));
    }

    public List<ItemDto> getAllItemByOwner(Long ownerId) {
        return itemStorage.getAllItemByOwner(ownerId).stream()
                .map(itemMapper::itemToItemDto)
                .collect(Collectors.toList());
    }

    public List<ItemDto> searchItem(String request) {
        if (request.isBlank()) {
            return new ArrayList<>();
        }
        return itemStorage.searchItem(request).stream()
                .map(itemMapper::itemToItemDto)
                .collect(Collectors.toList());
    }


}
