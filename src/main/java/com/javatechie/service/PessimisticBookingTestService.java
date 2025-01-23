package com.javatechie.service;

import org.springframework.stereotype.Service;

@Service
public class PessimisticBookingTestService {

    private final MovieTicketBookingService bookingService;

    public PessimisticBookingTestService(MovieTicketBookingService bookingService) {
        this.bookingService = bookingService;
    }

    public void testPessimisticLocking(Long seatId) {
        Thread thread1 = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " is attempting to book the seat pessimistically...");
                bookingService.bookSeatPessimistically(seatId);
                System.out.println(Thread.currentThread().getName() + " successfully booked the seat!");
            } catch (RuntimeException e) {
                System.out.println(Thread.currentThread().getName() + " failed: " + e.getMessage());
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " is attempting to book the seat pessimistically...");
                bookingService.bookSeatPessimistically(seatId);
                System.out.println(Thread.currentThread().getName() + " successfully booked the seat!");
            } catch (RuntimeException e) {
                System.out.println(Thread.currentThread().getName() + " failed: " + e.getMessage());
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            System.out.println("Test interrupted: " + e.getMessage());
        }
    }
}
