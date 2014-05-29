package com.mygreenbill.statistics;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.pdfbox.lucene.LucenePDFDocument;

import java.io.File;
import java.io.IOException;

/**
 * Created by Jacob on 5/24/14.
 */
public class Yad2BillParser implements BillParser
{
    private String category = "LifeStyle";
    private static Logger LOGGER = Logger.getLogger(Yad2BillParser.class);

    @Override
    public double parseTotalAmountToPayFromPdf(File file)
    {
        Document luceneDocument = null;
        try
        {
            LOGGER.info("Starting to parse " + file.getName());
            luceneDocument = LucenePDFDocument.getDocument(file);
            LOGGER.info("Done to parse " + file.getName());
            //LOGGER.info("File" + file.getAbsolutePath() + " as a String \r\n " + luceneDocument.toString());
            String summery = luceneDocument.get("summary");
            //LOGGER.info("Summery:" + summery);
            String[] splited = summery.split("\r\n");

            System.out.println("looping...........");
            for(String str : splited)
            {
                if (str.contains("לתשלום") || str.contains("םולשתל") )
                {
                    System.out.println(str);
                    str = str.replaceAll("[^\\d.]", "");
                    System.out.println(str);
                    str = new StringBuilder(str).reverse().toString();
                    String[] split = str.split("\\.");

                    double num = Double.parseDouble(new StringBuilder(split[0]).reverse().toString());
                    LOGGER.info("The Value parsed is: " + num);
                    return num;
                }
            }
        }
        catch (IOException e)
        {
            LOGGER.error("IOException in parseTotalAmountToPayFromPdf");
            LOGGER.error(e.getLocalizedMessage());
            LOGGER.error(e.getStackTrace());
        }

        return 0;
    }

    @Override
    public String getCategory()
    {
        return category;
    }
}
