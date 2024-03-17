package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwnerId(Long userId);

    @Query(value = "SELECT i FROM Item i WHERE i.available = true " +
            "AND (upper(i.name) LIKE %:string% OR upper(i.description) LIKE %:string%) " +
            "ORDER BY i.id")
    List<Item> searchItems(String string);
}
