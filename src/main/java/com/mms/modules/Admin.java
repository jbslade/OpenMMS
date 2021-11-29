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
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author J.B. Slade
 */
public class Admin {
    
    private final JTable userTable;
    private final JList maintTypeList, assetList, employeeList;
    private final JTextField companyName;
    
    public Admin(JTable u, JList m, JList a, JList e, JTextField n){
        userTable = u;
        maintTypeList = m;
        assetList = a;
        employeeList = e;
        companyName = n;
        TableTools.format(userTable);
        maintTypeList.setBackground(Color.WHITE);
        assetList.setBackground(Color.WHITE);
        employeeList.setBackground(Color.WHITE);
    }
    
    public void load(){
        loadUsers();
        loadCustomMaintType();
        loadCustomAssets();
        loadCustomEmployees();
        loadGeneralSettings();
    }
    
    //Users
    private void loadUsers(){
        int row = userTable.getSelectedRow() == -1 ? 0 : userTable.getSelectedRow();
        new Thread(){
            @Override
            public void run(){
                DefaultTableModel t = (DefaultTableModel)userTable.getModel();
                t.setRowCount(0);
                try {
                    try (ResultSet rs = Database.select("SELECT user_name, user_level FROM users")) {
                        while(rs.next()){
                            Object [] o = new Object[2];
                            o[0] = rs.getObject(1).toString().trim();
                            switch(rs.getInt(2)){
                                case 0:
                                    o[1] = "Worker"; break;
                                case 1:
                                    o[1] = "Supervisor"; break;
                                case 2:
                                    o[1] = "Manager"; break;
                                case 3:
                                    o[1] = "Administrator"; break;
                            }
                            if(!o[0].equals("Administrator")){
                                t.addRow(o);
                            }
                        }
                    }
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
        if(userTable.getRowCount() != 0){
            String username = userTable.getValueAt(userTable.getSelectedRow(), 0).toString();
            if(InternalDialog.showInternalConfirmDialog(MMS.getMainFrame().getDesktopPane(), "Are you sure you want to delete '"+username+"'?", "Delete User", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null) == 0){
                Database.executeQuery("DELETE FROM users WHERE user_name = ?",
                        new Object[]{username});
                DefaultTableModel m = (DefaultTableModel)userTable.getModel();
                m.removeRow(userTable.getSelectedRow());
            }
        }
    }
    public void resetUserPass(){
        if(userTable.getRowCount() != 0){
            PasswordFrame pd = new PasswordFrame(userTable.getValueAt(userTable.getSelectedRow(), 0).toString());
            pd.setSize(MMS.DIAG_WIDTH, pd.getHeight());
            pd.setLocation(MMS.getMainFrame().getDesktopPane().getWidth()/2-pd.getWidth()/2, MMS.getMainFrame().getDesktopPane().getHeight()/2-pd.getHeight()/2-50);
            MMS.getMainFrame().getDesktopPane().add(pd);
            MMS.getMainFrame().getDesktopPane().setLayer(pd, 1);
            pd.setVisible(true);
        } 
    }
    
    //Custom WO types
    private void loadCustomMaintType(){
        new Thread(){
            @Override
            public void run(){
                try {
                    try (ResultSet rs = Database.select("SELECT custom_value FROM custom_fields WHERE custom_type = 'maintenance_type'")) {
                        while(rs.next()){
                            ((DefaultListModel)maintTypeList.getModel()).addElement(rs.getObject(1).toString().trim());
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                maintTypeList.setSelectedIndex(0);
            }
        }.start();
    }
    public void addCustomMaintType(){
        new Thread(){
            @Override
            public void run(){
                String value = JOptionPane.showInputDialog("Field name:");
                if(value != null && !value.isEmpty()){
                    Database.executeQuery("INSERT INTO custom_fields (custom_type, custom_value) VALUES ('maintenance_type', ?)",
                        new Object[]{value});
                    ((DefaultListModel)maintTypeList.getModel()).addElement(value);
                    maintTypeList.setSelectedIndex(maintTypeList.getLastVisibleIndex());
                }
            }
        }.start();
    }
    public void deleteCustomMaintType(){
        if(maintTypeList.getVisibleRowCount() != 0){
            new Thread(){
                @Override
                public void run(){
                    Database.executeQuery("DELETE FROM custom_fields WHERE custom_type = 'maintenance_type' AND custom_value = ?",
                            new Object[]{maintTypeList.getSelectedValue()});
                    ((DefaultListModel)maintTypeList.getModel()).removeElement(maintTypeList.getSelectedValue());
                    maintTypeList.setSelectedIndex(0);
                }
            }.start();
        }  
    }
    
    //Custom Asset types
    private void loadCustomAssets(){
        new Thread(){
            @Override
            public void run(){
                try {
                    try (ResultSet rs = Database.select("SELECT custom_value FROM custom_fields WHERE custom_type = 'asset_type'")) {
                        while(rs.next()){
                            ((DefaultListModel)assetList.getModel()).addElement(rs.getObject(1).toString().trim());
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                assetList.setSelectedIndex(0);
            }
        }.start();
    }
    public void addCustomAsset(){
        new Thread(){
            @Override
            public void run(){
                String value = JOptionPane.showInputDialog("Field name:");
                if(value != null && !value.isEmpty()){
                    Database.executeQuery("INSERT INTO custom_fields (custom_type, custom_value) VALUES ('asset_type', ?)",
                            new Object[]{value});
                    ((DefaultListModel)assetList.getModel()).addElement(value);
                    assetList.setSelectedIndex(assetList.getLastVisibleIndex());
                }
            }
        }.start();
    }
    public void deleteCustomAsset(){
        if(assetList.getVisibleRowCount() != 0){
            new Thread(){
                @Override
                public void run(){
                    Database.executeQuery("DELETE FROM custom_fields WHERE custom_type = 'asset_type' AND custom_value = ?",
                            new Object[]{assetList.getSelectedValue()});
                    ((DefaultListModel)assetList.getModel()).removeElement(assetList.getSelectedValue());
                    assetList.setSelectedIndex(0);
                }
            }.start();
        }  
    }
    
    //Custom employee departments
    private void loadCustomEmployees(){
        new Thread(){
            @Override
            public void run(){
                try {
                    try (ResultSet rs = Database.select("SELECT custom_value FROM custom_fields WHERE custom_type = 'employee_dept'")) {
                        while(rs.next()){
                            ((DefaultListModel)employeeList.getModel()).addElement(rs.getObject(1).toString().trim());
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                employeeList.setSelectedIndex(0);
            }
        }.start();
    }
    public void addCustomEmployee(){
        new Thread(){
            @Override
            public void run(){
                String value = JOptionPane.showInputDialog("Field name:");
                if(value != null && !value.isEmpty()){
                    Database.executeQuery("INSERT INTO custom_fields (custom_type, custom_value) VALUES ('employee_dept', ?)",
                            new Object[]{value});
                    ((DefaultListModel)employeeList.getModel()).addElement(value);
                    employeeList.setSelectedIndex(employeeList.getLastVisibleIndex());
                }
            }
        }.start();
    }
    public void deleteCustomEmployee(){
        if(employeeList.getVisibleRowCount() != 0){
            new Thread(){
                @Override
                public void run(){
                    Database.executeQuery("DELETE FROM custom_fields WHERE custom_type = 'employee_dept' AND custom_value = ?",
                            new Object[]{employeeList.getSelectedValue()});
                    ((DefaultListModel)employeeList.getModel()).removeElement(employeeList.getSelectedValue());
                    employeeList.setSelectedIndex(0);
                }
            }.start();
        }
    }
    
    //General settings
    private void loadGeneralSettings(){
        new Thread(){
            @Override
            public void run(){
                try {
                    try (ResultSet rs = Database.select("SELECT custom_value FROM custom_fields WHERE custom_type = 'system_name'")) {
                        if(rs.next()) companyName.setText(rs.getString(1));
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.start();
    }
    public void saveGeneralSettings(){
        new Thread(){
            @Override
            public void run(){
                Database.executeQuery("UPDATE custom_fields SET custom_value = ? WHERE custom_type = 'system_name'",
                        new Object[]{companyName.getText()});
                MMS.getMainFrame().setTitle(MMS.NAME+" - "+companyName.getText());
                InternalDialog.showInternalConfirmDialog(MMS.getMainFrame().getDesktopPane(), "General settings saved.", "General Settings", -1, JOptionPane.INFORMATION_MESSAGE, null);
            }
        }.start();
    }
}
