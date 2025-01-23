package com.javatechie.controller;

import com.javatechie.service.OptimisticBookingTestService;
import com.javatechie.service.PessimisticBookingTestService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/booking")
public class BookingTestController {

    private final OptimisticBookingTestService optimisticService;
    private final PessimisticBookingTestService pessimisticService;

    public BookingTestController(OptimisticBookingTestService optimisticService,
                                 PessimisticBookingTestService pessimisticService) {
        this.optimisticService = optimisticService;
        this.pessimisticService = pessimisticService;
    }

    @GetMapping("/optimistic/{seatId}")
    public String testOptimistic(@PathVariable Long seatId) throws InterruptedException {
        optimisticService.testOptimisticLocking(seatId);
        return "Optimistic locking test started! Check logs for results.";
    }

    @GetMapping("/pessimistic/{seatId}")
    public String testPessimistic(@PathVariable Long seatId) throws InterruptedException {
        pessimisticService.testPessimisticLocking(seatId);
        return "Pessimistic locking test started! Check logs for results.";
    }
}
