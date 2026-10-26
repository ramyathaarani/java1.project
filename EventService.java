import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EventService {
    private final Map<Integer, Event> events = new LinkedHashMap<>();
    private int nextId = 1;

    public synchronized Event addEvent(String name, String venue, LocalDateTime dateTime,
                                       int totalSeats, double price) {
        if (totalSeats <= 0 || price < 0) {
            throw new IllegalArgumentException("Seats must be positive and price cannot be negative.");
        }
        Event event = new Event(nextId++, name, venue, dateTime, totalSeats, price);
        events.put(event.getId(), event);
        return event;
    }

    public Event getEvent(int id) throws ResourceNotFoundException {
        Event event = events.get(id);
        if (event == null) {
            throw new ResourceNotFoundException("Event with id " + id + " not found.");
        }
        return event;
    }

    public List<Event> getUpcomingEvents() {
        List<Event> result = new ArrayList<>();
        for (Event e : events.values()) {
            if (e.isUpcoming()) {
                result.add(e);
            }
        }
        return result;
    }
}
