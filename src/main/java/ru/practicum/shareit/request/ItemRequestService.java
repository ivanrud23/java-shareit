package ru.practicum.shareit.request;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ItemRequestService {
    @Transactional
    ItemRequestDtoResponse createItemRequest(ItemRequestDto itemRequestDto, Long requesterId);

    List<ItemRequestDtoResponse> getAllItemRequestByUser(Long userId);

    List<ItemRequestDtoResponse> getItemRequestByOtherUser(Long userId, Integer from, Integer size);

    ItemRequestDtoResponse getItemRequestById(Long requestId, Long userId);
}
