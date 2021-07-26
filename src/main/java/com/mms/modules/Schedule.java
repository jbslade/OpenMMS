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

import com.mms.Database;
import com.mms.MMS;
import com.mms.MainFrame;
import com.mms.dialogs.InternalDialog;
import com.mms.iframes.ScheduleFrame;
import com.mms.utilities.DateTools;
import com.mms.utilities.TableTools;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author J.B. Slade
 */
public class Schedule {
    
    private final JTable table;
    private final JLabel loadLabel;
    
    public Schedule(JTable t, JLabel l){
        table = t;
        loadLabel = l;
        TableTools.format(table);
    }
    
    public void load(){
        int row = table.getSelectedRow() == -1 ? 0 : table.getSelectedRow();
        loadLabel.setVisible(true);
        new Thread(){
            @Override
            public void run(){
                DefaultTableModel t = (DefaultTableModel)table.getModel();
                t.setRowCount(0);
                try {
                    ResultSet rs = Database.select("SELECT t0.id, t0.schedule_name, t0.schedule_type, t0.schedule_freq, t0.schedule_last_date, t0.schedule_from_date, t0.asset_id, t1.asset_name, t0.location_id, t2.location_name, t0.Archived"
                            + " FROM schedule t0 JOIN assets t1 ON t0.asset_id = t1.id JOIN locations t2 ON t0.location_id = t2.id ORDER BY t0.id DESC");
                    int size = MMS.getPrefs().getInt("table_size_schedule", -1), count = 0;
                    while(rs.next() && (size == -1 || count <= size)){
                        Object [] o = new Object[9];
                        o[0] = rs.getObject(1).toString().trim();//id
                        o[1] = rs.getObject(2).toString().trim();//name
                        o[2] = rs.getObject(3).toString().trim();//type
                        o[3] = rs.getObject(4).toString().trim();//freq
                        
                        //Last date
                        LocalDate lastDate = rs.getDate(5).toLocalDate();
                        LocalDate fromDate = rs.getDate(6).toLocalDate();
                        if(DateTools.isAfter(fromDate, lastDate)) o[4] = "Never";
                        else o[4] = rs.getObject(5).toString().trim();
                        
                        //Due date
                        Object row;
                        LocalDate dueDate = DateTools.getDueDate(rs.getDate(5).toLocalDate(), o[3].toString(), 1);
                        if(DateTools.isToday(dueDate)) row = "<html><b>Today</b></html>";
                        else if(DateTools.isPassed(dueDate)) row = "<html><p style=\"color:#ff4b40\"><b>Overdue</b></p><html>";
                        else row = dueDate;
                        o[5] = row;
                        
                        o[6] = rs.getObject(7).toString().trim().equals("-1") ? "No Asset" : rs.getObject(7).toString().trim()+" - "+rs.getObject(8).toString().trim();//asset_id-name
                        o[7] = rs.getObject(9).toString().trim()+" - "+rs.getObject(10).toString().trim();//location_id-name
                        o[8] = rs.getObject(11).toString().trim();//archived
                        if(o[8].equals("N")){
                            t.addRow(o);
                        }
                        count++;
                    }
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                TableTools.resize(table);
                if(t.getRowCount() != 0) table.setRowSelectionInterval(row, row);
                loadLabel.setVisible(false);
            }
        }.start();
    }
    
    public void newSchedule(){
        ScheduleFrame s = new ScheduleFrame(table, -1, false);
        s.setSize(MMS.DIAG_WIDTH*2-10, s.getHeight());
        s.setLocation(MMS.getMainFrame().getDesktopPane().getWidth()/2-s.getWidth()/2, MMS.getMainFrame().getDesktopPane().getHeight()/2-s.getHeight()/2-50);
        MMS.getMainFrame().getDesktopPane().add(s);
        MMS.getMainFrame().getDesktopPane().setLayer(s, 1);
        s.setVisible(true);
    }
    
    public void editSchedule(){
        ScheduleFrame s = new ScheduleFrame(table, table.getSelectedRow(), false);
        s.setSize(MMS.DIAG_WIDTH*2-10, s.getHeight());
        s.setTitle("Edit Scheduled Task #"+table.getValueAt(table.getSelectedRow(), 0));
        s.setLocation(MMS.getMainFrame().getDesktopPane().getWidth()/2-s.getWidth()/2, MMS.getMainFrame().getDesktopPane().getHeight()/2-s.getHeight()/2-50);
        MMS.getMainFrame().getDesktopPane().add(s);
        MMS.getMainFrame().getDesktopPane().setLayer(s, 1);
        s.setVisible(true);
    }
    
    public void viewSchedule(){
        ScheduleFrame s = new ScheduleFrame(table, table.getSelectedRow(), true);
        s.setSize(MMS.DIAG_WIDTH*2-10, s.getHeight());
        s.setTitle("View Scheduled Task #"+table.getValueAt(table.getSelectedRow(), 0));
        s.setLocation(MMS.getMainFrame().getDesktopPane().getWidth()/2-s.getWidth()/2, MMS.getMainFrame().getDesktopPane().getHeight()/2-s.getHeight()/2-50);
        MMS.getMainFrame().getDesktopPane().add(s);
        MMS.getMainFrame().getDesktopPane().setLayer(s, 1);
        s.setVisible(true);
    }
    
    public void deleteSchedule(){
        String message, title;
        if(table.getSelectedRowCount() > 1){
            message = "Are you sure you want to delete these "+table.getSelectedRowCount()+" Scheduled Tasks?";
            title = "Delete Scheduled Tasks";
        }
        else{
            message = "Are you sure you want to delete '"+table.getValueAt(table.getSelectedRow(), 1)+"'?";
            title = "Delete Scheduled Task #"+table.getValueAt(table.getSelectedRow(), 0);
        }
        if(InternalDialog.showInternalConfirmDialog(MMS.getMainFrame().getDesktopPane(), message, title, JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null) == 0){
            int rows [] = table.getSelectedRows();
            //Delete from DB
            for(int row : rows){
                int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                Database.executeQuery("DELETE FROM schedule WHERE id = ?",
                        new Object[]{id});
            }
            //Delete from table
            for(int i = rows.length-1; i >= 0; i--){
                DefaultTableModel m = (DefaultTableModel)table.getModel();
                m.removeRow(rows[i]);
            }
            
            //Select first row
            if(table.getRowCount() != 0) table.setRowSelectionInterval(0, 0);
        }
    }
    
    public void archiveSchedule(){
        String message, title;
        if(table.getSelectedRowCount() > 1){
            message = "Are you sure you want to archive these "+table.getSelectedRowCount()+" Scheduled Tasks?";
            title = "Archive Scheduled Tasks";
        }
        else{
            message = "Are you sure you want to archive '"+table.getValueAt(table.getSelectedRow(), 1)+"'?";
            title = "Archive Scheduled Task #"+table.getValueAt(table.getSelectedRow(), 0);
        }
        
        if(InternalDialog.showInternalConfirmDialog(MMS.getMainFrame().getDesktopPane(), message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null) == 0){
            int rows [] = table.getSelectedRows();
            //Set Archived = Y
            for(int row : rows){
                int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                Database.executeQuery("UPDATE schedule SET archived = ? WHERE id = ?",
                        new Object[]{"Y", id});
            }
            //Delete from table
            for(int i = rows.length-1; i >= 0; i--){
                DefaultTableModel m = (DefaultTableModel)table.getModel();
                m.removeRow(rows[i]);
            }
            
            //Select first row
            if(table.getRowCount() != 0) table.setRowSelectionInterval(0, 0);
        }
    }
    
    public void completeTask(){
        int id = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString());
        String freq = table.getValueAt(table.getSelectedRow(), 3).toString();
        //Get last date
        ResultSet rs = Database.select("SELECT schedule_last_date FROM schedule WHERE id = ?",
                new Object[]{id});
        try {
            if(rs.next()){
                LocalDate lastDate = rs.getDate(1).toLocalDate();
                //Get next date that isn't passed or today
                LocalDate doneDate = DateTools.getDueDate(lastDate, freq , 1);
                while(DateTools.isPassed(doneDate)){
                    doneDate = DateTools.getDueDate(doneDate, freq , 1);
                }
                //Update last date (increase by one increment)
                Database.executeQuery("UPDATE schedule SET schedule_last_date = ? WHERE id = ?",
                    new Object[]{DateTools.convertToSQLDate(doneDate), id});
                //Update table
                table.setValueAt("<html><p style=\"color:#00b800\"><b>Complete</b></p><html>", table.getSelectedRow(), 5);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
