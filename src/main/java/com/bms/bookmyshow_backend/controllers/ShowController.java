package com.bms.bookmyshow_backend.controllers;

import com.bms.bookmyshow_backend.dto.RegisterSeatBookingDto;
import com.bms.bookmyshow_backend.dto.RegisterShowDto;
import com.bms.bookmyshow_backend.dto.RegisterShowPriceMappingDto;
import com.bms.bookmyshow_backend.dto.SeatStatusDto;
import com.bms.bookmyshow_backend.exception.UnAuthorizedException;
import com.bms.bookmyshow_backend.exception.UserNotFoundException;
import com.bms.bookmyshow_backend.models.Bill;
import com.bms.bookmyshow_backend.models.Show;
import com.bms.bookmyshow_backend.models.ShowPriceMapping;
import com.bms.bookmyshow_backend.services.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/show")
public class ShowController {

    ShowService showService;

    @Autowired
    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    // When you want to create a show we need to know the requestBody
    // In that show what movie we are displaying in which hall and which user is basically creating that show
    @PostMapping("/register")
    public ResponseEntity createShow(@RequestBody RegisterShowDto registerShowDto, @RequestParam UUID movieId, @RequestParam UUID hallId, @RequestParam UUID userId) {
        try {
            Show show = showService.createShow(registerShowDto, movieId, hallId, userId);
            return new ResponseEntity(show, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        } catch (UnAuthorizedException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create-price-mapping")
    public ResponseEntity createShowPriceMapping(@RequestBody List<RegisterShowPriceMappingDto> mappingDtos, @RequestParam UUID userId) {
        try {
            List<ShowPriceMapping> priceMappings = showService.createPriceMapping(mappingDtos, userId);
            return new ResponseEntity(priceMappings, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        } catch (UnAuthorizedException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/show-seat-status/{showId}")
    public ResponseEntity fetchShowSeatStatus(@PathVariable UUID showId) {
        try {
            List<SeatStatusDto> seatStatusDtoList = showService.fetchAllSeatStatus(showId);
            return new ResponseEntity(seatStatusDtoList, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        } catch (UnAuthorizedException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/book-seat/{userId}/{showId}")
    public ResponseEntity createSeatBooking(@RequestBody RegisterSeatBookingDto seatBookingDto, @PathVariable UUID userId, @PathVariable UUID showId) {
        try {
            Bill bill = showService.bookSeatForShow(seatBookingDto, userId, showId);
            return new ResponseEntity(bill, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        } catch (UnAuthorizedException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
