package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO Sprint add-controllers.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemForBookingDto {
    private Long id;
    private String name;
}
