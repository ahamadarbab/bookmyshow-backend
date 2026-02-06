package com.bms.bookmyshow_backend.services;

import com.bms.bookmyshow_backend.models.Artist;
import com.bms.bookmyshow_backend.repositories.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArtistService {

    ArtistRepository artistRepository;

    @Autowired
    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public List<Artist> fetchAllArtists(List<String> artistNames) {
        List<Artist> artists = new ArrayList<>();

        for(int i = 0; i < artistNames.size(); i++) {
            String artistName = artistNames.get(i);
            // We want to check that is this artistName present in the artist table or not
            Artist artist = artistRepository.findByArtistName(artistName);

            if(artist == null) {
                artist = new Artist();
                artist.setArtistName(artistName);
                artist = artistRepository.save(artist);
            }

            artists.add(artist);
        }
        return artists;
    }

    public Artist fetchArtistByName(String artistName) {
        Artist artist = artistRepository.findByArtistName(artistName);
        if(artist == null) {
            artist = new Artist();
            artist.setArtistName(artistName);
            artist = artistRepository.save(artist);
        }
        return artist;
    }
}
