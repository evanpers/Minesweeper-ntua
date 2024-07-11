import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GameOptions {
    private int level;
    private int mines;
    private int time;
    private int hypermine;

    public int sizeOfBoard(){
        if (level == 1) return 9;
        if (level == 2) return 16;
        return 0;
    }
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMines() {
        return mines;
    }

    public void setMines(int mines) {
        this.mines = mines;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getHypermine() {
        return hypermine;
    }

    public void setHypermine(int hypermine) {
        this.hypermine = hypermine;
    }

    public void validation(GameOptions test) throws InvalidValueException{
        if(test.getLevel() ==1){
            if (test.getMines() < 9 || test.getMines() > 11) throw new InvalidValueException();
            if (test.getTime() < 120 || test.getTime() > 180) throw new InvalidValueException();
            if (test.getHypermine() == 1) throw new InvalidValueException();
        }
        if(test.getLevel() ==2){
            if (test.getMines() < 35 || test.getMines() > 45) throw new InvalidValueException();
            if (test.getTime() < 240 || test.getTime() > 360) throw new InvalidValueException();
        }
    }

    public static void popUpCreate(String path){
        Stage newWindow = new Stage();

        GridPane gridPopup = new GridPane();
        gridPopup.setPadding(new Insets(6));
        gridPopup.setHgap(3);
        gridPopup.setVgap(3);

        //1st row
        Label lblID = new Label("SCENARIO-ID");
        TextField txtID = new TextField();

        gridPopup.add(lblID, 0,0);
        GridPane.setHalignment(lblID, HPos.RIGHT);
        GridPane.setValignment(lblID, VPos.CENTER);

        gridPopup.add(txtID, 1,0);
        GridPane.setHalignment(txtID, HPos.LEFT);
        GridPane.setValignment(txtID, VPos.CENTER);
        GridPane.setColumnSpan(txtID, 2);

        //2nd row
        Label lblLevel = new Label("Level");
        RadioButton level1 = new RadioButton("level 1");
        RadioButton level2 = new RadioButton("level 2");
        ToggleGroup levelGroup = new ToggleGroup();
        level1.setToggleGroup(levelGroup);
        level2.setToggleGroup(levelGroup);

        gridPopup.add(lblLevel, 0,1);
        GridPane.setHalignment(lblLevel, HPos.RIGHT);
        GridPane.setValignment(lblLevel, VPos.CENTER);

        gridPopup.add(level1, 1, 1);
        GridPane.setHalignment(level1, HPos.RIGHT);
        GridPane.setValignment(level1, VPos.CENTER);
        gridPopup.add(level2, 2, 1);
        GridPane.setHalignment(level2, HPos.RIGHT);
        GridPane.setValignment(level2, VPos.CENTER);

        //3rd row
        Label lblMines = new Label("Mines");
        TextField txtMines = new TextField();

        gridPopup.add(lblMines, 0,2);
        GridPane.setHalignment(lblMines, HPos.RIGHT);
        GridPane.setValignment(lblMines, VPos.CENTER);

        gridPopup.add(txtMines, 1,2);
        GridPane.setHalignment(txtMines, HPos.LEFT);
        GridPane.setValignment(txtMines, VPos.CENTER);
        GridPane.setColumnSpan(txtMines, 2);

        //4th row
        Label lblHyper = new Label("Hyper Mine");
        ComboBox<String> hyperMine = new ComboBox<>();
        hyperMine.getItems().addAll("Yes", "No");

        gridPopup.add(lblHyper, 0,3);
        GridPane.setHalignment(lblHyper, HPos.RIGHT);
        GridPane.setValignment(lblHyper, VPos.CENTER);

        gridPopup.add(hyperMine, 1, 3);
        GridPane.setHalignment(hyperMine, HPos.RIGHT);
        GridPane.setValignment(hyperMine, VPos.CENTER);

        //5th row
        Label lblTime = new Label("Time");
        TextField txtTime = new TextField();
        txtTime.setPromptText("time in secs");

        gridPopup.add(lblTime, 0,4);
        GridPane.setHalignment(lblTime, HPos.RIGHT);
        GridPane.setValignment(lblTime, VPos.CENTER);

        gridPopup.add(txtTime, 1,4);
        GridPane.setHalignment(txtTime, HPos.LEFT);
        GridPane.setValignment(txtTime, VPos.CENTER);
        GridPane.setColumnSpan(txtTime, 2);

        //6th row
        Button createButton = new Button("Create");
        createButton.setOnAction(e -> {
            File gameDescription = new File(path + txtID.getText());
            try(BufferedWriter bw = new BufferedWriter(new FileWriter(gameDescription))){
                if(level1.isSelected())
                    bw.write( "1" + "\n");
                else
                    bw.write( "2" + "\n");
                bw.write(txtMines.getText() + "\n");
                bw.write(txtTime.getText() + "\n");
                if(hyperMine.getSelectionModel().getSelectedItem().matches("Yes"))
                    bw.write("1" + "\n");
                else
                    bw.write("0" + "\n");
                newWindow.close();
            }
            catch (IOException e1){
                System.out.println(e1);
            }
        });

        gridPopup.add(createButton, 2, 5);
        GridPane.setHalignment(createButton, HPos.RIGHT);
        GridPane.setValignment(createButton, VPos.CENTER);

        Scene s = new Scene(gridPopup);
        Modality modality = Modality.APPLICATION_MODAL;
        newWindow.initModality(modality);
        newWindow.setScene(s);
        newWindow.show();
    }

}
