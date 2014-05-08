package com.mygreenbill.restful;

/**
 * Created by Jacob on 5/8/14.
 */

import com.mygreenbill.common.GreenBillUser;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;

@Path("/hello")
public class HelloWorld
{
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String sayHello(@Context HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        GreenBillUser greenBillUser = (GreenBillUser) session.getAttribute("user");

        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("Greeting", greenBillUser.getFirstName() + " " + greenBillUser.getLastName());
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
