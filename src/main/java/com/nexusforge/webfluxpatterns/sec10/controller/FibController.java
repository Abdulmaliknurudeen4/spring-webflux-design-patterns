package com.nexusforge.webfluxpatterns.sec10.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@RestController
@RequestMapping("sec10")
public class FibController {

    // number of calls at a time
    private final Scheduler schedulers = Schedulers.newParallel("fib", 4);

    @GetMapping("fib/{input}")
    //Use Rate limiter to limit the number of request that you'll handle in a window
    public Mono<ResponseEntity<Long>> doubleInput(@PathVariable Long input) {
        //CPU -- create a parallel for CPU intensive
        return Mono.fromSupplier(() -> findFib(input))
                .subscribeOn(schedulers)
                .map(ResponseEntity::ok)
                .timeout(Duration.ofSeconds(15));
        // combine more patterns to make application Robust
    }

    private Long findFib(Long input) {
        if (input < 2) {
            return input;
        }
        return findFib(input - 1) + findFib(input - 2);
    }

}
