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
import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.UtilizatorService;
import socialnetwork.service.validators.ServiceException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ContController{
    private ObservableList<Utilizator> model_prieteni=FXCollections.observableArrayList();
    private ObservableList<FriendRequest> model_cereri = FXCollections.observableArrayList();

    //id-ul de cautare pentru adauga/sterge
    @FXML
    private TextField idCautare;
    private Long idCurent;

    //coloanele din tableview prieteni
    @FXML
    TableView<Utilizator> tablePrieteni;
    @FXML
    TableColumn<Utilizator,String> id;
    @FXML
    TableColumn<Utilizator,String> nume;
    @FXML
    TableColumn<Utilizator,String> prenume;
    @FXML
    TableColumn<Utilizator,String> data;

    //coloanele din tableview cereri de prietenie
    @FXML
    TableView<FriendRequest> tableF;
    @FXML
    TableColumn<FriendRequest,String> from;
    @FXML
    TableColumn<FriendRequest,String> to;
    @FXML
    TableColumn<FriendRequest,String> status;
    @FXML
    TableColumn<FriendRequest,String> dataF;

    private UtilizatorService serv;
    public void setServ(UtilizatorService uServ, TextField id){
        serv=uServ;
        idCurent=Long.parseLong(id.getText());
        initModelCereri();
        initModelPrieteni();
    }

    @FXML
    public void initialize(){
        id.setCellValueFactory(new PropertyValueFactory<Utilizator,String>("id"));
        nume.setCellValueFactory(new PropertyValueFactory<Utilizator,String>("firstName"));
        prenume.setCellValueFactory(new PropertyValueFactory<Utilizator,String>("lastName"));
        //data.setCellValueFactory(new PropertyValueFactory<Utilizator,String>("data"));
        from.setCellValueFactory(new PropertyValueFactory<FriendRequest,String>("from"));
        to.setCellValueFactory(new PropertyValueFactory<FriendRequest,String>("to"));
        status.setCellValueFactory(new PropertyValueFactory<FriendRequest,String>("mesaj"));
        dataF.setCellValueFactory(new PropertyValueFactory<FriendRequest,String>("date"));
        tablePrieteni.setItems(model_prieteni);
        tableF.setItems(model_cereri);
    }

    private void initModelPrieteni(){
        Iterable<Utilizator> p = serv.getFriendsIt(idCurent);
        List<Utilizator> listaP = StreamSupport.stream(p.spliterator(),false).collect(Collectors.toList());
        model_prieteni.setAll(listaP);
    }
    private void initModelCereri(){
        Iterable<FriendRequest> f = serv.getFriendRequest(idCurent);
        List<FriendRequest> listaF = StreamSupport.stream(f.spliterator(),false).collect(Collectors.toList());
        model_cereri.setAll(listaF);
    }
    public void adaugaPrieten(ActionEvent actionEvent) {
        try {
            Long id = Long.parseLong(idCautare.getText());
            serv.send(idCurent, id);
            initModelCereri();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void stergePrieten(ActionEvent actionEvent) {
        try {
            Long id = Long.parseLong(idCautare.getText());
            Prietenie pr= serv.delPrietenie(idCurent,id);
            initModelPrieteni();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void acceptaC(ActionEvent actionEvent) {
        try {
            Long id = Long.parseLong(idCautare.getText());
            serv.acceptFriendRequest(idCurent,id);
            initModelCereri();
            initModelPrieteni();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void refuzaC(ActionEvent actionEvent) {
        try {
            Long id = Long.parseLong(idCautare.getText());
            serv.rejectFriendRequest(idCurent,id);
            initModelCereri();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void retrageC(ActionEvent actionEvent) {
        try {
            Long id = Long.parseLong(idCautare.getText());
            serv.delFriendRequest(idCurent,id);
            initModelCereri();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void ceatAction(ActionEvent actionEvent) {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/ceatView.fxml"));
            AnchorPane rootLayout = loader.load();
            Stage ceatStage = new Stage();
            ceatStage.setTitle("Ceat");
            ceatStage.setScene(new Scene(rootLayout));
            CeatController ceatController = loader.getController();
            ceatController.setServ(serv,idCurent);

            ceatStage.show();
        }
        catch (ServiceException | IOException e){
            throw new ServiceException(e);
        }
    }
}