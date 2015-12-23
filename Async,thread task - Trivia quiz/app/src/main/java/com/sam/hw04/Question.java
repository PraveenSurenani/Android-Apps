package com.sam.hw04;

import android.util.Log;

/**
 * HW04
 * Sam Painter and Praveen Suenani
 * Question.java
 */
public class Question {
    private String question;
    private String[] options;
    private int attempt_index;
    private String image_url;
    private int questionID;

    private Question() {
    }

    public Question(String question, String[] options, int attempt_index, String image_url, int questionID) {
        this.question = question;
        this.options = options;
        this.attempt_index = attempt_index;
        this.image_url = image_url;
        this.questionID = questionID;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public int getAttempt_index() {
        return attempt_index;
    }

    public void setAttempt_index(int attempt_index) {
        this.attempt_index = attempt_index;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public static class QuestionUtil {
        public static Question genQuestion(String input) {
            Question nq = new Question();
            String[] parts = input.split(";", -1);
            int index_of_url = parts.length - 2;
            nq.setQuestionID(Integer.parseInt(parts[0]));
            nq.setQuestion(parts[1]);
            nq.setImage_url(parts[index_of_url]);
            String[] ops = new String[parts.length - 4];
            for (int i = 0; i < ops.length; i++) {
                ops[i] = parts[i + 2];
            }
            nq.setOptions(ops);
            return nq;
        }
    }
}
