package com.zosh.repository;

import com.zosh.modal.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long>
{
    Category findByCategoryId(String categoryId);
}
