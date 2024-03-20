package ru.practicum.shareit.booking;

import org.springframework.validation.BindingResult;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponse;

import java.util.List;

public interface BookingService {

    BookingResponse saveBooking(Long userId, BookingDto bookingDto, BindingResult bindingResult);

    BookingResponse updateBooking(Long userId, Long bookingId, String approved);

    BookingResponse getBookingById(Long bookingId, Long userId);

    List<BookingResponse> getAllBookings(Long userId, State state);

    List<BookingResponse> getAllBookingsByItemOwner(Long userId, State state);
}
