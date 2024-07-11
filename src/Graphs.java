import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Graphs extends GamePlay{
    private static final javafx.scene.image.Image BLANK = new javafx.scene.image.Image("resources/blank.png");
    private static final javafx.scene.image.Image FLAG = new javafx.scene.image.Image("resources/flag.png");
    private static final javafx.scene.image.Image EXPOSED = new javafx.scene.image.Image("resources/exposed.png");
    private static final javafx.scene.image.Image MINE = new javafx.scene.image.Image("resources/mine.png");
    private static final javafx.scene.image.Image HITMINE = new javafx.scene.image.Image("resources/hitmine.png");
    private static final javafx.scene.image.Image[] digits = new javafx.scene.image.Image[9];

    static {
        digits[0] = EXPOSED;

        for (int i = 1; i < digits.length; i++) {
            digits[i] = new javafx.scene.image.Image(String.format("resources/number%d.png", i));
        }
    }

    public static Image getDigit(int index) {
        return digits[index];
    }

    public static Image getImage(Squares square) {
        return switch (square) {
            case BLANK -> Graphs.BLANK;
            case FLAG -> Graphs.FLAG;
            case MINE -> Graphs.MINE;
            case EXPOSED -> Graphs.EXPOSED;
            case HITMINE -> Graphs.HITMINE;
        };
    }

    public static void popUpLoad(String path){
        GameOptions game = new GameOptions();
        Stage loadWindow = new Stage();
        Modality modality = Modality.APPLICATION_MODAL;
        loadWindow.initModality(modality);
        TextField scenarioId = new TextField();
        scenarioId.setPromptText("Scenario-ID");
        HBox textBox = new HBox(scenarioId);
        textBox.setAlignment(Pos.CENTER);
        Button loadButton = new Button("Load");
        HBox buttonBox = new HBox(loadButton);
        buttonBox.setPadding(new Insets(5));
        buttonBox.setAlignment(Pos.CENTER);
        VBox containerPopup = new VBox();
        containerPopup.setFillWidth(true);
        containerPopup.setAlignment(Pos.CENTER);
        containerPopup.setSpacing(20);
        containerPopup.getChildren().addAll(textBox, buttonBox);
        loadButton.setOnAction(e3 -> {
            File scenario = new File(path + scenarioId.getText());
            try(Scanner sc = new Scanner(scenario)) {
                if (!sc.hasNextLine()) throw new InvalidDescriptionException();
                game.setLevel(sc.nextInt());
                if (!sc.hasNextLine()) throw new InvalidDescriptionException();
                game.setMines(sc.nextInt());
                if (!sc.hasNextLine()) throw new InvalidDescriptionException();
                game.setTime(sc.nextInt());
                if (!sc.hasNextLine()) throw new InvalidDescriptionException();
                game.setHypermine(sc.nextInt());
                game.validation(game);
            }catch (InvalidDescriptionException e){
                System.out.println(e);
                loadWindow.close();
                return;
            }catch (FileNotFoundException e1){
                System.out.println(e1);
                Graphs.popUpMessage("File not found");
                return;
            }
            catch (InvalidValueException e2){
                System.out.println(e2);
                loadWindow.close();
                return;
            }
            System.out.println(scenario);
            loadWindow.close();
        });
        setLoadedScenario(game);
        Scene loadScene = new Scene(containerPopup);
        loadWindow.setScene(loadScene);
        loadWindow.setMinWidth(200);
        loadWindow.setMinHeight(100);
        loadWindow.show();

    }
    public static void popUpMessage(String reason){
        Label text = new Label(reason);
        text.setFont(new Font(20));
        HBox line = new HBox(text);
        line.setAlignment(Pos.CENTER);
        line.setPadding(new Insets(10));
        Button close = new Button("Close");
        HBox buttons = new HBox(close);
        buttons.setAlignment(Pos.BASELINE_CENTER);
        buttons.setPadding(new Insets(10));
        VBox container = new VBox(line, buttons);
        Scene scene = new Scene(container);
        Stage stage = new Stage();
        Modality modality = Modality.APPLICATION_MODAL;
        stage.initModality(modality);
        close.setOnAction(e -> stage.close());
        stage.setScene(scene);
        stage.show();
    }

}
