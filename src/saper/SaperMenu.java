package saper;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;


public class SaperMenu extends Application {
    private static Pane root;
    private static int WIDTH = 400;
    private static int HEIGHT = 400;
    private MenuItem newGame = new MenuItem("NEW GAME");
    private MenuItem settings = new MenuItem("SETTINGS");
    private MenuItem exit = new MenuItem("EXIT");
    private SubMenu mainMenu = new SubMenu(this.newGame, this.settings, this.exit);

    private MenuItem sound = new MenuItem("SOUND OFF|ON");
    private MenuItem buttonSettingsBack = new MenuItem("BACK");
    private SubMenu optionsMenu = new SubMenu(this.sound, this.buttonSettingsBack);

    private MenuItem newGameEasy = new MenuItem("EASY");
    private MenuItem newGameMedium = new MenuItem("MEDIUM");
    private MenuItem newGameHard = new MenuItem("HARD");
    private MenuItem newGameButtonBack = new MenuItem("BACK");
    private SubMenu newGameMenu = new SubMenu(this.newGameEasy, this.newGameMedium,
            this.newGameHard, this.newGameButtonBack);

    private MediaPlayer song;

    private MenuBox menuBox = new MenuBox(mainMenu);
    private boolean songIsPlaying;

    private static Minesweeper thread = new Minesweeper();

    private void addPicture()
    {
        root = new Pane();
        Image image = new Image(getClass().getResourceAsStream("images/saper.jpg"));
        ImageView img = new ImageView(image);
        img.setFitHeight(HEIGHT);
        img.setFitWidth(WIDTH);
        root.getChildren().add(img);
    }

    private void setButtonClicks(){
        this.newGame.setOnMouseClicked(event -> {this.menuBox.setSubMenu(this.newGameMenu); playClick();});
        this.settings.setOnMouseClicked(event -> {this.menuBox.setSubMenu(this.optionsMenu); playClick();});
        this.exit.setOnMouseClicked(event -> System.exit(0));

        this.buttonSettingsBack.setOnMouseClicked(event -> {this.menuBox.setSubMenu(this.mainMenu); playClick();});
        this.sound.setOnMouseClicked(event -> {
            if (this.songIsPlaying)
            {
                this.song.pause();
                playClick();
                this.songIsPlaying = false;
            }

            else {
                playClick();
                this.songIsPlaying = true;
                this.song.play();
            }
        });

        this.newGameButtonBack.setOnMouseClicked(event -> {this.menuBox.setSubMenu(this.mainMenu); playClick();});



        this.newGameEasy.setOnMouseClicked(event -> {
            playClick();
            Minesweeper.setBOMBS(12);
            Minesweeper.setCOLS(10);
            Minesweeper.setRAWS(10);
            Minesweeper.setHEIGHT(300);
            Minesweeper.setWIDTH(300);
            thread.run();});

        this.newGameMedium.setOnMouseClicked(event -> {
            playClick();
            Minesweeper.setBOMBS(30);
            Minesweeper.setCOLS(15);
            Minesweeper.setRAWS(15);
            Minesweeper.setHEIGHT(450);
            Minesweeper.setWIDTH(450);
            thread.run();});

        this.newGameHard.setOnMouseClicked(event -> {
            playClick();
            Minesweeper.setBOMBS(50);
            Minesweeper.setCOLS(20);
            Minesweeper.setRAWS(20);
            Minesweeper.setHEIGHT(600);
            Minesweeper.setWIDTH(600);
            thread.run();
        });


        root.getChildren().addAll(this.menuBox);
    }

    private void playMusic(){
        String musicfile = "mlad.mp3";
        Media sound = new Media(new File(musicfile).toURI().toString());
        this.song = new MediaPlayer(sound);
        this.songIsPlaying = true;
        this.song.play();
    }

    private void playClick()
    {
        String musicfile = "click.mp3";
        Media sound = new Media(new File(musicfile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    @Override
    public void start(final Stage primaryStage) {
        addPicture();
        setButtonClicks();
        playMusic();

        Scene scene = new Scene(root, WIDTH, HEIGHT);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE){
                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), this.menuBox);
                if (this.menuBox.isVisible())
                {
                    fadeTransition.setFromValue(1);
                    fadeTransition.setToValue(0);
                    fadeTransition.setOnFinished(event1 -> this.menuBox.setVisible(false));
                    fadeTransition.play();
                    playClick();
                }

                else{
                    fadeTransition.setFromValue(0);
                    fadeTransition.setToValue(1);
                    fadeTransition.play();
                    playClick();
                    this.menuBox.setVisible(true);
                }
            }
        });

        primaryStage.setTitle("MinesweeperMenu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private static class MenuItem extends StackPane{
        private  MenuItem(String name){
            Rectangle backGround = new Rectangle(140, 40, Color.BLACK);
            backGround.setOpacity(0.5);

            Text text = new Text(name);
            text.setFill(Color.WHITE);
            text.setFont(Font.font("Times New Roman", FontWeight.BOLD,15));

            setAlignment(Pos.CENTER);
            getChildren().addAll(backGround, text);
            FillTransition styleClick = new FillTransition(Duration.seconds(0.5), backGround);
            setOnMouseEntered(event -> {
                styleClick.setFromValue(Color.DARKGRAY);
                styleClick.setToValue(Color.DARKGOLDENROD);
                styleClick.setCycleCount(Animation.INDEFINITE);
                styleClick.setAutoReverse(true);
                styleClick.play();
            });

            setOnMouseExited(event -> {
                styleClick.stop();
                backGround.setFill(Color.BLACK);
            });
        }
    }

    private static class MenuBox extends Pane{
        static SubMenu subMenu;
        private MenuBox(SubMenu subMenu){
            MenuBox.subMenu = subMenu;

            setVisible(false);
            Rectangle backGround = new Rectangle(WIDTH, HEIGHT, Color.DARKGRAY);
            backGround.setOpacity(0.4);
            getChildren().addAll(backGround, subMenu);
        }
        private void setSubMenu(SubMenu subMenu){
            getChildren().remove(MenuBox.subMenu);
            MenuBox.subMenu = subMenu;
            getChildren().add(MenuBox.subMenu);
        }
    }

    private static class SubMenu extends VBox{
        private SubMenu(MenuItem...items){
            setSpacing(15);
            setTranslateY(150);
            setTranslateX(140);
            for(MenuItem item : items){
                getChildren().addAll(item);
            }
        }
    }
}
