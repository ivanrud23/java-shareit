package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item createItem(Item newItem);

    Item updateItem(Long itemId, Item newItem, Long ownerId);

    Item getItemById(Long itemId);

    List<Item> getAllItemByOwner(Long ownerId);

    List<Item> searchItem(String request);
}
