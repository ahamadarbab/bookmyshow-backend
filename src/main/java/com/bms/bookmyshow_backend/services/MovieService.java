package com.bms.bookmyshow_backend.services;

import com.bms.bookmyshow_backend.dto.RegisterMovieDto;
import com.bms.bookmyshow_backend.exception.UnAuthorizedException;
import com.bms.bookmyshow_backend.exception.UserNotFoundException;
import com.bms.bookmyshow_backend.models.Artist;
import com.bms.bookmyshow_backend.models.Movie;
import com.bms.bookmyshow_backend.models.User;
import com.bms.bookmyshow_backend.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MovieService {

    UserService userService;
    MovieRepository movieRepository;
    ArtistService artistService;

    @Autowired
    public MovieService(UserService userService, MovieRepository movieRepository, ArtistService artistService) {
        this.userService = userService;
        this.movieRepository = movieRepository;
        this.artistService = artistService;
    }

    public Movie registerMovie(RegisterMovieDto registerMovieDto) {
        UUID movieOwnerId = registerMovieDto.getMovieOwnerUserId();
        // We need to validate is this userId actual userId or not?
        // We should call userService to check
        User movieOwner = userService.isUserIdExists(movieOwnerId);
        if(movieOwner == null) {
            throw new UserNotFoundException("Movie owner Id is invalid");
        }

        if(!movieOwner.getUserType().equals("MOVIE_OWNER"))
        {
            throw new UnAuthorizedException("User does not have access to register movie");
        }

        // Now we want to register the movie -> to register the movie inside the movie table
        // we require movie class object, now to create movie class object
        // we are already getting the data in the dto

        Movie movie = new Movie();
        movie.setMovieName(registerMovieDto.getMovieName());
        movie.setMovieOwner(movieOwner);
        movie.setGenre(registerMovieDto.getGenre());
        movie.setLaunchDate(registerMovieDto.getLaunchDate());
        movie.setRating(0.0);
        movie.setMovieDurationInHours(registerMovieDto.getMovieDurationInHours());
        movie.setTotalIncome(0.0);
        movie.setTotalTicketSold(0);
        // movie.setArtists();  -> List<Artists>  but we are getting List<String>
        // movie.setDirector(); -> Artist -> To set director we need to set Artist object inside this
        List<Artist> artists = artistService.fetchAllArtists(registerMovieDto.getArtists());
        movie.setArtists(artists);
        Artist director = artistService.fetchArtistByName(registerMovieDto.getDirector());
        movie.setDirector(director);

        return movieRepository.save(movie);
    }

    public Movie isMovieIdValid(UUID movieId) {
        Optional<Movie> movie = movieRepository.findById(movieId);
        return movie.orElse(null);
    }

}
