package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findAllByBookerIdOrderByStartDesc(Long bookerID, Pageable page);

    Page<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long bookerID, LocalDateTime start,
                                                                             LocalDateTime before, Pageable page);

    Page<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Long bookerID, LocalDateTime before, Pageable page);

    Page<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Long bookerID, LocalDateTime before, Pageable page);

    Page<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long bookerID, Status status, Pageable page);

    Page<Booking> findAllByItemOwnerOrderByStartDesc(User itemOwner, Pageable page);

    Page<Booking> findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(User itemOwner, LocalDateTime start,
                                                                              LocalDateTime before, Pageable page);

    Page<Booking> findAllByItemOwnerAndEndBeforeOrderByStartDesc(User itemOwner, LocalDateTime before, Pageable page);

    Page<Booking> findAllByItemOwnerAndStartAfterOrderByStartDesc(User itemOwner, LocalDateTime before, Pageable page);

    Page<Booking> findAllByItemOwnerAndStatusOrderByStartDesc(User itemOwner, Status status, Pageable page);

    Booking findFirstByItemIdAndItemOwnerIdAndStartBeforeAndStatusOrderByStartDesc(Long itemId, Long bookerID,
                                                                                   LocalDateTime end, Status status);

    Booking findFirstByItemIdAndItemOwnerIdAndStartAfterAndStatusOrderByStartAsc(Long itemId, Long bookerID,
                                                                                 LocalDateTime start, Status status);

    Optional<Booking> findFirstByBookerIdAndItemIdAndEndBefore(Long itemId, Long bookerID, LocalDateTime end);
}