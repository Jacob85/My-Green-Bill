package com.mygreenbill.servlets;

import com.mygreenbill.authentication.AuthenticationManager;
import com.mygreenbill.common.GreenBillUser;
import com.mygreenbill.common.Status;
import com.mygreenbill.registration.AppRegistrationRequest;
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String uri = request.getRequestURI();
        LOGGER.debug(uri);
        if (uri.contains("loginByApp"))
        {
            String email = request.getParameter("email");
            String pictureUrl = request.getParameter("picture");
            request.getSession().setAttribute("userPicture", pictureUrl);
            processLoginRequest(email, "Aa123456"/*AppRegistrationRequest.defaultPassword*/, request.getSession(), request, response);
        }
        else if (uri.contains("login"))
        {
            String email = request.getParameter("login_form_email");
            String password = request.getParameter("login_form_password");
            HttpSession session = request.getSession();
            processLoginRequest(email, password, session, request, response);
        }
        else if (uri.contains("accountActivation"))
        {
            processAccountActivation(request, response);
        }
        else if (uri.contains("logout"))
        {
            processLogoutRequest(request, response);
        }
        else if (uri.contains("restorePassword"))
        {
            processRestorePasswordRequest(request, response);
        }
        else if (uri.contains("resendActivationEmail"))
        {
            processResendActivationEmail(request, response);
        }
    }

    private void processLoginRequest(String email, String password, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

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

    private void processResendActivationEmail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        GreenBillUser greenBillUser = (GreenBillUser) request.getSession().getAttribute("user");
        if (greenBillUser == null)
        {
            LOGGER.info("Session Timeout, User has to reconnect again..");
            processLogoutRequest(request, response);
            return;
        }
        // else - user is still logged in
        LOGGER.info("Composing Welcome and Validation email for the user " + greenBillUser.getFirstName() + " " + greenBillUser.getLastName());
        AuthenticationManager authenticationManager = AuthenticationManager.getInstance();
        authenticationManager.composeAndSendAuthenticationEmail(greenBillUser);

        request.getSession().setAttribute("resendEmail", "true");
        response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/success.jsp"));
    }

    private void processRestorePasswordRequest(HttpServletRequest request, HttpServletResponse response)
    {
        //todo yaki - implement this part
    }


    private void processLogoutRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.getSession().invalidate();
        response.sendRedirect("/greenbill");
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
        response.sendRedirect("/greenbill/error.jsp");
    }
}
