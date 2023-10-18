package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;


@Entity
@Table(name = "items")
@Getter
@Setter
@ToString
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Boolean available;

    @Column
    private Long ownerId;

    @Column
    private Long requestId;

    private Boolean request;

//    private List<Integer> commentId;

}
