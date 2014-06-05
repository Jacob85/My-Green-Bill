package com.mygreenbill.restful;

import com.mygreenbill.Exceptions.DatabaseException;
import com.mygreenbill.common.GeneralUtilities;
import com.mygreenbill.common.GreenBillUser;
import com.mygreenbill.database.DatabaseHandler;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * Created by Jacob on 5/8/14.
 */
@Path("/bills")
public class BillResource
{
    private final static Logger LOGGER = Logger.getLogger(BillResource.class);
    private final String getAllUserBills = "call mygreenbilldb.getalluserincomingmassages(?);";
    private final String getAllUserBillsFromCompany = "call mygreenbilldb.getalluserincomingmassagesfromcompany(?, ?);";

    @GET @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllBills(@Context HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        GreenBillUser greenBillUser = (GreenBillUser) session.getAttribute("user");
        if (greenBillUser == null)
        {
            LOGGER.info("Cannot get user bills, user need to login again");
            return errorJson("Please login again");
        }
        //user loged in, get all the user bills
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        String queryString = getAllUserBills.replace("?", greenBillUser.getUserId());
        try
        {
            List<Map<String, Object>> bills = databaseHandler.runGetQuery(queryString);
            if (GeneralUtilities.hasData(bills))
            {
                JSONArray jsonBills = new JSONArray();
                for (Map<String, Object> bill : bills)
                {
                    jsonBills.put(new JSONObject(bill));
                }
                return jsonBills.toString();
            }
            else
            {
                return errorJson("user has no messages");
            }

        } catch (DatabaseException e)
        {
            e.printStackTrace();
        }
     return null;
    }

    @GET @Path("/fromCompany")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllUserBillsFromCompany(@Context HttpServletRequest request, @QueryParam("companyId") int companyId)
    {

        HttpSession session = request.getSession();
        GreenBillUser greenBillUser = (GreenBillUser) session.getAttribute("user");
        if (greenBillUser == null)
        {
            LOGGER.info("Cannot get user bills, user need to login again");
            return errorJson("Please login again");
        }
        if (companyId == 0)
        {
            // this query param was not arrived
            LOGGER.info("Cannot get company id ");
            return errorJson("Please company id");
        }
        //user loged in, get all the user bills
        String queryString = getAllUserBillsFromCompany.replaceFirst("\\?", greenBillUser.getUserId());
        queryString = queryString.replace("?", String.valueOf(companyId));
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();

        try
        {
            List<Map<String, Object>> bills = databaseHandler.runGetQuery(queryString);
            if (GeneralUtilities.hasData(bills))
            {
                JSONArray jsonBills = new JSONArray();
                for (Map<String, Object> bill : bills)
                {
                    jsonBills.put(new JSONObject(bill));
                }
                return jsonBills.toString();
            }
            else
            {
                return errorJson("user has no messages");
            }
        } catch (DatabaseException e)
        {
            LOGGER.error("DatabaseException " + e.getMessage(), e);
        }

        return null;
    }

    private String errorJson(String message)
    {
        return "{\"error\": \"" + message + "\"," +
                "\"redirect\": \"http://www.mygreenbill.com\"}";
    }
}
