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

    private MenuItem sound = new MenuItem("SOUND");
    private MenuItem keys = new MenuItem("CONTROLS");
    private MenuItem buttonSettingsBack = new MenuItem("BACK");
    private SubMenu optionsMenu = new SubMenu(this.sound, this.keys, this.buttonSettingsBack);

    private MenuItem newGameEasy = new MenuItem("EASY");
    private MenuItem newGameMedium = new MenuItem("MEDIUM");
    private MenuItem newGameHard = new MenuItem("HARD");
    private MenuItem newGameButtonBack = new MenuItem("BACK");
    private SubMenu newGameMenu = new SubMenu(this.newGameEasy, this.newGameMedium,
            this.newGameHard, this.newGameButtonBack);

    private MenuBox menuBox = new MenuBox(mainMenu);

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
        this.newGame.setOnMouseClicked(event -> this.menuBox.setSubMenu(this.newGameMenu));
        this.settings.setOnMouseClicked(event -> this.menuBox.setSubMenu(this.optionsMenu));
        this.exit.setOnMouseClicked(event -> System.exit(0));

        this.buttonSettingsBack.setOnMouseClicked(event -> this.menuBox.setSubMenu(this.mainMenu));

        this.newGameButtonBack.setOnMouseClicked(event -> this.menuBox.setSubMenu(this.mainMenu));

        this.newGameEasy.setOnMouseClicked(event -> {System.out.printf("%s", "You have choosed easy level\n");
        });

        this.newGameMedium.setOnMouseClicked(event -> {System.out.printf("%s", "You have choosed medium level\n");
        });

        this.newGameHard.setOnMouseClicked(event -> {System.out.printf("%s", "You have choosed hard level\n");
        });


        Minesweeper thread = new Minesweeper();
        this.newGameEasy.setOnMouseClicked(event -> {
            thread.setBOMBS(12);
            thread.setCOLS(10);
            thread.setRAWS(10);
            thread.setHEIGHT(300);
            thread.setWIDTH(300);
            thread.run();});

        this.newGameMedium.setOnMouseClicked(event -> {
            thread.setBOMBS(30);
            thread.setCOLS(15);
            thread.setRAWS(15);
            thread.setHEIGHT(450);
            thread.setWIDTH(450);
            thread.run();});

        this.newGameHard.setOnMouseClicked(event -> {
            thread.setBOMBS(50);
            thread.setCOLS(20);
            thread.setRAWS(20);
            thread.setHEIGHT(600);
            thread.setWIDTH(600);
            thread.run();
        });

        root.getChildren().addAll(this.menuBox);
    }


    @Override
    public void start(final Stage primaryStage) {
        addPicture();
        setButtonClicks();
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
                }

                else{
                    fadeTransition.setFromValue(0);
                    fadeTransition.setToValue(1);
                    fadeTransition.play();
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
            Rectangle backGround = new Rectangle(100, 40, Color.BLACK);
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
            setTranslateX(160);
            for(MenuItem item : items){
                getChildren().addAll(item);
            }
        }
    }
}
