package edu.unm.entity;

import edu.unm.utils.DateUtils;

import java.time.LocalDateTime;

/**
 * created by:
 * author: MichaelMillar
 */
public class ElectionReport extends Report {

    // TODO: Fetch data from graph DB and build report
    public ElectionReport() {
        super("Election Report" ,
                "election_report-"
                        + DateUtils
                        .formatLocalDateTime(LocalDateTime.now()));
        populateData();
        buildReport();
    }

    private void populateData() {
        // TODO
    }

    public void buildReport() {
        // TODO
    }

}
