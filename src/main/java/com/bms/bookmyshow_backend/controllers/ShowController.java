package com.bms.bookmyshow_backend.controllers;

import com.bms.bookmyshow_backend.dto.RegisterShowDto;
import com.bms.bookmyshow_backend.services.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    }
}
