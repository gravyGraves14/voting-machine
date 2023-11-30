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
                QuestionType.CANDIDATE,
                1,
                1
        );
        question1.addOption(new QuestionOption("Candidate A"));
        question1.addOption(new QuestionOption("Candidate B"));
        question1.addOption(new QuestionOption("Candidate C"));

        // Create Question 2
        BallotQuestion question2 = new BallotQuestion(
                "Governor",
                "Who should be the Governor?",
                QuestionType.CANDIDATE,
                1,
                1
        );
        question2.addOption(new QuestionOption("Candidate X"));
        question2.addOption(new QuestionOption("Candidate Y"));
        question2.addOption(new QuestionOption("Candidate Z"));

        // Create Question 3
        BallotQuestion question3 = new BallotQuestion(
                "Referendum",
                "Should the referendum be passed?",
                QuestionType.QUESTION,
                1,
                1
        );

        // Create Question 4
        BallotQuestion question4 = new BallotQuestion(
                "CityMayor",
                "Who should be the Mayor of the City?",
                QuestionType.CANDIDATE,
                1,
                1
        );
        question4.addOption(new QuestionOption("Candidate M"));
        question4.addOption(new QuestionOption("Candidate N"));
        question4.addOption(new QuestionOption("Candidate O"));

        // Create Question 5
        BallotQuestion question5 = new BallotQuestion(
                "Senator",
                "Who should be the Senator?",
                QuestionType.CANDIDATE,
                1,
                1
        );
        question5.addOption(new QuestionOption("Candidate P"));
        question5.addOption(new QuestionOption("Candidate Q"));
        question5.addOption(new QuestionOption("Candidate R"));

        // You now have 5 voting questions ready for use
        Ballot ballot = new Ballot(allBallotQuestions);
        ballot.setBallotId("1");
//        BallotBuilder.buildBallotXML(ballot, "ballot");

    }
}
