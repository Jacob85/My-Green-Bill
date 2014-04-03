package com.mygreenbill.servlets;

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
        LOGGER.info(request.getRequestURI());
        PrintWriter out = response.getWriter();
        out.println("Hello World");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        LOGGER.info(request.getRequestURI());
        PrintWriter out = response.getWriter();
        out.println("Hello World " + this.getClass().getSimpleName());
    }
}
