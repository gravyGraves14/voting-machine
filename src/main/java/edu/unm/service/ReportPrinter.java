package edu.unm.service;

import edu.unm.entity.Report;
import edu.unm.utils.DateUtils;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * created by:
 * author: MichaelMillar
 */
public class ReportPrinter {

    private ReportPrinter() {
        // NO!
    }

    public static void print(Report report) throws IOException {
        String saveName = "reports/" + report.getFileName();


        try (PrintWriter pw = new PrintWriter(saveName)) {
            pw.println("Report: " + report.getReportName() + "\n");

            for (String line : report.getData()) {
                pw.println(line);
            }
        }
    }
}
