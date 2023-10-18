package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {


    @Query("select it " +
            "from Item as it " +
            "where it.ownerId = ?1 ")
    List<Item> getAllItemByOwnerId(Long ownerId);

    @Query("select it " +
            "from Item as it " +
            "where LOWER(it.name) LIKE LOWER((concat('%', ?1, '%'))) " +
            "OR LOWER(it.description) LIKE LOWER((concat('%', ?1, '%')))")
    List<Item> findItemByUserText(String request);


}