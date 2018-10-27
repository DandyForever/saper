package saper;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;


public class FXCell extends StackPane {

    private static boolean gameOver = false;

    private final int SIZE = 30;
    private int WIDTH;
    private int HEIGHT;
    private int RAWS = WIDTH / SIZE;
    private int COLS = HEIGHT / SIZE;
    private int BOMBS;

    private static final int BOMB = 9;
    private static final int FLAG = 10;
    private static final int OPEN = 11;

    private FXCell status;

    private static int numOfOpen = 0;

    private boolean isBeen = false;

    private int x;
    private int y;
    private boolean bomb;
    public Rectangle cell;
    private int mode;
    private int neighbours;
    Text text;
    List<FXCell> around = new ArrayList<>();

    EventHandler mouseEvent = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {
            MouseButton button = event.getButton();

            if (button == MouseButton.PRIMARY) {
                openCell();
            }

            if (button == MouseButton.SECONDARY) {
                if (getMode() == OPEN)
                    return;

                if (getMode() == FLAG) {
                    setMode(neighbours);
                    System.out.println(neighbours);
                    cell.setFill(Color.GRAY);
                    return;
                }

                setMode(FLAG);
                Image image = new Image(getClass().getResourceAsStream("images/flag.png"));
                cell.setFill(new ImagePattern(image));
            }

            if (numOfOpen == RAWS * COLS - BOMBS) {
                status.text.setText("Game over! YOU WIN");
                status.text.setFill(Color.GREEN);
                gameOver = true;
                for (FXCell cell : around)
                    if (cell.getMode() != OPEN)
                        cell.openCell();
            }
        }
    };

    public void setWIDTH(int WIDTH) {
        this.WIDTH = WIDTH;
    }

    public void setHEIGHT(int HEIGHT) {
        this.HEIGHT = HEIGHT;
    }

    public void setRAWS(int RAWS) {
        this.RAWS = RAWS;
    }

    public void setCOLS(int COLS) {
        this.COLS = COLS;
    }

    public void setBOMBS(int BOMBS) {
        this.BOMBS = BOMBS;
    }

    public void openCell() {
        if (gameOver){
            isBeen = true;
        }

        if (gameOver && neighbours == BOMB) {
            Image image = new Image(getClass().getResourceAsStream("images/mine.png"));
            cell.setFill(new ImagePattern(image));
            for (FXCell cell : around) {
                if (!cell.isBeen)
                    cell.openCell();
            }
            return;
        }

        if (gameOver && getMode() != BOMB) {
            for (FXCell cell : around)
                if (!cell.isBeen)
                    cell.openCell();
            return;
        }

        if (getMode() == OPEN || getMode() == FLAG)
            return;

        if (getMode() <= 8 && getMode() > 0) {
            cell.setFill(Color.LIGHTGRAY);
            text.setVisible(true);
            setMode(OPEN);
            numOfOpen++;
            for (FXCell cell : around)
                if (cell.getMode() == 0)
                    cell.openCell();
            return;
        }

        if (getMode() == 0) {
            cell.setFill(Color.LIGHTGRAY);
            setMode(OPEN);
            numOfOpen++;
            for (FXCell cell : around)
                if (cell.getMode() != OPEN)
                    cell.openCell();
            return;
        }

        if (getMode() == BOMB) {
            Image image = new Image(getClass().getResourceAsStream("images/mine.png"));
            cell.setFill(new ImagePattern(image));
            status.text.setText("Game over! YOU LOSE");
            status.text.setFill(Color.RED);
            gameOver = true;
            isBeen = true;
            for (FXCell cell : around) {
                if (!cell.isBeen)
                    cell.openCell();
            }
            //mouseEvent = noneEvent;
            /*try {
                Thread.sleep(5000);
            } catch(InterruptedException e){
                System.out.println("Sleep failure");
            }
            System.exit (0);*/
            return;
        }
    }

    public void setStatus(FXCell status) {
        this.status = status;
    }

    public int getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(int neighbours) {
        this.neighbours = neighbours;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setX(int x) {
        this.x = x;
    }


    public void setY(int y) {
        this.y = y;
    }

    public void setBomb(boolean bomb) {
        this.bomb = bomb;
    }

    public int isBomb() {
        if (this.bomb)
            return 1;
        else
            return 0;
    }
}