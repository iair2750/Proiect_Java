package socialnetwork.domain.validators;

import socialnetwork.domain.Utilizator;

public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        String errors = "";
        if(entity.getFirstName().equals(""))
            errors += "Numele nu poate fi vid!\n";
        if(entity.getLastName().equals(""))
            errors += "Prenumele nu poate fi vid!\n";
        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }
}
