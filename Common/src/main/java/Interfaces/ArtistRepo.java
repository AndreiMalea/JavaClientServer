package Interfaces;

import Domain.Artist;

import java.util.List;

public interface ArtistRepo extends Repository<Long, Artist>{
    List<Artist> filterByGenre(String genre);
    List<Artist> filterByName(String name);
}
