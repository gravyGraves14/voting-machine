package edu.unm.gui;

import edu.unm.dao.DAOFactory;
import edu.unm.dao.ElectionGremlinDAO;
import edu.unm.dao.ElectorDAO;
import edu.unm.entity.*;
import edu.unm.service.ElectionSetupScanner;
import edu.unm.service.UserService;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GevGUI {

    private final GridPane root;
    private final GUIUtils guiUtils = new GUIUtils();
    private final Scene scene;
    private int evType = 0;
    private String voterId;

    private String questionShort;
    private String question;
    private QuestionType type;

    private String schemaName = "test-schema.xml";
    private Ballot ballot = null;

    private Elector elector;
    public GevGUI(Scene scene) {
        this.scene = scene;

        //Set up the layout
        root = guiUtils.createRoot(4, 3);

        //Create gui items
        Label idLabel = new Label("SSN:");
        PasswordField idField = new PasswordField();
        Button enterBtn = new Button("Enter");

        guiUtils.createLabel(idLabel, 250, 100, 25);
        guiUtils.createPasswordField(idField, 250, 100);
        guiUtils.createBtn(enterBtn, 250, 100, 25);

        root.add(idLabel, 1, 1);
        root.add(idField, 1, 2);
        root.add(enterBtn, 2, 3);

        //Button actions
        enterBtn.setOnAction(event -> {
            voterId = idField.getText();
            idField.setText("");

            ElectorDAO electorDAO = DAOFactory.create(ElectorDAO.class);
            List<Elector> allElectorList = new ArrayList<>();
            try {
                allElectorList = electorDAO.listAllElectors();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            elector = getElector(voterId, allElectorList);

            if (elector == null){
                showPopup("Not Registered", "You have not registered to vote.");
                return;
            }

            if (!elector.isQualifiedToVote()){
                showPopup("Ineligible Voter", "You must be at least 18 years to vote.");
                return;
            }

            if (elector == null){
                showPopup("Not Registered", "You have not yet registered.");
            }

            if (elector.getVoted() == 1){
                showPopup("Already Voted", "You have already voted.");
            }
            scene.setRoot(createChoiceRoot());
        });
    }

    // Creates Root with MD and EV choices
    private GridPane createChoiceRoot() {
        GridPane choiceRoot = guiUtils.createRoot(4, 3);

        //Create buttons
        Button mdBtn = new Button("Marking Device");
        Button evBtn = new Button("Electronic Voting");

        guiUtils.createBtn(mdBtn, 250, 100, 25);
        guiUtils.createBtn(evBtn, 250, 100, 25);

        choiceRoot.add(mdBtn, 1, 1);
        choiceRoot.add(evBtn, 1, 2);

        //Button actions
        mdBtn.setOnAction(event -> {
            /*
            Needs backend:
            set something internally to know if user chose md or ev
             */
            evType = 1;

            createQstnGUIs();
        });

        evBtn.setOnAction(event -> {
            evType = 2;
            createQstnGUIs();
        });

        guiUtils.addBackBtn(choiceRoot, root, 0, 0, scene, 0);
        return choiceRoot;
    }

    private void createQstnGUIs() {
      //  Questions questions = new Questions();
        BallotQuestion myQuestions = new BallotQuestion(questionShort, question, type);
        ElectionSetupScanner electionSetupScanner = new ElectionSetupScanner("test-schema.xml");
        try {
            ballot = electionSetupScanner.parseSchema();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

        //Create question roots
        String[] a = new String[0];
        QuestionGUI[] questionGUIS = new QuestionGUI[ballot.getQuestions().size()];
        for (int i = 0; i < ballot.getQuestions().size(); i++) {
            questionGUIS[i] = new QuestionGUI(i + 1, ballot.getQuestionByIndex(i).getQuestion(), ballot.getQuestions().size(),
                    ballot.getQuestionByIndex(i).getOptions());
        }

        //Back and forward arrows
        for (int i = 0; i < ballot.getQuestions().size(); i++) {
            if (i == 0) {
                guiUtils.addBackBtn(questionGUIS[i].getRoot(), createChoiceRoot(), 0, 0, scene, 0);
                guiUtils.addBackBtn(questionGUIS[i].getRoot(), questionGUIS[i + 1].getRoot(), 0, 2, scene, 1);
            }
            else if (i == ballot.getQuestions().size() - 1) {
                guiUtils.addBackBtn(questionGUIS[i].getRoot(), questionGUIS[i - 1].getRoot(), 0, 0, scene, 0);

                GridPane submitRoot = createSubmitRoot(questionGUIS);
                guiUtils.addBackBtn(submitRoot, questionGUIS[i].getRoot(), 0, 0, scene, 0)
                ;
                guiUtils.addBackBtn(questionGUIS[i].getRoot(), submitRoot, 0, 2, scene, 1);
            }
            else {
                guiUtils.addBackBtn(questionGUIS[i].getRoot(), questionGUIS[i - 1].getRoot(), 0, 0, scene, 0);
                guiUtils.addBackBtn(questionGUIS[i].getRoot(), questionGUIS[i + 1].getRoot(), 0, 2, scene, 1);
            }
        }

        scene.setRoot(questionGUIS[0].getRoot());
    }

    //Creates root with submit button
    private GridPane createSubmitRoot(QuestionGUI[] questionGUIS) {
        GridPane submitRoot = guiUtils.createRoot(3, 3);

        //Create submit button
        Button submitBtn = new Button("Submit");
        guiUtils.createBtn(submitBtn, 250, 100, 25);
        submitRoot.add(submitBtn, 1, 1);

        //Button action
        submitBtn.setOnAction(event -> {
            //Popup
            try {
                UserService.setVoted(elector);
            } catch (SQLException e) {
                return;
            }
            if(evType == 1) {
                guiUtils.createPopUp("Ballot Printed Successfully");
            }
            else {
                guiUtils.createPopUp("Ballot Printed and Logged Successfully");
            }

            /*
            Needs backend
            save results
             */
            //Get results


            for (int i = 0; i < ballot.getQuestions().size(); i++) {
                for (int j = 0; j < ballot.getQuestionByIndex(i).getOptions().size(); j++) {
                    if(Objects.equals(questionGUIS[i].getSelected(), ballot.getQuestionByIndex(i).getOptions().get(j).getOption())) {
                        ballot.getQuestionByIndex(i).getOptions().get(j).setSelected(true);
                    }
                }
            }

            ElectionGremlinDAO electionGremlinDAO = new ElectionGremlinDAO();
            electionGremlinDAO.saveBallotVotes(ballot);


            scene.setRoot(root);
        });

        guiUtils.addBackBtn(submitRoot, root, 0, 0, scene, 0);

        return submitRoot;
    }

    public static Elector getElector(String id, List<Elector> allElectors){
        for (Elector elector : allElectors) {
            if (elector.getId().equals(id)) return elector;
        }
        return null;
    }

    private void showPopup(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public GridPane getRoot() {
        return root;
    }
}
