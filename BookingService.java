import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingService {
    private final UserService userService;
    private final EventService eventService;
    private final Map<Integer, Booking> bookings = new HashMap<>();
    private int nextBookingId = 1000;

    public BookingService(UserService userService, EventService eventService) {
        this.userService = userService;
        this.eventService = eventService;
    }

    public synchronized Booking createBooking(int userId, int eventId, int seats)
            throws ResourceNotFoundException, SeatsUnavailableException {
        User user = userService.getUser(userId);
        Event event = eventService.getEvent(eventId);

        if (!event.isUpcoming()) {
            throw new IllegalStateException("This event has already taken place.");
        }

        event.reserveSeats(seats); // throws if not enough seats

        Booking booking = new Booking(nextBookingId++, user, event, seats);
        bookings.put(booking.getBookingId(), booking);
        return booking;
    }

    public synchronized Booking cancelBooking(int bookingId, int userId)
            throws ResourceNotFoundException {
        Booking booking = bookings.get(bookingId);
        if (booking == null || booking.getUser().getId() != userId) {
            throw new ResourceNotFoundException("Booking #" + bookingId + " not found for this user.");
        }
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Booking is already cancelled.");
        }
        if (!booking.getEvent().isUpcoming()) {
            throw new IllegalStateException("Cannot cancel a booking for a past event.");
        }
        booking.cancel();
        booking.getEvent().releaseSeats(booking.getSeats());
        return booking;
    }

    public List<Booking> getBookingsByUser(int userId) {
        List<Booking> result = new ArrayList<>();
        for (Booking b : bookings.values()) {
            if (b.getUser().getId() == userId) {
                result.add(b);
            }
        }
        return result;
    }
}
