package socialnetwork.service.validators;

import socialnetwork.domain.Utilizator;

public class DataValidator {
    public static void validateUtilizatori(Utilizator u1, Utilizator u2) throws ServiceException {
        String errors = "";
        if (u1 == null)
            errors += "Primul utilizator nu exista!\n";
        if (u2 == null)
            errors += "Al doilea utilizator nu exista!\n";
        if (!errors.isEmpty())
            throw new ServiceException(errors);
    }
}
