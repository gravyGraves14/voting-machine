/**
 * Author: Raju Nayak
 */

package edu.unm.gui;

import edu.unm.dao.ElectionGremlinDAO;
import edu.unm.entity.Ballot;
import edu.unm.entity.ElectionReport;
import edu.unm.service.BallotScanner;
import edu.unm.service.ReportPrinter;
import javafx.animation.RotateTransition;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ResultGUI {
    private int totalVotes = 0;
    private Label voteCountLabel;
    private Label timeLabel;
    private GUIUtils guiUtils = new GUIUtils();
    private GridPane root;
    private Scene scene;
    private Stage dialog;
    private Label scanningLabel;
    private StackPane scanningPane;
    private Circle scanningCircle;
    private Line scanningLine;

    private ElectionGremlinDAO dao;

    public ResultGUI(Scene scene) {
        this.scene = scene;

        this.dao = new ElectionGremlinDAO();
        // Set up the layout
        root = guiUtils.createRoot(4, 3);

        // Initialize scanning dialog
        initializeScanningDialog();

        voteCountLabel = new Label("Total Votes: \n" + totalVotes);
        guiUtils.createLabel(voteCountLabel, 250, 100, 25);
        root.add(voteCountLabel, 2, 0);

        timeLabel = new Label("Time: " + getCurrentTime());
        guiUtils.createLabel(timeLabel, 250, 50, 25);
        root.add(timeLabel, 2, 1);

        // Tabulate Final Result Button
        Button finalResultButton = new Button("Tabulate Final Result");
        guiUtils.createBtn(finalResultButton, 300, 100, 25);
        finalResultButton.setOnAction(e -> startScanAnimation());
        root.add(finalResultButton, 1, 1);

        // Calculate Overall Result Button
        Button printResultButton = new Button("Print Result");
        guiUtils.createBtn(printResultButton, 300, 100, 25);
        printResultButton.setOnAction(e -> {
            printAndTabulateResult();
        });
        root.add(printResultButton, 1, 2);
    }

    public GridPane getRoot() {
        return root;
    }

    private void validateBallot() {

        dialog.close();
        boolean isValid = false;
        printAndTabulateResult();
        if (totalVotes > 0) {
            isValid = true;
        }
        Alert alert = new Alert(isValid ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        if (isValid) {
            alert.setContentText("All Vote has been counted.");
        } else {
            alert.setContentText("Tabulation Failed as vote is Empty");
        }
        // Update the GUI with the new vote count and time
        updateGUI();
        alert.show();
    }

    private void printAndTabulateResult() {
        try {
            Ballot ballot = BallotScanner.getBallot();
            ElectionReport electionReport = new ElectionReport(ballot.getSchemaName());

            // Print the report to a file using ReportPrinter
            ReportPrinter.print(electionReport);
            totalVotes = electionReport.getTotalVotes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void updateGUI() {
        voteCountLabel.setText("Total Votes: " + totalVotes);
        timeLabel.setText("Time: " + getCurrentTime());
    }

    private String getCurrentTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.now().format(formatter);
    }

    private void initializeScanningDialog() {
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);

        scanningPane = new StackPane();
        scanningPane.setPrefSize(200, 200);

        scanningCircle = new Circle(90);
        scanningCircle.setStroke(Color.BLACK);
        scanningCircle.setFill(Color.LIGHTCYAN);

        scanningLine = new Line();
        scanningLine.setStartX(-90);
        scanningLine.setEndX(90);
        scanningLine.setStroke(Color.GREEN);
        scanningLine.setStrokeWidth(2);

        scanningPane.getChildren().addAll(scanningCircle, scanningLine);

        // Label that stays at the center of the radar
        scanningLabel = new Label("Tabulating...");
        guiUtils.createLabel(scanningLabel, 150, 50, 25);

        scanningPane.getChildren().add(scanningLabel);

        dialog.setScene(new Scene(scanningPane));
    }

    private void startScanAnimation() {
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(3), scanningLine);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(1);
        rotateTransition.setOnFinished(event -> validateBallot());

        // Start the animation
        dialog.show();
        rotateTransition.play();
    }

    private void showSuccessPopUp(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showErrorPopUp(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
