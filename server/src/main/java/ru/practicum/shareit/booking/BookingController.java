package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingServiceImpl bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                         @RequestBody BookingDto bookingDto) {
        return ResponseEntity.ok().body(bookingService.saveBooking(userId, bookingDto));
    }

    @PatchMapping("{bookingId}")
    public ResponseEntity<BookingResponse> updateBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                         @PathVariable("bookingId") Long bookingId,
                                                         @RequestParam Boolean approved) {
        return ResponseEntity.ok().body(bookingService.updateBooking(userId, bookingId, approved));
    }

    @GetMapping("{bookingId}")
    public ResponseEntity<BookingResponse> getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                          @PathVariable("bookingId") Long bookingId) {
        return ResponseEntity.ok().body(bookingService.getBookingById(userId, bookingId));
    }

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                                @RequestParam(defaultValue = "ALL") State state,
                                                                @RequestParam(defaultValue = "0") Integer from,
                                                                @RequestParam(required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.ok().body(bookingService.getAllBookings(userId, state, from, size));
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingResponse>> getAllBookingByItemOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                                          @RequestParam(defaultValue = "ALL") State state,
                                                                          @RequestParam(defaultValue = "0") Integer from,
                                                                          @RequestParam(required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.ok().body(bookingService.getAllBookingByItemOwner(userId, state, from, size));
    }
}