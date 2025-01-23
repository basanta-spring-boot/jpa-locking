package com.javatechie.service;

import com.javatechie.entity.Seat;
import com.javatechie.repository.SeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovieTicketBookingService {

    private final SeatRepository seatRepository;

    public MovieTicketBookingService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    @Transactional
    public Seat bookSeatOptimistically(Long seatId) {

        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        System.out.println(Thread.currentThread().getName() +
                " fetched seat with version : " + seat.getVersion());

        if (seat.isBooked()) {
            throw new RuntimeException("Seat already booked");
        }

        seat.setBooked(true); // Mark as booked
        return seatRepository.save(seat); // Version check occurs here
    }

    @Transactional
    public void bookSeatPessimistically(Long seatId) {

        System.out.println(Thread.currentThread().getName() + " is attempting to fetch the seat with a pessimistic lock...");

        // Fetch the seat with a pessimistic lock
        Seat seat = seatRepository.findByIdAndLock(seatId);
        System.out.println(Thread.currentThread().getName() + " acquired the lock for seat ID: " + seatId);

        // Check if the seat is already booked
        if (seat.isBooked()) {
            System.out.println(Thread.currentThread().getName() + " failed: Seat ID " + seatId + " is already booked!");
            throw new RuntimeException("Seat already booked");
        }

        // Mark the seat as booked
        System.out.println(Thread.currentThread().getName() + " is booking the seat...");
        seat.setBooked(true);

        // Save the updated seat status
        seatRepository.save(seat); // Lock released after transaction commit
        System.out.println(Thread.currentThread().getName() + " successfully booked the seat with ID: " + seatId);
    }
}
