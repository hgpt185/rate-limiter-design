package limiter;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayDeque;

public class SlidingWindowLimiter implements interfaces.RateLimiter {
    private Map<String, Queue<Long>> userRequestTimestamps = new ConcurrentHashMap<>();
    private final int maxRequests;
    private final long windowSizeInSeconds;

    public SlidingWindowLimiter(int maxRequests, long windowSizeInSeconds) {
        this.maxRequests = maxRequests;
        this.windowSizeInSeconds = windowSizeInSeconds;
    }

    @Override
    public boolean isAllowed(String userId) {
        long currentTime = System.currentTimeMillis() / 1000;

        AtomicBoolean allowed = new AtomicBoolean(false);

        userRequestTimestamps.compute(userId, (k, timestamps) -> {
            if (timestamps == null) {
                timestamps = new ArrayDeque<>();
            }

            while(timestamps.size() > 0 && currentTime - timestamps.peek() >= windowSizeInSeconds) {
                timestamps.poll();
            }

            if (timestamps.size() < maxRequests) {
                timestamps.add(currentTime);
                allowed.set(true);
                return timestamps;
            } else {
                allowed.set(false);
                return timestamps;
            }
        });

        return allowed.get();
        
    }
}
