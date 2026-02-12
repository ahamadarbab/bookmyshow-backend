package com.bms.bookmyshow_backend.services;

import com.bms.bookmyshow_backend.dto.RegisterSeatBookingDto;
import com.bms.bookmyshow_backend.dto.RegisterShowDto;
import com.bms.bookmyshow_backend.dto.RegisterShowPriceMappingDto;
import com.bms.bookmyshow_backend.dto.SeatStatusDto;
import com.bms.bookmyshow_backend.exception.UnAuthorizedException;
import com.bms.bookmyshow_backend.exception.UserNotFoundException;
import com.bms.bookmyshow_backend.models.*;
import com.bms.bookmyshow_backend.repositories.BillRepository;
import com.bms.bookmyshow_backend.repositories.ShowPriceMappingRepository;
import com.bms.bookmyshow_backend.repositories.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class ShowService {

    ShowRepository showRepository;
    MovieService movieService;
    HallService hallService;
    UserService userService;
    ShowPriceMappingRepository showPriceMappingRepository;
    SeatBookingService seatBookingService;
    BillRepository billRepository;
    // 1/1/2015 00:00:00
    LocalDateTime worldStartTime = LocalDateTime.of(2015, 1, 1, 0, 0, 0);

    @Autowired
    public ShowService(ShowRepository showRepository, MovieService movieService, HallService hallService, UserService userService, ShowPriceMappingRepository showPriceMappingRepository, SeatBookingService seatBookingService, BillRepository billRepository) {
        this.showRepository = showRepository;
        this.movieService = movieService;
        this.hallService = hallService;
        this.userService = userService;
        this.showPriceMappingRepository = showPriceMappingRepository;
        this.seatBookingService = seatBookingService;
        this.billRepository = billRepository;
    }

    public Show createShow(RegisterShowDto registerShowDto, UUID movieId, UUID hallId, UUID userId) {
        // Validate all the ids are correct or not
        Movie movie = movieService.isMovieIdValid(movieId);
        Hall hall = hallService.isHallIdValid(hallId);
        User user = userService.isUserIdExists(userId);

        if(user == null || movie == null || hall == null) {
            throw new IllegalArgumentException("Invalid Id's passed");
        }

        if(!user.getUserType().equals("THEATER_OWNER")) {
            throw new UnAuthorizedException("User is not of type theater Owner");
        }

        if(!hall.getTheater().getTheaterOwner().getId().equals(userId)) {
            throw new UnAuthorizedException("User is not allowed to create show in this hall");
        }

        Show show = new Show();
        show.setShowName(registerShowDto.getShowName());
        show.setHall(hall);
        show.setMovie(movie);
        show.setDisplayStartTime(registerShowDto.getDisplayStartTime());
        show.setDisplayEndTime(registerShowDto.getDisplayEndTime());
        show.setTotalRevenue(0.0);
        show.setTotalTicketSold(0);
        LocalDateTime showStartTime = registerShowDto.getDisplayStartTime();
        long showStartTimeInSeconds = Duration.between(worldStartTime, showStartTime).getSeconds();
        LocalDateTime showEndTime = registerShowDto.getDisplayEndTime();
        long showEndTimeInSeconds = Duration.between(worldStartTime, showEndTime).getSeconds();
        show.setStartTimeInSec(showStartTimeInSeconds);
        show.setEndTimeInSec(showEndTimeInSeconds);

        // Before creating this show, I want to check that is this show overlapping with any other show created for this hall
        // Now I want to get all the shows created for the hall

        List<Show> shows = showRepository.findByHall(hall);
        shows.add(show);
        Collections.sort(shows, (a, b) -> Math.toIntExact(a.getStartTimeInSec() - b.getStartTimeInSec()));

        boolean isOverlapping = false;

        for(int i = 1; i < shows.size(); i++) {
            Show i1 = shows.get(i - 1);
            Show i2 = shows.get(i);

            if(i2.getStartTimeInSec() <= i1.getEndTimeInSec()  ) {
                isOverlapping = true;
                break;
            }
        }

        if(isOverlapping) {
            throw new IllegalArgumentException("Overlapping start and end timing");
        }

        return showRepository.save(show);
    }

    public Show getShowById(UUID showId) {
        return showRepository.findById(showId).orElse(null);
    }

    public List<ShowPriceMapping> createPriceMapping(List<RegisterShowPriceMappingDto> mappingDtos, UUID userId) {
        User theaterOwner = userService.isUserIdExists(userId);
        if(theaterOwner == null) {
            throw new UserNotFoundException("User does not exists");
        }

        if(!theaterOwner.getUserType().equals("THEATER_OWNER")) {
            throw new UnAuthorizedException("User does not have the permission to create show-price mapping");
        }

        List<ShowPriceMapping> priceMappings = new ArrayList<>();

        for(RegisterShowPriceMappingDto mappingDto : mappingDtos) {
            UUID showId = mappingDto.getShowId();
            Show show = this.getShowById(showId);
            UUID hallId = mappingDto.getHallId();
            Hall hall = hallService.isHallIdValid(hallId);
            UUID hallRowMappingId = mappingDto.getHallRowMappingId();
            HallRowMapping hallRowMapping = hallService.getHallRowMappingById(hallRowMappingId);

            if(show == null || hall == null || hallRowMapping == null) {
                throw new IllegalArgumentException("Invalid ids passed in request body");
            }

            ShowPriceMapping showPriceMapping = new ShowPriceMapping();
            showPriceMapping.setShow(show);
            showPriceMapping.setHall(hall);
            showPriceMapping.setHallRowMapping(hallRowMapping);
            showPriceMapping.setPrice(mappingDto.getPrice());

            priceMappings.add(showPriceMapping);
        }

        return showPriceMappingRepository.saveAll(priceMappings);
    }

    public List<SeatStatusDto> fetchAllSeatStatus(UUID showId) {
        Show show = this.getShowById(showId);
        Hall hall = show.getHall();
        List<HallRowMapping> rowMappings = hallService.getHallRowMappingByHall(hall);
        List<SeatStatusDto> seatDetails = new ArrayList<>();

        if(show == null || hall == null || rowMappings == null) {
            throw new IllegalArgumentException("Invalid ids are passed");
        }

        for(HallRowMapping hallRowMapping : rowMappings) {
            String rowRange = hallRowMapping.getRowRange();
            String[] range = rowRange.split("-");
            char st = range[0].charAt(0);
            char en = range[1].charAt(0);
            int seatCount = hallRowMapping.getSeatCount();
            ShowPriceMapping priceMapping = showPriceMappingRepository.getPriceMappingRecordByShowAndRowMapping(showId, hallRowMapping.getId());
            Double price = priceMapping.getPrice();
            for(char row = st; row <= en; row++) {
                for(int i = 1; i <= seatCount; i++) {
                    String seatId = row + "-" + i;
                    // I want to check that this seat is available or not?
                    boolean status = seatBookingService.isSeatAvailableForShow(seatId, show.getId());
                    SeatStatusDto seatStatusDto = new SeatStatusDto();
                    seatStatusDto.setStatus(status);
                    seatStatusDto.setShowId(showId);
                    seatStatusDto.setSeatType(hallRowMapping.getRowType());
                    seatStatusDto.setPrice(price);
                    seatStatusDto.setHallId(hall.getId());
                    seatStatusDto.setSeatId(seatId);
                    seatDetails.add(seatStatusDto);
                }
            }
        }
        return seatDetails;
    }

    public Bill bookSeatForShow(RegisterSeatBookingDto seatBookingDto, UUID userId, UUID showId) {
        // showId or userId -> are they valid or not
        Show show = this.getShowById(showId);
        User customer = userService.isUserIdExists(userId);

        if(customer == null || show == null) {
            throw new IllegalArgumentException("Invalid Id's passed");
        }

        List<String> seatIds = seatBookingDto.getSeatIds();

        for(String seatId : seatIds) {
            boolean seatStatus = seatBookingService.isSeatAvailableForShow(seatId, showId);
            if(!seatStatus) {
                throw new IllegalArgumentException("Seat is already booked");
            }
        }

        List<SeatBooking> seatBookingList = new ArrayList<>();
        double price = 0;

        for(String seatId : seatIds) {
            ShowPriceMapping showPriceMapping = this.getShowPriceOnTheBasisOfSeatId(seatId, show);
            price += showPriceMapping.getPrice();
            SeatBooking seatBooking = new SeatBooking();
            seatBooking.setShow(show);
            seatBooking.setSeatId(seatId);
            seatBooking.setUser(customer);
            String ticketId = seatBookingService.generateTicketId();
            seatBooking.setTicketId(ticketId);
            seatBooking = seatBookingService.saveOrUpdate(seatBooking);
            seatBookingList.add(seatBooking);
        }

        Bill bill = new Bill();
        bill.setSeatBookings(seatBookingList);
        bill.setTheater(show.getHall().getTheater());
        bill.setCustomer(customer);
        bill.setPaymentId(seatBookingDto.getPaymentId());
        bill.setPaymentSource(seatBookingDto.getPaymentSource());
        bill.setTotalPrice(price);

        bill = billRepository.save(bill);

        return bill;
    }

    public ShowPriceMapping getShowPriceOnTheBasisOfSeatId(String seatId, Show show) {
        HallRowMapping hallRowMapping = this.getRowMappingOnTheBasisOfSeatId(seatId, show.getHall());
        return showPriceMappingRepository.getPriceMappingRecordByShowAndRowMapping(show.getId(), hallRowMapping.getId());
    }

    public HallRowMapping getRowMappingOnTheBasisOfSeatId(String seatId, Hall hall) {
        List<HallRowMapping> rowMappings = hallService.getHallRowMappingByHall(hall);
        char seatRow = seatId.split("-")[0].charAt(0);
        for(HallRowMapping rowMapping : rowMappings) {
            String range = rowMapping.getRowRange();  // A-D
            String[] rangeArr = range.split("-");
            char stRange = rangeArr[0].charAt(0);
            char enRange = rangeArr[1].charAt(0);

            if(seatRow >= stRange && seatRow <= enRange) {
                return rowMapping;
            }
        }
        return null;
    }
}
