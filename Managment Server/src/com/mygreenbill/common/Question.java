package com.mygreenbill.common;

import java.io.Serializable;

/**
 * Created by Jacob on 3/16/14.
 */
public class Question implements Serializable
{
    private String question;
    private String answer;

    public Question(String question, String answer)
    {
        this.question = question;
        this.answer = answer;
    }

    public Question()
    {
    }

    public String getQuestion()
    {
        return question;
    }

    public void setQuestion(String question)
    {
        this.question = question;
    }

    public String getAnswer()
    {
        return answer;
    }

    public void setAnswer(String answer)
    {
        this.answer = answer;
    }

    @Override
    public String toString()
    {
        return "Question{" +
                "question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
