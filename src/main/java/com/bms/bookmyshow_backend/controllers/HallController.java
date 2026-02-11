package com.bms.bookmyshow_backend.controllers;

import com.bms.bookmyshow_backend.dto.RegisterHallDto;
import com.bms.bookmyshow_backend.dto.RegisterHallRowMappingDto;
import com.bms.bookmyshow_backend.exception.TheaterNotFoundException;
import com.bms.bookmyshow_backend.exception.UnAuthorizedException;
import com.bms.bookmyshow_backend.exception.UserNotFoundException;
import com.bms.bookmyshow_backend.models.Hall;
import com.bms.bookmyshow_backend.models.HallRowMapping;
import com.bms.bookmyshow_backend.services.HallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hall")
public class HallController {

    HallService hallService;

    @Autowired
    public HallController(HallService hallService) {
        this.hallService = hallService;
    }

    @PostMapping("/register")
    public ResponseEntity registerHall(@RequestBody RegisterHallDto registerHallDto, @RequestParam UUID userId, @RequestParam UUID theaterId) {
        try {
            Hall hall = hallService.registerHall(registerHallDto, userId, theaterId);
            return new ResponseEntity(hall, HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        } catch (UnAuthorizedException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
        } catch (TheaterNotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create-row-mapping")
    public ResponseEntity createHallRowMapping(@RequestBody List<RegisterHallRowMappingDto> mappingsDto, @RequestParam UUID userId) {
        try {
            List<HallRowMapping> mappings = hallService.createHallRowMappings(mappingsDto, userId);
            return new ResponseEntity(mappings, HttpStatus.CREATED);
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
