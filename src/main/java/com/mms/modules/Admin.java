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
import com.mms.iframes.PasswordFrame;
import com.mms.iframes.UserFrame;
import com.mms.utilities.TableTools;
import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author J.B. Slade
 */
public class Admin {
    
    private final JTable userTable;
    private final JList woList, scheduleList, assetList, employeeList;
    
    public Admin(JTable u, JList w, JList s, JList a, JList e){
        userTable = u;
        woList = w;
        scheduleList = s;
        assetList = a;
        employeeList = e;
        TableTools.format(userTable);
        woList.setBackground(Color.WHITE);
        scheduleList.setBackground(Color.WHITE);
        assetList.setBackground(Color.WHITE);
        employeeList.setBackground(Color.WHITE);
    }
    
    public void load(){
        loadUsers();
        loadCustomWO();
        loadCustomSchedule();
        loadCustomAssets();
        loadCustomEmployees();
    }
    
    private void loadUsers(){
        int row = userTable.getSelectedRow() == -1 ? 0 : userTable.getSelectedRow();
        new Thread(){
            @Override
            public void run(){
                DefaultTableModel t = (DefaultTableModel)userTable.getModel();
                t.setRowCount(0);
                try {
                    ResultSet rs = Database.select("SELECT user_name, user_level FROM users");
                    while(rs.next()){
                        Object [] o = new Object[2];
                        o[0] = rs.getObject(1).toString().trim();
                        o[1] = rs.getObject(2).toString().trim();
                        if(!o[0].equals("Administrator")){
                            t.addRow(o);
                        }
                    }
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(t.getRowCount() != 0) userTable.setRowSelectionInterval(row, row);
            }
        }.start();
    }
    
    public void addUser(){
        UserFrame u = new UserFrame(userTable);
        u.setSize(MMS.DIAG_WIDTH, u.getHeight());
        u.setLocation(MMS.getMainFrame().getDesktopPane().getWidth()/2-u.getWidth()/2, MMS.getMainFrame().getDesktopPane().getHeight()/2-u.getHeight()/2-50);
        MMS.getMainFrame().getDesktopPane().add(u);
        MMS.getMainFrame().getDesktopPane().setLayer(u, 1);
        u.setVisible(true);
    }
    public void deleteUser(){
        String username = userTable.getValueAt(userTable.getSelectedRow(), 0).toString();
        if(InternalDialog.showInternalConfirmDialog(MMS.getMainFrame().getDesktopPane(), "Are you sure you want to delete '"+username+"'?", "Delete User", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null) == 0){
            Database.executeQuery("DELETE FROM users WHERE user_name = ?",
                    new Object[]{username});
            DefaultTableModel m = (DefaultTableModel)userTable.getModel();
            m.removeRow(userTable.getSelectedRow());
        }
    }
    public void resetUserPass(){
        PasswordFrame pd = new PasswordFrame(userTable.getValueAt(userTable.getSelectedRow(), 0).toString());
        pd.setSize(MMS.DIAG_WIDTH, pd.getHeight());
        pd.setLocation(MMS.getMainFrame().getDesktopPane().getWidth()/2-pd.getWidth()/2, MMS.getMainFrame().getDesktopPane().getHeight()/2-pd.getHeight()/2-50);
        MMS.getMainFrame().getDesktopPane().add(pd);
        MMS.getMainFrame().getDesktopPane().setLayer(pd, 1);
        pd.setVisible(true);
    }
    
    private void loadCustomWO(){
        new Thread(){
            @Override
            public void run(){
                try {
                    ResultSet rs = Database.select("SELECT custom_value FROM custom_fields WHERE custom_type = 'wo_type'");
                    while(rs.next()){
                        ((DefaultListModel)woList.getModel()).addElement(rs.getObject(1));
                    }
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                woList.setSelectedIndex(0);
            }
        }.start();
    }
    private void loadCustomSchedule(){
        new Thread(){
            @Override
            public void run(){
                try {
                    ResultSet rs = Database.select("SELECT custom_value FROM custom_fields WHERE custom_type = 'schedule_type'");
                    while(rs.next()){
                        ((DefaultListModel)scheduleList.getModel()).addElement(rs.getObject(1));
                    }
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                scheduleList.setSelectedIndex(0);
            }
        }.start();
    }
    private void loadCustomAssets(){
        new Thread(){
            @Override
            public void run(){
                try {
                    ResultSet rs = Database.select("SELECT custom_value FROM custom_fields WHERE custom_type = 'asset_type'");
                    while(rs.next()){
                        ((DefaultListModel)assetList.getModel()).addElement(rs.getObject(1));
                    }
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                assetList.setSelectedIndex(0);
            }
        }.start();
    }
    private void loadCustomEmployees(){
        new Thread(){
            @Override
            public void run(){
                try {
                    ResultSet rs = Database.select("SELECT custom_value FROM custom_fields WHERE custom_type = 'employee_dept'");
                    while(rs.next()){
                        ((DefaultListModel)employeeList.getModel()).addElement(rs.getObject(1));
                    }
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                employeeList.setSelectedIndex(0);
            }
        }.start();
    }
}
