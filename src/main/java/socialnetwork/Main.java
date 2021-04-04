package socialnetwork;

import socialnetwork.config.ApplicationContext;
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
//import socialnetwork.ui.Ui;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        //Ui main = new Ui(AllFileService);
        //Ui main = new Ui(FileService);
        //main.run();
        MainFX.main(args);
    }
}

