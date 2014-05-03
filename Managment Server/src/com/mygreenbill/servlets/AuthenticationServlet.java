package com.mygreenbill.servlets;

import com.mygreenbill.authentication.AuthenticationManager;
import com.mygreenbill.common.Status;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Jacob on 4/17/14.
 */
public class AuthenticationServlet extends HttpServlet
{
    private static final Logger LOGGER = Logger.getLogger(AuthenticationServlet.class);
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doPost(request, response);
    }

    private void processLoginRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String email = request.getParameter("login_form_email");
        String password = request.getParameter("login_form_password");
        HttpSession session = request.getSession();
        AuthenticationManager authenticationManager = AuthenticationManager.getInstance();

        Status loginStatus = authenticationManager.processLoginRequest(email, password, session);
        if (loginStatus.getOperationStatus() == Status.OperationStatus.SUCCESS)
        {
            //forward to dashboard
            LOGGER.info("User %s logged in, forward to dashboard");
            response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/dashboard"));
        }
        else
        {
            forwardToErrorPage(request, response, loginStatus.getDescription());
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String uri = request.getRequestURI();
        LOGGER.debug(uri);

        if (uri.contains("login"))
        {
            processLoginRequest(request, response);
            return;
        }
        else if (uri.contains("accountActivation"))
        {
            processAccountActivation(request, response);
            return;
        }


    }

    private void processAccountActivation(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String email = request.getParameter("email");
        String hash = request.getParameter("hash");

        AuthenticationManager  authenticationManager = AuthenticationManager.getInstance();
        Status activationStatus = authenticationManager.processActivationRequest(email, hash, request.getSession());
        if (activationStatus.getOperationStatus() == Status.OperationStatus.SUCCESS)
        {
            LOGGER.info("User was successfully activate, forwarding to dashboard");
            response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/dashboard"));
        }

    }

    private void forwardToErrorPage(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException
    {
        request.getSession().setAttribute("message", message);
        LOGGER.error(String.format("Operation failed! forwarding to error.jsp (%s)", message));
        request.getRequestDispatcher("/error.jsp").forward(request, response);
    }
}
