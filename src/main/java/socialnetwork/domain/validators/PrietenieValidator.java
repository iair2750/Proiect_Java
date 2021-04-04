package socialnetwork.domain.validators;
import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Utilizator;
import socialnetwork.repository.Repository;

public class PrietenieValidator implements Validator<Prietenie> {

    @Override
    public void validate(Prietenie entity) throws ValidationException {
        if(entity.getId().getLeft().equals(entity.getId().getRight()))
            throw new ValidationException("Nu exista prietenie a utilizatorului cu el insusi!\n");
    }
}
