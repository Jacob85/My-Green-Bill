package com.mygreenbill.registration;

import com.mygreenbill.common.Question;
import com.mygreenbill.security.EncryptionType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacob on 3/16/14.
 */
public abstract class RegistrationRequestAbstract
{
    protected int id;
    protected SimpleIdentityValidationResponse validationResponse;
    protected Question firstValidationQuestion;
    protected Question secondValidationQuestion;
    protected List<Integer> companiesToAdd = new ArrayList<Integer>();


    protected RegistrationRequestAbstract(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
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
    public abstract String getEncriptPassword(EncryptionType encryptionType);
    public abstract boolean isRequestValid();
}
