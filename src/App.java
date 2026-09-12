import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import enums.Tier;
import model.User;
import service.RateLimiterService;

public class App {

    static void checkconcurrent() {
        // checking concurrent requests for users with multiple threads
        User user1 = new User("user1", Tier.FREE);
        // User user2 = new User("user2", Tier.PAID);

        RateLimiterService rateLimiterService = new RateLimiterService();
        int threads = 25;

        ExecutorService executorService = java.util.concurrent.Executors.newFixedThreadPool(threads);

        for (int i = 0; i < threads; i++) {
            final int requestNumber = i + 1;
            executorService.submit(() -> {
                boolean allowedUser1 = rateLimiterService.isAllowed(user1);
                System.out.println("User1 Request " + requestNumber + ": " + (allowedUser1 ? "Allowed" : "Blocked"));

                // boolean allowedUser2 = rateLimiterService.isAllowed(user2);
                // System.out.println("User2 Request " + requestNumber + ": " + (allowedUser2 ? "Allowed" : "Blocked"));
            });
        }

        executorService.shutdown();
    }

    public static void main(String[] args) throws Exception {
        // User user1 = new User("user1", Tier.FREE);
        // User user2 = new User("user2", Tier.PAID);

        // RateLimiterService rateLimiterService = new RateLimiterService();

        // System.out.println("User1 requests (Free Tier):");

        // for (int i = 0; i < 12; i++) {
        //     boolean allowed = rateLimiterService.isAllowed(user1);
        //     System.out.println("Request " + (i + 1) + ": " + (allowed ? "Allowed" : "Blocked"));
        // }

        // System.out.println("\nUser2 requests (Paid Tier):");
        // for (int i = 0; i < 22; i++) {
        //     boolean allowed = rateLimiterService.isAllowed(user2);
        //     System.out.println("Request " + (i + 1) + ": " + (allowed ? "Allowed" : "Blocked"));
        // }

        checkconcurrent();
    }
}
