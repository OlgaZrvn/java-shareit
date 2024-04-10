package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingResponse saveBooking(Long userId, BookingDto bookingDto) {
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() ->
                new NotFoundException("Товар не найден"));
        isAvailable(item);
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));
        checkBookingTime(bookingDto);
        checkOwner(userId, item);
        Booking booking = bookingMapper.toBooking(bookingDto);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);
        log.info("Добавлено новое бронирование");
        return bookingMapper.toBookingResponse(booking);
    }

    @Override
    @Transactional
    public BookingResponse updateBooking(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException("Бронирование не найдено"));
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не является владельцем товара");
        }
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new ValidationException("Бронирование уже одобрено");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        bookingRepository.save(booking);
        log.info("Бронирование успешно изменено");
        return bookingMapper.toBookingResponse(booking);
    }

    @Override
    public BookingResponse getBookingById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException("Бронирование не найдено"));
        if (!booking.getItem().getOwner().getId().equals(userId) && !booking.getBooker().getId().equals(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не является владельцем товара");
        }
        return bookingMapper.toBookingResponse(booking);
    }

    @Override
    public List<BookingResponse> getAllBookings(Long userId, State state, Integer from, Integer size) {
        if (from < 0 || size < 0) {
            throw new ValidationException("Неверный from или size");
        }
        PageRequest page = PageRequest.of(from / size, size, Sort.by(DESC, "start"));
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id " + userId + " не найден"));
        Page<Booking> bookingList;
        switch (state) {
            case ALL:
                bookingList = bookingRepository.findAllByBookerIdOrderByStartDesc(userId, page);
                break;
            case CURRENT:
                bookingList = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        page);
                break;
            case PAST:
                bookingList = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId,
                        LocalDateTime.now(),
                        page);
                break;
            case FUTURE:
                bookingList = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId,
                        LocalDateTime.now(),
                        page);
                break;
            case WAITING:
                bookingList = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING, page);
                break;
            case REJECTED:
                bookingList = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED, page);
                break;
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
        log.info("Получен список всех бронирований");
        return bookingList.stream().map(bookingMapper::toBookingResponse).collect(Collectors.toList());
    }

    @Override
    public List<BookingResponse> getAllBookingByItemOwner(Long userId, State state, Integer from, Integer size) {
        if (from < 0 || size < 0) {
            throw new ValidationException("Неверный from или size");
        }
        PageRequest page = PageRequest.of(from / size, size, Sort.by(DESC, "start"));
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id " + userId + " не найден"));
        Page<Booking> bookingList;
        switch (state) {
            case ALL:
                bookingList = bookingRepository.findAllByItemOwnerOrderByStartDesc(user, page);
                break;
            case CURRENT:
                bookingList = bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(user,
                        LocalDateTime.now(), LocalDateTime.now(), page);
                break;
            case PAST:
                bookingList = bookingRepository.findAllByItemOwnerAndEndBeforeOrderByStartDesc(user,
                        LocalDateTime.now(), page);
                break;
            case FUTURE:
                bookingList = bookingRepository.findAllByItemOwnerAndStartAfterOrderByStartDesc(user,
                        LocalDateTime.now(), page);
                break;
            case WAITING:
                bookingList = bookingRepository.findAllByItemOwnerAndStatusOrderByStartDesc(user, Status.WAITING, page);
                break;
            case REJECTED:
                bookingList = bookingRepository.findAllByItemOwnerAndStatusOrderByStartDesc(user, Status.REJECTED, page);
                break;
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
        log.info("Получен список всех бронирований пользователя с id {}", userId);
        return bookingList.stream().map(bookingMapper::toBookingResponse).collect(Collectors.toList());
    }

    public static void isAvailable(Item item) {
        if (!item.getAvailable()) {
            throw new ValidationException("Товар недоступен для бронирования");
        }
    }

    public static void checkBookingTime(BookingDto bookingDto) {
        if (bookingDto.getStart() == null) {
            throw new ValidationException("Время начала бронирования не может быть пустым");
        }
        if (bookingDto.getEnd() == null) {
            throw new ValidationException("Время окончания бронирования не может быть пустым");
        }
        if (bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Время начала бронирования не может быть в прошлом");
        }
        if (bookingDto.getEnd().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Время окончания бронирования не может быть в прошлом");
        }
        if (bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new ValidationException("Время начала не может быть равно времени окончания бронирования");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new ValidationException("Время начала не может быть позже времени окончания бронирования");
        }
    }

    public static void checkOwner(Long userId, Item item) {
        if (item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " является владельцем товара");
        }
    }
}