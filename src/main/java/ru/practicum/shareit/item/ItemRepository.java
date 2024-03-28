package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findByOwnerId(Long userId, Pageable pageable);

    List<Item> findByRequestId(Long requestId);

    List<Item> getItemsByRequestId(Long requestId, Sort sort);

    List<Item> findByRequestIdIn(List<Long> itemRequestIds);

    @Query(value = "SELECT i FROM Item i WHERE i.available = true " +
            "AND (upper(i.name) LIKE %:string% OR upper(i.description) LIKE %:string%) " +
            "ORDER BY i.id")
    Page<Item> searchItems(String string, Pageable pageable);
}
