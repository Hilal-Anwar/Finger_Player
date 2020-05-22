package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    static Stage stage=new Stage();
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Layout.fxml"));
        stage.setScene(new Scene(root,754,423));
        stage.setMinWidth(754);
        stage.setMinHeight(423);
        stage.setMaximized(true);
        stage.setTitle("Finger Player");
        stage.getIcons().addAll(new Image(Controller.class.getResource("MyIcon.png").toString()));
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
