package edu.unm.entity;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class PaperBallot {
    private Questions questions = new Questions();

    public PaperBallot() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/papers/paperBallot.txt"));

        writer.write("""
                *****************************************************************
                
                BALLOT
                Please vote for at most one option in each category.
                Place an 'x' in the brackets next to the option you select.
                
                *****************************************************************
                
                Social Security Number:
                
                *****************************************************************
                
                """);

        for (int i = 0; i < questions.getNumQuestions(); i++) {
            writer.write("Question #" + (i + 1) + "\n" +
                    questions.getQuestion(i) + "\n");

            for (int j = 0; j < questions.getNumChoices(i); j++) {
                writer.write("[ ] " + questions.getQuestionChoices(i)[j] + "\n");
            }

            writer.write("\n*****************************************************************\n\n");
        }
        writer.close();
    }
}
