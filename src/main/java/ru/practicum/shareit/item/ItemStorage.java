package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemStorage {
    ItemDto createItem(ItemDto newItem, Long ownerId);

    ItemDto updateItem(Long itemId, ItemDto newItem, Long ownerId);

    ItemDto getItemById(Long itemId);

    List<ItemDto> getAllItemByOwner(Long ownerId);

    List<ItemDto> searchItem(String request);
}
