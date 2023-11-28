package edu.unm.entity;

/**
 * created by:
 * author: MichaelMillar
 */
public class QuestionOption {

    private final String option;
    private boolean selected;

    public QuestionOption(String option) {
        this.option = option;
    }

    public String getOption() {
        return option;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
