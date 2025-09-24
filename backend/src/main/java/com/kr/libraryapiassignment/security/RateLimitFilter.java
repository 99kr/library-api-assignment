package com.kr.libraryapiassignment.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

// https://medium.com/@AlexanderObregon/rate-throttling-per-user-in-spring-boot-endpoints-385c71e100b7#54e2

@Component
public class RateLimitFilter extends OncePerRequestFilter {
    // REQUEST_LIMIT requests within TIME_FRAME_MS ms
    private static final int REQUEST_LIMIT = 10;
    private static final long TIME_FRAME_MS = TimeUnit.SECONDS.toMillis(1);

    private final Logger logger = LoggerFactory.getLogger(RateLimitFilter.class);

    private final Map<String, Deque<Long>> requests = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {

        String ip = request.getRemoteAddr();

        long now = System.currentTimeMillis();
        Deque<Long> times = requests.computeIfAbsent(ip, k -> new ArrayDeque<>());

        synchronized (times) {
            while (!times.isEmpty() && now - times.peekFirst() > TIME_FRAME_MS) {
                times.pollFirst();
            }

            if (times.size() >= REQUEST_LIMIT) {
                logger.info("IP Address {} sent too many requests", ip);

                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                return;
            }
            times.addLast(now);
        }

        chain.doFilter(request, response);
    }
}
