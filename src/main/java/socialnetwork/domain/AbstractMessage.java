package socialnetwork.domain;

import java.time.LocalDateTime;

public abstract class AbstractMessage<ID> extends Entity<ID> {
    private final Long from;
    private String mesaj;
    private LocalDateTime date;

    public AbstractMessage(Long from) {
        this.from = from;
        this.mesaj = null;
        this.date = null;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }

    public Long getFrom() {
        return from;
    }

    public String getMesaj() {
        return mesaj;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate() {
        this.date = LocalDateTime.now();
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
