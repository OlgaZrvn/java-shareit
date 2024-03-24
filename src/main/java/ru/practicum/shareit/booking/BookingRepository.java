package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(Long bookerID);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long bookerID, LocalDateTime start, LocalDateTime before);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Long bookerID, LocalDateTime before);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Long bookerID, LocalDateTime before);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long bookerID, Status status);

    List<Booking> findAllByItemOwnerOrderByStartDesc(User itemOwner);

    List<Booking> findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(User itemOwner, LocalDateTime start, LocalDateTime before);

    List<Booking> findAllByItemOwnerAndEndBeforeOrderByStartDesc(User itemOwner, LocalDateTime before);

    List<Booking> findAllByItemOwnerAndStartAfterOrderByStartDesc(User itemOwner, LocalDateTime before);

    List<Booking> findAllByItemOwnerAndStatusOrderByStartDesc(User itemOwner, Status status);

    Booking findFirstByItemIdAndItemOwnerIdAndStartBeforeAndStatusOrderByStartDesc(Long itemId, Long bookerID, LocalDateTime end, Status status);

    Booking findFirstByItemIdAndItemOwnerIdAndStartAfterAndStatusOrderByStartAsc(Long itemId, Long bookerID, LocalDateTime start, Status status);

    Optional<Booking> findFirstByBookerIdAndItemIdAndEndBefore(Long itemId, Long bookerID, LocalDateTime end);
}