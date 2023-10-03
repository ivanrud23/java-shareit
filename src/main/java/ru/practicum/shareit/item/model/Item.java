package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO Sprint add-controllers.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private long ownerId;
    private Boolean request;
}
