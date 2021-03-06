package com.mygreenbill.restful;

import com.mygreenbill.Exceptions.DatabaseException;
import com.mygreenbill.common.GeneralUtilities;
import com.mygreenbill.common.GreenBillCompany;
import com.mygreenbill.common.GreenBillUser;
import com.mygreenbill.common.SendEmailsHandler;
import com.mygreenbill.database.DatabaseHandler;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
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

    private final String getAllCompanies = "call mygreenbilldb.GetAllCompanies();";
    private final String getAllOtherCompanies = "call mygreenbilldb.GetAllOtherCompaniesOfUser(?);";
    private final String addCompanyToUser = "call mygreenbilldb.AddCompanyToUser(?, ?);";
    private final String deleteUserFromCompany = "call mygreenbilldb.DeleteUserFromCompany(?, ?);";
    private final String getCompany = "call mygreenbilldb.GetCompany(?);";

    /**
     * This rest will return for the current user (the logged in one) the comanies list
     * which the user is registered for electronic mailing service
     * @param request
     * @return
     */
    @GET
    @Path("/forUser")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCompanyForUser(@Context HttpServletRequest request)
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

    /**
     * This rest will return all the companies we work with
     * which the user is registered for electronic mailing service
     * @param request
     * @return
     */
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllCompanies(@Context HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        GreenBillUser greenBillUser = (GreenBillUser) session.getAttribute("user");
        if (greenBillUser == null)
        {
            LOGGER.info("Cannot get user companies, user need to login again");
            return errorJson("Please login again");
        }

        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        try
        {
            List<Map<String, Object>> companies = databaseHandler.runGetQuery(getAllCompanies);
            if (GeneralUtilities.hasData(companies))
            {
                JSONArray jsonCompanies = new JSONArray();
                for (int i=0 ; i < companies.size(); i++)
                {
                    jsonCompanies.put(new JSONObject(companies.get(i)));
                }

                return jsonCompanies.toString();
            }
            else
            {
                return errorJson("user has no companies");
            }

        }
        catch (DatabaseException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This rest will return all the companies the current user is not subscribed to
     * which the user is registered for electronic mailing service
     * @param request
     * @return
     */
    @GET
    @Path("/allOtherCompanies")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllOtherCompanies(@Context HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        GreenBillUser greenBillUser = (GreenBillUser) session.getAttribute("user");
        if (greenBillUser == null)
        {
            LOGGER.info("Cannot get user companies, user need to login again");
            return errorJson("Please login again");
        }

        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        try
        {
            String queryString = getAllOtherCompanies.replaceFirst("\\?", greenBillUser.getUserId());
            List<Map<String, Object>> companies = databaseHandler.runGetQuery(queryString);

            if (GeneralUtilities.hasData(companies))
            {
                JSONArray jsonCompanies = new JSONArray();
                for (int i=0 ; i < companies.size(); i++)
                {
                    jsonCompanies.put(new JSONObject(companies.get(i)));
                }

                return jsonCompanies.toString();
            }
            else
            {
                return errorJson("User is already subscribed to all companies");
            }

        }
        catch (DatabaseException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This rest will add to the current user the companies he selected
     * which the user is registered for electronic mailing service
     * @param request
     * @return
     */
    @POST
    @Path("/addUserCompanies")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public String addUserCompanies(@Context HttpServletRequest request, @FormParam("company") List<String> companies)
    {
        HttpSession session = request.getSession();
        GreenBillUser greenBillUser = (GreenBillUser) session.getAttribute("user");
        if (greenBillUser == null)
        {
            LOGGER.info("Cannot get user companies, user need to login again");
            return errorJson("Please login again");
        }

        SendEmailsHandler sendEmailsHandler = SendEmailsHandler.getInstance();
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();

        try
        {
            for (String companyId : companies)
            {
                // Don't add the company if the user is already subscribed to
                if (greenBillUser.isUserRegisteredToCompany(companyId))
                    continue;

                String queryString = addCompanyToUser.replaceFirst("\\?", greenBillUser.getUserId());
                queryString = queryString.replaceFirst("\\?", companyId);
                databaseHandler.runUpdateQuery(queryString);

                queryString = getCompany.replaceFirst("\\?", String.valueOf(companyId));
                databaseHandler.runGetQuery(queryString);
                GreenBillCompany greenBillCompany = new GreenBillCompany(databaseHandler.runGetQuery(queryString).get(0));

                sendEmailsHandler.sendRegisterMailToCompany(greenBillUser, greenBillCompany); // Send email to the company for registering the user
            }
            databaseHandler.retrieveUserCompanies(greenBillUser); // Updating the current user companies
        }
        catch (DatabaseException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This rest will remove for the current user the companies he deselected
     * which the user is registered for electronic mailing service
     * @param request
     * @return
     */
    @POST
    @Path("/removeUserCompanies")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String removeUserCompanies(@Context HttpServletRequest request, String id)
    {
        HttpSession session = request.getSession();
        GreenBillUser greenBillUser = (GreenBillUser) session.getAttribute("user");
        if (greenBillUser == null)
        {
            LOGGER.info("Cannot get user companies, user need to login again");
            return errorJson("Please login again");
        }

        SendEmailsHandler sendEmailsHandler = SendEmailsHandler.getInstance();
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();

        try
        {
            JSONObject companyIdJson = new JSONObject(id); // Getting the given ID of the company to remove
            String queryString = getCompany.replaceFirst("\\?", String.valueOf(companyIdJson.get("id")));
            databaseHandler.runGetQuery(queryString);
            GreenBillCompany greenBillCompany = new GreenBillCompany(databaseHandler.runGetQuery(queryString).get(0));

            queryString = deleteUserFromCompany.replaceFirst("\\?", greenBillUser.getUserId());
            queryString = queryString.replaceFirst("\\?", String.valueOf(greenBillCompany.getId()));
            databaseHandler.runUpdateQuery(queryString);

            sendEmailsHandler.sendUnregisterMailToCompany(greenBillUser, greenBillCompany); // Send email to the company for unregistering the user

            databaseHandler.retrieveUserCompanies(greenBillUser); // Updating the current user companies
        }
        catch (DatabaseException e)
        {
            e.printStackTrace();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private String errorJson(String message)
    {
        return "{error: " + message + "," +
                "redirect: http://www.mygreenbill.com}";
    }
}
