package limiter;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class TokenBucketLimiter implements interfaces.RateLimiter {
    private Map<String, Integer> userTokens = new ConcurrentHashMap<>();
    private Map<String, Long> userLastRefillTime = new HashMap<>();
    private final int maxTokens;
    private final long refillRateInSeconds;

    public TokenBucketLimiter(int maxTokens, long refillRateInSeconds) {
        this.maxTokens = maxTokens;
        this.refillRateInSeconds = refillRateInSeconds;
    }

    @Override
    public boolean isAllowed(String userId) {
        Long currentTime = System.currentTimeMillis() / 1000; 

        AtomicBoolean allowed = new AtomicBoolean(false);

        userTokens.compute(userId, (k, tokens) -> {
            if (tokens == null) {
                tokens = maxTokens;
            }
            Long lastRefillTime = userLastRefillTime.getOrDefault(userId, 0L);

            long timeSinceLastRefill = currentTime - lastRefillTime;
            int tokensToAdd = (int) (timeSinceLastRefill / refillRateInSeconds);

            tokens = Math.min(maxTokens, tokens + tokensToAdd);
            if (tokensToAdd > 0) {
                userLastRefillTime.put(
                    userId,
                    lastRefillTime + tokensToAdd * refillRateInSeconds
                );
            }

            if(tokens > 0) {
                allowed.set(true);
                return tokens - 1;
            } else {
                allowed.set(false);
                return tokens; 
            }
        });

        return allowed.get();
    }

}
