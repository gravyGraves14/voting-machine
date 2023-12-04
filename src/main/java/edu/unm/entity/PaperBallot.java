package edu.unm.entity;

import edu.unm.dao.DAOFactory;
import edu.unm.dao.ElectorDAO;
import edu.unm.dao.ElectorSQLiteDAO;
import edu.unm.service.ElectionSetupScanner;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import static edu.unm.gui.GevGUI.getElector;

//TODO: ev paper vote go into seperate txt file
//TODO: Fix duplicate votes when marking
//TODO: make md submit go to scan paper ballot

public class PaperBallot {
    private Ballot ballot;
    private final String stars = "*****************************************************************";
    private final String intro = """


            BALLOT
            Please vote for at most one option in each category.
            Place an 'x' in the brackets next to the option you select.

            """;
    private final StringBuilder currentChunk = new StringBuilder();
    private String line;

    public PaperBallot() {
        ElectionSetupScanner electionSetupScanner = new ElectionSetupScanner("test-schema.xml");

        try {
            ballot = electionSetupScanner.parseSchema();
        } catch (IOException | SAXException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public void createPaperBallot() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/papers/paperBallot.txt"));

        String ssn = "\n\nSocial Security Number: \n\n";
        writer.write(stars + intro + stars + ssn + stars + "\n\n");

        for (int i = 0; i < ballot.getQuestions().size(); i++) {
            writer.write("Question #" + (i + 1) + "\n" +
                    ballot.getQuestionByIndex(i).getQuestion() + "\n");

            for (int j = 0; j < ballot.getQuestionByIndex(i).getOptions().size(); j++) {
                writer.write("[ ] " + ballot.getQuestionByIndex(i).getOptions().get(j).getOption() + "\n");
            }

            writer.write("[ ] Other: " + "\n");

            writer.write("\n" + stars + "\n\n");
        }
        writer.close();
    }

    public boolean processBallot() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/papers/paperBallot.txt"));

        //Check the first stars
        if (!currentChunk.append(reader.readLine()).toString().equals(stars)) {
            return false;
        }

        //Check the intro
        populateCurrentChunk(reader);

        if (!currentChunk.toString().equals(intro)) {
            return false;
        }

        //Check the ssn
        populateCurrentChunk(reader);

        //check if registered
        ElectorDAO electorDAO = DAOFactory.create(ElectorDAO.class);
        List<Elector> allElectorList;
        try {
            allElectorList = electorDAO.listAllElectors();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String ssn = currentChunk.toString().replaceAll("\\D", "");
        Elector elector = getElector(ssn, allElectorList);

        if (!currentChunk.toString().matches("\n\n+Social Security Number: +\\d{9}+\n\n") || elector == null
        || !elector.isQualifiedToVote() || elector.getVoted() == 1) {
            return false;
        }

        //check the questions
        for (int i = 0; i < ballot.getQuestions().size(); i++) {
            StringBuilder correctQuestion = new StringBuilder();
            correctQuestion.append("\n\n");
            currentChunk.setLength(0);
            currentChunk.append("\n");

            //check the first part of the question
            correctQuestion.append("Question #").append(i + 1).append("\n").append(ballot.getQuestionByIndex(i).getQuestion()).append("\n");

            while (!Objects.equals(line = reader.readLine(), null) && !line.matches("\\[.*")){
                if(line.equals("")){
                    currentChunk.append("\n");
                }
                else {
                    currentChunk.append(line).append("\n");
                }
            }

            if (!currentChunk.toString().contentEquals(correctQuestion)) {
                return false;
            }

            //Check the main options of the question
            int choiceCount = 0;
            for (int j = 0; j < ballot.getQuestions().size(); j++) {
                if (line.equals("[x] " + ballot.getQuestionByIndex(i).getOptions().get(j).getOption())) {
                    if (choiceCount > 0){
                        return false;
                    }
                    choiceCount++;
                    ballot.getQuestionByIndex(i).getOptions().get(j).setSelected(true);
                }
                else if (!line.equals("[ ] " + ballot.getQuestionByIndex(i).getOptions().get(j).getOption())) {
                    return false;
                }

                line = reader.readLine();
            }

            //check other option
            String other = """
                    [ ] Other:\s

                    """;

            String otherLine = line;

            currentChunk.setLength(0);
            currentChunk.append(line);
            currentChunk.append("\n");
            while (!Objects.equals(line = reader.readLine(), stars) && !Objects.equals(line, null)){
                if(line.equals("")){
                    currentChunk.append("\n");
                }
                else {
                    currentChunk.append(line).append("\n");
                }
            }

            if (otherLine.matches("\\[x] .*")) {
                if (choiceCount > 0){
                    return false;
                }
                String[] parts = otherLine.split(": ");

                QuestionOption otherQ = new QuestionOption(parts[1], "none");
                otherQ.setSelected(true);
                ballot.getQuestionByIndex(i).getOptions().add(otherQ);
            }
            else if (!currentChunk.toString().equals(other)) {
                return false;
            }
        }
        elector.setVoted();

        //last stars
        return line.equals(stars);
    }

    private void populateCurrentChunk(BufferedReader reader) throws IOException {
        currentChunk.setLength(0);
        currentChunk.append("\n");
        while (!Objects.equals(line = reader.readLine(), stars) && !Objects.equals(line, null)){
            if(line.equals("")){
                currentChunk.append("\n");
            }
            else {
                currentChunk.append(line).append("\n");
            }
        }
    }

    public Ballot getBallot() {
        return ballot;
    }


    //Used to write a ballot from md or ev to paper form
    public void writeBallot(Ballot paperBallot, String ssNum) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/papers/paperBallot.txt"));

        String ssn = "\n\nSocial Security Number: \n\n";
        writer.write(stars + intro + stars + "\n\nSocial Security Number: " + ssNum + "\n\n" + stars + "\n\n");

        for (int i = 0; i < paperBallot.getQuestions().size(); i++) {
            writer.write("Question #" + (i + 1) + "\n" +
                    paperBallot.getQuestionByIndex(i).getQuestion() + "\n");

            for (int j = 0; j < paperBallot.getQuestionByIndex(i).getOptions().size(); j++) {
                if (paperBallot.getQuestionByIndex(i).getOptions().get(j).isSelected()) {
                    writer.write("[x] " + paperBallot.getQuestionByIndex(i).getOptions().get(j).getOption() + "\n");
                }
                else {
                    writer.write("[ ] " + paperBallot.getQuestionByIndex(i).getOptions().get(j).getOption() + "\n");
                }
            }

            writer.write("[ ] Other: " + "\n");

            writer.write("\n" + stars + "\n\n");
        }
        writer.close();
    }
}
