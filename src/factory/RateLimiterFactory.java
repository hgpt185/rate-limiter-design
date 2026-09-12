package factory;

import enums.RateLimiterType;
import interfaces.RateLimiter;
import limiter.FixedWindowLimiter;
import limiter.SlidingWindowLimiter;
import limiter.TokenBucketLimiter;

public class RateLimiterFactory {
    public static RateLimiter getRateLimiter(RateLimiterType type, int limit, int windowSize) {
        switch (type) {
            case FIXED_WINDOW:
                return new FixedWindowLimiter(windowSize, limit);
            case SLIDING_WINDOW:
                return new SlidingWindowLimiter(windowSize, limit);
            case TOKEN_BUCKET:
                return new TokenBucketLimiter(limit, windowSize);
            default:
                throw new IllegalArgumentException("Invalid Rate Limiter Type");
        }
        
    }
}
