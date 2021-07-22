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

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 *
 * @author J.B. Slade
 */
public class DateTools {
    
    public static Date convertToSQLDate(LocalDate d){
        return new Date(java.util.Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
    }
    
    public static LocalDate getNextDue(LocalDate date, String freq){
        switch(freq){
            case "Daily":
                break;
            case "Weekly":
                break;
            case "Monthly":
                break;
            case "Quarterly":
                break;
            case "Bianually":
                break;
            case "Anually":
                break;
        }
        return null;
    }
    
}
