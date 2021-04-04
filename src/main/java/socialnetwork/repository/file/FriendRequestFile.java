package socialnetwork.repository.file;

import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;

import java.time.LocalDateTime;
import java.util.List;

public class FriendRequestFile extends AbstractFileRepository<Tuple<Long,Long>, FriendRequest> {

    public FriendRequestFile(String fileName, Validator<FriendRequest> validator) {
        super(fileName, validator);
    }

    @Override
    public FriendRequest extractEntity(List<String> attributes) {
        Long    from = Long.parseLong(attributes.get(0)),
                to = Long.parseLong(attributes.get(3));

        FriendRequest fr = new FriendRequest(from, attributes.get(1),
                LocalDateTime.parse(attributes.get(2)), to);
        fr.setId(new Tuple<>(from,to));
        return fr;
    }

    @Override
    protected String createEntityAsString(FriendRequest entity) {
        // from, mesaj, date, to
        return entity.getFrom()+";"+entity.getMesaj()+";"+entity.getDate()+";"+entity.getTo();
    }
}
