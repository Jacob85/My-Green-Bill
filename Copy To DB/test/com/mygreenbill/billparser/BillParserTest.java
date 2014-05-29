package com.mygreenbill.billparser;

import com.mygreenbill.statistics.BillParser;
import com.mygreenbill.statistics.CellcomBillParser;
import com.mygreenbill.statistics.OrangeBillParser;
import com.mygreenbill.statistics.Yad2BillParser;
import junit.framework.TestCase;

import java.io.File;

/**
 * Created by Jacob on 5/24/14.
 */
public class BillParserTest extends TestCase
{
    public void testParseTotalAmountToPayFromPdf() throws Exception
    {
        System.out.println(new File(".").getAbsolutePath());
        BillParser billParser = new Yad2BillParser();
        double amount = billParser.parseTotalAmountToPayFromPdf(new File("./Copy To DB/conf/bills/yad2.pdf"));
        assertEquals(amount, 12.0);

        billParser = new CellcomBillParser();
        amount = billParser.parseTotalAmountToPayFromPdf(new File("./Copy To DB/conf/bills/cellcom.pdf"));
        assertEquals(amount, 112.0);

        billParser = new OrangeBillParser();
        amount = billParser.parseTotalAmountToPayFromPdf(new File("./Copy To DB/conf/bills/orange.pdf"));
        assertEquals(amount, 173.0);
    }
}
