package limiter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class FixedWindowLimiter implements interfaces.RateLimiter {

    private Map<String, Integer> userRequestCounts = new ConcurrentHashMap<>();
    // The below should ideally be a ConcurrentHashMap as well, but for thsi case, we are using a HashMap here because it is used inside compute. 
    private Map<String, Long> userLastRequestTime = new HashMap<>();
    private final int maxRequests;
    private final long windowSizeInSeconds;

    // private final ConcurrentHashMap<String, Object> locks = new ConcurrentHashMap<>();

    public FixedWindowLimiter(int maxRequests, long windowSizeInSeconds) {
        this.maxRequests = maxRequests;
        this.windowSizeInSeconds = windowSizeInSeconds;
    }

    // private Object getLock(String userId) {
    //     return locks.computeIfAbsent(userId, id -> new Object());
    // }

    @Override
    public boolean isAllowed(String userId) {
        long currentTime = System.currentTimeMillis() / 1000; 

        AtomicBoolean allowed = new AtomicBoolean(false);

        userRequestCounts.compute(userId, (k, requestCount) -> {
            long lastRequestTime = userLastRequestTime.getOrDefault(userId, 0L);

            if (currentTime - lastRequestTime >= windowSizeInSeconds) {
                userLastRequestTime.put(userId, currentTime);
                allowed.set(true);
                return 1; 
            } else {
                if (requestCount == null) {
                    requestCount = 0;
                }
                if (requestCount < maxRequests) {
                    allowed.set(true);
                    return requestCount + 1; 
                } else {
                    allowed.set(false);
                    return requestCount; 
                }
            }
        });

        return allowed.get();
    }
    
}
