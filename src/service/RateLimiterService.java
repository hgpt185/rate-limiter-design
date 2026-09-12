package service;

import interfaces.RateLimiter;
import limiter.FixedWindowLimiter;
import limiter.TokenBucketLimiter;
import model.User;
import enums.Tier;

import java.util.HashMap;
import java.util.Map;

public class RateLimiterService {

    private Map<Tier, RateLimiter> rateLimiters = new HashMap<>();

    public RateLimiterService() {
        // Initialize rate limiters for different tiers
        rateLimiters.put(Tier.FREE, new FixedWindowLimiter(10, 60)); // 10 requests per minute for FREE tier
        rateLimiters.put(Tier.PAID, new TokenBucketLimiter(20, 60)); // 100 requests per minute for PAID tier
    }
    
    public boolean isAllowed(User user) {
        RateLimiter rateLimiter = rateLimiters.get(user.getTier());

        if(rateLimiter == null) {
            throw new IllegalArgumentException("No rate limiter found for tier: " + user.getTier());
        }

        return rateLimiter.isAllowed(user.getId());
    }
}
