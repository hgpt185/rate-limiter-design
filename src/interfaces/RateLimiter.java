package interfaces;

public interface RateLimiter {
    boolean isAllowed(String userId);
}
