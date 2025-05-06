package com.zosh.modal;

import com.zosh.domain.HomeCategorySection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class HomeCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private String image;
    private String categoryId;
    private HomeCategorySection section;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
