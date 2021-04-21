package Domain.Validator;

import Domain.Show;

public class ShowValidator implements Validator<Show>{
    @Override
    public void Validate(Show show) {
        if (show == null || show.getId() == null || show.getArtist() == null || show.getTicketNumber() == null) {
            throw new ValidationException("Invalid show!");
        }
    }
}
