package com.mygreenbill.servlets;

import com.mygreenbill.authentication.AuthenticationManager;
import com.mygreenbill.common.GreenBillUser;
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
           /*
                The Registration process is build of phases:
                1. the first phase is when the user choose to register and submit its details (phase = 0 || phase = null)
                2. second phase is after validation questions returned from the user with the answers (phase = 1)
                3. we can add some more phases like choosing companies
                4. we can add some more phases like validate email address

                **IMPORTANT** - In the End of each method remember to ser the phase to be the next phase!
            */
            HttpSession session = request.getSession();
            Integer registrationPhase = (Integer) session.getAttribute("phase");
            if (registrationPhase == null)
            {
                // new request
                processNewSession(request, response);
            }
            else
            {
                switch (registrationPhase)
                {
                    case 1:
                        // process First Phase
                        processFirstPhase(request, response);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void processFirstPhase(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession currentSession = request.getSession();
        String answer1 = request.getParameter("first_validation_question");
        String answer2 = request.getParameter("second_validation_question");
        FullRegistrationRequest registrationRequest = (FullRegistrationRequest) currentSession.getAttribute("registrationRequest");

        RegistrationManager registrationManager = RegistrationManager.getInstance();
        boolean answersAreValid = registrationManager.areAnswersValid(registrationRequest, answer1, answer2);
        if (!answersAreValid)
        {
            forwardToErrorPage(request, response, String.format("Failed to validate answers (%s, %s)", answer1, answer2) );
            return;
        }
        // answers are valid create new user and forward to dashboard
        //todo yaki - if we will want to let the user choose companies this is the phase to do it at the moment we create new user with all companies
        LOGGER.info("User validation phase is finished - creating new user in hte database");
        Status registerUserStatus = registrationManager.registerUser(registrationRequest);
        // if operation failed forward to error page
        if (registerUserStatus.getOperationStatus() == Status.OperationStatus.FAILED)
        {
            forwardToErrorPage(request, response, registerUserStatus.getDescription());
            return;
        }
        //else the user was successfully added to the Database (Register successfully)

        //todo yaki - at the moment just forward to success page with proper meaasge in the future to redirect the user to dashboard
        registrationManager.updateCurrentSessionWithUserInfo(registrationRequest, currentSession);

        GreenBillUser greenBillUser = (GreenBillUser) currentSession.getAttribute("user");
        LOGGER.info("Composing Welcome and Validation email for the user " + greenBillUser.getFirstName() + " " + greenBillUser.getLastName());
        AuthenticationManager authenticationManager = AuthenticationManager.getInstance();
        authenticationManager.composeAndSendAuthenticationEmail(greenBillUser);

         /*end of phase 1 next phase is 2*/
        request.getSession().setAttribute("phase", 2);
        request.getRequestDispatcher("/success.jsp").forward(request, response);

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
            forwardToErrorPage(request, response, status.getDescription());
            return;
        }

        // get validation from user success = return the questions to the user and attache the registration request to the current session
        request.getSession().setAttribute("question1", registrationManager.getFirstValidationQuestion(registrationRequest));
        request.getSession().setAttribute("question2", registrationManager.getSecondValidationQuestion(registrationRequest));
        request.getSession().setAttribute("registrationRequest", registrationRequest);
        /*end of phase 0 next phase is 1*/
        request.getSession().setAttribute("phase", 1);

        LOGGER.info("return the question to the user for validation");
        request.getRequestDispatcher("/validate.jsp").forward(request, response);
    }

    private void forwardToErrorPage(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException
    {
        request.getSession().setAttribute("message", message);
        LOGGER.error(String.format("Operation failed! forwarding to error.jsp (%s)", message));
        response.sendRedirect("/greenbill/error.jsp");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        LOGGER.info(request.getRequestURI());
        PrintWriter out = response.getWriter();
        out.println("Hello World " + this.getClass().getSimpleName());
    }

}
