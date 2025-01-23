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
        Seat seat = seatRepository.findByIdAndLock(seatId);

        if (seat.isBooked()) {
            throw new RuntimeException("Seat already booked");
        }

        seat.setBooked(true); // Mark as booked
        seatRepository.save(seat); // Lock released after transaction
    }
}
