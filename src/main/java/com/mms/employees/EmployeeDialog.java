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
package com.mms.employees;

import com.mms.MMS;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author J.B. Slade
 */
public class EmployeeDialog extends javax.swing.JInternalFrame {
    
    private int row = -1;
    private final JTable table;
    
    public EmployeeDialog(JTable t) {
        initComponents();
        getRootPane().setDefaultButton(button);
        table = t;
    }
    
    public EmployeeDialog(JTable t, int r) {
        initComponents();
        getRootPane().setDefaultButton(button);
        table = t;
        row = r;
        setTitle("Edit Employee");
        button.setText("Save");
        nameField.setText(t.getModel().getValueAt(row, 1).toString());
        descField.setText(t.getModel().getValueAt(row, 2).toString());
        nameField.requestFocus();
        nameField.selectAll();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        backPanel = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        descLabel = new javax.swing.JLabel();
        button = new javax.swing.JButton();
        descField = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setTitle("New Employee");
        setToolTipText("");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/iframes/employees.png"))); // NOI18N
        setPreferredSize(new java.awt.Dimension(241, 197));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameDeiconified(evt);
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        nameLabel.setText("Employee Name:");

        descLabel.setText("Employee Designation:");

        button.setText("Add");
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout backPanelLayout = new javax.swing.GroupLayout(backPanel);
        backPanel.setLayout(backPanelLayout);
        backPanelLayout.setHorizontalGroup(
            backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backPanelLayout.createSequentialGroup()
                        .addGap(0, 115, Short.MAX_VALUE)
                        .addComponent(button))
                    .addComponent(nameField)
                    .addComponent(nameLabel)
                    .addComponent(descLabel)
                    .addComponent(descField, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        backPanelLayout.setVerticalGroup(
            backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(descLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(descField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(button)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonActionPerformed
        String name = nameField.getText(), desc = descField.getText();
        if(name.isEmpty()) nameField.requestFocus();
        else if(desc.isEmpty()) descField.requestFocus();
        else{
            if(row == -1){ //New employee
                    //Get next no
                    int empNum = 0;
                    ResultSet rs = MMS.select("SELECT MAX(EmployeeNo) FROM Employee");
                    try {
                        if(rs.next()) empNum = rs.getInt(1);
                        rs.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(EmployeeDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    empNum++;
                    //Insert into DB
                    MMS.executeQuery("INSERT INTO Employees (EmployeeNo, EmployeeName, Designation, Archived) VALUES (?, ?, ?, 'N')",
                            new Object[]{empNum, name, desc});
                    //Insert into table
                    Object [] o = {empNum, name, desc};
                    DefaultTableModel m = (DefaultTableModel)table.getModel();
                    m.insertRow(0, o);
                    //Select new row
                    table.setRowSelectionInterval(0, 0);
                }
            else{ //Edit employee
                //Get selected number
                int empNum = Integer.parseInt(table.getValueAt(row, 0).toString());
                //Update database
                MMS.executeQuery("UPDATE Employees SET EmployeeName = ?, Designation = ? WHERE EmployeeNo = ?",
                        new Object[]{name, desc, empNum});
                //Update table
                table.setValueAt(name, row, 1);
                table.setValueAt(desc, row, 2);
                //Select updated row
                table.setRowSelectionInterval(row, row);
            }
            dispose();
        }
    }//GEN-LAST:event_buttonActionPerformed

    private void formInternalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameDeiconified
        getRootPane().setDefaultButton(button);
    }//GEN-LAST:event_formInternalFrameDeiconified

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel backPanel;
    private javax.swing.JButton button;
    private javax.swing.JTextField descField;
    private javax.swing.JLabel descLabel;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLabel;
    // End of variables declaration//GEN-END:variables
}
