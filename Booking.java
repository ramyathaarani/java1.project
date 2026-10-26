import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Booking {
    private final int bookingId;
    private final User user;
    private final Event event;
    private final int seats;
    private final double totalAmount;
    private final LocalDateTime bookedAt;
    private BookingStatus status;

    public Booking(int bookingId, User user, Event event, int seats) {
        this.bookingId = bookingId;
        this.user = user;
        this.event = event;
        this.seats = seats;
        this.totalAmount = seats * event.getPricePerTicket();
        this.bookedAt = LocalDateTime.now();
        this.status = BookingStatus.CONFIRMED;
    }

    public int getBookingId() { return bookingId; }
    public User getUser() { return user; }
    public Event getEvent() { return event; }
    public int getSeats() { return seats; }
    public double getTotalAmount() { return totalAmount; }
    public LocalDateTime getBookedAt() { return bookedAt; }
    public BookingStatus getStatus() { return status; }

    public void cancel() {
        this.status = BookingStatus.CANCELLED;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
        return String.format("Booking #%d | %s | %d ticket(s) | Rs.%.2f | %s | booked %s",
                bookingId, event.getName(), seats, totalAmount, status, bookedAt.format(fmt));
    }
}
