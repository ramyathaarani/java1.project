import java.util.HashMap;
import java.util.Map;

public class UserService {
    private final Map<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    public synchronized User registerUser(String name, String email) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (email == null || !email.matches("^[\\w.+-]+@[\\w-]+\\.[\\w.-]+$")) {
            throw new IllegalArgumentException("Invalid email address.");
        }
        for (User u : users.values()) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                throw new IllegalArgumentException("Email already registered.");
            }
        }
        User user = new User(nextId++, name.trim(), email.trim());
        users.put(user.getId(), user);
        return user;
    }

    public User getUser(int id) throws ResourceNotFoundException {
        User user = users.get(id);
        if (user == null) {
            throw new ResourceNotFoundException("User with id " + id + " not found.");
        }
        return user;
    }
}
