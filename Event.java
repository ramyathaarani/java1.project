import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event {
    private final int id;
    private final String name;
    private final String venue;
    private final LocalDateTime dateTime;
    private final int totalSeats;
    private int availableSeats;
    private final double pricePerTicket;

    public Event(int id, String name, String venue, LocalDateTime dateTime,
                 int totalSeats, double pricePerTicket) {
        this.id = id;
        this.name = name;
        this.venue = venue;
        this.dateTime = dateTime;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.pricePerTicket = pricePerTicket;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getVenue() { return venue; }
    public LocalDateTime getDateTime() { return dateTime; }
    public int getTotalSeats() { return totalSeats; }
    public synchronized int getAvailableSeats() { return availableSeats; }
    public double getPricePerTicket() { return pricePerTicket; }

    public boolean isUpcoming() {
        return dateTime.isAfter(LocalDateTime.now());
    }

    // synchronized so two users cannot book the last seats at the same time
    public synchronized void reserveSeats(int count) throws SeatsUnavailableException {
        if (count <= 0) {
            throw new IllegalArgumentException("Number of seats must be positive.");
        }
        if (count > availableSeats) {
            throw new SeatsUnavailableException(
                "Only " + availableSeats + " seat(s) left for '" + name + "'.");
        }
        availableSeats -= count;
    }

    public synchronized void releaseSeats(int count) {
        availableSeats = Math.min(totalSeats, availableSeats + count);
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
        return String.format("[%d] %-25s | %-20s | %s | Rs.%.2f | Seats left: %d/%d",
                id, name, venue, dateTime.format(fmt), pricePerTicket,
                getAvailableSeats(), totalSeats);
    }
}
