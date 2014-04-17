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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        GreenBillUser user = (GreenBillUser) req.getSession().getAttribute("user");
        resp.getWriter().write("hello " + user.getFirstName() + "you are in dashboard!");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        super.doPost(req, resp);
    }
}
