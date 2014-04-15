package com.mygreenbill.registration;

import com.mygreenbill.common.Question;
import com.mygreenbill.security.EncryptionType;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacob on 3/16/14.
 */
public abstract class RegistrationRequestAbstract implements Serializable
{
    protected String id;
    protected SimpleIdentityValidationResponse validationResponse;
    protected Question firstValidationQuestion;
    protected Question secondValidationQuestion;
    protected List<Integer> companiesToAdd = new ArrayList<Integer>();


    protected RegistrationRequestAbstract(String id)
    {
        this.id = id;
    }

    protected RegistrationRequestAbstract()
    {
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public SimpleIdentityValidationResponse getValidationResponse()
    {
        return validationResponse;
    }

    public void setValidationResponse(SimpleIdentityValidationResponse validationResponse)
    {
        this.validationResponse = validationResponse;
        //populate the questions
        if (firstValidationQuestion == null)
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
           firstValidationQuestion = new Question("What is your birth date?", dateFormat.format(validationResponse.getBirthDate()));
        }
        if (secondValidationQuestion == null)
        {
            secondValidationQuestion = new Question("What is your father name?", validationResponse.getFatherName());
        }
    }

    public Question getFirstValidationQuestion()
    {
        return firstValidationQuestion;
    }

    public void setFirstValidationQuestion(Question firstValidationQuestion)
    {
        this.firstValidationQuestion = firstValidationQuestion;
    }

    public Question getSecondValidationQuestion()
    {
        return secondValidationQuestion;
    }

    public void setSecondValidationQuestion(Question secondValidationQuestion)
    {
        this.secondValidationQuestion = secondValidationQuestion;
    }

    public List<Integer> getCompaniesToAdd()
    {
        return companiesToAdd;
    }

    public void setCompaniesToAdd(List<Integer> companiesToAdd)
    {
        this.companiesToAdd = companiesToAdd;
    }
    public abstract String getEncryptPassword(EncryptionType encryptionType);
    public abstract boolean isRequestValid();
}
