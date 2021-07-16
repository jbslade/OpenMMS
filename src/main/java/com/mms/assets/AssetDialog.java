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
package com.mms.assets;

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
public class AssetDialog extends javax.swing.JInternalFrame {
    
    private int row = -1;
    private final JTable assetTable, locationTable;
    
    public AssetDialog(JTable t, JTable l) {
        initComponents();
        
        getRootPane().setDefaultButton(button);
        assetTable = t;
        locationTable = l;
        
        //Set locations
        for(int i = 0; i < locationTable.getModel().getRowCount(); i++){
            locationCombo.addItem(locationTable.getValueAt(i, 0).toString()+" - "+locationTable.getValueAt(i, 1).toString());
        }
    }
    
    public AssetDialog(JTable t, JTable l, int r) {
        initComponents();
        getRootPane().setDefaultButton(button);
        assetTable = t;
        locationTable = l;
        row = r;
        setTitle("Edit Asset");
        button.setText("Save");
        
        //Set locations
        for(int i = 0; i < locationTable.getModel().getRowCount(); i++){
            locationCombo.addItem(locationTable.getValueAt(i, 0).toString()+" - "+locationTable.getValueAt(i, 1).toString());
        }
        locationCombo.setSelectedItem(assetTable.getValueAt(r, 3));
        
        nameField.setText(t.getModel().getValueAt(r, 1).toString());
        descField.setText(t.getModel().getValueAt(r, 2).toString());
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
        descScroll = new javax.swing.JScrollPane();
        descField = new javax.swing.JTextArea();
        button = new javax.swing.JButton();
        locationLabel = new javax.swing.JLabel();
        locationCombo = new javax.swing.JComboBox<>();

        setClosable(true);
        setIconifiable(true);
        setTitle("New Asset");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/iframes/assets.png"))); // NOI18N
        setPreferredSize(new java.awt.Dimension(285, 374));
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

        nameLabel.setText("Name:");

        descLabel.setText("Description:");

        descField.setColumns(17);
        descField.setRows(4);
        descScroll.setViewportView(descField);

        button.setText("Add");
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonActionPerformed(evt);
            }
        });

        locationLabel.setText("Location:");

        javax.swing.GroupLayout backPanelLayout = new javax.swing.GroupLayout(backPanel);
        backPanel.setLayout(backPanelLayout);
        backPanelLayout.setHorizontalGroup(
            backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nameField)
                    .addComponent(descScroll)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(button))
                    .addComponent(locationCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(backPanelLayout.createSequentialGroup()
                        .addGroup(backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameLabel)
                            .addComponent(descLabel)
                            .addComponent(locationLabel))
                        .addGap(0, 0, Short.MAX_VALUE)))
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
                .addComponent(descScroll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(locationLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(locationCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        String name = nameField.getText(), desc = descField.getText(), locName = locationCombo.getSelectedItem().toString();
        int locNum = Integer.parseInt(locationCombo.getSelectedItem().toString().substring(0, locationCombo.getSelectedItem().toString().indexOf("-")-1));
        if(name.isEmpty()) nameField.requestFocus();
        else if(desc.isEmpty()) descField.requestFocus();
        else{
            if(row == -1){ //New asset
                    //Get next no
                    int assNum = 0;
                    ResultSet rs = MMS.select("SELECT MAX(AssetNo) FROM Assets");
                    try {
                        if(rs.next()) assNum = rs.getInt(1);
                        rs.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(AssetDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    assNum++;
                    //Insert into DB
                    MMS.executeQuery("INSERT INTO Assets (AssetNo, AssetName, AssetDescription, LocationNo, Archived) VALUES (?, ?, ?, ?, 'N')",
                            new Object[]{assNum, name, desc, locNum});
                    //Insert into table
                    Object [] o = {assNum, name, desc, locName};
                    DefaultTableModel m = (DefaultTableModel)assetTable.getModel();
                    m.insertRow(0, o);
                    //Select new row
                    assetTable.setRowSelectionInterval(0, 0);
                }
            else{ //Edit asset
                //Get selected number
                int assNum = Integer.parseInt(assetTable.getValueAt(row, 0).toString());
                //Update database
                MMS.executeQuery("UPDATE Assets SET AssetName = ?, AssetDescription = ?, LocationNo = ? WHERE AssetNo = ?",
                        new Object[]{name, desc, locNum, assNum});
                //Update table
                assetTable.setValueAt(name, row, 1);
                assetTable.setValueAt(desc, row, 2);
                assetTable.setValueAt(locName, row, 3);
                //Select updated row
                assetTable.setRowSelectionInterval(row, row);
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
    private javax.swing.JTextArea descField;
    private javax.swing.JLabel descLabel;
    private javax.swing.JScrollPane descScroll;
    private javax.swing.JComboBox<String> locationCombo;
    private javax.swing.JLabel locationLabel;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLabel;
    // End of variables declaration//GEN-END:variables
}
