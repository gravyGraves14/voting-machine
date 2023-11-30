package edu.unm.entity;

import java.awt.print.Printable;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * created by:
 * author: MichaelMillar
 */
public abstract class Report {

    protected final String reportName;
    protected final String fileName;
    protected final List<String> data;
    protected final LocalDateTime reportDateTime;

    protected Report(String reportName, String fileName) {
        this.reportName = reportName;
        this.fileName = fileName;
        this.data = new ArrayList<>();
        this.reportDateTime = LocalDateTime.now();
    }

    public String getReportName() {
        return reportName;
    }

    public String getFileName() {
        return fileName;
    }

    public List<String> getData() {
        return data;
    }

    public LocalDateTime getReportDateTime() {
        return reportDateTime;
    }
}
