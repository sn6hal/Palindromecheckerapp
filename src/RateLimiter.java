import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
public class RateLimiter {
    private final ConcurrentHashMap<String, TokenBucket> clientBuckets = new ConcurrentHashMap<>();
    private final long MAX_TOKENS = 1000;
    private final long REFILL_INTERVAL_MS = 3600000; // 1 hour in milliseconds
    static class TokenBucket {
        long tokens;
        long lastRefillTime;

        TokenBucket(long maxTokens) {
            this.tokens = maxTokens;
            this.lastRefillTime = System.currentTimeMillis();
        }
    }
    public synchronized String checkRateLimit(String clientId) {
        TokenBucket bucket = clientBuckets.computeIfAbsent(clientId, k -> new TokenBucket(MAX_TOKENS));
        refill(bucket);
        if (bucket.tokens > 0) {
            bucket.tokens--;
            return "Allowed (" + bucket.tokens + " requests remaining)";
        } else {
            long waitTime = (bucket.lastRefillTime + REFILL_INTERVAL_MS - System.currentTimeMillis()) / 1000;
            return "Denied (0 requests remaining, retry after " + waitTime + "s)";
        }
    }
    private void refill(TokenBucket bucket) {
        long now = System.currentTimeMillis();
        if (now > bucket.lastRefillTime + REFILL_INTERVAL_MS) {
            bucket.tokens = MAX_TOKENS;
            bucket.lastRefillTime = now;
        }
    }
    public static void main(String[] args) {
        RateLimiter limiter = new RateLimiter();
        String client = "abc123";
        System.out.println(limiter.checkRateLimit(client));
        System.out.println(limiter.checkRateLimit(client));
    }
}