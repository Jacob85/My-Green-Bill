package com.mygreenbill.registration;

import com.mygreenbill.Exceptions.CorruptIdentityParameter;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jacob on 3/16/14.
 */
public class SimpleIdentityValidationResponse
{
    private String id;
    private String firstName;
    private String lastName;
    private String fatherName;
    private String addressString;
    private Date birthDate;
    private boolean isAlive;
    private Date deathDate;

    //Logger
    private static final Logger LOGGER = Logger.getLogger(SimpleIdentityValidationResponse.class);

    public SimpleIdentityValidationResponse(JSONObject jsonObject) throws CorruptIdentityParameter
    {
       // PropertyConfigurator.configure("WEB-INF/log4j.properties");
       try
       {
           this.id = jsonObject.getString("id");
           this.firstName = jsonObject.getString("first_name");
           this.lastName = jsonObject.getString("last_name");
           this.fatherName = jsonObject.getString("father_name");
           this.addressString = jsonObject.getString("address");
           this.birthDate = parseDate(jsonObject.getString("birth_date"));
           this.isAlive = Boolean.parseBoolean(jsonObject.getString("is_alive"));
           this.deathDate = parseDate(jsonObject.getString("death_date"));
       } catch (JSONException e)
       {
           LOGGER.error("Could not parse the Json identity: " + jsonObject.toString(), e);
           throw new CorruptIdentityParameter(e.getMessage());
       }
    }

    private Date parseDate(String toParse) throws CorruptIdentityParameter
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date toReturn = null;
        try
        {
            toReturn = dateFormat.parse(toParse);
        } catch (ParseException e)
        {
            LOGGER.error("Could not parse the String: " + toParse + "into Date object", e);
            throw new CorruptIdentityParameter(e.getMessage());
        }
       return toReturn;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getFatherName()
    {
        return fatherName;
    }

    public void setFatherName(String fatherName)
    {
        this.fatherName = fatherName;
    }

    public String getAddressString()
    {
        return addressString;
    }

    public void setAddressString(String addressString)
    {
        this.addressString = addressString;
    }

    public Date getBirthDate()
    {
        return birthDate;
    }

    public void setBirthDate(Date birthDate)
    {
        this.birthDate = birthDate;
    }

    public boolean isAlive()
    {
        return isAlive;
    }

    public void setAlive(boolean isAlive)
    {
        this.isAlive = isAlive;
    }

    public Date getDeathDate()
    {
        return deathDate;
    }

    public void setDeathDate(Date deathDate)
    {
        this.deathDate = deathDate;
    }
}
