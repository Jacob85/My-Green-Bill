package com.mygreenbill.restful;


import com.ibm.icu.impl.duration.impl.DataRecord;
import com.mygreenbill.Exceptions.DatabaseException;
import com.mygreenbill.common.DateRange;
import com.mygreenbill.common.GeneralUtilities;
import com.mygreenbill.common.GreenBillUser;
import com.mygreenbill.database.DatabaseHandler;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.pdfbox.lucene.LucenePDFDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Jacob on 5/13/14.
 */
@Path("/stats")
public class StatisticsResource
{
    private final Logger LOGGER = Logger.getLogger(StatisticsResource.class);
    private final String getCurrentMonthStats = "call mygreenbilldb.GetUserStatsBetweenDates(?, ?, ?);";

    @GET @Path("/currentMonth")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCurrentMonthStats(@Context HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        GreenBillUser greenBillUser = (GreenBillUser) session.getAttribute("user");
        if (greenBillUser == null)
        {
            LOGGER.info("Cannot get user bills, user need to login again");
            return errorJson("Please login again");
        }
        DateRange dateRange = GeneralUtilities.getCurrentMonthDateRange();
        return getStatsBetweenDates(dateRange, greenBillUser);
    }

    @GET @Path("/lastMonth")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLastMonthStats(@Context HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        GreenBillUser greenBillUser = (GreenBillUser) session.getAttribute("user");
        if (greenBillUser == null)
        {
            LOGGER.info("Cannot get user bills, user need to login again");
            return errorJson("Please login again");
        }
        DateRange dateRange = GeneralUtilities.getLastMonthDateRange();
      return getStatsBetweenDates(dateRange, greenBillUser);
    }

    @GET @Path("/CurrMonthByCategory")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCurrMonthByCategoty(@Context HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        GreenBillUser greenBillUser = (GreenBillUser) session.getAttribute("user");
        if (greenBillUser == null)
        {
            LOGGER.info("Cannot get user bills, user need to login again");
            return errorJson("Please login again");
        }
        DateRange dateRange = GeneralUtilities.getCurrentMonthDateRange();
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String queryString = getCurrentMonthStats.replaceFirst("\\?", greenBillUser.getUserId());
        queryString = queryString.replaceFirst("\\?", "'" + dateFormat.format(dateRange.getStartDate()) + "'");
        queryString = queryString.replace("?", "'" + dateFormat.format(dateRange.getEndDate()) + "'");

        try
        {
            List<Map<String, Object>> stats = databaseHandler.runGetQuery(queryString);
            if (GeneralUtilities.hasData(stats))
            {
                Map<String, Integer> toReturn = new HashMap<String, Integer>();
                for (Map<String, Object> stat : stats)
                {
                    if (toReturn.containsKey(stat.get("category")))
                    {
                        toReturn.put((String)stat.get("category"), toReturn.get("category") + (Integer)stat.get("amount"));
                    }
                    else
                    {
                        toReturn.put((String)stat.get("category"), (Integer) stat.get("amount"));
                    }
                }
                LOGGER.debug("After aggregating the Stats: " + toReturn);
                JSONArray jsonArray = new JSONArray();
                JSONObject currObject = null;
                for (String key: toReturn.keySet())
                {
                    currObject = new JSONObject();
                    currObject.put("label", key);
                    currObject.put("value", toReturn.get(key));
                    jsonArray.put(currObject);
                }
                LOGGER.info("Returning Json: " + jsonArray.toString());
                return jsonArray.toString();
            }
            else
            {
                return new JSONObject().toString();         //if no stats available, return empty json
            }
        } catch (DatabaseException e)
        {
            LOGGER.error(e.getMessage(), e);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;

    }

    @GET @Path("/pastYear")
    @Produces(MediaType.APPLICATION_JSON)
    public String getPastYearStats(@Context HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        GreenBillUser greenBillUser = (GreenBillUser) session.getAttribute("user");
        if (greenBillUser == null)
        {
            LOGGER.info("Cannot get user bills, user need to login again");
            return errorJson("Please login again");
        }
        DateRange dateRange = GeneralUtilities.getPastYearDateRange();
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String queryString = getCurrentMonthStats.replaceFirst("\\?", greenBillUser.getUserId());
        queryString = queryString.replaceFirst("\\?", "'" + dateFormat.format(dateRange.getStartDate()) + "'");
        queryString = queryString.replace("?", "'" + dateFormat.format(dateRange.getEndDate()) + "'");

        try
        {
            List<Map<String, Object>> stats = databaseHandler.runGetQuery(queryString);
            if (GeneralUtilities.hasData(stats))
            {
                Map<String, Integer> toReturn = new HashMap<String, Integer>();
                Date currDate = null;
                SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
                for (Map<String, Object> statRaw : stats)
                {
                    currDate = dateFormat.parse((String) statRaw.get("recieved_date").toString());
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(currDate);
                    // current month
                    if (toReturn.containsKey(monthFormat.format(calendar.getTime())))
                    {
                        toReturn.put(monthFormat.format(calendar.getTime()), toReturn.get(monthFormat.format(calendar.getTime())) + (Integer)statRaw.get("amount"));
                    }
                    else
                    {
                        toReturn.put(monthFormat.format(calendar.getTime()), (Integer)statRaw.get("amount"));
                    }
                }
                LOGGER.debug("After aggregating the Stats: " + toReturn);
               /* JSONArray jsonArray = new JSONArray();
                JSONObject currObject = null;
                for (String key: toReturn.keySet())
                {
                    currObject = new JSONObject();
                    currObject.put("Month", key);
                    currObject.put("Value", toReturn.get(key));
                    jsonArray.put(currObject);
                }*/
                //LOGGER.info("Returning Json: " + jsonArray.toString());

                return sortResponseByTimeLine(toReturn);
            }
            else
            {
                return new JSONObject().toString();         //if no stats available, return empty json
            }
        } catch (DatabaseException e)
        {
            LOGGER.error(e.getMessage(), e);
        } catch (ParseException e)
        {
            LOGGER.error(e.getMessage(), e);
        }/* catch (JSONException e)
        {
            LOGGER.error(e.getMessage(), e);
        }*/
        return null;
    }

    private String sortResponseByTimeLine(Map<String, Integer> stats)
    {
        int NUMBER_OF_MONTH = 12;
        try
        {
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            JSONArray jsonArray = new JSONArray();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM");
            JSONObject currObject = null;
            for (int i =0 ; i < NUMBER_OF_MONTH ; i++ )
            {
                calendar.add(Calendar.MONTH, - i);
                String monthAsKey =  dateFormat.format(calendar.getTime());

                if (stats.containsKey(monthAsKey))  //if stats exists
                {
                    currObject = new JSONObject();
                    currObject.put("Month", monthAsKey);
                    currObject.put("Value", stats.get(monthAsKey));
                }
                else
                {
                    //enter empty json
                    currObject = new JSONObject();
                    currObject.put("Month", monthAsKey);
                    currObject.put("Value", 0);
                }
                jsonArray.put(NUMBER_OF_MONTH -1 -i, currObject);
                //reset the calendar to current date;
                calendar = Calendar.getInstance(TimeZone.getDefault());
            }
            return jsonArray.toString();
        } catch (JSONException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    private String getStatsBetweenDates(DateRange dateRange, GreenBillUser greenBillUser)
    {
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String queryString = getCurrentMonthStats.replaceFirst("\\?", greenBillUser.getUserId());
        queryString = queryString.replaceFirst("\\?", "'" + dateFormat.format(dateRange.getStartDate()) + "'");
        queryString = queryString.replace("?", "'" + dateFormat.format(dateRange.getEndDate()) + "'");

        try
        {
            List<Map<String, Object>> stats = databaseHandler.runGetQuery(queryString);
            if (GeneralUtilities.hasData(stats))
            {
                JSONArray jsonStats = new JSONArray();
                for (Map<String, Object> stat : stats)
                {
                    jsonStats.put(new JSONObject(stat));
                }
                return jsonStats.toString();
            }
            else
            {
                return new JSONObject().toString();         //if no stats available, return empty json
            }
        } catch (DatabaseException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        return null;

    }
    private String errorJson(String message)
    {
        return "{error: " + message + "," +
                "redirect: http://www.mygreenbill.com}";
    }
}
