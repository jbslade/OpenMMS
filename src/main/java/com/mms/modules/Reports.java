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
package com.mms.modules;

import com.mms.MMS;
import com.mms.iframes.AssetFrame;
import com.mms.iframes.ReportFrame;
import com.mms.utilities.TableTools;
import javax.swing.JComboBox;
import javax.swing.JTable;

/**
 *
 * @author J.B. Slade
 */
public class Reports {
    
    private final JTable table;
    private final JComboBox combo;
    
    public Reports(JTable t, JComboBox b){
        table = t;
        combo = b;
        TableTools.format(table);
    }
    
    public void load(){
        //Load reports into combo
    }
    
    public void runReport(){
        //Run report and update table
    }
    
    public void newReport(){
        ReportFrame r = new ReportFrame();
        r.setSize(MMS.DIAG_WIDTH, r.getHeight());
        r.setLocation(MMS.getMainFrame().getDesktopPane().getWidth()/2-r.getWidth()/2, MMS.getMainFrame().getDesktopPane().getHeight()/2-r.getHeight()/2-50);
        MMS.getMainFrame().getDesktopPane().add(r);
        MMS.getMainFrame().getDesktopPane().setLayer(r, 1);
        r.setVisible(true);
    }
    
    public void editReport(){
        
    }
    
    public void deleteReport(){
        
    }
}
