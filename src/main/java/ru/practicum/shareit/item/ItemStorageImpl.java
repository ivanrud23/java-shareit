package ru.practicum.shareit.item;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Data
public class ItemStorageImpl implements ItemStorage {
    private final Map<Long, Item> itemStorage = new HashMap<>();
    private int idCounter = 1;


    public int counter() {
        return idCounter++;
    }

    @Override
    public Item createItem(Item newItem) {
        newItem.setId(counter());
        itemStorage.put(newItem.getId(), newItem);
        return newItem;
    }

    @Override
    public Item updateItem(Long itemId, Item newItem, Long ownerId) {
        newItem.setId(itemId);
        newItem.setOwnerId(ownerId);
        itemStorage.put(itemId, newItem);
        return newItem;
    }

    @Override
    public Item getItemById(Long itemId) {
        return itemStorage.get(itemId);
    }

    @Override
    public List<Item> getAllItemByOwner(Long ownerId) {
        return itemStorage.values().stream()
                .filter(itemDto -> itemDto.getOwnerId() == ownerId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItem(String request) {
        return itemStorage.values().stream()
                .filter(itemDto -> itemDto.getName().toLowerCase().contains(request.toLowerCase()) ||
                        itemDto.getDescription().toLowerCase().contains(request.toLowerCase()))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }
}
