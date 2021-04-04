package socialnetwork.domain.validators;

import socialnetwork.domain.FriendRequest;

public class FriendRequestValidator implements Validator<FriendRequest> {
    @Override
    public void validate(FriendRequest entity) throws ValidationException {
        if (entity.getId().getLeft().equals(entity.getId().getRight()))
            throw new ValidationException("Nu iti poti trimite tie cerere de prietenie!\n");
    }
}
