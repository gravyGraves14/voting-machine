package edu.unm.entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * created by:
 * author: MichaelMillar
 */
public class VVPATReport extends Report {
    private final Ballot ballot;

    public VVPATReport(Ballot ballot) throws IllegalArgumentException {
        super("VVPAT" + ballot.getBallotId(),
                "VVPAT" + ballot.getBallotId() + ".txt");
        this.ballot = ballot;
        buildReportData();
    }

    private void buildReportData() throws IllegalArgumentException {
        if (ballot == null) {
            throw new IllegalArgumentException("Ballot cannot be null");
        }
        data.add("VVPAT Report for Ballot: " + ballot.getBallotId());
        List<BallotQuestion> questions = ballot.getQuestions();
        questions.forEach(question -> {
            String questionSummary = formatQuestion(question);
            data.add(questionSummary);
        });
    }

    private String formatQuestion(BallotQuestion question) {
        StringBuilder sb = new StringBuilder(500);
        sb.append(question.getQuestionShort())
                .append(" : ");
        question.getSelectedOptions()
                .forEach(o -> sb.append(o.getOption()).append("\n"));
        return sb.toString();
    }

}
