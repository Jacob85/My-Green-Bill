package com.mygreenbill.servlets;

import com.mygreenbill.authentication.AuthenticationManager;
import com.mygreenbill.common.GeneralUtilities;
import com.mygreenbill.common.GreenBillCompany;
import com.mygreenbill.common.GreenBillUser;
import com.mygreenbill.common.Status;
import com.mygreenbill.database.DatabaseHandler;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Jacob on 4/17/14.
 */
public class DashboardServlet extends HttpServlet
{
    private static final Logger LOGGER = Logger.getLogger(DashboardServlet.class);
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String uri = request.getRequestURI();
        if (uri.endsWith("/dashboard"))
        {
            request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
            return;
        }
        else if (uri.contains("/downloadBill"))
        {
            processBillDownload(request, response);
            return;
        }
        else if (uri.contains("/setNewForwardAddress"))
        {
            processNewAddress(request, response);
            return;
        }
    }

    private void processNewAddress(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        GreenBillUser greenBillUser = (GreenBillUser) request.getSession().getAttribute("user");
        if (greenBillUser == null)
        {
            response.sendRedirect("/greenbill");
            return;
        }

        String newAddress = request.getParameter("newAddress");
        if (GeneralUtilities.hasData(newAddress))
        {
            AuthenticationManager authenticationManager = AuthenticationManager.getInstance();
            Status status = authenticationManager.setNewForwardAddress(greenBillUser, newAddress);

            if (status.getOperationStatus() == Status.OperationStatus.SUCCESS)
            {
                greenBillUser.setForwardEmail(newAddress); // Setting the new forward address to the current logged in user
                response.sendRedirect("/greenbill/dashboard");
            }
            else
            {
                response.sendRedirect("/greenbill/error.jsp");
            }
        }
    }

    private void processBillDownload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String serverPrefix = "http://54.72.109.83";
        String path = request.getParameter("path");
        if (!GeneralUtilities.hasData(path))
        {
            LOGGER.info("unable to get the user path, forward to 404 not found");
            forwardTo404(request, response);
        }
        else
        {
            //get only from: /UsersFiles
            path = path.replace("/var/www/html", "");
            try
            {
                URL url = new URL(serverPrefix + path);
                URLConnection connection = url.openConnection();
                response.setContentLength(connection.getContentLength());
                response.setContentType(connection.getContentType());

                OutputStream out = response.getOutputStream();
                InputStream in = connection.getInputStream();
                // Copy the contents of the file to the output stream
                byte[] buf = new byte[1024];
                int count = 0;
                while ((count = in.read(buf)) >= 0)
                {
                    out.write(buf, 0, count);
                }
                in.close();
                out.close();

            } catch (MalformedURLException e)
            {
                LOGGER.error(e.getMessage(), e);
                forwardTo404(request, response);
            } catch (IOException e)
            {
                LOGGER.error(e.getMessage(), e);
                forwardTo404(request, response);
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
       doGet(request, response);
    }

    private void forwardTo404(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.sendRedirect("/greenbill/error404.jsp");
        return;
    }
}
