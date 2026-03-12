import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder; // Required for LongAdder
import java.util.stream.Collectors;
public class AnalyticsDashboard {
    private final ConcurrentHashMap<String, LongAdder> pageViews = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Set<String>> uniqueUsers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LongAdder> trafficSources = new ConcurrentHashMap<>();
    public void processEvent(String url, String userId, String source) {
        pageViews.computeIfAbsent(url, k -> new LongAdder()).increment();
        trafficSources.computeIfAbsent(source, k -> new LongAdder()).increment();
        uniqueUsers.computeIfAbsent(url, k -> ConcurrentHashMap.newKeySet()).add(userId);
    }
    public void getDashboard() {
        System.out.println("\n--- Real-Time Dashboard ---");
        System.out.println("Top Pages:");
        PriorityQueue<Map.Entry<String, LongAdder>> topPages = new PriorityQueue<>(
                Comparator.comparingLong(e -> e.getValue().sum())
        );
        for (Map.Entry<String, LongAdder> entry : pageViews.entrySet()) {
            topPages.offer(entry);
            if (topPages.size() > 10) topPages.poll();
        }
        List<Map.Entry<String, LongAdder>> sortedTop = new ArrayList<>(topPages);
        sortedTop.sort((e1, e2) -> Long.compare(e2.getValue().sum(), e1.getValue().sum()));
        for (int i = 0; i < sortedTop.size(); i++) {
            String url = sortedTop.get(i).getKey();
            long views = sortedTop.get(i).getValue().sum();
            int uniques = uniqueUsers.getOrDefault(url, Collections.emptySet()).size();
            System.out.printf("%d. %s - %d views (%d unique)\n", i + 1, url, views, uniques);
        }
        System.out.println("\nTraffic Sources:");
        long totalViews = trafficSources.values().stream().mapToLong(LongAdder::sum).sum();
        if (totalViews > 0) {
            trafficSources.forEach((source, count) -> {
                double percentage = (count.sum() * 100.0) / totalViews;
                System.out.printf("%s: %.1f%%\n", source, percentage);
            });
        }
    }
    public static void main(String[] args) {
        AnalyticsDashboard dashboard = new AnalyticsDashboard();
        dashboard.processEvent("/news/tech", "user1", "Google");
        dashboard.processEvent("/news/tech", "user2", "Google");
        dashboard.processEvent("/home", "user3", "Direct");
        dashboard.getDashboard();
    }
}