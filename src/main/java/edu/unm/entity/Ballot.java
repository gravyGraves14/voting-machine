package edu.unm.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * created by:
 * author: MichaelMillar
 */
@XmlRootElement
public class Ballot {
    private final String ballotId;
    private final String ballotSchemaName;
    private final List<BallotQuestion> questions;

    public Ballot(String schemaName, List<BallotQuestion> questions) {
        this.ballotId = UUID.randomUUID().toString();
        this.ballotSchemaName = schemaName;
        this.questions = questions;
    }

    public Ballot(String schemaName) {
        this(schemaName, new ArrayList<>());
    }

    public String getBallotId() {
        return ballotId;
    }

    public String getSchemaName() {
        return ballotSchemaName;
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
