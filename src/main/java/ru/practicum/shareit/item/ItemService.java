package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {

    ItemDto createItem(ItemDto newItem, Long ownerId);

    ItemResponseDto updateItem(Long itemId, ItemDto newItem, Long ownerId);

    List<ItemResponseDto> getAllItems(Long userId, Integer from, Integer size);

    ItemResponseDto getItemById(Long itemId, Long userId);

    List<ItemDto> searchItem(String request, Integer from, Integer size);

    CommentDto createComment(Long itemId, CommentDto commentDto, Long userId);
}

