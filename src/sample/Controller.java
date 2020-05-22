package sample;

import com.jfoenix.controls.JFXSlider;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    Stage stageAbout = new Stage();
    Node node;
    private final FileChooser fileChooser = new FileChooser();
    @FXML
    StackPane stack;
    @FXML
    AnchorPane mediaController;
    @FXML
    Button start, cross,ForwardButt,BackButt;
    @FXML
    Button stop;
    @FXML
    Button sound,browse,about;
    @FXML
    Button fullScreenButton;
    @FXML
    MediaView Screen;
    @FXML
    AnchorPane pane;
    @FXML
    JFXSlider Slider, volumeSlider;
    @FXML
    Label TotalTime, stopWatch,tip;
    @FXML
    ImageView playIcon, soundIcon, Welcome, fullScreenIcon;
    private MediaPlayer mediaPlayer = new MediaPlayer(new Media(Controller.class.getResource("Aerial Shot Of Sunset.m4v").toString()));
    private int time;
    private int h, m, s;
    private double aDoubleTime;
    private double soundValue = 0.3;
    private String Hours, Minutes, Seconds;
    private final Stage BrowseStage = new Stage();
    @FXML
    protected void dragPlay(DragEvent dragEvent) {
        Dragboard dragboard = dragEvent.getDragboard();
        String file = dragboard.getFiles().toString().substring(1, dragboard.getFiles().toString().length() - 1);
        mediaPlayerStarter(file);
    }

    private void mediaPlayerStarter(String file) {
        try {
            Main.stage.setTitle(file.substring(file.lastIndexOf('\\')+1)+" - "+"Finger Player");
            h = 0;
            m = 0;
            s = 0;
            playIcon.setImage(new Image(Controller.class.getResource("pause.png").toString()));
            mediaPlayer.stop();
            Screen.setMediaPlayer(null);
            mediaPlayer = new MediaPlayer(new Media(new File(file).toURI().toString()));
                Screen.setMediaPlayer(mediaPlayer);
                mediaPlayer.setOnReady(() -> {
                    aDoubleTime = mediaPlayer.getTotalDuration().toSeconds();
                    time = (int) mediaPlayer.getTotalDuration().toSeconds();
                    TotalTime.setText(new Clock().CalculateTime(time));
                    mediaPlayer.setVolume(soundValue);

                });
                mediaPlayer.play();
                watch();
            if (file.contains(".mp3") || file.contains(".wav")||file.contains(".m4a"))
            {
                fullScreenButton.setDisable(true);
                Welcome.setVisible(true);
            }
            else {
                fullScreenButton.setDisable(false);
                Welcome.setVisible(false);
            }
            mediaController.setDisable(false);
            Slider.setDisable(false);
        } catch (Exception e) {
            System.out.println("This media is not supported by Finger Player");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Finger Player dialog");
            alert.setContentText("Finger Player does not support this file");
            alert.setGraphic(new ImageView(new Image(Controller.class.getResource("file.png").toString())));
            alert.showAndWait();
        }
    }
    @FXML
    protected void SlideTip(MouseEvent mouseEvent){
        double value=mouseEvent.getPickResult().getIntersectedPoint().getX()*100/Slider.getWidth();
        Duration time=Duration.seconds(value*mediaPlayer.getTotalDuration().toSeconds()/100);
        tip.setText(new Clock().CalculateTime((int)time.toSeconds()));
        tip.setLayoutX(mouseEvent.getSceneX()-20);
        tip.setLayoutY(mouseEvent.getSceneY()-40);
    }
    @FXML
    protected void showTip(){
        tip.setVisible(true);
    }
    @FXML
    protected void HideTip(){
        tip.setVisible(false);
    }
    //File Chooser
    @FXML
    protected void Browse() {
        configuringFileChooser(fileChooser);
        Main.stage.getScene().getRoot().setDisable(true);
        try {
            String files = fileChooser.showOpenMultipleDialog(BrowseStage).toString();
            files = files.substring(1, files.length() - 1);
            mediaPlayerStarter(files);
            Main.stage.getScene().getRoot().setDisable(false);
        } catch (Exception e) {
            System.out.println("Choose a correct folder");
            Main.stage.getScene().getRoot().setDisable(false);
        }
    }

    private void configuringFileChooser(FileChooser fileChooser) {
        // Set title for FileChooser
        fileChooser.setTitle("Select Media");

        // Set Initial Directory
        fileChooser.setInitialDirectory(new File("D:\\"));

        // Add Extension Filters
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP4", "*.mp4"),
                new FileChooser.ExtensionFilter("MP3", "*.mp3"));
    }

    @FXML
    protected void Show(MouseEvent mouseEvent) {
        if (!pane.isVisible())
        {
        node =(Node) mouseEvent.getSource();
        pane.setVisible(true);
        Delay();
        }
    }
    @FXML
    protected void Hide() {
        pane.setVisible(false);
    }

    @FXML
    protected void setAction(ActionEvent actionEvent) {
        Button clickedButton = (Button) actionEvent.getSource();
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            playIcon.setImage(new Image(Controller.class.getResource("play.png").toString()));
            mediaPlayer.pause();
        }
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED || mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED) {
            Welcome.setVisible(true);
            playIcon.setImage(new Image(Controller.class.getResource("pause.png").toString()));
            mediaPlayer.play();
            Screen.setMediaPlayer(mediaPlayer);
        }
        if (clickedButton.getGraphic().getId().equals("stopIcon")) {
            Welcome.setVisible(true);
            mediaPlayer.stop();
            Screen.setMediaPlayer(null);
            mediaPlayer.seek(Duration.millis(0));
            Slider.setValue(0);
            playIcon.setImage(new Image(Controller.class.getResource("play.png").toString()));
            m = 0;
            s = 0;
            h = 0;
            Welcome.setVisible(true);
            stopWatch.setText(h + "0" + ":" + m + "0" + ":" + s + "0");
        }
    }
    private void watch() {
        Task<Void> sleep = new Task<>() {
            @Override
            protected Void call() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
                return null;
            }
        };
        sleep.setOnSucceeded(event2 -> {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                Slider.setValue(mediaPlayer.getCurrentTime().toMillis() * 0.1 / time);
                s = (int) mediaPlayer.getCurrentTime().toSeconds();
                if (s >= 60) {
                    m = s / 60;
                    s = s % 60;
                }
                if (m >= 60) {
                    h = m / 60;
                    m = m % 60;
                }
                if (h <= 9)
                    Hours = "0" + h;
                else
                    Hours = "" + h;
                if (m <= 9)
                    Minutes = "0" + m;
                else
                    Minutes = "" + m;
                if (s <= 9)
                    Seconds = "0" + s;
                else
                    Seconds = "" + s;
                stopWatch.setText(Hours + ":" + Minutes + ":" + Seconds);
                if (mediaPlayer.getCurrentTime().toSeconds() == aDoubleTime)
                {
                    mediaPlayer.stop();
                    mediaPlayer.seek(Duration.millis(0));
                    Screen.setMediaPlayer(null);
                    playIcon.setImage(new Image(Controller.class.getResource("play.png").toString()));
                    m = 0;
                    s = 0;
                    h = 0;
                    stopWatch.setText("0" + h + ":" + "0" + m + ":" + "0" + s);
                    Slider.setValue(0);
                    Welcome.setVisible(true);
                }
            }
            watch();
        });
        new Thread(sleep).start();
    }

    @FXML
    protected void SliderClicked(MouseEvent mouseEvent) {
        mediaPlayer.pause();
        JFXSlider jfxSlider = (JFXSlider) mouseEvent.getSource();
        double value = jfxSlider.getValue();
        mediaPlayer.seek(new Duration(value * 10 * time));
        mediaPlayer.play();
        Duration time=Duration.seconds(value*mediaPlayer.getTotalDuration().toSeconds()/100);
        tip.setText(new Clock().CalculateTime((int)time.toSeconds()));
        tip.setLayoutX(mouseEvent.getSceneX()-20);
        tip.setLayoutY(mouseEvent.getSceneY()-40);
        m = 0;
        s = 0;
        h = 0;
    }

    @FXML
    protected void volumeController() {
        mediaPlayer.setMute(false);
        mediaPlayer.setVolume(volumeSlider.getValue() / 100);
        soundValue = mediaPlayer.getVolume();
        if (soundValue == 0)
            soundIcon.setImage(new Image(Controller.class.getResource("mute.png").toString()));
        if (soundValue > 0 && soundValue < 0.5)
            soundIcon.setImage(new Image(Controller.class.getResource("less.png").toString()));
        if (soundValue >= 0.5 && soundValue < 0.9)
            soundIcon.setImage(new Image(Controller.class.getResource("half.png").toString()));
        if (soundValue > 0.9)
            soundIcon.setImage(new Image(Controller.class.getResource("full.png").toString()));
    }

    @FXML
    protected void About() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        stageAbout.setScene(new Scene(root,587,469));
        stageAbout.setTitle("About");
        stageAbout.setResizable(false);
        stageAbout.getIcons().addAll(new Image(Controller.class.getResource("MyIcon.png").toString()));
        stageAbout.showAndWait();
    }

    @FXML
    protected void muteClicked() {
        if (!mediaPlayer.isMute()) {
            soundValue = mediaPlayer.getVolume();
            mediaPlayer.setMute(true);
            volumeSlider.setValue(0);
            soundIcon.setImage(new Image(Controller.class.getResource("mute.png").toString()));
        } else {
            mediaPlayer.setMute(false);
            if (soundValue == 0)
                soundIcon.setImage(new Image(Controller.class.getResource("mute.png").toString()));
            if (soundValue > 0 && soundValue < 0.5)
                soundIcon.setImage(new Image(Controller.class.getResource("less.png").toString()));
            if (soundValue >= 0.5 && soundValue < 0.9)
                soundIcon.setImage(new Image(Controller.class.getResource("half.png").toString()));
            if (soundValue > 0.9)
                soundIcon.setImage(new Image(Controller.class.getResource("full.png").toString()));
            volumeSlider.setValue(soundValue * 100);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        stageAbout.initModality(Modality.APPLICATION_MODAL);
        start=circularButton(start,30,"Play");
        fullScreenButton=circularButton(fullScreenButton,20,"Full Screen");
        stop=circularButton(stop,30,"Stop");
        ForwardButt=circularButton(ForwardButt,30,"Forward");
        BackButt=circularButton(BackButt,30,"Backward");
        sound=circularButton(sound,20,"Mute");
        Main.stage.widthProperty().addListener((observable ->
        {
            mediaController.setLayoutX((Main.stage.getWidth() / 2) - (213.5));
            TotalTime.setLayoutX(Main.stage.getWidth() - 127);
            stack.setAlignment(Pos.CENTER);
            stack.setPrefWidth(Main.stage.getWidth() - 16);
            Screen.setFitWidth(stack.getPrefWidth());
            fullScreenButton.setLayoutX(Main.stage.getWidth() - 100);
        }));
        Main.stage.heightProperty().addListener(observable -> {
            pane.setLayoutY(Main.stage.getHeight() - 165);
            stack.setAlignment(Pos.CENTER);
            stack.setPrefHeight(Main.stage.getHeight() - 39);
            Screen.setFitHeight(stack.getPrefHeight());
        });
    }

    @FXML
    protected void fullScreenAction() {
        if (!Main.stage.isFullScreen()) {
            about.setDisable(true);
            browse.setDisable(true);
            cross.setVisible(true);
            Main.stage.setFullScreen(true);
            pane.setLayoutY(pane.getLayoutY() + 40);
            fullScreenIcon.setImage(new Image(Controller.class.getResource("ReSize.png").toString()));
            fullScreenButton.setLayoutX(Main.stage.getWidth() - 100);
            stack.setPrefWidth(stack.getPrefWidth() + 15);
            stack.setPrefHeight(stack.getPrefHeight() + 40);
            Screen.setFitWidth(stack.getPrefWidth());
            Screen.setFitHeight(stack.getPrefHeight());
        } else {
            cross.setVisible(false);
            Main.stage.setFullScreen(false);
            about.setDisable(false);
            browse.setDisable(false);
            fullScreenIcon.setImage(new Image(Controller.class.getResource("FullScreen.png").toString()));
            fullScreenButton.setLayoutX(Main.stage.getWidth() - 100);
            Hide();
        }
    }

    @FXML
    private void onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ESCAPE) {
            fullScreenIcon.setImage(new Image(Controller.class.getResource("FullScreen.png").toString()));
            cross.setVisible(false);
            Hide();
        }
    }

    @FXML
    protected void close() {
        System.exit(-1);
    }

    @FXML
    protected void forwardAction() {
        mediaPlayer.pause();
        mediaPlayer.seek(new Duration(mediaPlayer.getCurrentTime().toMillis() + 10000));
        mediaPlayer.play();
        m = 0;
        s = 0;
        h = 0;
    }

    @FXML
    protected void backwardAction() {
        mediaPlayer.pause();
        mediaPlayer.seek(new Duration(mediaPlayer.getCurrentTime().toMillis() - 10000));
        mediaPlayer.play();
        m = 0;
        s = 0;
        h = 0;
    }
    Button circularButton(Button bt,double r,String name){
        Tooltip tooltip=new Tooltip(name);
        bt.setTooltip(tooltip);
        bt.setShape(new Circle(r));
        bt.setMinSize(2*r,2*r);
        bt.setMaxSize(2*r,2*r);
        return bt;
    }
    private void Delay(){
        Task<Void> sleep = new Task<>() {
            @Override
            protected Void call() {
                try {
                    Thread.sleep(8000);
                } catch (InterruptedException ignored) {
                }
                return null;
            }
        };
        sleep.setOnSucceeded(event2 -> {
            if (!node.getId().equals("pane"))
            Hide();
        });
        new Thread(sleep).start();
    }
}