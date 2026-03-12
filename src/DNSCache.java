import java.util.*;
import java.util.concurrent.*;
public class DNSCache {
    private final int MAX_CAPACITY = 1000;
    private long hits = 0;
    private long misses = 0;
    static class DNSEntry {
        String ip;
        long expiryTime;

        DNSEntry(String ip, int ttlSeconds) {
            this.ip = ip;
            this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000L);
        }
        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }
    private final Map<String, DNSEntry> cache = Collections.synchronizedMap(
            new LinkedHashMap<String, DNSEntry>(MAX_CAPACITY, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                    return size() > MAX_CAPACITY;
                }
            }
    );
    public String resolve(String domain) {
        DNSEntry entry = cache.get(domain);
        if (entry != null && !entry.isExpired()) {
            hits++;
            return "Cache HIT -> " + entry.ip;
        }
        if (entry != null && entry.isExpired()) {
            cache.remove(domain);
        }
        misses++;
        String ipFromUpstream = queryUpstream(domain);
        cache.put(domain, new DNSEntry(ipFromUpstream, 5)); // 5 second TTL for demo
        return "Cache MISS/EXPIRED -> Query upstream -> " + ipFromUpstream;
    }
    private String queryUpstream(String domain) {
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        return "172.217.14." + (new Random().nextInt(255));
    }
    public void getCacheStats() {
        double total = hits + misses;
        double hitRate = (total == 0) ? 0 : (hits / total) * 100;
        System.out.println("Stats -> Hits: " + hits + ", Misses: " + misses + ", Hit Rate: " + hitRate + "%");
    }
    public static void main(String[] args) throws InterruptedException {
        DNSCache dns = new DNSCache();
        System.out.println(dns.resolve("google.com"));
        System.out.println(dns.resolve("google.com"));
        System.out.println("Waiting for TTL to expire...");
        Thread.sleep(6000);
        System.out.println(dns.resolve("google.com"));
        dns.getCacheStats();
    }
}