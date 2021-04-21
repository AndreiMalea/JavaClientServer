package Interfaces;

import Domain.Artist;
import Domain.Show;

import java.time.LocalDate;
import java.util.List;

public interface ShowRepo extends Repository<Long, Show> {
    List<Show> filterByArtist(Artist artist);
    List<Show> filterByDate(LocalDate date);
}
