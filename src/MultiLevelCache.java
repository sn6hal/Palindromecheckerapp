import java.util.*;
import java.util.concurrent.*;

public class MultiLevelCache {
    private final int L1_SIZE = 10000;
    private final int L2_SIZE = 100000;
    private final int PROMOTION_THRESHOLD = 3;

    private long l1Hits = 0, l2Hits = 0, l3Hits = 0;

    // L1: In-Memory (LinkedHashMap for LRU)
    private final Map<String, String> l1Cache = Collections.synchronizedMap(
            new LinkedHashMap<String, String>(L1_SIZE, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                    return size() > L1_SIZE;
                }
            }
    );

    // L2: Simulated SSD-backed (using a simple Map for logic)
    private final Map<String, String> l2Cache = new ConcurrentHashMap<>();
    private final Map<String, Integer> accessCounts = new ConcurrentHashMap<>();
    public String getVideo(String videoId) {
        if (l1Cache.containsKey(videoId)) {
            l1Hits++;
            return "L1 HIT: " + l1Cache.get(videoId);
        }
        if (l2Cache.containsKey(videoId)) {
            l2Hits++;
            String data = l2Cache.get(videoId);
            trackAccessAndPromote(videoId, data);
            return "L2 HIT: " + data;
        }
        l3Hits++;
        String dataFromDb = "VideoContent_" + videoId;
        simulateDbLatency();
        l2Cache.put(videoId, dataFromDb);
        return "L3 HIT (DB): " + dataFromDb;
    }
    private void trackAccessAndPromote(String videoId, String data) {
        int count = accessCounts.merge(videoId, 1, Integer::sum);
        if (count >= PROMOTION_THRESHOLD) {
            l1Cache.put(videoId, data); // Promote to L1
            accessCounts.remove(videoId);
        }
    }
    private void simulateDbLatency() {
        try { Thread.sleep(150); } catch (InterruptedException e) {}
    }
    public void getStatistics() {
        long total = l1Hits + l2Hits + l3Hits;
        System.out.println("--- Cache Stats ---");
        System.out.printf("L1 Hit Rate: %.2f%%\n", (l1Hits * 100.0 / total));
        System.out.printf("L2 Hit Rate: %.2f%%\n", (l2Hits * 100.0 / total));
        System.out.printf("L3 Hit Rate: %.2f%%\n", (l3Hits * 100.0 / total));
    }
    public static void main(String[] args) {
        MultiLevelCache netflix = new MultiLevelCache();
        System.out.println(netflix.getVideo("vid1")); // L3 Hit
        System.out.println(netflix.getVideo("vid1")); // L2 Hit
        System.out.println(netflix.getVideo("vid1")); // L2 Hit -> Promotes to L1
        System.out.println(netflix.getVideo("vid1")); // L1 Hit

        netflix.getStatistics();
    }
}