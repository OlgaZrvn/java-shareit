package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponse;
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
    public BookingResponse saveBooking(Long userId, BookingDto bookingDto, BindingResult bindingResult) {
        validation(bindingResult);
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() ->
                new NotFoundException("Товар не найден"));
        if (!item.getAvailable()) {
            throw new ValidationException("Товар недоступен для бронирования");
        }
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));
        if (bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new ValidationException("Время начала не может быть равно времени окончания бронирования");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart()) || bookingDto.getEnd() == bookingDto.getStart()) {
            throw new ValidationException("Время начала не может быть позже времени окончания бронирования");
        }
        if (item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " является владельцем товара");
        }
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
    public BookingResponse updateBooking(Long userId, Long bookingId, String approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException("Бронирование не найдено"));
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не является владельцем товара");
        }
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new ValidationException("Бронирование уже одобрено");
        }
        if (approved.equals("true")) {
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
    public List<BookingResponse> getAllBookings(Long userId, State state) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id " + userId + " не найден"));
        List<Booking> bookingList;
        switch (state) {
            case ALL:
                bookingList = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
                break;
            case CURRENT:
                bookingList = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                        LocalDateTime.now(),
                        LocalDateTime.now());
                break;
            case PAST:
                bookingList = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId,
                        LocalDateTime.now());
                break;
            case FUTURE:
                bookingList = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId,
                        LocalDateTime.now());
                break;
            case WAITING:
                bookingList = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
                break;
            case REJECTED:
                bookingList = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
                break;
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookingList.stream().map(bookingMapper::toBookingResponse).collect(Collectors.toList());
    }

    @Override
    public List<BookingResponse> getAllBookingByItemOwner(Long userId, State state) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id " + userId + " не найден"));
        List<Booking> bookingList;
        switch (state) {
            case ALL:
                bookingList = bookingRepository.findAllByItemOwnerOrderByStartDesc(user);
                break;
            case CURRENT:
                bookingList = bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(user,
                        LocalDateTime.now(), LocalDateTime.now());
                break;
            case PAST:
                bookingList = bookingRepository.findAllByItemOwnerAndEndBeforeOrderByStartDesc(user, LocalDateTime.now());
                break;
            case FUTURE:
                bookingList = bookingRepository.findAllByItemOwnerAndStartAfterOrderByStartDesc(user, LocalDateTime.now());
                break;
            case WAITING:
                bookingList = bookingRepository.findAllByItemOwnerAndStatusOrderByStartDesc(user, Status.WAITING);
                break;
            case REJECTED:
                bookingList = bookingRepository.findAllByItemOwnerAndStatusOrderByStartDesc(user, Status.REJECTED);
                break;
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookingList.stream().map(bookingMapper::toBookingResponse).collect(Collectors.toList());
    }

    public static void validation(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField()).append(" - ")
                        .append(error.getDefaultMessage()).append(";");
            }
            throw new ValidationException(errorMsg.toString());
        }
    }
}