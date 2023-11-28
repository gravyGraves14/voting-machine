package edu.unm.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * created by:
 * author: MichaelMillar
 */
public class Ballot {
    private String ballotId;
    private final List<BallotQuestion> questions;

    public Ballot(List<BallotQuestion> questions) {
        this.questions = questions;
    }

    public Ballot() {
        this(new ArrayList<>());
    }

    public String getBallotId() {
        return ballotId;
    }

    public void setBallotId(String ballotId) {
        this.ballotId = ballotId;
    }

    public List<BallotQuestion> getQuestions() {
        return this.questions;
    }

    public BallotQuestion getQuestionByIndex(int index) {
        try {
            return questions.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
}
