package com.javatechie.service;

import org.springframework.stereotype.Service;

@Service
public class PessimisticBookingTestService {

    private final MovieTicketBookingService bookingService;

    public PessimisticBookingTestService(MovieTicketBookingService bookingService) {
        this.bookingService = bookingService;
    }

    public void testPessimisticLocking(Long seatId) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            try {
                bookingService.bookSeatPessimistically(seatId);
            } catch (RuntimeException e) {
                System.out.println(Thread.currentThread().getName() + " failed: " + e.getMessage());
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                bookingService.bookSeatPessimistically(seatId);
            } catch (RuntimeException e) {
                System.out.println(Thread.currentThread().getName() + " failed: " + e.getMessage());
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }

}
