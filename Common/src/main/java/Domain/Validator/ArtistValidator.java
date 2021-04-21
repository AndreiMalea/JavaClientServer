package Domain.Validator;

import Domain.Artist;

public class ArtistValidator implements Validator<Artist> {
    @Override
    public void Validate(Artist artist) {
        if (artist==null || artist.getId() == null || artist.getGenre() == null || artist.getName() == null) {
            throw new ValidationException("Invalid artist!");
        }
    }
}
