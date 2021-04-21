package Domain.Validator;

import Domain.Office;

public class OfficeValidator implements Validator<Office>{
    @Override
    public void Validate(Office office) {
        if (office == null || office.getId() == null || office.getLocation() == null) {
            throw new ValidationException("Invalid office!");
        }
    }
}
