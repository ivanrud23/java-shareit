package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingItemDto;
import ru.practicum.shareit.user.User;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {

    public static ItemDto mapToItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

    public static Item mapToItem(ItemDto itemDto, User owner) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(owner);
        return item;
    }

    public static ItemResponseDto mapToItemResponseDto(Item item, BookingItemDto lastBooking, BookingItemDto nextBooking, List<CommentDto> commentDtoList) {
        return new ItemResponseDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastBooking,
                nextBooking,
                commentDtoList
        );
    }

    public static ItemForBookingDto mapForBookingDto(Item item) {
        return new ItemForBookingDto(
                item.getId(),
                item.getName());
    }
}
