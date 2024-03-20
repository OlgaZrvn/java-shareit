package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.State;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingServiceImpl bookingService;

    @PostMapping
    public BookingResponse addBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @Valid @RequestBody BookingDto bookingDto, BindingResult bindingResult) {
        return bookingService.saveBooking(userId, bookingDto, bindingResult);
    }

    @PatchMapping("{bookingId}")
    public BookingResponse updateStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @PathVariable("bookingId") Long bookingId,
                                        @RequestParam String approved) {
        return bookingService.updateBooking(userId, bookingId, approved);
    }

    @GetMapping("{bookingId}")
    public BookingResponse getBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @PathVariable("bookingId") Long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingResponse> getBookingList(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestParam(defaultValue = "ALL") State state) {
        return bookingService.getBookingList(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingResponse> getBookingListByItemOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                           @RequestParam(defaultValue = "ALL") State state) {
        return bookingService.getBookingListByItemOwner(userId, state);
    }
}