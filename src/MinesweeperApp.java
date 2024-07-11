import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;

/**
 * Program's main entry point
 * @author Vaggelis Persopoulos
 */
public class MinesweeperApp extends Application {
    private final String path = ("C:\\Users\\vagge\\Java_Projects\\Minesweeper\\medialab\\");
    //index of the main window
    MenuBar menuBar = new MenuBar();
    HBox menuPane = new HBox();
    //Application menu
    Menu menuApplications = new Menu("Application");
    MenuItem menuItemStart = new MenuItem("Start");
    MenuItem menuItemCreate = new MenuItem("Create");
    MenuItem menuItemLoad = new MenuItem("Load");
    MenuItem menuItemExit = new MenuItem("Exit");
    //Details menu
    Menu menuDetails = new Menu("Details");
    MenuItem menuItemRounds = new MenuItem("Rounds");
    MenuItem menuItemSolution = new MenuItem("Solution");
    BorderStroke[] borderStrokes = new BorderStroke[]{
            new BorderStroke(Color.GRAY,
                    BorderStrokeStyle.SOLID,
                    new CornerRadii(0.0),
                    new BorderWidths(2.0))
    };

    @Override
    public void start(Stage stage){
        GamePlay gamePlay = new GamePlay();
        //Application menu
        menuApplications.getItems().addAll(menuItemCreate, menuItemLoad, menuItemStart, menuItemExit);

        //Details menu
        menuDetails.getItems().addAll(menuItemRounds, menuItemSolution);

        //index of the main window
        menuBar.getMenus().addAll(menuApplications, menuDetails);
        menuPane.getChildren().add(menuBar);
        menuPane.setStyle("-fx-background-color: lightgray;");
        menuPane.setBorder(new Border(borderStrokes));

        //creation of the main window
        Scene scene = new Scene(menuPane, 300, 200);
        menuBar.setPrefWidth(scene.getWidth());

        //Applications Actions
        menuItemCreate.setOnAction(e -> GameOptions.popUpCreate(path));
        menuItemLoad.setOnAction(e -> Graphs.popUpLoad(path));
        menuItemStart.setOnAction(e -> {
            if(GamePlay.getLoadedScenario().getMines() == 0) {
                Graphs.popUpMessage("Please load a game scenario");
                return;
            }
            VBox container = new VBox();
            container.getChildren().addAll(menuPane,gamePlay.startGame(path));
            Scene gameScene = new Scene(container);
            menuPane.setPrefWidth(container.getWidth());
            gameScene.widthProperty().addListener((observableValue, oldValue, newValue) ->
                    menuBar.setPrefWidth(gameScene.getWidth()));
            stage.setScene(gameScene);
        });
        menuItemExit.setOnAction(e -> stage.close());

        //Details Actions
        menuItemRounds.setOnAction(e -> Info.popUpRounds());
        menuItemSolution.setOnAction(e -> gamePlay.solution());
        stage.setScene(scene);
        stage.setTitle("MediaLab Minesweeper");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}