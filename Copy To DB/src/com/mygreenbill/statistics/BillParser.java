package com.mygreenbill.statistics;

import java.io.File;

/**
 * Created by Jacob on 5/15/14.
 */
public interface BillParser
{
    public double parseTotalAmountToPayFromPdf(File file);
    public String getCategory();
}
