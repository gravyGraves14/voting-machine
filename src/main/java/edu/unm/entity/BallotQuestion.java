package edu.unm.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * created by:
 * author: MichaelMillar
 */
public class BallotQuestion {

    // questionShort is a very short summary of the overall question
    // for example: President of the United States would be shorted to POTUS or President
    private final String questionShort;
    private final String question;
    private final QuestionType type;
    private final List<QuestionOption> options;

    public BallotQuestion(String questionShort, String question,
                          QuestionType type, List<QuestionOption> options) {
        this.questionShort = questionShort;
        this.question = question;
        this.type = type;
        this.options = options;
    }

    public BallotQuestion(String questionShort, String question, QuestionType type) {
        this(questionShort, question, type, new ArrayList<>());
    }

    public String getQuestionShort() {
        return questionShort;
    }

    public String getQuestion() {
        return question;
    }

    public QuestionType getType() {
        return type;
    }

    public List<QuestionOption> getOptions() {
        return options;
    }

    public List<QuestionOption> getSelectedOptions() {
        return options.stream()
                .filter(QuestionOption::isSelected)
                .collect(Collectors.toList());
    }

    public void addOption(QuestionOption option) {
        options.add(option);
    }
}
