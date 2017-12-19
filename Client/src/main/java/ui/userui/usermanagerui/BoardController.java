package ui.userui.usermanagerui;


import blService.userblService.UserManagerblService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.*;
import javafx.util.Duration;
import ui.util.PaneSwitchAnimation;


public class BoardController{

    @FXML
    StackPane board;

    UserManagerUIController userManagerUIController;

    UserManagerblService userManagerblService;


    UserListPane userListpane;

    UserDetailPane userDetailPane;

    PaneSwitchAnimation paneSwitchAnimation;

    public void setUserManagerUIController(UserManagerUIController userManagerUIController){
        this.userManagerblService=userManagerblService;
    }

    public void setUserManagerblService(UserManagerblService userManagerblService) {
        this.userManagerblService = userManagerblService;
    }

    public void setUserListpane(UserListPane userListpane) {
        this.userListpane = userListpane;
    }



    public void setPaneSwitchAnimation(PaneSwitchAnimation paneSwitchAnimation) {
        this.paneSwitchAnimation = paneSwitchAnimation;
    }

    public void switchTo(AnchorPane pane){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                 if(HistoricalRecord.addRecord(pane)){
                        paneSwitchAnimation.setNode(pane);
                    }
                }catch (Exception e){

                }
            }
        });
    }

    public void Loading(){
         board.getChildren().setAll(new Loading());
    }


    public void historicalSwitchTo(AnchorPane pane){
        paneSwitchAnimation.setNode(pane);
    }



    public String getId(){
        return board.getChildren().get(0).getId();
    }







}
