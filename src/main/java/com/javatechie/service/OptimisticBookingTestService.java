package com.javatechie.service;

import com.javatechie.entity.Seat;
import org.springframework.stereotype.Service;

@Service
public class OptimisticBookingTestService {

    private final MovieTicketBookingService bookingService;

    public OptimisticBookingTestService(MovieTicketBookingService bookingService) {
        this.bookingService = bookingService;
    }

    public void testOptimisticLocking(Long seatId) throws InterruptedException {

        Thread thread1 = new Thread(() -> {

            try {
                System.out.println(Thread.currentThread().getName() + " is attempting to book the seat optimistically...");
                Seat seat=bookingService.bookSeatOptimistically(seatId);
                System.out.println(Thread.currentThread().getName() + " successfully booked the seat ! and version is "+seat.getVersion());
            } catch (RuntimeException e) {
                System.out.println(Thread.currentThread().getName() + " failed: " + e.getMessage());
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " is attempting to book the seat optimistically...");
                Seat seat=bookingService.bookSeatOptimistically(seatId);
                System.out.println(Thread.currentThread().getName() + " successfully booked the seat ! and version is "+seat.getVersion());
            } catch (RuntimeException e) {
                System.out.println(Thread.currentThread().getName() + " failed: " + e.getMessage());
            }
        });

        thread1.start();
        // 2-second pause before starting the second thread
        Thread.sleep(2000);
        thread2.start();

        thread1.join();
        thread2.join();

    }
}
