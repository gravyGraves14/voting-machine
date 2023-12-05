/**
 * Author: Raju Nayak, Emely Seheon, Ester Aguilera
 */

package edu.unm.gui;
import edu.unm.dao.ElectionGremlinDAO;
import edu.unm.entity.Ballot;
import edu.unm.entity.PaperBallot;
import edu.unm.service.PaperVoteCounter;
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
 * The TabulationGUI class is responsible for providing a graphical user interface
 * for the tabulation process of voting. This includes initiating the paper ballot scanning process,
 * and tabulating results.
 */
public class TabulationGUI {
    private final Label voteCountLabel;
    private final Label timeLabel;
    private final GUIUtils guiUtils = new GUIUtils();
    private final GridPane root;
    private Stage dialog;
    private Line scanningLine;

    /**
     * Constructor for TabulationGUI.
     * Sets up the user interface, including labels and buttons for scanning and tabulating
     * paper ballots, and initializes the scanning dialog window.
     * @param scene the primary scene for this GUI component
     */
    public TabulationGUI(Scene scene) {

        // Set up the layout
        root = guiUtils.createRoot(4, 3);

        // Initialize scanning dialog
        initializeScanningDialog();

        // Creating and styling the vote count label
        voteCountLabel = new Label("Total Paper Votes: \n" + PaperVoteCounter.getCurrentVoteCount());
        guiUtils.createLabel(voteCountLabel, 250, 100, 25);
        root.add(voteCountLabel, 2, 0);

        // Creating and styling the time label
        timeLabel = new Label("Time: " + getCurrentTime());
        guiUtils.createLabel(timeLabel, 250, 50, 25);
        root.add(timeLabel, 2, 1);

        // Scan Paper Ballot Button
        Button scanBallotButton = new Button("Scan Paper Ballot");
        guiUtils.createBtn(scanBallotButton, 250, 100, 25);
        scanBallotButton.setOnAction(e -> {
            if(Configuration.isGevEnabled()){
                startScanAnimation();
            }
            else{
                // If voting is disabled, then we know voting has either ended or not begun
                // so optical scanning should not be permitted
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("FEATURE DISABLED");
                alert.setContentText("Voting has ended.");
                alert.showAndWait();
            }
        });
        root.add(scanBallotButton, 1, 1);

        // Tabulate Button
        Button calculateResultButton = new Button("Tabulate Result");
        guiUtils.createBtn(calculateResultButton, 250, 100, 25);
        calculateResultButton.setOnAction(e -> {
            if(Configuration.isGevEnabled()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("FEATURE DISABLED");
                alert.setContentText("Can not tabulate while voting in progress.");
                alert.showAndWait();
            }else{
                LoginStaffGUI loginStaffGUI = new LoginStaffGUI(scene, 1);
                guiUtils.addBackBtn(loginStaffGUI.getRoot(), root, 0 ,0, scene, 0);
                scene.setRoot(loginStaffGUI.getRoot());
            }
        });
        root.add(calculateResultButton, 1, 2);
    }

    public GridPane getRoot() {
        return root;
    }

    /**
     * Updates the GUI components, particularly the paper vote count and time labels,
     * to reflect the current total paper votes and time.
     */
    private void updateGUI() {
        voteCountLabel.setText("Total Paper Votes: \n" + PaperVoteCounter.getCurrentVoteCount());
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

        StackPane scanningPane = new StackPane();
        scanningPane.setPrefSize(200, 200);

        Circle scanningCircle = new Circle(90);
        scanningCircle.setStroke(Color.BLACK);
        scanningCircle.setFill(Color.LIGHTCYAN);

        scanningLine = new Line();
        scanningLine.setStartX(-90);
        scanningLine.setEndX(90);
        scanningLine.setStroke(Color.GREEN);
        scanningLine.setStrokeWidth(2);

        scanningPane.getChildren().addAll(scanningCircle, scanningLine);

        // Label that stays at the center of the radar
        Label scanningLabel = new Label("Scanning...");
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

    /**
     * Validates the scanned paper ballot and save the result.
     * It closes the scanning dialog, checks the validity of the ballot,
     * and updates the GUI accordingly.
     */
    private void validateBallot() {

        dialog.close();

        PaperBallot paperBallot = new PaperBallot();
        boolean isValid = false;
        try {
            isValid = paperBallot.processBallot();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Alert alert = new Alert(isValid ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        if (isValid) {
            //Clear the paper ballot
            try {
                paperBallot.createPaperBallot();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //put votes in database
            Ballot ballot = paperBallot.getBallot();
            ElectionGremlinDAO electionGremlinDAO = new ElectionGremlinDAO();
            electionGremlinDAO.saveBallotVotes(ballot);

            // Increment paper voter
            PaperVoteCounter.incrementVoteCount();

            //alert
            alert.setContentText("Valid ballot. Vote has been counted.");
        } else {
            alert.setContentText("Invalid ballot paper, please resubmit after making correction.");
        }
        updateGUI();
        alert.show();
    }
}