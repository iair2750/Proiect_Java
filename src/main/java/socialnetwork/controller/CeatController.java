package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.Message;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.UtilizatorService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CeatController {
    //@FXML
    //TableColumn<Message,String> reply;
    private ObservableList<Message> model_mesaj=FXCollections.observableArrayList();
    private Message msg;
    @FXML
    public TextField idX;

    @FXML
    TableView<Message> tableMesaj;
    @FXML
    TableColumn<Message,String> idMesaj;
    @FXML
    TableColumn<Message,String> from;
    @FXML
    TableColumn<Message,String> to;
    @FXML
    TableColumn<Message,String> mesaj;
    @FXML
    TableColumn<Message,String> data;
    @FXML
    private TextField mesajCurent;

    private Long idCurent;
    private UtilizatorService serv;
    public void setServ(UtilizatorService serv, Long idCurent) {
        this.idCurent=idCurent;
        this.serv=serv;
        initModelMesaj();
        msg=serv.createMesaj(idCurent);
    }

    @FXML
    public void initialize(){
        idMesaj.setCellValueFactory(new PropertyValueFactory<Message,String>("id"));
        data.setCellValueFactory(new PropertyValueFactory<Message,String>("date"));
        from.setCellValueFactory(new PropertyValueFactory<Message,String>("from"));
        //reply.setCellValueFactory(new PropertyValueFactory<Message,String>("replyMessage"));
        to.setCellValueFactory(new PropertyValueFactory<Message,String>("to"));
        mesaj.setCellValueFactory(new PropertyValueFactory<Message,String>("mesaj"));
        tableMesaj.setItems(model_mesaj);
    }

    private void initModelMesaj(){
        Iterable<Message> m = serv.getMesaje(idCurent);
        List<Message> listaM = StreamSupport.stream(m.spliterator(),false).collect(Collectors.toList());
        model_mesaj.setAll(listaM);
    }


    public void adaugaDestinatar(ActionEvent actionEvent) {
        try{
            Long id = Long.parseLong(idX.getText());
            msg.addTo(serv.findOne(id).getId());
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void seteazaMesaj(ActionEvent actionEvent) {
        msg.setMesaj(mesajCurent.getText());
    }

    public void sendMesaj(ActionEvent actionEvent) {
        try {
            serv.send(msg);
            initModelMesaj();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
