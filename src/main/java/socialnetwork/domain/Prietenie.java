package socialnetwork.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Prietenie extends Entity<Tuple<Long,Long>> {

    LocalDateTime date;

    public Prietenie(Long id1, Long id2) {
        date = LocalDateTime.now();
        this.setId(new Tuple<>(id1, id2));
    }

    public Prietenie(Long id1, Long id2, LocalDateTime date) {
        this.setId(new Tuple<>(id1, id2));
        this.date = date;
    }

    @Override
    public String toString() {
        return "Prietenie{" +
                "utilizator 1="+this.getId().getLeft() +
                ", utilizator 2="+this.getId().getRight()+
                ", date=" + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) +
                '}';
    }

    /**
     *
     * @return the date when the friendship was created
     */

    public LocalDateTime getDate() {
        return date;
    }
}
