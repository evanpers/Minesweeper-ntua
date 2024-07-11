import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePlay {
    private final Tile[][] gridTile = new Tile[50][50];
    private static GameOptions loadedScenario = new GameOptions();
    private static boolean gameOn = false;
    private static int rightClicks;
    private static final IntegerProperty minesChecked = new SimpleIntegerProperty(0);
    private static final IntegerProperty emptyTiles = new SimpleIntegerProperty(0);
    Timeline timeline;

    BorderStroke[] borderStrokes = new BorderStroke[]{
            new BorderStroke(Color.GRAY,
                    BorderStrokeStyle.SOLID,
                    new CornerRadii(0.0),
                    new BorderWidths(3.0))
    };
    public static int getEmptyTiles(){
        return emptyTiles.get();
    }
    public static void setEmptyTiles(int value){
        emptyTiles.set(value);
    }
    public static int getMinesChecked() {
        return minesChecked.get();
    }

    public static void setMinesChecked(int value) {
        minesChecked.set(value);
    }

    public static void setLoadedScenario(GameOptions game){
        GamePlay.loadedScenario = game;
    }
    public static GameOptions getLoadedScenario(){
        return loadedScenario;
    }

    private List<Tile> getNeighbors(Tile tile) {
        List<Tile> neighbors = new ArrayList<>();

        // ttt
        // tXt
        // ttt

        int[] points = new int[] {
                -1, -1,
                -1, 0,
                -1, 1,
                0, -1,
                0, 1,
                1, -1,
                1, 0,
                1, 1
        };

        for (int i = 0; i < points.length; i++) {
            int dx = points[i];
            int dy = points[++i];

            int newX = tile.x + dx;
            int newY = tile.y + dy;

            if (newX >= 0 && newX < loadedScenario.sizeOfBoard()
                    && newY >= 0 && newY < loadedScenario.sizeOfBoard()) {
                neighbors.add(gridTile[newX][newY]);
            }
        }
        return neighbors;
    }

    private class Tile extends StackPane {
        private final int x;
        private final int y;
        private final boolean hasMine;
        private boolean hasHyperMine;
        private boolean isOpen = false;
        private boolean isMarked = false;
        private final Rectangle bottom = new Rectangle(24, 24);
        private final Rectangle top = new Rectangle(24, 24);
        private final Text text = new Text();


        public void setHasHyperMine(boolean hasHyperMine) {
            this.hasHyperMine = hasHyperMine;
        }

        public void setBottom(Squares square){
            bottom.setFill(new ImagePattern(Graphs.getImage(square)));
        }
        public void setBottom(Image img){
            bottom.setFill(new ImagePattern(img));
        }
        public Tile(int x, int y, boolean hasMine) {
            this.x = x;
            this.y = y;
            this.hasMine = hasMine;

            bottom.setStroke(Color.GRAY);
            top.setFill(new ImagePattern(Graphs.getImage(Squares.BLANK)));
            bottom.setFill(new ImagePattern(Graphs.getImage(Squares.EXPOSED)));
            getChildren().addAll(bottom, top);

            setOnMouseClicked(e -> {
                if (gameOn){
                    if (e.getButton() == MouseButton.PRIMARY) {
                        if (!isOpen) {
                            if (hasMine) {
                                bottom.setFill(new ImagePattern(Graphs.getImage(Squares.HITMINE)));
                                openAll();
                                gameOver(false, "You hit mine GAME OVER");
                            }
                            else {
                                rightClicks++;
                                open();
                            }
                        }
                    } else if (e.getButton() == MouseButton.SECONDARY) {
                            if (!isOpen) {
                                if (isMarked) {
                                    top.setFill(new ImagePattern(Graphs.getImage(Squares.BLANK)));
                                    setMinesChecked(getMinesChecked() - 1);
                                    isMarked = false;
                                } else if (getMinesChecked() < loadedScenario.getMines()) {
                                    setMinesChecked(getMinesChecked() + 1);
                                    if (hasHyperMine) {
                                        hyperMarked();
                                        return;
                                    }
                                    top.setFill(new ImagePattern(Graphs.getImage(Squares.FLAG)));
                                    isMarked = true;
                                }
                            }
                    }
                }
            });
        }

        public void open() {
            if (isOpen) return;
            isOpen = true;
            if(isMarked) setMinesChecked(getMinesChecked() - 1);
            bottom.toFront();
            setEmptyTiles(getEmptyTiles() - 1);
            if (text.getText().isEmpty()) {
                getNeighbors(this).forEach(Tile::open);
            }
        }

        public void openAll(){
            for(int i = 0; i < loadedScenario.sizeOfBoard(); i++) {
                for (int j = 0; j < loadedScenario.sizeOfBoard(); j++) {
                    gridTile[i][j].bottom.toFront();
                    gridTile[i][j].isOpen = true;
                }
            }
        }
        public void openOnHyper(){
            isOpen = true;
            if (hasMine) {
                top.setFill(new ImagePattern(Graphs.getImage(Squares.FLAG)));
                isMarked = true;
                isOpen = true;
                setMinesChecked(getMinesChecked() + 1);
                return;
            }
            bottom.toFront();
            setEmptyTiles(getEmptyTiles() - 1);
        }

        public void hyperMarked() {
            isMarked = true;
            if (rightClicks>4){
                top.setFill(new ImagePattern(Graphs.getImage(Squares.FLAG)));
                return;
            }
            isOpen = true;
            top.setFill(Color.RED);
            for(int i = 0; i < 16; i++){
                if(i == y) continue;
                gridTile[x][i].openOnHyper();
            }
            for(int i = 0; i < 16; i++){
                if(i == x) continue;
                gridTile[i][y].openOnHyper();
            }
        }

    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    public VBox startGame(String path) {
        //initialize game values
        gameOn = true;
        setEmptyTiles((loadedScenario.sizeOfBoard()*loadedScenario.sizeOfBoard())-loadedScenario.getMines());
        setMinesChecked(0);
        rightClicks = 0;
        int[] timeRemaining = {loadedScenario.getTime()};
        //info bar label total mines
        Label totalMines = new Label();
        totalMines.setText("  "+loadedScenario.getMines());
        totalMines.setFont(new Font(15));
        totalMines.setTextFill(Color.RED);
        //info bar label time remaining
        Label timeLabel = new Label();
        timeLabel.setFont(new Font(15));
        timeLabel.setTextFill(Color.RED);
        timeLabel.setText(formatTime(timeRemaining[0]));
        //info bar label mines marked
        Label markedMines = new Label();
        markedMines.setText(getMinesChecked()+"  ");
        markedMines.setFont(new Font(15));
        markedMines.setTextFill(Color.RED);
        //frames for labels
        Rectangle frameTotalMines = new Rectangle(40,30, Color.BLACK);
        frameTotalMines.setStrokeType(StrokeType.INSIDE);
        frameTotalMines.setStrokeWidth(2);
        frameTotalMines.setStroke(Color.WHITE);

        Rectangle frameTime = new Rectangle(60,30, Color.BLACK);
        frameTime.setStrokeType(StrokeType.INSIDE);
        frameTime.setStrokeWidth(2);
        frameTime.setStroke(Color.WHITE);

        Rectangle frameMarkedMines = new Rectangle(40,30, Color.BLACK);
        frameMarkedMines.setStrokeType(StrokeType.INSIDE);
        frameMarkedMines.setStrokeWidth(2);
        frameMarkedMines.setStroke(Color.WHITE);

        //info bar
        StackPane infoPane = new StackPane();
        infoPane.setStyle("-fx-background-color: lightgray;");
        infoPane.setPadding(new Insets(4));
        infoPane.setBorder(new Border(borderStrokes));
        infoPane.getChildren().addAll(frameTotalMines, totalMines, frameTime, timeLabel, frameMarkedMines, markedMines);

        StackPane.setAlignment(frameTotalMines, Pos.CENTER_LEFT);
        StackPane.setAlignment(totalMines, Pos.CENTER_LEFT);
        StackPane.setAlignment(frameTime, Pos.CENTER);
        StackPane.setAlignment(timeLabel, Pos.CENTER);
        StackPane.setAlignment(frameMarkedMines, Pos.CENTER_RIGHT);
        StackPane.setAlignment(markedMines, Pos.CENTER_RIGHT);

        GridPane gameBoard = new GridPane();
        //create minefield and mines.txt file
        boolean[][] bombLocation = new boolean[loadedScenario.sizeOfBoard()][loadedScenario.sizeOfBoard()];
        Random random = new Random();
        int hyperRow = -1, row;
        int hyperCol = -1, col;
        File minesFile = new File(path + "mines.txt");
        boolean flagHyperMine = false;
        try(BufferedWriter bwMines = new BufferedWriter(new FileWriter(minesFile))) {
            for (int i = 0; i < loadedScenario.getMines(); i++) {
                do {
                    row = random.nextInt(loadedScenario.sizeOfBoard());
                    col = random.nextInt(loadedScenario.sizeOfBoard());
                } while (bombLocation[row][col]);
                if (loadedScenario.getHypermine() == 1 && !flagHyperMine){
                    bombLocation[row][col] = true;
                    hyperRow = row;
                    hyperCol = col;
                    flagHyperMine = true;
                    bwMines.write(col + "," + row + "," + "1" + "\n");
                    continue;
                }
                bombLocation[row][col] = true;
                bwMines.write(col + "," + row + "," + "0" + "\n");
            }
        }catch(IOException a){
            System.out.println(a);
        }
        //add the tiles to the scene according the minefield from bombLocation
        for (row = 0; row < loadedScenario.sizeOfBoard(); row++) {
            for (col = 0; col < loadedScenario.sizeOfBoard(); col++) {
                Tile tile = new Tile(row, col, bombLocation[row][col]);
                if (hyperRow == row && hyperCol == col) tile.setHasHyperMine(true);
                if (tile.hasMine || tile.hasHyperMine) tile.setBottom(Squares.MINE);
                gridTile[row][col] = tile;
                gameBoard.add(tile,row,col);
            }
        }
        //set numbers according mines
        for (row = 0; row < loadedScenario.sizeOfBoard(); row++) {
            for (col = 0; col < loadedScenario.sizeOfBoard(); col++) {
                Tile tile = gridTile[row][col];

                if (tile.hasMine)
                    continue;

                long bombs = getNeighbors(tile).stream().filter(t -> t.hasMine).count();

                if (bombs > 0)
                {
                    tile.text.setText(String.valueOf(bombs));
                    tile.setBottom(Graphs.getDigit((int)bombs));
                }
            }
        }
        VBox gameContainer = new VBox();
        gameContainer.setBorder(new Border(borderStrokes));
        gameContainer.getChildren().addAll(infoPane,gameBoard);
        //time action
        timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> {
            timeRemaining[0]--;
            timeLabel.setText(formatTime(timeRemaining[0]));
            if (timeRemaining[0] == 0) {
               gameOver(false, "Time is Up GAME OVER");
               gridTile[0][0].openAll(); //we use an element of gridtile to gain access openAll() method
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        //win action
        emptyTiles.addListener(((observableValue, oldValue, newValue) -> {
            if(getEmptyTiles() == 0) {
                setEmptyTiles(1);
                gameOver(true, "Congratulations you won!!!");
            }
        }));
        //mines marked action
        minesChecked.addListener(((observableValue, oldValue, newValue) -> markedMines.setText(newValue + " ")));

        return gameContainer;
    }

    public void gameOver(boolean isPlayerWinner,String msg){
        timeline.stop();
        gameOn = false;
        Info completed = new Info(loadedScenario, rightClicks ,isPlayerWinner);
        Info.addToHistory(completed);
        Graphs.popUpMessage(msg);
    }

    public void solution(){
        if(!gameOn) {
            Graphs.popUpMessage("Please start a Game");
            return;
        }
        timeline.stop();
        gameOn = false;
        gridTile[0][0].openAll();
        Info completed = new Info(loadedScenario, rightClicks ,false);
        Info.addToHistory(completed);
    }

}
