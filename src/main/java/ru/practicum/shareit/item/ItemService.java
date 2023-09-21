package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.NoDataException;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
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

    public Item createItem(ItemDto newItem, Long ownerId) {
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
        return itemFromDto(itemStorage.createItem(newItem, ownerId));
    }

    public Item updateItem(Long itemId, ItemDto newItem, Long ownerId) {
        ItemDto oldItem = itemStorage.getItemStorage().get(itemId);
        if (userStorage.getUser(ownerId) == null) {
            throw new NoDataException("Пользователь не существует");
        }
        if (itemStorage.getItemById(itemId).getOwnerId() != ownerId) {
            throw new NoDataException("Выбранный пользователь не является владельцем");
        }
        if (newItem.getName() == null) {
            newItem.setName(oldItem.getName());
        }
        if (newItem.getDescription() == null) {
            newItem.setDescription(oldItem.getDescription());
        }
        if (newItem.getAvailable() == null) {
            newItem.setAvailable(oldItem.getAvailable());
        }

        return itemFromDto(itemStorage.updateItem(itemId, newItem, ownerId));
    }

    public Item getItemById(Long itemId) {
        return itemFromDto(itemStorage.getItemById(itemId));
    }

    public List<Item> getAllItemByOwner(Long ownerId) {
        return itemStorage.getAllItemByOwner(ownerId).stream()
                .map(this::itemFromDto)
                .collect(Collectors.toList());
    }

    public List<Item> searchItem(String request) {
        if (request.isBlank()) {
            return new ArrayList<>();
        }
        return itemStorage.searchItem(request).stream()
                .map(this::itemFromDto)
                .collect(Collectors.toList());
    }

    public Item itemFromDto(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable());
    }
}
