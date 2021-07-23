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
import com.mms.modules.iframes.EmployeeFrame;
import com.mms.dialogs.InternalDialog;
import com.mms.utilities.TableTools;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class Employees {
    
    private final JTable table;
    private final JLabel loadLabel;
    
    public Employees(JTable t, JLabel l){
        table = t;
        loadLabel = l;
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
                    ResultSet rs = Database.select("SELECT id, employee_name, employee_desc, employee_dept, archived FROM employees ORDER BY id DESC");
                    int size = MMS.getPrefs().getInt("table_size_employees", -1), count = 0;
                    while(rs.next() && (size == -1 || count <= size)){
                        Object [] o = new Object[5];
                        o[0] = rs.getObject(1).toString().trim();
                        o[1] = rs.getObject(2).toString().trim();
                        o[2] = rs.getObject(3).toString().trim();
                        o[3] = rs.getObject(4).toString().trim();
                        o[4] = rs.getObject(5).toString().trim();
                        if(o[4].equals("N")){
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
    
    public void newEmployee(){
        EmployeeFrame e = new EmployeeFrame(table, -1);
        e.setSize(MMS.DIAG_WIDTH, e.getHeight());
        e.setLocation(MMS.getMainFrame().getDesktopPane().getWidth()/2-e.getWidth()/2, MMS.getMainFrame().getDesktopPane().getHeight()/2-e.getHeight()/2-50);
        MMS.getMainFrame().getDesktopPane().add(e);
        MMS.getMainFrame().getDesktopPane().setLayer(e, 1);
        e.setVisible(true);
    }
    
    public void editEmployee(){
        EmployeeFrame e = new EmployeeFrame(table, table.getSelectedRow());
        e.setSize(MMS.DIAG_WIDTH, e.getHeight());
        e.setTitle("Edit Employee #"+table.getValueAt(table.getSelectedRow(), 0));
        e.setLocation(MMS.getMainFrame().getDesktopPane().getWidth()/2-e.getWidth()/2, MMS.getMainFrame().getDesktopPane().getHeight()/2-e.getHeight()/2-50);
        MMS.getMainFrame().getDesktopPane().add(e);
        MMS.getMainFrame().getDesktopPane().setLayer(e, 1);
        e.setVisible(true);
    }
    
    public void deleteEmployee(){
        String message, title;
        if(table.getSelectedRowCount() > 1){
            message = "Are you sure you want to delete these "+table.getSelectedRowCount()+" Employees?";
            title = "Delete Employees";
        }
        else{
            message = "Are you sure you want to delete '"+table.getValueAt(table.getSelectedRow(), 1)+"'?";
            title = "Delete Employee #"+table.getValueAt(table.getSelectedRow(), 0);
        }
        if(InternalDialog.showInternalConfirmDialog(MMS.getMainFrame().getDesktopPane(), message, title, JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null) == 0){
            int rows [] = table.getSelectedRows();
            //Delete from DB
            for(int row : rows){
                int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                Database.executeQuery("DELETE FROM employees WHERE id = ?",
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
    
    public void archiveEmployee(){
        String message, title;
        if(table.getSelectedRowCount() > 1){
            message = "Are you sure you want to archive these "+table.getSelectedRowCount()+" Employees?";
            title = "Archive Employees";
        }
        else{
            message = "Are you sure you want to archive '"+table.getValueAt(table.getSelectedRow(), 1)+"'?";
            title = "Archive Employee #"+table.getValueAt(table.getSelectedRow(), 0);
        }
        
        if(InternalDialog.showInternalConfirmDialog(MMS.getMainFrame().getDesktopPane(), message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null) == 0){
            int rows [] = table.getSelectedRows();
            //Set Archived = Y
            for(int row : rows){
                int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                Database.executeQuery("UPDATE employees SET archived = ? WHERE id = ?",
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
}
