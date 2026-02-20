package com.kivora.JsWater.infrastructure.security.ratelimiting;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginRateLimiter {

    private static class AttemptInfo {
        int failures;
        Instant firstFailureAt;
    }

    private final Map<String, AttemptInfo> attempts = new ConcurrentHashMap<>();

    private final int maxFailures = 5;
    private final long windowSeconds = 15 * 60; // 15 minutes

    public boolean isBlocked(String key) {
        AttemptInfo info = attempts.get(key);
        if (info == null) {
            return false;
        }
        Instant now = Instant.now();
        if (now.getEpochSecond() - info.firstFailureAt.getEpochSecond() > windowSeconds) {
            attempts.remove(key);
            return false;
        }
        return info.failures >= maxFailures;
    }

    public void registerFailure(String key) {
        Instant now = Instant.now();
        attempts.compute(key, (k, existing) -> {
            if (existing == null) {
                AttemptInfo info = new AttemptInfo();
                info.failures = 1;
                info.firstFailureAt = now;
                return info;
            }
            if (now.getEpochSecond() - existing.firstFailureAt.getEpochSecond() > windowSeconds) {
                existing.failures = 1;
                existing.firstFailureAt = now;
            } else {
                existing.failures++;
            }
            return existing;
        });
    }

    public void reset(String key) {
        attempts.remove(key);
    }
}
