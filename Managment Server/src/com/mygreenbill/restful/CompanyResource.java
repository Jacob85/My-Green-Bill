package com.mygreenbill.restful;

import com.mygreenbill.common.GreenBillCompany;
import com.mygreenbill.common.GreenBillUser;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * Created by ipeleg on 5/18/14.
 */
@Path("/company")
public class CompanyResource
{
    private final static Logger LOGGER = Logger.getLogger(CompanyResource.class);

    /**
     * This rest will return for the current user (the logged in one) the comanies list
     * which the user is registered for electronic mailing service
     * @param request
     * @return
     */
    @GET
    @Path("/forUser")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllBills(@Context HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        GreenBillUser greenBillUser = (GreenBillUser) session.getAttribute("user");
        if (greenBillUser == null)
        {
            LOGGER.info("Cannot get user companies, user need to login again");
            return errorJson("Please login again");
        }

        //user is logged in, get all the user companies
        List<GreenBillCompany> companies = greenBillUser.getUserCompanyList();
        if (companies == null)
        {
            return errorJson("Problem with fetching user companies");
        }

        JSONArray jsonCompanies = new JSONArray();
        for (int i=0 ; i < companies.size(); i++)
        {
            jsonCompanies.put(new JSONObject(companies.get(i)));
        }

        return jsonCompanies.toString();
    }

    private String errorJson(String message)
    {
        return "{error: " + message + "," +
                "redirect: http://www.mygreenbill.com}";
    }
}
