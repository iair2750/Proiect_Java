package socialnetwork.domain;

import java.util.*;

public class Utilizator extends Entity<Long>{
    private String firstName;
    private String lastName;
    private List<Utilizator> friends;
    private List<Message> mesaje;
    private List<FriendRequest> friendRequests;

    public Utilizator(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        friends = new ArrayList<>();
        mesaje = new ArrayList<>();
        friendRequests = new ArrayList<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Utilizator> getFriends() {
        return friends;
    }

    public void addFriend(Utilizator ot) {
        friends.add(ot);
    }

    public void removeFriend(Utilizator ot) {
        friends.remove(ot);
    }

    public void addMessage(Message m) {
        mesaje.add(m);
    }

    public void delMessage(Message m) {
        mesaje.remove(m);
    }

    public List<Message> getMesaje() {
        return mesaje;
    }

    public void addFriendRequest(FriendRequest f) {
        friendRequests.add(f);
    }

    public void delFriendRequest(FriendRequest f) {
        friendRequests.remove(f);
    }

    public List<FriendRequest> getFriendRequests() {
        return friendRequests;
    }

    @Override
    public String toString() {
        String msg = "Utilizator{" +
                "ID=" + super.getId() + '\'' +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", friends=[";
        int nr = 0;
        for (Utilizator u : friends) {
            msg = msg + u.getId();
            nr++;
            if (nr != friends.size())
                msg = msg + ",";
        }
        msg = msg + "]}";
        return msg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilizator)) return false;
        Utilizator that = (Utilizator) o;
/*        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends());*/
        return this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        //return Objects.hash(getFirstName(), getLastName(), getFriends());
        return Objects.hash(getId());
    }
}