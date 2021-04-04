package socialnetwork.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class FriendRequest extends AbstractMessage<Tuple<Long,Long>> {
    Long to;

    public FriendRequest(Long from, Long to) {
        super(from);
        this.to = to;
        setMesaj("none");
        ///TODO: set id from here; remove it form service and FriendRequestFile
    }

    public FriendRequest(Long from, String mesaj, LocalDateTime data, Long to) {
        super(from);
        this.to = to;
        setMesaj(mesaj);
        setDate(data);
    }

    public Long getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "FriendRequest{" +
                "from=" + getFrom() +
                ", to=" + to +
                ", status=" + getMesaj() +
                ", date=" + getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FriendRequest)) return false;
        FriendRequest friendRequest = (FriendRequest) o;
        return getId().equals(friendRequest.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
