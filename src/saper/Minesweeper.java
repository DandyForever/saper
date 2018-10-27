package saper;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Minesweeper implements Runnable {
    private static final int SIZE = 30;
    private static int WIDTH;
    private static int HEIGHT;
    private static int RAWS;
    private static int COLS;
    private static int BOMBS;

    public static void setWIDTH(int WIDTH) {
        Minesweeper.WIDTH = WIDTH;
    }

    public static void setHEIGHT(int HEIGHT) {
        Minesweeper.HEIGHT = HEIGHT;
    }

    public static void setRAWS(int RAWS) {
        Minesweeper.RAWS = RAWS;
    }

    public static void setCOLS(int COLS) {
        Minesweeper.COLS = COLS;
    }

    public static void setBOMBS(int BOMBS) {
        Minesweeper.BOMBS = BOMBS;
    }


    public void numbers(FXCell[][] cell_) {
        FXCell[][] cells = cell_;

        for (int i = 0; i < RAWS; i++) {
            for (int j = 0; j < COLS; j++) {
                int neighbours = 0;

                if (i != 0 && j != 0) {
                    neighbours = neighbours + cells[i - 1][j].isBomb() + cells[i][j - 1].isBomb() + cells[i - 1][j - 1].isBomb();
                    cells[i][j].around.add(cells[i - 1][j]);
                    cells[i][j].around.add(cells[i][j - 1]);
                    cells[i][j].around.add(cells[i - 1][j - 1]);
                } else {
                    if (i != 0) {
                        neighbours = neighbours + cells[i - 1][j].isBomb();
                        cells[i][j].around.add(cells[i - 1][j]);
                    } else if (j != 0) {
                        neighbours = neighbours + cells[i][j - 1].isBomb();
                        cells[i][j].around.add(cells[i][j - 1]);
                    }
                }

                if (i != RAWS - 1 && j != 0) {
                    neighbours = neighbours + cells[i + 1][j - 1].isBomb() + cells[i + 1][j].isBomb();
                    cells[i][j].around.add(cells[i + 1][j - 1]);
                    cells[i][j].around.add(cells[i + 1][j]);
                } else {
                    if (i != RAWS - 1) {
                        neighbours = neighbours + cells[i + 1][j].isBomb();
                        cells[i][j].around.add(cells[i + 1][j]);
                    }
                }

                if (i != RAWS - 1 && j != COLS - 1) {
                    neighbours = neighbours + cells[i + 1][j + 1].isBomb() + cells[i][j + 1].isBomb();
                    cells[i][j].around.add(cells[i + 1][j + 1]);
                    cells[i][j].around.add(cells[i][j + 1]);
                } else {
                    if (j != COLS - 1) {
                        neighbours = neighbours + cells[i][j + 1].isBomb();
                        cells[i][j].around.add(cells[i][j + 1]);
                    }
                }

                if (i != 0 && j != COLS - 1) {
                    neighbours = neighbours + cells[i - 1][j + 1].isBomb();
                    cells[i][j].around.add(cells[i - 1][j + 1]);
                }



                cells[i][j].setMode(neighbours);

                cells[i][j].setNeighbours(neighbours);

                cells[i][j].text.setFont(Font.font(18));
                cells[i][j].text.setText(String.valueOf(neighbours));
                cells[i][j].text.setVisible(false);
                if (cells[i][j].isBomb() == 1) {
                    cells[i][j].setMode(9);
                    cells[i][j].setNeighbours(9);
                }

                System.out.printf("%d(%d) ", cells[i][j].getMode(), cells[i][j].getNeighbours());
            }
            System.out.printf("\n");
        }

    }

    @Override
    public void run() {
        Pane pane = new Pane();
        Scene scene = new Scene(pane, WIDTH, HEIGHT);
        Stage stage = new Stage();
        stage.setTitle("saper.Minesweeper Game");
        stage.setScene(scene);
        stage.show();

        int bombs = 0;

        FXCell[][] cells = new FXCell[RAWS][COLS];

        FXCell status = new FXCell();
        status.text = new Text("Play the Game!");
        Rectangle field = new Rectangle(WIDTH, SIZE);
        status.setTranslateX(0);
        status.setTranslateY(0);
        field.setFill(Color.AQUA);
        status.setStatus(status);
        status.getChildren().addAll(field, status.text);
        pane.getChildren().addAll(status);

        int bb = 0;
        for (int i = 0; i < RAWS; i++) {
            for (int j = 0; j < COLS; j++) {
                FXCell cell = new FXCell();

                cell.setBOMBS(BOMBS);
                cell.setRAWS(RAWS);
                cell.setCOLS(COLS);
                cell.setHEIGHT(HEIGHT);
                cell.setWIDTH(WIDTH);

                cell.setX(i);
                cell.setY(j);

                if (RAWS * COLS - RAWS * i - j == BOMBS - bombs) {
                    cell.setBomb(true);
                    bombs++;
                } else if (bombs < BOMBS) {
                    boolean isBomb = Math.random() < 0.15;
                    if (isBomb) {
                        cell.setBomb(true);
                        bombs++;
                    } else
                        cell.setBomb(false);
                }

                cells[i][j] = cell;

                if (cells[i][j].isBomb() == 1) {
                    bb++;
                }

                cells[i][j].setStatus(status);

                cells[i][j].text = new Text();

                cells[i][j].cell = new Rectangle(SIZE - 1, SIZE - 1);
                cells[i][j].cell.setFill(Color.GRAY);
                cells[i][j].getChildren().addAll(cells[i][j].cell, cells[i][j].text);
                cells[i][j].setTranslateX(i * SIZE);
                cells[i][j].setTranslateY(j * SIZE + SIZE);
                pane.getChildren().addAll(cells[i][j]);
                cells[i][j].setOnMouseClicked(cells[i][j].mouseEvent);
            }
        }

        System.out.println(bb);

        numbers(cells);
    }
}