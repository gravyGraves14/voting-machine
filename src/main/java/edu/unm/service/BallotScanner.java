package edu.unm.service;

import edu.unm.entity.Ballot;

/**
 * created by:
 * author: MichaelMillar
 */
public class BallotScanner {
    private static Ballot currentBallot;

    public static void setBallot(Ballot ballot) {
        currentBallot = ballot;
    }

    public static Ballot getBallot() {
        return currentBallot;
    }
}
