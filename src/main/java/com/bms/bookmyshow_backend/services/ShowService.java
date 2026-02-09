package com.bms.bookmyshow_backend.services;

import com.bms.bookmyshow_backend.dto.RegisterShowDto;
import com.bms.bookmyshow_backend.exception.UnAuthorizedException;
import com.bms.bookmyshow_backend.models.Hall;
import com.bms.bookmyshow_backend.models.Movie;
import com.bms.bookmyshow_backend.models.Show;
import com.bms.bookmyshow_backend.models.User;
import com.bms.bookmyshow_backend.repositories.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class ShowService {

    ShowRepository showRepository;
    MovieService movieService;
    HallService hallService;
    UserService userService;
    // 1/1/2015 00:00:00
    LocalDateTime worldStartTime = LocalDateTime.of(2015, 1, 1, 0, 0, 0);

    @Autowired
    public ShowService(ShowRepository showRepository, MovieService movieService, HallService hallService, UserService userService) {
        this.showRepository = showRepository;
        this.movieService = movieService;
        this.hallService = hallService;
        this.userService = userService;
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
}
