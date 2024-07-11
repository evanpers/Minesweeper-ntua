import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

/**
 * Program's class that implement the Rounds action of the Details menu bar.
 * With the Rounds action we see information for the last five games such as
 * number of bombs, number of tries, the available time and the winner.
 * @author Vaggelis Persopoulos
 * @version 1.0
 */
public class Info {
    private final GameOptions game;
    private final int tries;
    private final boolean winner;
    private final static List<Info> last5Games = new ArrayList<>(5);

    /**
     * Constructor of the Info class.
     * @param game GameOptions class instance with the parameters of the loaded scenario
     * @param rightClicks number of successful right clicks
     * @param winner indicates if player won the game
     */
    public Info(GameOptions game, int rightClicks, boolean winner){
        this.game = game;
        tries = rightClicks;
        this.winner = winner;
    }

    /**
     * Method that manage the last5games ArrayList. The size of the list mustn't
     * be larger than 5. So every time checks the size and remove the last elements
     * if it is necessary.
     * @param game the Info class instance to be added in the list
     */
    public static void addToHistory(Info game){
        if(last5Games.size() == 5) last5Games.remove(0);
        last5Games.add(game);
    }

    /**
     * Method that creates a new window with the information of
     * the last 5 games. The method exports array from the last5games
     * ArrayList and display information in a new window with grid pane
     * arrangement.
     */
    public static void popUpRounds(){
        if(last5Games.isEmpty()) {
            Graphs.popUpMessage("No data history");
            return;
        }
        Info[] array = new Info[5];
        last5Games.toArray(array);
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(8));
        grid.setHgap(5);
        grid.setVgap(5);
        //1st row
        Label title = new Label("5 Last Games Summary");
        Font font = Font.font("Arial",FontWeight.BOLD,14);
        title.setFont(font);
        grid.add(title, 0,0);
        GridPane.setColumnSpan(title, 4);
        GridPane.setHalignment(title, HPos.CENTER);
        GridPane.setValignment(title, VPos.CENTER);


        Label lblMines = new Label("Mines");
        lblMines.setFont(new Font(14));
        lblMines.setUnderline(true);

        Label ldlTries = new Label("Tries");
        ldlTries.setFont(new Font(14));
        ldlTries.setUnderline(true);

        Label ldlTime = new Label("Time");
        ldlTime.setFont(new Font(14));
        ldlTime.setUnderline(true);

        Label ldlWinner = new Label("Winner");
        ldlWinner.setFont(new Font(14));
        ldlWinner.setUnderline(true);

        grid.add(lblMines, 0,1);
        GridPane.setHalignment(lblMines, HPos.CENTER);
        GridPane.setValignment(lblMines, VPos.CENTER);
        grid.add(ldlTries, 1,1);
        GridPane.setHalignment(ldlTries, HPos.CENTER);
        GridPane.setValignment(ldlTries, VPos.CENTER);
        grid.add(ldlTime, 2,1);
        GridPane.setHalignment(ldlTime, HPos.CENTER);
        GridPane.setValignment(ldlTime, VPos.CENTER);
        grid.add(ldlWinner, 3,1);
        GridPane.setHalignment(ldlWinner, HPos.CENTER);
        GridPane.setValignment(ldlWinner, VPos.CENTER);
        for (int i = 1; i<= last5Games.size(); i++){
            Label lblM = new Label(array[i-1].game.getMines()+"");
            Label lblTr = new Label(array[i-1].tries+"");
            Label lblT = new Label(array[i-1].game.getTime()+"");
            Label lblW = new Label();
            if (array[i-1].winner) lblW.setText("Player");
            else lblW.setText("COM");
            grid.add(lblM, 0, i+1);
            GridPane.setHalignment(lblM, HPos.CENTER);
            GridPane.setValignment(lblM, VPos.CENTER);
            grid.add(lblTr, 1, i+1);
            GridPane.setHalignment(lblTr, HPos.CENTER);
            GridPane.setValignment(lblTr, VPos.CENTER);
            grid.add(lblT, 2, i+1);
            GridPane.setHalignment(lblT, HPos.CENTER);
            GridPane.setValignment(lblT, VPos.CENTER);
            grid.add(lblW, 3, i+1);
            GridPane.setHalignment(lblW, HPos.CENTER);
            GridPane.setValignment(lblW, VPos.CENTER);
        }
        Button btn = new Button("Close");
        grid.add(btn, 0, 7);
        GridPane.setColumnSpan(btn, 4);
        GridPane.setHalignment(btn, HPos.CENTER);
        GridPane.setValignment(btn, VPos.CENTER);

        grid.setBorder(new Border(new BorderStroke(Color.GRAY,
                BorderStrokeStyle.SOLID,
                new CornerRadii(0.0),
                new BorderWidths(3.0))));
        Scene scene = new Scene(grid);
        Stage stage = new Stage();
        Modality modality = Modality.APPLICATION_MODAL;
        stage.initModality(modality);
        btn.setOnAction(e -> stage.close());
        stage.setScene(scene);
        stage.show();
    }
}



