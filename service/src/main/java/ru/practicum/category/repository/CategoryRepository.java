package ru.practicum.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.category.model.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    @Query(value = """
            SELECT *
            FROM categories AS c
            GROUP BY c.category_id
            ORDER BY c.category_id ASC
            LIMIT :size OFFSET :from""",nativeQuery = true)
    List<Category> findAllCategories(Integer from,Integer size);
}
