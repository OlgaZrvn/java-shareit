package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepositoryImpl userRepository;

    @Override
    public Item saveItem(Long userId, Item item) {
        checkUser(userId);
        log.info("Создан новый товар {}", item.getName());
        return itemRepository.saveItem(userRepository.findById(userId), item);
    }

    @Override
    public List<Item> getAllItemsUser(Long userId) {
        return itemRepository.getAllItemsUser(userId);
    }

    @Override
    public Item getItemById(Long id) {
        return itemRepository.getItemById(id);
    }

    @Override
    public Item updateItem(Long itemId, Long userId, Item item) {
        checkUser(userId);
        checkOwner(userId, itemId);
        log.info("Товар с id {} обновлен", itemId);
        return itemRepository.updateItem(itemId, item);
    }

    @Override
    public List<Item> searchItems(String string) {
        if (string.isBlank()) {
            return new ArrayList<>();
        } else {
            return itemRepository.searchItems(string);
        }
    }

    private void checkUser(Long id) {
        if (null == userRepository.findById(id)) {
            log.error("Пользователь не найден");
            throw new NotFoundException("Пользователь не найден");
        }
    }

    private void checkOwner(Long userId, Long itemId) {
        if (!userRepository.findById(userId).equals(itemRepository.getItemById(itemId).getOwner())) {
                throw new NotFoundException("Нельзя обновлять вещь через другого пользователя");
        }
    }
}
