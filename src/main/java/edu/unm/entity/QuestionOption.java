package edu.unm.entity;

/**
 * created by:
 * author: MichaelMillar
 */
public class QuestionOption {

    private final String option;
    private final String affiliation;
    private boolean selected;

    public QuestionOption(String option, String affiliation) {
        this.option = option;
        this.affiliation = affiliation;
    }

    public String getOption() {
        return option;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
