package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestServiceImpl itemRequestService;

    @PostMapping
    public ItemRequestDtoResponse createItemRequest(@Valid @RequestBody ItemRequestDto itemRequestDto, @RequestHeader("X-Sharer-User-Id") Long requesterId) {
        return itemRequestService.createItemRequest(itemRequestDto, requesterId);
    }

    @GetMapping
    public List<ItemRequestDtoResponse> getItemRequest(@RequestHeader("X-Sharer-User-Id") Long requesterId) {
        return itemRequestService.getAllItemRequestByUser(requesterId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoResponse> getItemRequestAnotherUser(@RequestHeader("X-Sharer-User-Id") Long requesterId,
                                                                  @RequestParam(defaultValue = "0") Integer from,
                                                                  @RequestParam(defaultValue = "10") Integer size) {
        return itemRequestService.getItemRequestByOtherUser(requesterId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoResponse getItemRequestById(@PathVariable Long requestId,
                                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getItemRequestById(requestId, userId);
    }

}
