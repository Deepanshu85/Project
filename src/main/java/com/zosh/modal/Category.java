package com.zosh.modal;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @NonNull
    @Column(unique = true)
    private String categoryId;

    @ManyToOne
    private Category parentCategory;


    @NonNull
    private Integer level;

}
