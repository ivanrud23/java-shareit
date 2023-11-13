package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestBody ItemDto newItem, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.createItem(newItem, userId);
    }

    @PatchMapping("/{id}")
    public ItemResponseDto updateItem(@PathVariable("id") Long itemId, @RequestBody ItemDto newItem, @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemService.updateItem(itemId, newItem, ownerId);
    }


    @GetMapping("/{id}")
    public ItemResponseDto getItemByIdResponse(@PathVariable("id") Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemResponseDto> getAllItemsWithBooking(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                        @RequestParam(defaultValue = "0") Integer from,
                                                        @RequestParam(defaultValue = "10") Integer size) {
        return itemService.getAllItems(ownerId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam("text") String request,
                                    @RequestParam(defaultValue = "0") Integer from,
                                    @RequestParam(defaultValue = "10") Integer size) {
        return itemService.searchItem(request, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@PathVariable("itemId") Long itemId,
                                    @RequestBody CommentDto commentDto,
                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.createComment(itemId, commentDto, userId);
    }

}
