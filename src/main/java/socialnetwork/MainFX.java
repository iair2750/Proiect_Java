package socialnetwork;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import socialnetwork.config.ApplicationContext;
import socialnetwork.controller.MainController;
import socialnetwork.domain.*;
import socialnetwork.domain.validators.FriendRequestValidator;
import socialnetwork.domain.validators.MessageValidator;
import socialnetwork.domain.validators.PrietenieValidator;
import socialnetwork.domain.validators.UtilizatorValidator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.file.FriendRequestFile;
import socialnetwork.repository.file.MessageFile;
import socialnetwork.repository.file.PrietenieFile;
import socialnetwork.repository.file.UtilizatorFile;
import socialnetwork.repository.memory.InMemoryRepository;
import socialnetwork.service.UtilizatorService;


public class MainFX extends Application {
    String fileUsers = ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.users");
    String fileFriends = ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.friends");
    String fileMessage = ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.mesaje");
    String fileFriendRequest = ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.friendRequest");


    Repository<Long, Utilizator> FileRepository = new UtilizatorFile(fileUsers, new UtilizatorValidator());
    Repository<Tuple<Long,Long>, Prietenie> repoPrietenie = new PrietenieFile(fileFriends, new PrietenieValidator());
    Repository<Long, Message> repoMesaje = new InMemoryRepository<>(new MessageValidator());
    Repository<Tuple<Long, Long>, FriendRequest> repoFR = new InMemoryRepository<>(new FriendRequestValidator());
    UtilizatorService FileService = new UtilizatorService(FileRepository, repoPrietenie, repoMesaje, repoFR);


    Repository<Long, Message> FilerepoMesaje = new MessageFile(fileMessage, new MessageValidator());
    Repository<Tuple<Long, Long>, FriendRequest> FilerepoFR = new FriendRequestFile(fileFriendRequest,
            new FriendRequestValidator());

    UtilizatorService AllFileService = new UtilizatorService(FileRepository,repoPrietenie, FilerepoMesaje, FilerepoFR);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.show();
    }

    private void init(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/mainView.fxml"));
        AnchorPane rootLayout = loader.load();

        MainController mainController = loader.getController();
        mainController.setServ(FileService);
        Scene mainScene = new Scene(rootLayout);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Social Network");

    }

}

