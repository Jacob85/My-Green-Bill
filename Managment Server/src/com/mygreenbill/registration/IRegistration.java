package com.mygreenbill.registration;

import com.mygreenbill.common.Status;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Jacob on 3/18/14.
 */
public interface IRegistration
{

    public boolean isUserExists(RegistrationRequestAbstract request);
    public String getFirstValidationQuestion(RegistrationRequestAbstract request);
    public String getSecondValidationQuestion(RegistrationRequestAbstract request);
    public boolean areAnswersValid(RegistrationRequestAbstract request, String answer1, String answer2);
    public Status registerUser(RegistrationRequestAbstract request);
    public Status updateCurrentSessionWithUserInfo(RegistrationRequestAbstract request, HttpSession currentSession);
    public Status processRegistrationRequest(RegistrationRequestAbstract registrationRequest);
    public RegistrationRequestAbstract creteRegistrationRequest(HttpServletRequest request);
}
