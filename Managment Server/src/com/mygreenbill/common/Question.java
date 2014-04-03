package com.mygreenbill.common;

/**
 * Created by Jacob on 3/16/14.
 */
public class Question
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
}
