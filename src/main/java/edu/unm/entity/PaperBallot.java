package edu.unm.entity;

import java.io.*;
import java.util.Objects;

public class PaperBallot {
    private final Questions questions = new Questions();
    private final String stars = "*****************************************************************";
    private final String intro = """


            BALLOT
            Please vote for at most one option in each category.
            Place an 'x' in the brackets next to the option you select.

            """;
    private final String ssn = "\n\nSocial Security Number: \n\n";

    public void createPaperBallot() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/papers/paperBallot.txt"));

        writer.write(stars + intro + stars + ssn + stars + "\n\n");

        for (int i = 0; i < questions.getNumQuestions(); i++) {
            writer.write("Question #" + (i + 1) + "\n" +
                    questions.getQuestion(i) + "\n");

            for (int j = 0; j < questions.getNumChoices(i); j++) {
                writer.write("[ ] " + questions.getQuestionChoices(i)[j] + "\n");
            }

            writer.write("[ ] Other: " + "\n");

            writer.write("\n" + stars + "\n\n");
        }
        writer.close();
    }

    public boolean checkBallot() throws IOException {
        StringBuilder currentChunk = new StringBuilder();
        String line;
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/papers/paperBallot.txt"));

        //Check the first stars
        if (!currentChunk.append(reader.readLine()).toString().equals(stars)) {
            return false;
        }

        //Check the intro
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
        if (!currentChunk.toString().equals(intro)) {
            return false;
        }

        //Check the ssn
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
        if (!currentChunk.toString().matches("\n\n+Social Security Number: +\\d{9}+\n\n")) {
            return false;
        }

        //check the questions
        for (int i = 0; i < questions.getNumQuestions(); i++) {
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

            StringBuilder correctQuestion = new StringBuilder();
            correctQuestion.append("\n\n");

            correctQuestion.append("Question #").append(i + 1).append("\n").append(questions.getQuestion(i)).append("\n");

            for (int j = 0; j < questions.getNumChoices(i); j++) {
                correctQuestion.append("[ ] ").append(questions.getQuestionChoices(i)[j]).append("\n");
            }

            correctQuestion.append("""
                    [ ] Other:\s

                    """);

            if (!currentChunk.toString().contentEquals(correctQuestion)) {
                return false;
            }
        }

        //last stars
        return line.equals(stars);
    }

    private void getchoices() {

    }

    private void submitChoices() {

    }
}
