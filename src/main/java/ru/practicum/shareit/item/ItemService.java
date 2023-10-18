package ru.practicum.shareit.item;

import java.util.List;

interface ItemService {

    ItemDto createItem(ItemDto newItem, Long ownerId);

    ItemResponseDto updateItem(Long itemId, ItemDto newItem, Long ownerId);

    List<ItemResponseDto> getAllItemsWithBooking(Long userId);

//    ItemResponseDto getItemWithBooking(Long itemId, Long ownerId);

    ItemResponseDto getItemByIdResponse(Long itemId, Long userId);

    List<ItemDto> getAllItemByOwner(Long ownerId);

    List<ItemDto> searchItem(String request);

    CommentDto createComment(Long itemId, CommentDto commentDto, Long userId);
}

