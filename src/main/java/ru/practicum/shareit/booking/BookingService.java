package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponse;

import java.util.List;

public interface BookingService {

    BookingResponse saveBooking(Long userId, BookingDto bookingDto);

    BookingResponse updateBooking(Long userId, Long bookingId, Boolean approved);

    BookingResponse getBookingById(Long bookingId, Long userId);

    List<BookingResponse> getAllBookings(Long userId, State state, Integer from, Integer size);

    List<BookingResponse> getAllBookingByItemOwner(Long userId, State state, Integer from, Integer size);
}
