package edu.unm.entity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
TODO: These questions need to go into a database somewhere
This needs a function called readQuestionsAndChoicesFromFile() that
has what is currently in the constructor. That function will populate the database.
This function will be called from the open ballot button in StaffGUI.
This way we don't need to re-read the questions file when we want access
to the questions from different classes.
 */
public class Questions {
    private final List<String> questions;
    private final List<String[]> questionChoices;

    public Questions() {
        String filePath = "src/main/resources/papers/questions.txt";
        questions = new ArrayList<>();
        questionChoices = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            StringBuilder currentQuestion = new StringBuilder();
            List<String> currentChoices = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                // If the line starts with a number, it is the beginning of a new question
                if (line.matches("\\d+\\..*")) {
                    if (currentQuestion.length() > 0) {
                        // Save the previous question and choices
                        questions.add(currentQuestion.toString());
                        questionChoices.add(currentChoices.toArray(new String[currentChoices.size()]));
                        // Reset for the new Question
                        currentQuestion = new StringBuilder();
                        currentChoices.clear();
                    }

                    // Add the new question
                    currentQuestion.append(line.substring(line.indexOf('.') + 1).trim());
                } else if (line.startsWith("-")) {
                    // If the line starts with a dash, it is a choice
                    currentChoices.add(line.substring(1).trim());
                }
            }
            // Add the last question and choices after reaching the end of the file
            if (currentQuestion.length() > 0) {
                questions.add(currentQuestion.toString());
                questionChoices.add(currentChoices.toArray(new String[0]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getQuestions() {
        return questions;
    }

    public List<String[]> getAllQuestionChoices() {
        return questionChoices;
    }

    public String getQuestion(int index) {
        return questions.get(index);
    }

    public String[] getQuestionChoices(int index) {
        return questionChoices.get(index);
    }

    public int getNumQuestions() {
        return questions.size();
    }

    public int getNumChoices(int index) {
        return questionChoices.get(index).length;
    }
}
