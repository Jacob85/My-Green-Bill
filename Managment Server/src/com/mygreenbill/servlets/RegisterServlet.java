package com.mygreenbill.servlets;

import com.mygreenbill.common.Status;
import com.mygreenbill.registration.FullRegistrationRequest;
import com.mygreenbill.registration.RegistrationManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Jacob on 3/15/14.
 */
public class RegisterServlet extends HttpServlet
{

    private static final Logger LOGGER = Logger.getLogger(RegisterServlet.class);

    public RegisterServlet()
    {
        LOGGER.debug(RegisterServlet.class.getSimpleName() + " Was created");

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        LOGGER.debug(request.getRequestURI());
        String uri = request.getRequestURI();

        // if we are dealing with full registration
        if (uri.contains("/full"))
        {
            // new Registration request
            if (request.getSession().isNew())
            {
                processNewSession(request, response);
            }
            else
            {
                // processReturningRequest
                processReturningRequest(request, response);
            }

        }
    }

    private void processReturningRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession currentSession = request.getSession();
        String answer1 = request.getParameter("first_validation_question");
        String answer2 = request.getParameter("second_validation_question");
        FullRegistrationRequest registrationRequest = (FullRegistrationRequest) currentSession.getAttribute("registrationRequest");

        RegistrationManager registrationManager = RegistrationManager.getInstance();
        boolean answersAreValid = registrationManager.areAnswersValid(registrationRequest, answer1, answer2);
        if (!answersAreValid)
        {
            request.getSession().setAttribute("message", String.format("Failed to validate answers (%s, %s)", answer1, answer2));
            LOGGER.error("Failed to validate answers, validation failed. forwarding to error.jsp");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
        // answers are valid create new user and forward to dashboard
        //todo yaki - if we will want to let the user choose companies this is the phase to do it at the moment we create new user with all companies
        LOGGER.info("User validation phase is finished - creating new user in hte database");
        registrationManager.registerUser(registrationRequest);



    }

    private void processNewSession(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String email =  request.getParameter("full_registration_inputEmail");
        String id =  request.getParameter("full_registration_inputId");
        String password = request.getParameter("full_registration_inputPassword");

        FullRegistrationRequest registrationRequest = new FullRegistrationRequest(id, email, password);

        RegistrationManager registrationManager = RegistrationManager.getInstance();
        Status status = registrationManager.processRegistrationRequest(registrationRequest);
        if (status.getOperationStatus() == Status.OperationStatus.FAILED)
        {
            request.getSession().setAttribute("message", status.getDescription());
            LOGGER.error(String.format("user validation failed. forwarding to error.jsp (%s)", status.getDescription()));
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }

        // get validation from user success = return the questions to the user and attache the registration request to the current session
        request.getSession().setAttribute("question1", registrationManager.getFirstValidationQuestion(registrationRequest));
        request.getSession().setAttribute("question2", registrationManager.getSecondValidationQuestion(registrationRequest));
        request.getSession().setAttribute("registrationRequest", registrationRequest);
        LOGGER.info("return the question to the user for validation");
        request.getRequestDispatcher("/validate.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        LOGGER.info(request.getRequestURI());
        PrintWriter out = response.getWriter();
        out.println("Hello World " + this.getClass().getSimpleName());
    }
}
