package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentMapperNew;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentResponse;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemBooking;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemMapper itemMapper;
    private final CommentMapperNew commentMapper;

    @Override
    @Transactional
    public ItemResponse saveItem(Long userId, Item item) {
        log.info("Проверяем пользователя с id {}", userId);
        User owner = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id " + userId + " не найден"));
        item.setOwner(owner);
        log.info("Создан новый товар {}", item.getName());
        return itemMapper.toItemResponse(itemRepository.save(item));
    }

    @Override
    public List<ItemResponse> getAllItemsUser(Long userId) {
        return itemRepository.findByOwnerId(userId).stream().map(x ->
                getItemById(x.getId(), userId)).collect(Collectors.toList());
    }

    @Override
    public ItemResponse getItemById(Long itemId, Long userId) {
        Item item =  itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException("Товар с id " + itemId + " не найден"));
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id " + userId + " не найден"));
        ItemResponse itemResponse = itemMapper.toItemResponse(item);
        if (user.getId() == item.getOwner().getId()) {
            addLastAndNextBooking(itemResponse, userId);
        }
        List<Comment> commentList = commentRepository.findByItemId(itemId);
        itemResponse.setComments(commentList
                .stream()
                .map(commentMapper::toCommentResponse)
                .collect(Collectors.toList()));
        return itemResponse;
    }

    @Override
    @Transactional
    public ItemResponse updateItem(Long itemId, Long userId, Item item) {
        User owner = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id " + userId + " не найден"));
        checkOwner(userId, itemId);
        ItemResponse updatedItem = getItemById(itemId, userId);
        if (item.getName() != null) {
            updatedItem.setName(item.getName());
        } else {
            updatedItem.setName(itemRepository.getReferenceById(itemId).getName());
        }
        if (item.getDescription() != null) {
            updatedItem.setDescription(item.getDescription());
        } else {
            updatedItem.setDescription(itemRepository.getReferenceById(itemId).getDescription());
        }
        if (item.getAvailable() != null) {
            updatedItem.setAvailable(item.getAvailable());
        } else {
            updatedItem.setAvailable(itemRepository.getReferenceById(itemId).getAvailable());
        }
        log.info("Товар с id {} обновлен", itemId);
        itemRepository.save(itemMapper.toItem(updatedItem));
        return updatedItem;
    }

    @Override
    public List<Item> searchItems(String string) {
        if (string.isBlank()) {
            return new ArrayList<>();
        } else {
            return itemRepository.searchItems(string.toUpperCase())
                    .stream()
                    .collect(Collectors.toList());
        }
    }

    private void addLastAndNextBooking(ItemResponse item, Long ownerId) {
        Booking lastBooking = bookingRepository
                .findFirstByItemIdAndItemOwnerIdAndStartBeforeAndStatusOrderByStartDesc(
                        item.getId(),
                        ownerId,
                        LocalDateTime.now(),
                        Status.APPROVED);
        Booking nextBooking = bookingRepository
                .findFirstByItemIdAndItemOwnerIdAndStartAfterAndStatusOrderByStartAsc(
                        item.getId(),
                        ownerId,
                        LocalDateTime.now(),
                        Status.APPROVED);

        if (lastBooking != null) {
            item.setLastBooking(new ItemBooking(lastBooking.getId(), lastBooking.getBooker().getId()));
        }
        if (nextBooking != null) {
            item.setNextBooking(new ItemBooking(nextBooking.getId(), nextBooking.getBooker().getId()));
        }
    }

    @Override
    @Transactional
    public CommentResponse saveComment(Long userId, CommentDto commentDto, Long itemId) {
        if (commentDto.getText().isBlank() || commentDto.getText() == null) {
            throw new ValidationException("Текст комментария не может быть пустым");
        }
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException("Товар с id " + itemId + " не найден"));
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователяс id " + userId + " не найден"));
        bookingRepository.findFirstByBookerIdAndItemIdAndEndBefore(userId, itemId, LocalDateTime.now())
                .orElseThrow(() -> new ValidationException("Пользователь с id " + userId +
                        " ещё не брал в аренду этот товар"));
        if (item.getOwner().getId() == userId) {
            throw new ValidationException("Пользователь с id " + userId + " является владельцем");
        }
        Comment comment = new Comment();
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setText(commentDto.getText());
        comment.setCreated(LocalDateTime.now());
        // commentResponse.setAuthorName(user.getName());
        log.info("Добавлен отзыв");
        return commentMapper.toCommentResponse(commentRepository.save(comment));
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
