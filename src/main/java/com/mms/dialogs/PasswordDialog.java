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
import com.mms.utilities.Hasher;
import javax.swing.JOptionPane;

/**
 *
 * @author J.B. Slade
 */
public class PasswordDialog extends javax.swing.JInternalFrame {

    private final String user;
    
    public PasswordDialog(String u) {
        initComponents();
        getRootPane().setDefaultButton(button);
        user = u;
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
        newPassLabel = new javax.swing.JLabel();
        passField = new javax.swing.JPasswordField();
        retypeLabel = new javax.swing.JLabel();
        retypeField = new javax.swing.JPasswordField();
        button = new javax.swing.JButton();

        setClosable(true);
        setTitle("Change Password");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/user.png"))); // NOI18N

        newPassLabel.setText("New Password:");

        retypeLabel.setText("Retype Password:");

        button.setText("Save");
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
                    .addGroup(backPanelLayout.createSequentialGroup()
                        .addGroup(backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(newPassLabel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(retypeLabel, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 124, Short.MAX_VALUE))
                    .addComponent(retypeField)
                    .addComponent(passField, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(button)))
                .addContainerGap())
        );
        backPanelLayout.setVerticalGroup(
            backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(newPassLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(passField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(retypeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(retypeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        String pass = passField.getText(), retype = retypeField.getText();
        if(pass.isEmpty()) passField.requestFocus();
        else if(retype.isEmpty()) retypeField.requestFocus();
        else if(!pass.equals(retype)){
            retypeField.requestFocus();
            retypeField.selectAll();
        }
        else{
            String salt = Hasher.getSalt();
            pass = Hasher.getHash(pass, salt);
            Database.executeQuery("UPDATE users SET salt = ?, password = ? WHERE user_name = ?",
                new Object[]{salt, pass, user});
            InternalDialog.showInternalConfirmDialog(this, "Password changed succesfully.", "Change Password", -1, JOptionPane.INFORMATION_MESSAGE, null);
            dispose();
        }
    }//GEN-LAST:event_buttonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel backPanel;
    private javax.swing.JButton button;
    private javax.swing.JLabel newPassLabel;
    private javax.swing.JPasswordField passField;
    private javax.swing.JPasswordField retypeField;
    private javax.swing.JLabel retypeLabel;
    // End of variables declaration//GEN-END:variables
}
