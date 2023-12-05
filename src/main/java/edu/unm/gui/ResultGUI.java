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

/**
 * The ResultGUI class is responsible for providing a graphical user interface
 * to tabulate the final results of an election. This class includes
 * functionality for initiating the result tabulation process, displaying the
 * total voter count, and showing the current time. It leverages the ElectionGremlinDAO
 * for data access.
 */
public class ResultGUI {
    private int totalVoter = 0;
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

    /**
     * Constructor for ResultGUI.
     * Sets up the user interface including labels and buttons for final result tabulation.
     * @param scene the primary scene for this GUI component
     */
    public ResultGUI(Scene scene) {
        this.scene = scene;

        this.dao = new ElectionGremlinDAO();
        // Set up the layout
        root = guiUtils.createRoot(4, 3);

        // Initialize scanning dialog
        initializeScanningDialog();

        voteCountLabel = new Label("Total Voter: \n" + totalVoter);
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

    }

    public GridPane getRoot() {
        return root;
    }

    /**
     * Validates the ballot count and initiates the result tabulation and printing process.
     * It closes the scanning dialog, checks the validity of the vote count,
     * and updates the GUI accordingly.
     */
    private void validateBallot() {

        dialog.close();
        boolean isValid = false;
        printAndTabulateResult();
        if (totalVoter > 0) {
            isValid = true;
        }
        Alert alert = new Alert(isValid ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        if (isValid) {
            alert.setContentText("All Vote has been counted and report has been printed");
        } else {
            alert.setContentText("Tabulation Failed as vote is Empty");
        }
        // Update the GUI with the new vote count and time
        updateGUI();
        alert.show();
    }

    /**
     * Prints and tabulates the final election results.
     * This method retrieves the current ballot, generates an election report,
     * and prints it using ReportPrinter. It also updates the total voter count.
     */
    private void printAndTabulateResult() {
        try {
            ElectionGremlinDAO electionGremlinDAO = new ElectionGremlinDAO();
            Ballot ballot = electionGremlinDAO.getBallotFromSchema(BallotScanner.getBallot().getSchemaName());
            ElectionReport electionReport = new ElectionReport(ballot.getSchemaName());

            // Print the report to a file using ReportPrinter
            ReportPrinter.print(electionReport);
            totalVoter = electionReport.getTotalVoters();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the GUI components, particularly the total voter count and time labels,
     * to reflect total voter and current time.
     */
    private void updateGUI() {
        voteCountLabel.setText("Total Voter: \n" + totalVoter);
        timeLabel.setText("Time: " + getCurrentTime());
    }

    /**
     * Gets the current time formatted as HH:mm.
     * @return String the current time
     */
    private String getCurrentTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.now().format(formatter);
    }

    /**
     * Initializes the scanning dialog, which is used during the paper ballot scanning
     * process. This includes setting up the layout and the animation elements.
     */
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

    /**
     * Starts the scanning animation when a paper ballot is being scanned.
     * This method shows the scanning dialog with an animation for visual feedback.
     */
    private void startScanAnimation() {
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(3), scanningLine);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(1);
        rotateTransition.setOnFinished(event -> validateBallot());

        // Start the animation
        dialog.show();
        rotateTransition.play();
    }

}
