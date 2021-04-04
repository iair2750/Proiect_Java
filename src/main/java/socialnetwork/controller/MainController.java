package socialnetwork.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import socialnetwork.domain.Utilizator;
import socialnetwork.service.UtilizatorService;
import socialnetwork.service.validators.ServiceException;
import sun.nio.ch.Util;

import java.awt.*;
import java.io.IOException;

public class MainController {
    @FXML
    private TextField loginID;

    private UtilizatorService serv;

    public void setServ(UtilizatorService uServ){
        serv=uServ;
    }

    @FXML
    public void cautaID(){
        try{
            Utilizator u=serv.findOne(Long.parseLong(loginID.getText()));
            loginAction(u);
        }
        catch (ServiceException e){
            throw new ServiceException(e);
        }
    }

    public void loginAction(Utilizator u){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/contView.fxml"));
            AnchorPane rootLayout = loader.load();
            Stage contStage = new Stage();
            contStage.setTitle("Contul lui "+u.getId()+" "+u.getFirstName()+" "+u.getLastName());
            contStage.setScene(new Scene(rootLayout));
            ContController contController = loader.getController();
            contController.setServ(serv,loginID);

            contStage.show();
        }
        catch (ServiceException | IOException e){
            throw new ServiceException(e);
        }
    }


}
