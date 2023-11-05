package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@Validated
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@Valid @RequestBody ItemRequestDto itemRequestDto, @RequestHeader("X-Sharer-User-Id") Long requesterId) {
        return itemRequestClient.createItemRequest(itemRequestDto, requesterId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequest(@RequestHeader("X-Sharer-User-Id") Long requesterId) {
        return itemRequestClient.getAllItemRequestByUser(requesterId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getItemRequestAnotherUser(@RequestHeader("X-Sharer-User-Id") Long requesterId,
                                                            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Не задан стартовый элемент") Integer from,
                                                            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Не задано количество выводимых элементов") Integer size) {
        return itemRequestClient.getItemRequestByOtherUser(requesterId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@PathVariable Long requestId,
                                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestClient.getItemRequestById(requestId, userId);
    }

}
