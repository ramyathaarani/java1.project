import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        UserService userService = new UserService();
        EventService eventService = new EventService();
        BookingService bookingService = new BookingService(userService, eventService);

        seedEvents(eventService);

        System.out.println("=== Online Event Ticket Booking & Reservation System ===");
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");
            try {
                switch (choice) {
                    case 1 -> registerUser(userService);
                    case 2 -> listEvents(eventService);
                    case 3 -> bookTickets(bookingService);
                    case 4 -> cancelBooking(bookingService);
                    case 5 -> viewMyBookings(bookingService);
                    case 0 -> {
                        running = false;
                        System.out.println("Thank you for using the system. Goodbye!");
                    }
                    default -> System.out.println("Invalid option. Try again.");
                }
            } catch (ResourceNotFoundException | SeatsUnavailableException
                     | IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n1. Register user");
        System.out.println("2. View upcoming events");
        System.out.println("3. Book tickets");
        System.out.println("4. Cancel booking");
        System.out.println("5. View my bookings");
        System.out.println("0. Exit");
    }

    private static void registerUser(UserService userService) {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        User user = userService.registerUser(name, email);
        System.out.println("Registered successfully. Your User ID is " + user.getId());
    }

    private static void listEvents(EventService eventService) {
        List<Event> events = eventService.getUpcomingEvents();
        if (events.isEmpty()) {
            System.out.println("No upcoming events.");
            return;
        }
        events.forEach(System.out::println);
    }

    private static void bookTickets(BookingService bookingService)
            throws ResourceNotFoundException, SeatsUnavailableException {
        int userId = readInt("Your User ID: ");
        int eventId = readInt("Event ID: ");
        int seats = readInt("Number of tickets: ");
        Booking booking = bookingService.createBooking(userId, eventId, seats);
        System.out.println("Booking confirmed!");
        System.out.println(booking);
    }

    private static void cancelBooking(BookingService bookingService)
            throws ResourceNotFoundException {
        int userId = readInt("Your User ID: ");
        int bookingId = readInt("Booking ID: ");
        Booking booking = bookingService.cancelBooking(bookingId, userId);
        System.out.printf("Booking #%d cancelled. Refund amount: Rs.%.2f%n",
                booking.getBookingId(), booking.getTotalAmount());
    }

    private static void viewMyBookings(BookingService bookingService) {
        int userId = readInt("Your User ID: ");
        List<Booking> list = bookingService.getBookingsByUser(userId);
        if (list.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }
        list.forEach(System.out::println);
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static void seedEvents(EventService eventService) {
        LocalDateTime now = LocalDateTime.now();
        eventService.addEvent("Rock Night Live", "Nehru Stadium", now.plusDays(10), 100, 1500.00);
        eventService.addEvent("Tech Conference 2026", "Convention Centre", now.plusDays(20), 250, 999.00);
        eventService.addEvent("Stand-up Comedy Show", "City Auditorium", now.plusDays(5), 60, 600.00);
        eventService.addEvent("Classical Music Evening", "Town Hall", now.plusDays(30), 80, 450.00);
    }
}
