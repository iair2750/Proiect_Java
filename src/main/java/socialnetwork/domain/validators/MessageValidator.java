package socialnetwork.domain.validators;

import socialnetwork.domain.Message;

public class MessageValidator implements Validator<Message> {
    @Override
    public void validate(Message entity) throws ValidationException {
        String error = "";
        if (entity.getMesaj() == null || entity.getMesaj().isEmpty())
            error += "Introduceti un mesaj!\n";
        if (entity.getTo().size() == 0)
            error += "Introduceti cel putin un destinatar!\n";
        if (!error.isEmpty())
            throw new ValidationException(error);
    }
}
