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
import com.mms.iframes.LocationFrame;
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
public class Locations {
    
    private final JTable table;
    private final JLabel loadLabel;
    
    public Locations(JTable t, JLabel l){
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
                    ResultSet rs = Database.select("SELECT id, location_name, location_desc, archived FROM locations ORDER BY id DESC");
                    int size = MMS.getPrefs().getInt("table_size_locations", -1), count = 0;
                    while(rs.next() && (size == -1 || count <= size)){
                        Object [] o = new Object[4];
                        o[0] = rs.getObject(1).toString().trim();
                        o[1] = rs.getObject(2).toString().trim();
                        o[2] = rs.getObject(3).toString().trim();
                        o[3] = rs.getObject(4).toString().trim();
                        if(o[3].equals("N")){
                            t.addRow(o);
                        }
                        count++;
                    }
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                TableTools.resize(table, 20);
                if(t.getRowCount() != 0) table.setRowSelectionInterval(row, row);
                loadLabel.setVisible(false);
            }
        }.start();
    }
    
    public void newLocation(){
        LocationFrame l = new LocationFrame(table, -1);
        l.setSize(MMS.DIAG_WIDTH, l.getHeight());
        l.setLocation(MMS.getMainFrame().getDesktopPane().getWidth()/2-l.getWidth()/2, MMS.getMainFrame().getDesktopPane().getHeight()/2-l.getHeight()/2-50);
        MMS.getMainFrame().getDesktopPane().add(l);
        MMS.getMainFrame().getDesktopPane().setLayer(l, 1);
        l.setVisible(true);
    }
    
    public void editLocation(){
        LocationFrame l = new LocationFrame(table, table.getSelectedRow());
        l.setSize(MMS.DIAG_WIDTH, l.getHeight());
        l.setTitle("Edit Location #"+table.getValueAt(table.getSelectedRow(), 0));
        l.setLocation(MMS.getMainFrame().getDesktopPane().getWidth()/2-l.getWidth()/2, MMS.getMainFrame().getDesktopPane().getHeight()/2-l.getHeight()/2-50);
        MMS.getMainFrame().getDesktopPane().add(l);
        MMS.getMainFrame().getDesktopPane().setLayer(l, 1);
        l.setVisible(true);
    }
    
    public void deleteLocation(){
        String message, title;
        if(table.getSelectedRowCount() > 1){
            message = "Are you sure you want to delete these ("+table.getSelectedRowCount()+") Locations?";
            title = "Delete Locations";
        }
        else{
            message = "Are you sure you want to delete '"+table.getValueAt(table.getSelectedRow(), 1)+"'?";
            title = "Delete Location #"+table.getValueAt(table.getSelectedRow(), 0);
        }
        if(InternalDialog.showInternalConfirmDialog(MMS.getMainFrame().getDesktopPane(), message, title, JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null) == 0){
            int rows [] = table.getSelectedRows();
            //Delete from DB
            for(int row : rows){
                int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                Database.executeQuery("DELETE FROM locations WHERE id = ?",
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
    
    public void archiveLocation(){
        String message, title;
        if(table.getSelectedRowCount() > 1){
            message = "Are you sure you want to archive these ("+table.getSelectedRowCount()+") Locations?";
            title = "Archive Locations";
        }
        else{
            message = "Are you sure you want to archive '"+table.getValueAt(table.getSelectedRow(), 1)+"'?";
            title = "Archive Location #"+table.getValueAt(table.getSelectedRow(), 0);
        }
        
        if(InternalDialog.showInternalConfirmDialog(MMS.getMainFrame().getDesktopPane(), message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null) == 0){
            int rows [] = table.getSelectedRows();
            //Set Archived = Y
            for(int row : rows){
                int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                Database.executeQuery("UPDATE locations SET archived = ? WHERE id = ?",
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
