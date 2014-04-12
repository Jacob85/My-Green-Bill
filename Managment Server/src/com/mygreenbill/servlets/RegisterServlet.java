package com.mygreenbill.servlets;

import com.mygreenbill.common.Status;
import com.mygreenbill.database.DatabaseHandler;
import com.mygreenbill.registration.FullRegistrationRequest;
import com.mygreenbill.registration.RegistrationManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

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
            String email =  request.getParameter("full_registration_inputEmail");
            String id =  request.getParameter("full_registration_inputId");
            String password = request.getParameter("full_registration_inputPassword");

            FullRegistrationRequest registrationRequest = new FullRegistrationRequest(id, email, password);

            RegistrationManager registrationManager = RegistrationManager.getInstance();
            Status status = registrationManager.processRegistrationRequest(registrationRequest);
            if (status.getOperationStatus() == Status.OperationStatus.FAILED)
            {
                request.getSession().setAttribute("message", status.getDescription());
                LOGGER.error("user validation failed. forwarding to error.jsp");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }

            // get validation from user success = return the questions to the user and attache the registration request to the current session
            request.getSession().setAttribute("question1", registrationManager.getFirstValidationQuestion(registrationRequest));
            request.getSession().setAttribute("question2", registrationManager.getSecondValidationQuestion(registrationRequest));
            request.getSession().setAttribute("registrationRequest", registrationRequest);
            LOGGER.info("return the question to the user for validation");
            request.getRequestDispatcher("/validate.jsp").forward(request, response);

        }
        PrintWriter out = response.getWriter();
        out.println("Hello World " + request.getRequestURI());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        LOGGER.info(request.getRequestURI());
        PrintWriter out = response.getWriter();
        out.println("Hello World " + this.getClass().getSimpleName());
    }
}
