/*
 * Copyright 2021 J.B. Slade.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mms.utilities;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;

/**
 *
 * @author J.B. Slade
 */
public class DateTools {
    
    public static java.sql.Date convertToSQLDate(LocalDate d){
        return new java.sql.Date(getDate(d).getTime());
    }
    
    private static java.util.Date getDate(LocalDate d){
        return java.util.Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    
    private static LocalDate getLocalDate(java.util.Date d){
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    
    public static LocalDate getDueDate(LocalDate date, String freq, int i){
        LocalDate d = null;
        Calendar c = Calendar.getInstance();
        c.setTime(getDate(date));
        switch(freq){
            case "Daily":
                c.add(Calendar.DAY_OF_MONTH, i*1);
                d = getLocalDate(c.getTime());
                break;
            case "Weekly":
                c.add(Calendar.DAY_OF_MONTH, i*7);
                d = getLocalDate(c.getTime());
                break;
            case "Monthly":
                c.add(Calendar.MONTH, i*1);
                d = getLocalDate(c.getTime());
                break;
            case "Quarterly":
                c.add(Calendar.MONTH, i*3);
                d = getLocalDate(c.getTime());
                break;
            case "Bianually":
                c.add(Calendar.MONTH, i*6);
                System.out.println(c.getTime());
                d = getLocalDate(c.getTime());
                break;
            case "Anually":
                c.add(Calendar.YEAR, i*1);
                d = getLocalDate(c.getTime());
                break;
        }
        return d;
    }
    
    public static boolean isToday(LocalDate date) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(getDate(date));
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }
    
    public static boolean isPassed(LocalDate date) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(getDate(date));
        return cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR) || (cal1.get(Calendar.DAY_OF_YEAR) > cal2.get(Calendar.DAY_OF_YEAR) && cal2.get(Calendar.YEAR) == cal1.get(Calendar.YEAR));
    }
    
    public static boolean isAfter(LocalDate date1, LocalDate date2){
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(getDate(date1));
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(getDate(date2));
        return cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR) || (cal1.get(Calendar.DAY_OF_YEAR) > cal2.get(Calendar.DAY_OF_YEAR) && cal2.get(Calendar.YEAR) == cal1.get(Calendar.YEAR));
    }
}
