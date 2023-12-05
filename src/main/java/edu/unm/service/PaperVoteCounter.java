/**
 * Author: Raju Nayak
 */

package edu.unm.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The PaperVoteCounter class is a utility class for managing the count of paper votes.
 * It provides functionality to increment and retrieve the current total of paper votes.
 * This class handles the persistence of the vote count by reading from and writing to
 * a file. It ensures that the vote count is maintained across system restarts or application relaunches.
 */
public class PaperVoteCounter {
    private static final String VOTE_COUNT_FILE = "paper_vote_count.txt";
    private static int totalPaperVotes;

    static {
        totalPaperVotes = readVoteCountFromFile();
    }

    /**
     * Increments the paper vote count and updates the file.
     * This method is called whenever a new paper vote is registered.
     */
    public static void incrementVoteCount() {
        totalPaperVotes++;
        writeVoteCountToFile(totalPaperVotes);
    }

    /**
     * Returns the current total number of paper votes.
     * @return int the current total paper vote count
     */
    public static int getCurrentVoteCount() {
        return totalPaperVotes;
    }

    /**
     * Reads the vote count from the file.
     * This method is used to initialize the vote count when the application starts.
     * @return int the number of votes read from the file, or 0 if an error occurs
     */
    private static int readVoteCountFromFile() {
        try {
            if (!Files.exists(Paths.get(VOTE_COUNT_FILE))) {
                return 0; // File does not exist, assume 0 votes
            }

            String content = new String(Files.readAllBytes(Paths.get(VOTE_COUNT_FILE)));
            return Integer.parseInt(content.trim());
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return 0; // In case of an error, return 0
        }
    }

    /**
     * Writes the current vote count to the file.
     * This method ensures that the vote count is persisted.
     * @param voteCount the current total vote count to write to the file
     */
    private static void writeVoteCountToFile(int voteCount) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(VOTE_COUNT_FILE))) {
            writer.write(String.valueOf(voteCount));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}