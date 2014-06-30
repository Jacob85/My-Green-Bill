package com.mygreenbill.servlets;

import com.mygreenbill.authentication.AuthenticationManager;
import com.mygreenbill.common.GreenBillUser;
import com.mygreenbill.common.MailTemplate;
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
            processLoginRequest(email, AppRegistrationRequest.defaultPassword, request.getSession(), request, response);
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
        else if (uri.contains("restorePasswordSubmitted"))
        {
            processChangePassword(request, response);
        }
        else if (uri.contains("restorePasswordUrl"))
        {
            processRestorePasswordUrlPressed(request, response);
        }
        else if (uri.contains("restorePassword"))
        {
            processRestorePasswordRequest(request, response);
        }
        else if (uri.contains("resendActivationEmail"))
        {
            processResendActivationEmail(request, response);
        }
        else if (uri.contains("resendRestorePasswordEmail"))
        {
            processResendResetPasswordEmail(request, response);
        }
        else if (uri.contains("contactUs"))
        {
            processContactUs(request, response);
        }

    }

    private void processContactUs(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        String title = request.getParameter("title");
        String content = request.getParameter("message");

        AuthenticationManager authenticationManager = AuthenticationManager.getInstance();
        Status status = authenticationManager.processContactUs(email, name, title, content);
        if (status.getOperationStatus() == Status.OperationStatus.SUCCESS)
        {
            LOGGER.info("contact us inquiry was submitted, redirect to contact us page");
            request.getSession().setAttribute("contactUs", true);
            response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/contact.jsp"));
        }
        else
        {
            LOGGER.info("failed to submit inquiry, forward to error page");
            forwardToErrorPage(request, response, "Failed to send feedback");
        }
    }

    private void processChangePassword(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String password = request.getParameter("password");
        GreenBillUser greenBillUser = (GreenBillUser) request.getSession().getAttribute("user");
        AuthenticationManager authenticationManager = AuthenticationManager.getInstance();
        Status changePasswordStatus = authenticationManager.changePassword(greenBillUser, password);
        if (changePasswordStatus.getOperationStatus() == Status.OperationStatus.SUCCESS)
        {
            //forward to old_index.jsp
            LOGGER.info("User's password was successfully restored, forwarding to mainPage");
            request.getSession().setAttribute("passRestore", true);
            response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/"));
        }
        else
        {
            forwardToErrorPage(request, response, changePasswordStatus.getDescription());
        }

    }

    private void processResendResetPasswordEmail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        GreenBillUser greenBillUser = (GreenBillUser) request.getSession().getAttribute("user");
        if (greenBillUser == null)
        {
            LOGGER.info("Session Timeout, User has to reconnect again..");
            processLogoutRequest(request, response);
            return;
        }
        // else - user is still logged in
        LOGGER.info("Composing Reset Password email for the user " + greenBillUser.getFirstName() + " " + greenBillUser.getLastName());
        AuthenticationManager authenticationManager = AuthenticationManager.getInstance();
        authenticationManager.composeAndSendEmailFromTemplate(greenBillUser, MailTemplate.PASSWORD_RESET);

        request.getSession().setAttribute("resendEmail", "true");
        response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/restorePasswordSuccess.jsp"));

    }

    private void processRestorePasswordUrlPressed(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String email = request.getParameter("email");
        String hash = request.getParameter("hash");

        AuthenticationManager  authenticationManager = AuthenticationManager.getInstance();
        Status restorePassword = authenticationManager.processRestorePasswordRequestPressed(email, hash, request.getSession());
        if (restorePassword.getOperationStatus() == Status.OperationStatus.SUCCESS)
        {
            // forward to password restore form
            response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/restorePasswordPage.jsp"));
        }
        else
        {
            forwardToErrorPage(request, response, restorePassword.getDescription());
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
        authenticationManager.composeAndSendEmailFromTemplate(greenBillUser, MailTemplate.WELCOME);

        request.getSession().setAttribute("resendEmail", "true");
        response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/success.jsp"));
    }

    private void processRestorePasswordRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        LOGGER.info("Start process restore password request");
        AuthenticationManager authenticationManager = AuthenticationManager.getInstance();
        Status restorePasswordStatus = authenticationManager.processRestorePasswordRequest(request.getParameter("email"), request.getSession());
        if (restorePasswordStatus.getOperationStatus() == Status.OperationStatus.FAILED)
        {
            forwardToErrorPage(request, response, restorePasswordStatus.getDescription());
            return;
        }
        response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/restorePasswordSuccess.jsp"));

    }


    private void processLogoutRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.getSession().invalidate();
        response.sendRedirect("/greenbill");
    }



    private void forwardToErrorPage(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException
    {
        request.getSession().setAttribute("message", message);
        LOGGER.error(String.format("Operation failed! forwarding to error.jsp (%s)", message));
        response.sendRedirect("/greenbill/error.jsp");
    }
}
