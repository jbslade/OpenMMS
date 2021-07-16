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
package com.mms;

import com.mms.utilities.Hasher;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author J.B. Slade
 */
public class Setup extends javax.swing.JDialog {
    
    private final Component thisForm = this;
    
    /**
     * Creates new form Setup
     * @param parent
     * @param modal
     */
    public Setup(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setIconImage(MMS.systemIcon.getImage());
        setLocationRelativeTo(parent);
        getRootPane().setDefaultButton(continueButton);
        try {
            derbyDirField.setText(new File(MMS.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getPath());
        } catch (URISyntaxException ex) {
            Logger.getLogger(Setup.class.getName()).log(Level.SEVERE, null, ex);
        }
        MMS.phf.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        typeRadioGroup = new javax.swing.ButtonGroup();
        derbyRadioGroup = new javax.swing.ButtonGroup();
        serverRadioGroup = new javax.swing.ButtonGroup();
        dbSelector = new javax.swing.JFileChooser();
        backPanel = new javax.swing.JPanel();
        derbyRadio = new javax.swing.JRadioButton();
        serverRadio = new javax.swing.JRadioButton();
        microsoftPanel = new javax.swing.JPanel();
        srvrField = new javax.swing.JTextField();
        srvrLabel = new javax.swing.JLabel();
        srvrDBField = new javax.swing.JTextField();
        srvrDBLabel = new javax.swing.JLabel();
        srvrUsrLabel = new javax.swing.JLabel();
        srvrUsrField = new javax.swing.JTextField();
        srvrPassField = new javax.swing.JPasswordField();
        srvrPassLabel = new javax.swing.JLabel();
        mssqlRadio = new javax.swing.JRadioButton();
        derbyServerRadio = new javax.swing.JRadioButton();
        derbyPanel = new javax.swing.JPanel();
        derbyNewRadio = new javax.swing.JRadioButton();
        derbyNameField = new javax.swing.JTextField();
        derbyNameLabel = new javax.swing.JLabel();
        derbyExistingRadio = new javax.swing.JRadioButton();
        derbyDirLabel = new javax.swing.JLabel();
        derbyDirField = new javax.swing.JTextField();
        derbyDirButton = new javax.swing.JButton();
        continueButton = new javax.swing.JButton();
        statusLabel = new javax.swing.JLabel();

        dbSelector.setDialogTitle("Select Database Location");
        dbSelector.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Database Setup");
        setPreferredSize(new java.awt.Dimension(320, 557));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        typeRadioGroup.add(derbyRadio);
        derbyRadio.setSelected(true);
        derbyRadio.setText("Standalone (Derby)");
        derbyRadio.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                derbyRadioStateChanged(evt);
            }
        });

        typeRadioGroup.add(serverRadio);
        serverRadio.setText("Database Server");
        serverRadio.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                serverRadioStateChanged(evt);
            }
        });

        srvrField.setEnabled(false);
        srvrField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                srvrFieldFocusGained(evt);
            }
        });

        srvrLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        srvrLabel.setText("Address:");
        srvrLabel.setEnabled(false);

        srvrDBField.setText(MMS.NAME+"_DB");
        srvrDBField.setEnabled(false);
        srvrDBField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                srvrDBFieldFocusGained(evt);
            }
        });

        srvrDBLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        srvrDBLabel.setText("Database:");
        srvrDBLabel.setEnabled(false);

        srvrUsrLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        srvrUsrLabel.setText("Username:");
        srvrUsrLabel.setEnabled(false);

        srvrUsrField.setEnabled(false);
        srvrUsrField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                srvrUsrFieldFocusGained(evt);
            }
        });

        srvrPassField.setEnabled(false);
        srvrPassField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                srvrPassFieldFocusGained(evt);
            }
        });

        srvrPassLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        srvrPassLabel.setText("Password:");
        srvrPassLabel.setEnabled(false);

        serverRadioGroup.add(mssqlRadio);
        mssqlRadio.setSelected(true);
        mssqlRadio.setText("MSSQL");
        mssqlRadio.setEnabled(false);

        serverRadioGroup.add(derbyServerRadio);
        derbyServerRadio.setText("Derby");
        derbyServerRadio.setEnabled(false);

        javax.swing.GroupLayout microsoftPanelLayout = new javax.swing.GroupLayout(microsoftPanel);
        microsoftPanel.setLayout(microsoftPanelLayout);
        microsoftPanelLayout.setHorizontalGroup(
            microsoftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(microsoftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(microsoftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(microsoftPanelLayout.createSequentialGroup()
                        .addComponent(mssqlRadio)
                        .addGap(2, 2, 2)
                        .addComponent(derbyServerRadio)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(microsoftPanelLayout.createSequentialGroup()
                        .addGroup(microsoftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(srvrDBLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(srvrPassLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(srvrLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(srvrUsrLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(microsoftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(srvrField, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(srvrDBField, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                            .addComponent(srvrUsrField, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(srvrPassField))))
                .addContainerGap())
        );
        microsoftPanelLayout.setVerticalGroup(
            microsoftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, microsoftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(microsoftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mssqlRadio)
                    .addComponent(derbyServerRadio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(microsoftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(srvrLabel)
                    .addComponent(srvrField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(microsoftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(srvrDBLabel)
                    .addComponent(srvrDBField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(microsoftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(srvrUsrLabel)
                    .addComponent(srvrUsrField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(microsoftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(srvrPassLabel)
                    .addComponent(srvrPassField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        derbyRadioGroup.add(derbyNewRadio);
        derbyNewRadio.setSelected(true);
        derbyNewRadio.setText("Create new database");
        derbyNewRadio.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                derbyNewRadioStateChanged(evt);
            }
        });

        derbyNameField.setText(MMS.NAME+"_DB");
        derbyNameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                derbyNameFieldFocusGained(evt);
            }
        });

        derbyNameLabel.setText("Name:");

        derbyRadioGroup.add(derbyExistingRadio);
        derbyExistingRadio.setText("Open existing database");

        derbyDirLabel.setText("Location:");

        derbyDirField.setEditable(false);
        derbyDirField.setText("Default");

        derbyDirButton.setText("...");
        derbyDirButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                derbyDirButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout derbyPanelLayout = new javax.swing.GroupLayout(derbyPanel);
        derbyPanel.setLayout(derbyPanelLayout);
        derbyPanelLayout.setHorizontalGroup(
            derbyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(derbyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(derbyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(derbyPanelLayout.createSequentialGroup()
                        .addGroup(derbyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(derbyPanelLayout.createSequentialGroup()
                                .addComponent(derbyExistingRadio)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(derbyDirField))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(derbyDirButton))
                    .addComponent(derbyNewRadio)
                    .addComponent(derbyDirLabel)
                    .addGroup(derbyPanelLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(derbyNameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(derbyNameField)))
                .addContainerGap())
        );
        derbyPanelLayout.setVerticalGroup(
            derbyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(derbyPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(derbyNewRadio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(derbyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(derbyNameLabel)
                    .addComponent(derbyNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(derbyExistingRadio, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(derbyDirLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(derbyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(derbyDirField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(derbyDirButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        continueButton.setText("Continue");
        continueButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                continueButtonActionPerformed(evt);
            }
        });

        statusLabel.setText("   ");

        javax.swing.GroupLayout backPanelLayout = new javax.swing.GroupLayout(backPanel);
        backPanel.setLayout(backPanelLayout);
        backPanelLayout.setHorizontalGroup(
            backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(microsoftPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(derbyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(backPanelLayout.createSequentialGroup()
                        .addGroup(backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(backPanelLayout.createSequentialGroup()
                                .addGroup(backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(serverRadio)
                                    .addComponent(derbyRadio))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backPanelLayout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(statusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(continueButton)))
                        .addContainerGap())))
        );
        backPanelLayout.setVerticalGroup(
            backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(derbyRadio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(derbyPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(serverRadio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(microsoftPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(continueButton)
                    .addComponent(statusLabel))
                .addContainerGap())
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

    private void continueButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_continueButtonActionPerformed
        if(derbyRadio.isSelected()){ //DERBY STANDALONE
            setPanelEnabled(backPanel, false);
            statusLabel.setIcon(MMS.ajaxIcon);
            statusLabel.setForeground(Color.black);
            statusLabel.setText("Connecting...");
            new Thread(){
                @Override
                public void run(){
                    try {
                        //Register driver
                        DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
                        //Set name & directory
                        String dbName, dbDir;
                        if(derbyNewRadio.isSelected()){
                            dbName = derbyNameField.getText();
                            dbDir = derbyDirField.getText()+"\\";
                        }
                        else{
                            dbName = derbyDirField.getText().substring(derbyDirField.getText().lastIndexOf("\\")+1, derbyDirField.getText().length());
                            dbDir = derbyDirField.getText().substring(0, derbyDirField.getText().lastIndexOf("\\"));
                        }         
                        System.setProperty("user.dir", dbDir);
                        System.setProperty("derby.system.home", dbDir);
                        //Try connect
                        if(derbyNewRadio.isSelected()){//Create new
                            File f = new File(dbDir+"\\"+dbName);
                            if(f.exists()){ //If database exists
                                if(JOptionPane.showConfirmDialog(thisForm, "A database with the same name was found at this location.\n\n"
                                        + "Would you like to overwrite it?", "Database Exists", JOptionPane.YES_NO_OPTION) == 0){
                                    //Delete old database directory
                                    deleteDir(f);
                                }
                                else{
                                    setPanelEnabled(backPanel, true);
                                    statusLabel.setIcon(null);
                                    statusLabel.setText("");
                                    this.stop();
                                }
                            }
                            Connection conn = DriverManager.getConnection("jdbc:derby:"+dbName+";create=true");
                            MMS.setConnection(conn);
                            System.out.println("[DATABASE] Connected to Derby: "+dbName);
                            
                            //Create database tables
                            statusLabel.setText("Creating DB...");
                            createTables();
                        }
                        else{ //Open existing
                            MMS.setConnection(DriverManager.getConnection("jdbc:derby:"+dbName));
                            System.out.println("[DATABASE] Connected to Derby: "+dbName);
                        }

                        //Put preferences
                        MMS.getPrefs().put("dbType", "derby");
                        MMS.getPrefs().put("derbyHome", dbDir);
                        MMS.getPrefs().put("derbyName", dbName);
                        MMS.getPrefs().putBoolean("firstRun", false);
                       
                        dispose();
                    } catch (SQLException ex) {
                        Logger.getLogger(MMS.class.getName()).log(Level.SEVERE, null, ex);
                        setPanelEnabled(backPanel, true);
                        statusLabel.setIcon(null);
                        statusLabel.setForeground(Color.red);
                        statusLabel.setText("Connection failed!");
                    }
                }
            }.start();
        }
        else{ //DATABASE SERVER
            String srvr = srvrField.getText(), 
               db = srvrDBField.getText(),
               usr = srvrUsrField.getText(),
               pass = srvrPassField.getText();
            if(srvr.isEmpty()) srvrField.requestFocus();
            else if(db.isEmpty()) srvrDBField.requestFocus();
            else if(usr.isEmpty()) srvrUsrField.requestFocus();
            else if(pass.isEmpty()) srvrPassField.requestFocus();
            else{
                setPanelEnabled(backPanel, false);
                statusLabel.setIcon(MMS.ajaxIcon);
                statusLabel.setForeground(Color.black);
                statusLabel.setText("Connecting...");
                new Thread(){
                    @Override
                    public void run(){                        
                        try {
                            if(mssqlRadio.isSelected()){ //MSSQL
                                //Register driver
                                DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());

                                //Try connect
                                MMS.setConnection(DriverManager.getConnection("jdbc:sqlserver://"+srvr+";databaseName="+db+";user="+usr+";password="+pass+";loginTimeout=4"));
                                System.out.println("[DATABASE] Connected to MSSQL: "+db);

                                //Put preferences
                                MMS.getPrefs().put("dbType", "mssql");
                                MMS.getPrefs().put("mssqlsrvr", srvr);
                                MMS.getPrefs().put("mssqldb", db);
                                MMS.getPrefs().put("mssqlusr", usr);
                                MMS.getPrefs().put("mssqlpass", pass);
                                MMS.getPrefs().putBoolean("firstRun", false);

                                dispose();
                            }
                            else{ //Derby server
                                //CONNECT TO DERBY SERVER
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(MMS.class.getName()).log(Level.SEVERE, null, ex);
                            setPanelEnabled(backPanel, true);
                            statusLabel.setIcon(null);
                            statusLabel.setForeground(Color.red);
                            statusLabel.setText("Connection failed!");
                            srvrField.requestFocus();
                        }
                    }
                }.start();
            }
        }  
    }//GEN-LAST:event_continueButtonActionPerformed

    private void serverRadioStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_serverRadioStateChanged
        if(backPanel.isEnabled()) setPanelEnabled(microsoftPanel, serverRadio.isSelected());
    }//GEN-LAST:event_serverRadioStateChanged

    private void derbyDirButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_derbyDirButtonActionPerformed
        if(dbSelector.showDialog(this, "Select") == JFileChooser.APPROVE_OPTION){
            derbyDirField.setText(dbSelector.getSelectedFile()+"");
        }
    }//GEN-LAST:event_derbyDirButtonActionPerformed

    private void derbyNewRadioStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_derbyNewRadioStateChanged
        derbyNameField.setEnabled(derbyNewRadio.isSelected());
        derbyNameLabel.setEnabled(derbyNewRadio.isSelected());
    }//GEN-LAST:event_derbyNewRadioStateChanged

    private void derbyRadioStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_derbyRadioStateChanged
        if(backPanel.isEnabled()) setPanelEnabled(derbyPanel, derbyRadio.isSelected());
    }//GEN-LAST:event_derbyRadioStateChanged

    private void derbyNameFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_derbyNameFieldFocusGained
        derbyNameField.selectAll();
    }//GEN-LAST:event_derbyNameFieldFocusGained

    private void srvrDBFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_srvrDBFieldFocusGained
        srvrDBField.selectAll();
    }//GEN-LAST:event_srvrDBFieldFocusGained

    private void srvrFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_srvrFieldFocusGained
        srvrField.selectAll();
    }//GEN-LAST:event_srvrFieldFocusGained

    private void srvrUsrFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_srvrUsrFieldFocusGained
        srvrUsrField.selectAll();
    }//GEN-LAST:event_srvrUsrFieldFocusGained

    private void srvrPassFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_srvrPassFieldFocusGained
        srvrPassField.selectAll();
    }//GEN-LAST:event_srvrPassFieldFocusGained

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        MMS.shutdown();
    }//GEN-LAST:event_formWindowClosing

    private void createTables(){
        //Users
        MMS.executeQuery("CREATE TABLE Users("
                + "Username VARCHAR(50) PRIMARY KEY,"
                + "Password VARCHAR(50),"
                + "Salt VARCHAR(16),"
                + "Logged VARCHAR(1)"
                + ")");
        String salt = Hasher.getSalt(), pass = Hasher.getHash("admin", salt);
        MMS.executeQuery("INSERT INTO Users (Username, Password, Salt, Logged) VALUES (?, ?, ?, ?)",
                new Object[]{"Administrator", pass, salt, "N"});
        //Locations
        MMS.executeQuery("CREATE TABLE Locations("
                + "LocationNo INT PRIMARY KEY,"
                + "LocationName VARCHAR(100),"
                + "LocationDescription VARCHAR(100),"
                + "Archived VARCHAR(1)"
                + ")");
        //Assets
        MMS.executeQuery("CREATE TABLE Assets("
                + "AssetNo INT PRIMARY KEY,"
                + "AssetName VARCHAR(100),"
                + "AssetDescription VARCHAR(100),"
                + "LocationNo INT,"
                + "Archived VARCHAR(1)"
                + ")");
        //Employees
        MMS.executeQuery("CREATE TABLE Employees("
                + "EmployeeNo INT PRIMARY KEY,"
                + "EmployeeName VARCHAR(100),"
                + "Designation VARCHAR(100),"
                + "Archived VARCHAR(1)"
                + ")");
        System.out.println("[DATABASE] Derby database created.");
    }
    
    private void setPanelEnabled(JPanel panel, Boolean isEnabled) {
        panel.setEnabled(isEnabled);
        Component[] components = panel.getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                setPanelEnabled((JPanel)component, isEnabled);
            }
            else if((component.equals(derbyNameField) || component.equals(derbyNameLabel)) && backPanel.isEnabled() && derbyPanel.isEnabled()) component.setEnabled(derbyNewRadio.isSelected());
            else if(!component.equals(statusLabel)) component.setEnabled(isEnabled);
        }
    }
    
    private void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (! Files.isSymbolicLink(f.toPath())) {
                    deleteDir(f);
                }
            }
        }
        file.delete();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel backPanel;
    private javax.swing.JButton continueButton;
    private javax.swing.JFileChooser dbSelector;
    private javax.swing.JButton derbyDirButton;
    private javax.swing.JTextField derbyDirField;
    private javax.swing.JLabel derbyDirLabel;
    private javax.swing.JRadioButton derbyExistingRadio;
    private javax.swing.JTextField derbyNameField;
    private javax.swing.JLabel derbyNameLabel;
    private javax.swing.JRadioButton derbyNewRadio;
    private javax.swing.JPanel derbyPanel;
    private javax.swing.JRadioButton derbyRadio;
    private javax.swing.ButtonGroup derbyRadioGroup;
    private javax.swing.JRadioButton derbyServerRadio;
    private javax.swing.JPanel microsoftPanel;
    private javax.swing.JRadioButton mssqlRadio;
    private javax.swing.JRadioButton serverRadio;
    private javax.swing.ButtonGroup serverRadioGroup;
    private javax.swing.JTextField srvrDBField;
    private javax.swing.JLabel srvrDBLabel;
    private javax.swing.JTextField srvrField;
    private javax.swing.JLabel srvrLabel;
    private javax.swing.JPasswordField srvrPassField;
    private javax.swing.JLabel srvrPassLabel;
    private javax.swing.JTextField srvrUsrField;
    private javax.swing.JLabel srvrUsrLabel;
    private javax.swing.JLabel statusLabel;
    private javax.swing.ButtonGroup typeRadioGroup;
    // End of variables declaration//GEN-END:variables
}
