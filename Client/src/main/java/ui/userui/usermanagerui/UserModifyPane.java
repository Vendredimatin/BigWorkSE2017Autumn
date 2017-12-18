package ui.userui.usermanagerui;

import com.jfoenix.controls.JFXDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserModifyPane extends AnchorPane {


    final FileChooser fileChooser = new FileChooser();


    public UserModifyPane() throws Exception{
        super();
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/userui/usermodifypane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.load();


    }
    @FXML
    public void choosefile(){
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(this.getScene().getWindow());
      //  Image image;
        System.out.println(file.getPath());
  /*      if(image.getWidth()!=image.getHeight()){
            JFXDialog dialog=new JFXDialog();
            dialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
            StackPane stackPane=new StackPane();
            stackPane.getChildren().add(new Label("you are stupid."));
            dialog.show(stackPane);
        }*/

    }

    private static void configureFileChooser(
            final FileChooser fileChooser) {
        fileChooser.setTitle("Pictures Choose");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

    }
}