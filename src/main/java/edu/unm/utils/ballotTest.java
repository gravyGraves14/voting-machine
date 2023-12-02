package edu.unm.utils;

import edu.unm.entity.*;

import java.util.ArrayList;
import java.util.List;

public class ballotTest {
    public static void main(String[] args) {
        List<BallotQuestion> allBallotQuestions = new ArrayList<>();
        // Create Question 1
        BallotQuestion question1 = new BallotQuestion(
                "POTUS",
                "Who should be the President of the United States?",
                QuestionType.CANDIDATE
        );
        question1.addOption(new QuestionOption("Candidate A", "republican"));
        question1.addOption(new QuestionOption("Candidate B", "republican"));
        question1.addOption(new QuestionOption("Candidate C", "republican"));

        // Create Question 2
        BallotQuestion question2 = new BallotQuestion(
                "Governor",
                "Who should be the Governor?",
                QuestionType.CANDIDATE
        );
        question2.addOption(new QuestionOption("Candidate X", "republican"));
        question2.addOption(new QuestionOption("Candidate Y", "republican"));
        question2.addOption(new QuestionOption("Candidate Z", "republican"));

        // Create Question 3
        BallotQuestion question3 = new BallotQuestion(
                "Referendum",
                "Should the referendum be passed?",
                QuestionType.QUESTION
        );

        // Create Question 4
        BallotQuestion question4 = new BallotQuestion(
                "CityMayor",
                "Who should be the Mayor of the City?",
                QuestionType.CANDIDATE
        );
        question4.addOption(new QuestionOption("Candidate M", "republican"));
        question4.addOption(new QuestionOption("Candidate N", "republican"));
        question4.addOption(new QuestionOption("Candidate O", "republican"));

        // Create Question 5
        BallotQuestion question5 = new BallotQuestion(
                "Senator",
                "Who should be the Senator?",
                QuestionType.CANDIDATE
        );
        question5.addOption(new QuestionOption("Candidate P", "republican"));
        question5.addOption(new QuestionOption("Candidate Q", "republican"));
        question5.addOption(new QuestionOption("Candidate R", "republican"));

        // You now have 5 voting questions ready for use
        Ballot ballot = new Ballot(allBallotQuestions);
        ballot.setBallotId("1");
//        BallotBuilder.buildBallotXML(ballot, "ballot");

    }
}
