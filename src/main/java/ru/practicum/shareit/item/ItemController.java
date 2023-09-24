package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

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
    public ItemDto createItem(@RequestBody ItemDto newItem, @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.createItem(newItem, ownerId);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@PathVariable("id") Long itemId, @RequestBody ItemDto newItem, @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.updateItem(itemId, newItem, ownerId);
    }

    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable("id") Long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDto> getAllItemByOwner(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.getAllItemByOwner(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam("text") String request) {
        return itemService.searchItem(request);
    }

}
