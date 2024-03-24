package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(Long bookerID, Pageable page);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long bookerID, LocalDateTime start,
                                                                             LocalDateTime before, Pageable page);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Long bookerID, LocalDateTime before, Pageable page);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Long bookerID, LocalDateTime before, Pageable page);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long bookerID, Status status, Pageable page);

    List<Booking> findAllByItemOwnerOrderByStartDesc(User itemOwner, Pageable page);

    List<Booking> findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(User itemOwner, LocalDateTime start,
                                                                              LocalDateTime before, Pageable page);

    List<Booking> findAllByItemOwnerAndEndBeforeOrderByStartDesc(User itemOwner, LocalDateTime before, Pageable page);

    List<Booking> findAllByItemOwnerAndStartAfterOrderByStartDesc(User itemOwner, LocalDateTime before, Pageable page);

    List<Booking> findAllByItemOwnerAndStatusOrderByStartDesc(User itemOwner, Status status, Pageable page);

    Booking findFirstByItemIdAndItemOwnerIdAndStartBeforeAndStatusOrderByStartDesc(Long itemId, Long bookerID,
                                                                                   LocalDateTime end, Status status);

    Booking findFirstByItemIdAndItemOwnerIdAndStartAfterAndStatusOrderByStartAsc(Long itemId, Long bookerID,
                                                                                 LocalDateTime start, Status status);

    Optional<Booking> findFirstByBookerIdAndItemIdAndEndBefore(Long itemId, Long bookerID, LocalDateTime end);
}