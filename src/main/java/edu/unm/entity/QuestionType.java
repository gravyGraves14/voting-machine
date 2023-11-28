package edu.unm.entity;

/**
 * created by:
 * author: MichaelMillar
 */
public enum QuestionType {
    CANDIDATE,
    QUESTION,
    OTHER;

    public static QuestionType of(String type) {
        return switch (type) {
            case "candidate" -> CANDIDATE;
            case "question" -> QUESTION;
            default -> OTHER;
        };
    }
}
