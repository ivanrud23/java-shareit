package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exeption.ValidationException;

import javax.validation.constraints.Min;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@Validated
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestBody ItemDto newItem, @RequestHeader("X-Sharer-User-Id") Long userId) {
        if (newItem.getAvailable() == null) {
            throw new ValidationException("Не задан статус");
        }
        if (newItem.getName().isEmpty()) {
            throw new ValidationException("Не задано имя");
        }
        if (newItem.getDescription() == null) {
            throw new ValidationException("Не задано описание");
        }
        return itemClient.createItem(newItem, userId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@PathVariable("id") Long itemId, @RequestBody ItemDto newItem,
                                             @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemClient.updateItem(itemId, newItem, ownerId);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemByIdResponse(@PathVariable("id") Long itemId,
                                                      @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemsWithBooking(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                         @RequestParam(defaultValue = "0") @Min(value = 0, message = "Не задан стартовый элемент") Integer from,
                                                         @RequestParam(defaultValue = "10") @Min(value = 1, message = "Не задано количество выводимых элементов") Integer size) {
        return itemClient.getAllItems(ownerId, from, size);
    }


    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestParam("text") String request,
                                             @RequestParam(defaultValue = "0") @Min(value = 0, message = "Не задан стартовый элемент") Integer from,
                                             @RequestParam(defaultValue = "10") @Min(value = 1, message = "Не задано количество выводимых элементов") Integer size) {
        return itemClient.searchItem(request, userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@PathVariable("itemId") Long itemId,
                                                @RequestBody CommentDto commentDto,
                                                @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.createComment(itemId, userId, commentDto);
    }

}
