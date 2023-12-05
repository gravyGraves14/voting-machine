package edu.unm.entity;

import edu.unm.dao.ElectionGremlinDAO;
import edu.unm.utils.DateUtils;

import java.time.LocalDateTime;

/**
 * created by:
 * author: MichaelMillar
 */
public class ElectionReport extends Report {

    private final String schemaName;
    private Ballot ballotData;
    private int totalVotes;

    public ElectionReport(String schemaName) throws IllegalArgumentException {
        super("Election Report" ,
                "election_report-"
                        + DateUtils.formatLocalDateTime(LocalDateTime.now()));
        this.schemaName = schemaName;
        populateData();
        buildReport();
    }

    private void populateData() {
        ElectionGremlinDAO dao = new ElectionGremlinDAO();
        ballotData = dao.getTabulation(schemaName);
        totalVotes = dao.findAllVerticesByLabelWithProperty(
                "ballot", "schemaName", schemaName).size();
    }

    public void buildReport() throws IllegalArgumentException {
        if (ballotData == null) {
            throw new IllegalArgumentException("Unable to load election results, schema not present in DB.");
        }
        data.add("Election Report '" + schemaName + "'");
        data.add("Total voters: " + totalVotes);
        for (BallotQuestion question : ballotData.getQuestions()) {
            data.add("Question: " + question.getQuestion());
            for (QuestionOption option : question.getOptions()) {
                data.add("\t" + option.getTotalVotes() + " - " + option.getOption());
            }
        }

    }
    public int getTotalVotes() {
        return totalVotes;
    }

}
