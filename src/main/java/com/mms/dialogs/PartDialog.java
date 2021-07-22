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
package com.mms.dialogs;

import com.mms.Database;
import com.mms.MMS;
import com.mms.utilities.TableTools;
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
public class PartDialog extends javax.swing.JInternalFrame {
    
    private final int row;
    private final JTable table;

    public PartDialog(JTable t, int r) {
        initComponents();
        table = t;
        row = r;
        getRootPane().setDefaultButton(continueButton);
        
        //Set right click listeners
        nameField.addMouseListener(MMS.getMouseListener());
        costField.addMouseListener(MMS.getMouseListener());
          
        //Edit
        if(row != -1){
            continueButton.setText("Save");
            nameField.setText(t.getModel().getValueAt(row, 1).toString());
            qtySpinner.setValue(t.getModel().getValueAt(row, 2).toString());
            costField.setValue(t.getModel().getValueAt(row, 3).toString());
            nameField.requestFocus();
            nameField.selectAll();
        }
        
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
        qtyLabel = new javax.swing.JLabel();
        qtySpinner = new javax.swing.JSpinner();
        continueButton = new javax.swing.JButton();
        priceLabel = new javax.swing.JLabel();
        costField = new javax.swing.JFormattedTextField();

        setClosable(true);
        setIconifiable(true);
        setTitle("New Part");
        setToolTipText("");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/dialogs/parts.png"))); // NOI18N
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

        qtyLabel.setText("Quantity:");

        qtySpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        qtySpinner.setPreferredSize(costField.getPreferredSize());

        continueButton.setText("Add");
        continueButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                continueButtonActionPerformed(evt);
            }
        });

        priceLabel.setText("Cost/Unit:");

        costField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.###"))));
        costField.setText("0.00");

        javax.swing.GroupLayout backPanelLayout = new javax.swing.GroupLayout(backPanel);
        backPanel.setLayout(backPanelLayout);
        backPanelLayout.setHorizontalGroup(
            backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nameField)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(continueButton))
                    .addComponent(nameLabel)
                    .addGroup(backPanelLayout.createSequentialGroup()
                        .addGroup(backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(qtyLabel)
                            .addComponent(qtySpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(priceLabel)
                            .addComponent(costField, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))))
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
                .addGroup(backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(qtyLabel)
                    .addComponent(priceLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(qtySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(costField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(continueButton)
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
                .addComponent(backPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void continueButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_continueButtonActionPerformed
        String name = nameField.getText();
        int qty = Integer.parseInt(qtySpinner.getValue().toString());
        double price;
        if(name.isEmpty()) nameField.requestFocus();
        else if(costField.getText().isEmpty()) costField.requestFocus();
        else{
            price = Double.parseDouble(costField.getText());
            if(row == -1){ //New part
                    //Get next no
                    int partNo = 0;
                    ResultSet rs = Database.select("SELECT MAX(id) FROM parts");
                    try {
                        if(rs.next()) partNo = rs.getInt(1);
                        rs.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(PartDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    partNo++;
                    //Insert into DB
                    Database.executeQuery("INSERT INTO parts (id, part_name, part_qty, part_cost, archived) VALUES (?, ?, ?, ?, 'N')",
                            new Object[]{partNo, name, qty, price});
                    //Insert into table
                    Object [] o = {partNo, name, qty, price};
                    DefaultTableModel m = (DefaultTableModel)table.getModel();
                    m.insertRow(0, o);
                    //Select new row
                    table.setRowSelectionInterval(0, 0);
                }
            else{ //Edit part
                //Get selected number
                int partNo = Integer.parseInt(table.getValueAt(row, 0).toString());
                //Update database
                Database.executeQuery("UPDATE parts SET part_name = ?, part_qty = ?, part_cost = ? WHERE id = ?",
                        new Object[]{name, qty, partNo, price});
                //Update table
                table.setValueAt(name, row, 1);
                table.setValueAt(qty, row, 2);
                table.setValueAt(price, row, 3);
                //Select updated row
                table.setRowSelectionInterval(row, row);
            }
            TableTools.resize(table);
            dispose();
        }
    }//GEN-LAST:event_continueButtonActionPerformed

    private void formInternalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameDeiconified
        getRootPane().setDefaultButton(continueButton);
    }//GEN-LAST:event_formInternalFrameDeiconified

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel backPanel;
    private javax.swing.JButton continueButton;
    private javax.swing.JFormattedTextField costField;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel priceLabel;
    private javax.swing.JLabel qtyLabel;
    private javax.swing.JSpinner qtySpinner;
    // End of variables declaration//GEN-END:variables
}
