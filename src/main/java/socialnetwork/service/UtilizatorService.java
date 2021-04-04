package socialnetwork.service;

import socialnetwork.domain.*;
import socialnetwork.repository.Repository;
import socialnetwork.service.validators.DataValidator;
import socialnetwork.service.validators.ServiceException;

import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Collectors;

public class UtilizatorService  {
    private final Repository<Long, Utilizator> repo;
    private final Repository<Tuple<Long, Long>, Prietenie> repoPrieteni;
    private final Repository<Long, Message> repoMesaje;
    private final Repository<Tuple<Long, Long>, FriendRequest> repoFR;
    private Long id = 0L;
    private Long idMesaje = 0L;

    public UtilizatorService(Repository<Long, Utilizator> repo, Repository<Tuple<Long, Long>, Prietenie> repoPrieteni,
                             Repository<Long, Message> repoMesaje, Repository<Tuple<Long, Long>, FriendRequest> repoFR) {
        this.repo = repo;
        this.repoMesaje = repoMesaje;
        this.repoPrieteni = repoPrieteni;
        this.repoFR = repoFR;

        repo.findAll().forEach(x->{
            if(x.getId() > id)
                id = x.getId();
        });

        repoPrieteni.findAll().forEach(P->{
            Utilizator  u1 = repo.findOne(P.getId().getLeft()),
                        u2 = repo.findOne(P.getId().getRight());
            u1.addFriend(u2);
            u2.addFriend(u1);
        });

        repoMesaje.findAll().forEach(m->{
            if (m.getId() > idMesaje)
                idMesaje = m.getId();
            this.findOne(m.getFrom()).addMessage(m);
            m.getTo().forEach(u->findOne(u).addMessage(m));
        });

        repoFR.findAll().forEach(FR->{
            findOne(FR.getFrom()).addFriendRequest(FR);
            findOne(FR.getTo()).addFriendRequest(FR);
        });
    }

    public Utilizator findOne(Long id) {
        Utilizator u = repo.findOne(id);
        if (u == null)
            throw new ServiceException("Utilizator inexistent!\n");
        return u;
    }

    public Utilizator addUtilizator(String nume, String prenume) {
        Utilizator u = new Utilizator(nume, prenume);
        u.setId(++id);
        return repo.save(u);
    }

    public Message createMesaj(Utilizator from) {
        return new Message(from.getId());
    }
    public Message createMesaj(Long id){
        return createMesaj(findOne(id));
    }

    public void send(Message mesaj) {
        mesaj.setId(++idMesaje);
        mesaj.setDate();
        if (repoMesaje.save(mesaj) != null)
            throw new ServiceException("A aparut o eroare la trimiterea mesajului!\n"); // idMesaje problem..
        this.findOne(mesaj.getFrom()).addMessage(mesaj);
        mesaj.getTo().forEach(u-> {
            if (!u.equals(mesaj.getFrom()))
                this.findOne(u).addMessage(mesaj);
        });
    }

    public Message getMesaj(Long id) {
        Message m = repoMesaje.findOne(id);
        if (m == null)
            throw new ServiceException("Mesaj inexistent");
        return m;
    }

    public Iterable<Message> getMesaje(Utilizator u) {
        return u.getMesaje();
    }
    public Iterable<Message> getMesaje(Long id){
        return getMesaje(findOne(id));
    }

    public Iterable<FriendRequest> getFriendRequest(Utilizator u) {
        return u.getFriendRequests();
    }

    public Iterable<FriendRequest> getFriendRequest(Long id){
        Utilizator u = repo.findOne(id);
        if (u == null)
            throw new ServiceException("Utilizator inexistent!");
        return u.getFriendRequests();
    }

    public FriendRequest newFirendRequest(Utilizator from, Utilizator to) {
        return new FriendRequest(from.getId(), to.getId());
    }
    public FriendRequest send(Long from, Long to){
        Utilizator u1 = findOne(from);
        Utilizator u2 = findOne(to);
        FriendRequest fr = newFirendRequest(u1,u2);
        return send(fr);
    }

    public FriendRequest getFriendRequest(Utilizator from, Utilizator to) {
        FriendRequest fr = repoFR.findOne(new Tuple<>(from.getId(), to.getId()));
        if (fr == null)
            throw new ServiceException("Cerere de prietenie inexistenta!\n");
        return fr;
    }

    public FriendRequest send(FriendRequest fr) {
        Tuple<Long, Long> tuple = new Tuple<>(fr.getFrom(), fr.getTo());
        fr.setId(tuple);
        if (repoPrieteni.findOne(tuple) != null) {
            fr.setMesaj("approved");
            return fr;
        }
        fr.setMesaj("pending");
        fr.setDate();
        FriendRequest save = repoFR.save(fr);
        if (save == null) {
            this.findOne(fr.getFrom()).addFriendRequest(fr);
            this.findOne(fr.getTo()).addFriendRequest(fr);
            return null;
        }
        if (save.getMesaj().equals("rejected")) {
            this.findOne(save.getFrom()).delFriendRequest(save);
            this.findOne(save.getTo()).delFriendRequest(save);
            repoFR.delete(save.getId());
            repoFR.save(fr);
            this.findOne(fr.getFrom()).addFriendRequest(fr);
            this.findOne(fr.getTo()).addFriendRequest(fr);
            return null;
        }
        return save;
    }

    public void acceptFriendRequest(Utilizator from, FriendRequest fr) {
        if (from.getId() == fr.getFrom())
            throw new ServiceException("Nu poti sa iti accepti cererea ta!");
        fr.setMesaj("approved");
        fr.setDate();
        from.delFriendRequest(fr);
        if (this.addPrieteni(this.findOne(fr.getFrom()), this.findOne(fr.getTo())) != null)
            throw new ServiceException("Prietenie deja existenta!");
    }
    public void acceptFriendRequest(Long from, Long to){
        FriendRequest fr = this.getFriendRequest(findOne(from),findOne(to));
        acceptFriendRequest(findOne(from),fr);
    }

    public void rejectFriendRequest(Utilizator from, FriendRequest fr) {
        if (from.getId() == fr.getFrom())
            throw new ServiceException("Nu poti sa iti respingi cererea ta!");
        fr.setMesaj("rejected");
        fr.setDate();
        from.delFriendRequest(fr);
    }
    public void rejectFriendRequest(Long from, Long to) {
        Utilizator u = findOne(from);
        rejectFriendRequest(u,getFriendRequest(u,findOne(to)));
    }

    public void delFriendRequest(Utilizator from, FriendRequest fr) {
        if (fr.getFrom() == from.getId()) {
            if (fr.getMesaj().equals("pending")) {
                from.delFriendRequest(fr);
                this.findOne(fr.getTo()).delFriendRequest(fr);
                repoFR.delete(fr.getId());
            }
            else {
                this.findOne(fr.getFrom()).delFriendRequest(fr);
                this.findOne(fr.getTo()).delFriendRequest(fr);
                repoFR.delete(fr.getId());
            }
        }
        else {
            if (fr.getMesaj().equals("pending"))
                rejectFriendRequest(from, fr);
            else {
                this.findOne(fr.getFrom()).delFriendRequest(fr);
                this.findOne(fr.getTo()).delFriendRequest(fr);
                repoFR.delete(fr.getId());
            }
        }
    }

    public void delFriendRequest(Long from, Long to){
        delFriendRequest(findOne(from),getFriendRequest(findOne(from),findOne(to)));
    }

    public Iterable<Utilizator> getAll() {
        return repo.findAll();
    }
    public Iterable<Prietenie> getFriends() {
        return repoPrieteni.findAll();
    }

    public Iterable<Tuple<Utilizator,Prietenie>> getFriends(Long id) {
        Utilizator u = repo.findOne(id);
        if (u == null)
            throw new ServiceException("Utilizator inexistent!");
        return u.getFriends()
                .stream()
                .map(P-> new Tuple<>(P, repoPrieteni.findOne(new Tuple<>(u.getId(), P.getId()))))
                .collect(Collectors.toList());
    }
    public Iterable<Utilizator> getFriendsIt(Long id){
        Utilizator u = repo.findOne(id);
        if (u == null)
            throw new ServiceException("Utilizator inexistent!");
        return new HashSet<>(u.getFriends());
    }

    public Iterable<Tuple<Utilizator,Prietenie>> getFriends(Long id, Integer luna) {
        Utilizator u = repo.findOne(id);
        if (u == null)
            throw new ServiceException("Utilizator inexistent!");
        return u.getFriends()
                .stream()
                .filter(p->luna.equals(repoPrieteni.findOne(new Tuple<>(u.getId(),p.getId())).getDate()
                        .get(ChronoField.MONTH_OF_YEAR)))
                .map(p -> new Tuple<>(p, repoPrieteni.findOne(new Tuple<>(u.getId(), p.getId()))))
                .collect(Collectors.toList());
    }

    public Utilizator delUtilizator(Utilizator u){
        Utilizator sters = repo.delete(u.getId());
        if (sters != null) {
            delAllFriends(sters);
            delAllFR(sters);
            delAllMessage(sters);
        }
        return sters;
    }

    public Prietenie addPrieteni(Utilizator u1, Utilizator u2) {
        DataValidator.validateUtilizatori(u1, u2);
        Prietenie p = new Prietenie(u1.getId(), u2.getId());
        Prietenie p2 = repoPrieteni.save(p);
        if (p2 == null) {
            u1.addFriend(u2);
            u2.addFriend(u1);
        }
        return p2;
    }

    public Prietenie delPrietenie(Utilizator u1, Utilizator u2) {
        DataValidator.validateUtilizatori(u1, u2);
        Prietenie p;
        p = repoPrieteni.delete(new Tuple<>(u1.getId(),u2.getId()));
        if (p != null){
            u1.removeFriend(u2);
            u2.removeFriend(u1);
        }
        return p;
    }
    public Prietenie delPrietenie(Long id1, Long id2){
        return delPrietenie(findOne(id1),findOne(id2));
    }

    private void delAllFriends(Utilizator u1) {
        u1.getFriends().forEach(u2->{
           u2.removeFriend(u1);
           repoPrieteni.delete(new Tuple<>(u1.getId(),u2.getId()));
        });
    }

    private void delAllFR(Utilizator u) {
        u.getFriendRequests().forEach(fr->{
            findOne(fr.getTo()).delFriendRequest(fr);
            findOne(fr.getFrom()).delFriendRequest(fr);
            repoFR.delete(fr.getId());
        });
    }

    private void delAllMessage(Utilizator u) {
        u.getMesaje().forEach(m->{
            if (m.getFrom() == u.getId()) {
                findOne(m.getFrom()).delMessage(m);
                m.getTo().forEach(u2->findOne(u2).delMessage(m));
                repoMesaje.delete(m.getId());
            }
            else {
                u.delMessage(m);
                m.removeTo(u.getId());
                if (m.getTo().size() == 0) {
                    findOne(m.getFrom()).delMessage(m);
                    repoMesaje.delete(m.getId());
                }
            }
        });
    }

    public Iterable<Message> ceat(Long id1, Long id2) {
        Utilizator  u1 = repo.findOne(id1),
                    u2 = repo.findOne(id2);
        return
        u1.getMesaje().stream()
                .filter(m->{
                    if (m.getFrom() == u2.getId()) return true;
                    return m.findTo(u2);
                })
                .sorted((x,y)->x.getDate().compareTo(y.getDate()))
                .collect(Collectors.toList());
    }

    public List<Utilizator> comunitateSociabila() {
        int nrcomp = 0;
        List<Integer> viz = new ArrayList<>(Collections.nCopies(id.intValue() + 1,0));
        List<List<Utilizator>> conexe = new ArrayList<>();

        for (Utilizator u : repo.findAll()) {
            if(viz.get(u.getId().intValue()).equals(0)) {
                conexe.add(new ArrayList<>());
                conexe.get(nrcomp).add(u);
                ++nrcomp;
                DFS(u, viz, nrcomp);
            }
            else {
                conexe.get(viz.get(u.getId().intValue()) - 1).add(u);
            }
        }

        int lenDrum = -1;
        List<Utilizator> rasp = null;
        for (List<Utilizator> l : conexe) {
            int lenComp = drumComponenta(l);
            if (lenComp > lenDrum) {
                lenDrum = lenComp;
                rasp = l;
            }
        }

        return rasp;
    }

    private int drum(Utilizator u, Map<Utilizator,Integer> viz, int len) {
        viz.put(u, 1);
        int rasp = len;
        for (Utilizator u2 : u.getFriends()) {
            if (viz.get(u2) == 0) {
                int len2 = drum(u2, viz, len + 1);
                if (len2 > rasp)
                    rasp = len2;
            }
        }
        viz.put(u, 0);
        return rasp;
    }

    private int drumComponenta(List<Utilizator> l) {
        Map<Utilizator,Integer> vizitat = new HashMap<>();
        int rasp = 0;
        for (Utilizator u : l) {
            vizitat.put(u,0);
        }

        for (Utilizator u : l) {
            if (rasp == l.size() -1)
                break;
            int lenLoc = drum(u, vizitat, 0);
            if (lenLoc > rasp)
                rasp = lenLoc;
        }

        return rasp;
    }

    public int nrComponente(){
        int nrcomp = 0;
        List<Integer> viz = new ArrayList<>(Collections.nCopies(id.intValue() + 1,0));
        for (Utilizator u : repo.findAll()) {
            if(viz.get(u.getId().intValue()).equals(0)) {
                ++nrcomp;
                DFS(u, viz, nrcomp);
            }
        }
        return nrcomp;
    }

    private void DFS(Utilizator u, List<Integer> viz, int nrcomp) {
        viz.set(u.getId().intValue(), nrcomp);
        for (Utilizator u2 : u.getFriends()) {
            if (viz.get(u2.getId().intValue()).equals(0))
                DFS(u2, viz, nrcomp);
        }
    }

}
