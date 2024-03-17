package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Item saveItem(Long userId, Item item) {
        log.info("Проверяем пользователя с id {}", userId);
        User owner = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id " + userId + " не найден"));
        item.setOwner(owner);
        log.info("Создан новый товар {}", item.getName());
        return itemRepository.save(item);
    }

    @Override
    public List<Item> getAllItemsUser(Long userId) {
        return itemRepository.findByOwnerId(userId);
    }

    @Override
    public Item getItemById(Long id) {
        return itemRepository.getReferenceById(id);
    }

    @Override
    public Item updateItem(Long itemId, Long userId, Item item) {
        User owner = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id " + userId + " не найден"));
        item.setOwner(owner);
        checkOwner(userId, itemId);
        item.setId(itemId);
        if (item.getName() != null) item.setName(item.getName());
        if (item.getDescription() != null) item.setDescription(item.getDescription());
        if (item.getAvailable() != null) item.setAvailable(item.getAvailable());
        log.info("Товар с id {} обновлен", itemId);
        return itemRepository.save(item);
    }

 /*   @Override
    public List<Item> searchItems(String string) {
        if (string.isBlank()) {
            return new ArrayList<>();
        } else {
            return itemRepository.searchItems(string);
        }
    }

  */

    private void checkUser(Long id) {
        if (null == userRepository.findById(id)) {
            log.error("Пользователь c id {} не найден", id);
            throw new NotFoundException("Пользователь не найден");
        }
    }

    private void checkOwner(Long userId, Long itemId) {
        if (!userRepository.getReferenceById(userId).equals(itemRepository.getReferenceById(itemId).getOwner())) {
            log.error("Пользователь {} не явлеется владельцем товара {}",
                    userRepository.getReferenceById(userId).getName(),
                    itemRepository.getReferenceById(itemId).getName());
            throw new NotFoundException("Нельзя обновлять товар другого пользователя");
        }
    }
}
