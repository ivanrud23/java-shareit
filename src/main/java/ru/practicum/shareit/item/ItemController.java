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
        return itemService.getItemByIdResponse(itemId, userId);
    }

    @GetMapping
    public List<ItemResponseDto> getAllItemsWithBooking(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.getAllItemsWithBooking(ownerId);
    }


    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam("text") String request) {
        return itemService.searchItem(request);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@PathVariable("itemId") Long itemId,
                                    @RequestBody CommentDto commentDto,
                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.createComment(itemId, commentDto, userId);
    }

}
