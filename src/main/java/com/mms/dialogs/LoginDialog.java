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
import com.mms.MMS;
import com.mms.MainFrame;
import com.mms.utilities.OtherTools;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author J.B. Slade
 */
public class LoginDialog extends javax.swing.JDialog {

    private final ImageIcon toolbox = new ImageIcon(MMS.class.getResource("/toolbox.png"));
    private boolean success = false;
    public boolean success(){return success;}
    
    public LoginDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        passwordField.putClientProperty("JTextField.placeholderText", "Password");
        getRootPane().setDefaultButton(button);

        //Set users
        try {
            ResultSet rs = Database.select("SELECT user_name FROM users");
            while(rs.next()){     
                userCombo.addItem(rs.getString(1).trim());
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Select default user
        String u = MMS.getPrefs().get("default_user", "");
        if(!u.isEmpty()){
            userCombo.setSelectedItem(u);
            passwordField.requestFocus();
        }
        
        passwordField.requestFocus();
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
        logoLabel = new javax.swing.JLabel();
        separator = new javax.swing.JSeparator();
        userCombo = new javax.swing.JComboBox<>();
        passwordField = new javax.swing.JPasswordField();
        button = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Login");
        setResizable(false);

        logoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbox.png"))); // NOI18N
        logoLabel.setToolTipText("Click me!");
        logoLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoLabelMouseClicked(evt);
            }
        });

        separator.setOrientation(javax.swing.SwingConstants.VERTICAL);

        userCombo.setPreferredSize(passwordField.getPreferredSize());

        button.setText("Continue");
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
                .addComponent(logoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separator, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(userCombo, 0, 183, Short.MAX_VALUE)
                    .addComponent(passwordField)
                    .addComponent(button))
                .addContainerGap())
        );
        backPanelLayout.setVerticalGroup(
            backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(logoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(separator, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, backPanelLayout.createSequentialGroup()
                        .addComponent(userCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(button)))
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

    private void buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonActionPerformed
        String u = userCombo.getSelectedItem().toString(), p = new String(passwordField.getPassword());
        int ul = -1;
        if(p.isEmpty()) passwordField.requestFocus();
        else{
            String password = "", salt = "";
            try {
                ResultSet rs = Database.select("SELECT password, salt, user_level FROM users WHERE user_name = ?",
                        new Object[]{u});
                if(rs.next()){
                    password = rs.getObject(1).toString().trim();
                    salt = rs.getObject(2).toString().trim();
                    ul = rs.getInt(3);
                }
                else fail();             
            } catch (SQLException ex) {
                Logger.getLogger(LoginDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //Get hash
            p = Hasher.getHash(p, salt);

            if(p.contentEquals(password)){
                success = true;
                MMS.setUser(u);
                MMS.setUserLevel(ul);
                
                //Put user in preferences
                MMS.getPrefs().put("default_user", u);
                
                dispose();//LOGIN SUCCESSFUL
            }
            else fail();
        }
    }//GEN-LAST:event_buttonActionPerformed

    private void fail(){
        passwordField.requestFocus();
        passwordField.selectAll();
    }
    
    private void logoLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoLabelMouseClicked
        OtherTools.barrelRoll(logoLabel, toolbox);
    }//GEN-LAST:event_logoLabelMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel backPanel;
    private javax.swing.JButton button;
    private javax.swing.JLabel logoLabel;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JSeparator separator;
    private javax.swing.JComboBox<String> userCombo;
    // End of variables declaration//GEN-END:variables
}
