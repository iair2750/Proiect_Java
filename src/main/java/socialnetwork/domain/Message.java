package socialnetwork.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Message extends AbstractMessage<Long> {
    private Map<Long, Long> to;
    private Message replyMessage;

    public Message(Long from) {
        super(from);
        this.replyMessage = null;
        this.to = new HashMap<>();
    }

    public Message(Long from, String mesaj, LocalDateTime date) {
        super(from);
        replyMessage = null;
        setMesaj(mesaj);
        setDate(date);
        this.to = new HashMap<>();
    }

    public boolean findTo(Utilizator u) {
        return to.get(u.getId()) != null;
    }

    public Message getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(Message replyMessage) {
        this.replyMessage = replyMessage;
    }

    public Long addTo(Long u) {
        if (to.get(u) != null)
            return u;
        to.put(u, u);
        return null;
    }

    public Long removeTo(Long u) {
        if (to.get(u) == null)
            return null;
        return to.remove(u);
    }

    public Collection<Long> getTo() {
        return to.values();
    }

    @Override
    public String toString() {
        String string = "Mesaj{" +
                "Id=" + getId() +
                ", from=" + getFrom();
        string += ", to=[";
        int nr = 0;
        for (Long u : getTo()) {
            string += u;
            nr++;
            if (nr != to.size())
                string = string + ",";
        }
        string += ']' +
                ", text='" + getMesaj() + '\'' +
                ", date=" + getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) +
                '}';
        return string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return getId().equals(message.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
