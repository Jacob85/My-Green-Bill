package com.mygreenbill.statistics;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.pdfbox.lucene.LucenePDFDocument;

import java.io.File;
import java.io.IOException;

/**
 * Created by Jacob on 5/15/14.
 */
public class OrangeBillParser implements BillParser
{
    private String category = "Communication";
    private Logger LOGGER =Logger.getLogger(OrangeBillParser.class);

    @Override
    public double parseTotalAmountToPayFromPdf(File file)
    {
        try
        {
            LOGGER.info("Starting to parse " + file.getName());
            // the first line is for debugging
            //Document luceneDocument = LucenePDFDocument.getDocument(new File("C:\\Users\\Jacob\\Downloads\\2014-04-07.cycle2_0.pdf"));
            Document luceneDocument = LucenePDFDocument.getDocument(file);
            //LOGGER.info("File" + file.getAbsolutePath() + " as a String \r\n " + luceneDocument.toString());
            String summery = luceneDocument.get("summary");

            LOGGER.debug("Summery:" + summery);
            String[] splited = summery.split("\r\n");

            System.out.println("looping...........");
            for(String str : splited)
            {
                if (str.contains("ללוכ") || str.contains("כולל"))
                {
                    System.out.println(str);
                    str = str.replaceAll("[^\\d.]", "");
                    System.out.println(str);
                    String[] split = str.split("\\.");

                    double num = Double.parseDouble(new StringBuilder(split[0]).reverse().toString());
                    LOGGER.info("The Value parsed is: " + num);
                    return num;
                }
            }

        } catch (IOException e)
        {
            LOGGER.error("Error While reading PDF : " + file.getPath() + file.getName(), e);
        }

         return 0;
    }

    @Override
    public String getCategory()
    {
        return category;
    }
}
