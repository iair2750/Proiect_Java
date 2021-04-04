package socialnetwork.repository.file;

import socialnetwork.domain.Message;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class MessageFile extends AbstractFileRepository<Long, Message> {

    public MessageFile(String fileName, Validator<Message> validator) {
        super(fileName, validator);
    }

    @Override
    public Message extractEntity(List<String> attributes) {
        Long from = Long.parseLong(attributes.get(1)); // id from utilizator
        Message mesaj = new Message(from, attributes.get(2), LocalDateTime.parse(attributes.get(3))); // from,mesaj,date
        mesaj.setId(Long.parseLong(attributes.get(0))); // id
        String to = attributes.get(4);
        for (String s: to.split(","))
            mesaj.addTo(Long.parseLong(s));
        return mesaj;
    }

    @Override
    protected String createEntityAsString(Message entity) {
        String rasp = entity.getId()+";"+entity.getFrom()+";"+entity.getMesaj()+";"+entity.getDate()+";";
        Collection<Long> collection = entity.getTo();
        int poz = 0;
        for (Long u : collection) {
            rasp += u;
            poz++;
            if (poz != collection.size())
                rasp += ",";
        }
        return rasp;
    }
}
