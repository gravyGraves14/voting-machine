/**
 * Author: Raju Nayak
 */

package edu.unm.gui;

import edu.unm.dao.ElectionGremlinDAO;
import edu.unm.entity.Ballot;
import edu.unm.entity.BallotQuestion;
import edu.unm.entity.ElectionReport;
import edu.unm.entity.QuestionOption;
import edu.unm.service.ElectionSetupScanner;
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
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ResultGUI {
    private int totalVotes = 0;
    private final Label voteCountLabel;
    private final Label timeLabel;
    private final GUIUtils guiUtils = new GUIUtils();
    private final GridPane root;
    private Stage dialog;
    private Line scanningLine;
    private final ElectionGremlinDAO dao = new ElectionGremlinDAO();

    public ResultGUI() {

        // Set up the layout
        root = guiUtils.createRoot(4, 3);

        // Initialize scanning dialog
        initializeScanningDialog();

        voteCountLabel = new Label("Total Votes: \n" + getTotalVotes());
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
            printResult();
        });
        root.add(printResultButton, 1, 2);
    }

    public GridPane getRoot() {
        return root;
    }
    private void calculateFinalResult() {
//        try {
//            ElectionGremlinDAO dao = new ElectionGremlinDAO();
//            Ballot results = dao.getTabulation("test-schema.xml");
//
//            int maxVotesForSingleOption = 0; // Variable to store the maximum votes received by any option
//
//            for (BallotQuestion question : results.getQuestions()) {
//                for (QuestionOption option : question.getOptions()) {
//                    if (option.getTotalVotes() > maxVotesForSingleOption) {
//                        maxVotesForSingleOption = (int) option.getTotalVotes(); // Update if current option has more votes
//                    }
//                }
//            }
//            System.out.println("Estimated total number of voters: " + maxVotesForSingleOption);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        ElectionSetupScanner scanner = new ElectionSetupScanner("test-schema.xml");
        try {
            Ballot ballot = scanner.parseSchema();
            dao.loadBallotSchema(ballot.getSchemaName(), ballot);

            ballot.getQuestions().forEach(q -> q.getOptions().get(1).setSelected(true));
            dao.saveBallotVotes(ballot);

            Ballot results = dao.getTabulation(ballot.getSchemaName());
            System.out.println("Results for schema: " + ballot.getSchemaName());
            for (BallotQuestion question : results.getQuestions()) {
                System.out.println("Question: " + question.getQuestion());
                for (QuestionOption option : question.getOptions()) {
                    this.totalVotes = (int)option.getTotalVotes();
                    System.out.println("\t" + option.getTotalVotes() + " - " + option.getOption());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            List<Vertex> schemaVertices = dao.findAllVerticesByLabel("schema");
//            String schemaName = null;
//            for (Vertex v : schemaVertices) {
//                schemaName = (String) v.property("name").value();
//            }
//            List<Vertex> ballots = dao.findAllVerticesByLabelWithProperty("ballot", schemaName, "");
//
//            setTotalVotes(ballots.size());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void printResult() {
        try {
            // Create an instance of ElectionReport
            List<Vertex> schemaVertices = dao.findAllVerticesByLabel("schema");
            String schemaName = null;
            for (Vertex v : schemaVertices) {
                schemaName = (String) v.property("name").value();
            }
            ElectionReport electionReport = new ElectionReport(schemaName);

            // Print the report to a file using ReportPrinter
            ReportPrinter.print(electionReport);

            // Show a success popup
            showSuccessPopUp("Print Successful", "The election results have been printed.");
        } catch (IllegalArgumentException e) {
            showErrorPopUp("Error", "Failed to generate the election report: " + e.getMessage());
        } catch (IOException e) {
            showErrorPopUp("IO Error", "Error writing the report to a file: " + e.getMessage());
        }

    }

    private void updateGUI() {
        voteCountLabel.setText("Total Votes: " + getTotalVotes());
        timeLabel.setText("Time: " + getCurrentTime());
    }

    private String getCurrentTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.now().format(formatter);
    }

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
        Label scanningLabel = new Label("Tabulating...");
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

    private void validateBallot() {

        dialog.close();
        boolean isValid = false;
        calculateFinalResult();
        if (getTotalVotes() > 0) {
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
    public int getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(int totalVotes) {
        this.totalVotes = totalVotes;
    }

}
