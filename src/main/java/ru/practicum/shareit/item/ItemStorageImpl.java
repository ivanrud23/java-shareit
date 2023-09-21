package ru.practicum.shareit.item;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Data
public class ItemStorageImpl implements ItemStorage {
    private final Map<Long, ItemDto> itemStorage = new HashMap<>();
    private int idCounter = 1;


    public int counter() {
        return idCounter++;
    }

    @Override
    public ItemDto createItem(ItemDto newItem, Long ownerId) {
        newItem.setId(counter());
        newItem.setOwnerId(ownerId);
        itemStorage.put(newItem.getId(), newItem);
        return newItem;
    }

    @Override
    public ItemDto updateItem(Long itemId, ItemDto newItem, Long ownerId) {
        newItem.setId(itemId);
        newItem.setOwnerId(ownerId);
        itemStorage.put(itemId, newItem);
        return newItem;
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        return itemStorage.get(itemId);
    }

    @Override
    public List<ItemDto> getAllItemByOwner(Long ownerId) {
        return itemStorage.values().stream()
                .filter(itemDto -> itemDto.getOwnerId() == ownerId)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItem(String request) {
        return itemStorage.values().stream()
                .filter(itemDto -> itemDto.getName().toLowerCase().contains(request.toLowerCase()) ||
                        itemDto.getDescription().toLowerCase().contains(request.toLowerCase()))
                .filter(ItemDto::getAvailable)
                .collect(Collectors.toList());
    }
}
