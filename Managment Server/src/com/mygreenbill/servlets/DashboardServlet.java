package com.mygreenbill.servlets;

import com.mygreenbill.common.GreenBillUser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Jacob on 4/17/14.
 */
public class DashboardServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String uri = request.getRequestURI();
        if (uri.contains("/dashboard"))
        {
            request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        super.doPost(request, response);
    }
}
