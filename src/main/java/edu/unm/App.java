package edu.unm;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Hello world!
 *
 */
public class App extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // Window Set-up
        primaryStage.setTitle("Ohio Voting System");
        primaryStage.setMaximized(true);
        MainGUI mainGUI = new MainGUI();
        Scene scene = new Scene(mainGUI.getRoot());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
