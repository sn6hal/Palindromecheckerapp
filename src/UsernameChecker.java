import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class UsernameChecker {
    private final ConcurrentHashMap<String, Long> usernameToUserId = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicLong> attemptFrequency = new ConcurrentHashMap<>();
    public UsernameChecker() {
        usernameToUserId.put("john_doe", 12345L);
        usernameToUserId.put("admin", 1L);
        usernameToUserId.put("jane_smith", 67890L);
    }
    public boolean checkAvailability(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        boolean available = !usernameToUserId.containsKey(username);
        attemptFrequency.computeIfAbsent(username, k -> new AtomicLong(0))
                      .incrementAndGet();
        return available;
    }
    public boolean registerUsername(String username, long userId) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        Long existingUserId = usernameToUserId.putIfAbsent(username, userId);
        return existingUserId == null; // true if successfully registered
    }
    public List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();
        
        if (username == null || username.trim().isEmpty()) {
            return suggestions;
        }
        for (int i = 1; i <= 3; i++) {
            String alt = username + i;
            if (checkAvailability(alt)) {
                suggestions.add(alt);
            }
        }
        String dotVersion = username.replace("_", ".");
        if (!dotVersion.equals(username) && checkAvailability(dotVersion)) {
            suggestions.add(dotVersion);
        }

        String theVersion = "the" + username;
        if (checkAvailability(theVersion)) {
            suggestions.add(theVersion);
        }
        String underscoreVersion = username.replace(".", "_");
        if (!underscoreVersion.equals(username) && checkAvailability(underscoreVersion)) {
            suggestions.add(underscoreVersion);
        }
        
        return suggestions;
    }
    public List<Map.Entry<String, Long>> getMostAttempted(int topK) {
        return attemptFrequency.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue().get()))
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(topK)
                .collect(Collectors.toList());
    }
    public static void main(String[] args) {
        UsernameChecker checker = new UsernameChecker();
        System.out.println("checkAvailability('john_doe'): " + 
                          checker.checkAvailability("john_doe")); // false
        System.out.println("checkAvailability('jane_smith'): " + 
                          checker.checkAvailability("jane_smith")); // false
        System.out.println("checkAvailability('new_user123'): " + 
                          checker.checkAvailability("new_user123")); // true
        System.out.println("Suggestions for 'john_doe': " + 
                          checker.suggestAlternatives("john_doe"));
        boolean registered = checker.registerUsername("new_user123", 99999L);
        System.out.println("Registered 'new_user123': " + registered);
        List<Map.Entry<String, Long>> top = checker.getMostAttempted(3);
        System.out.println("Top attempted: " + top);
    }
}
