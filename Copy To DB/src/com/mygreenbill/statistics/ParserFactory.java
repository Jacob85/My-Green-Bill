package com.mygreenbill.statistics;

import org.apache.log4j.Logger;

/**
 * Created by ipeleg on 5/29/14.
 */
public class ParserFactory
{
    //Create class logger
    private static final Logger LOGGER = Logger.getLogger(ParserFactory.class);

    public BillParser getParser(String parserName)
    {
        try
        {
            String fullParserName = "com.mygreenbill.statistics." + parserName;
            LOGGER.info(fullParserName);
            return (BillParser) Class.forName(fullParserName).newInstance();
        }
        catch (InstantiationException e)
        {
            LOGGER.error("InstantiationException in getParser");
            LOGGER.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            LOGGER.error("IllegalAccessException in getParser");
            LOGGER.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            LOGGER.error("ClassNotFoundException in getParser");
            LOGGER.error(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return null;
    }
}
