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
package com.mms.iframes;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.mms.Database;
import com.mms.MMS;
import com.mms.utilities.DateTools;
import com.mms.utilities.OtherTools;
import com.mms.utilities.TableTools;
import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.MouseListener;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author J.B. Slade
 */
public class ScheduleFrame extends javax.swing.JInternalFrame {
    
    private final int row;
    private final JTable table;
    private final DatePicker datePicker;
    private ArrayList<Integer> assetLocations;

    
    public ScheduleFrame(JTable t, int r, boolean view) {
        initComponents();
        table = t;
        row = r;
        assetLocations = new ArrayList<>();
        getRootPane().setDefaultButton(continueButton);

        //Desc view area
        descViewScroll.setVisible(false);
        descViewPane.setBackground(Color.WHITE);
        
        //Set right click listeners
        nameField.addMouseListener(MMS.getMouseListener());
        descArea.addMouseListener(MMS.getMouseListener());
        
        //Set date picker
        datePanel.setPreferredSize(nameField.getPreferredSize());
        DatePickerSettings pickerSettings = new DatePickerSettings();
        pickerSettings.setFormatForDatesCommonEra(DateTimeFormatter.ISO_LOCAL_DATE);
        pickerSettings.setBorderCalendarPopup(nameField.getBorder());
        pickerSettings.setAllowEmptyDates(false);
        datePicker = new DatePicker(pickerSettings);
        if(!MMS.DEBUG) pickerSettings.setDateRangeLimits(LocalDate.now(), LocalDate.MAX);
        datePicker.setDateToToday();
        datePicker.getComponentDateTextField().setBorder(nameField.getBorder());
        datePicker.getComponentDateTextField().setMargin(new Insets(2,5,2,2));
        datePanel.add(datePicker);
        datePanel.repaint();
        
        //Set locations
        ResultSet rs = Database.select("SELECT id, location_name FROM locations WHERE archived = 'N'");
        try {
            while(rs.next()){
                locationCombo.addItem(rs.getString(1)+" - "+rs.getString(2));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Set assets
        rs = Database.select("SELECT id, asset_name, location_id FROM assets WHERE archived = 'N'");
        try {
            while(rs.next()){
                assetCombo.addItem(rs.getString(1)+" - "+rs.getString(2));
                assetLocations.add(rs.getInt(3));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Set types
        rs = Database.select("SELECT custom_value FROM custom_fields WHERE custom_type = 'maintenance_type'");
        try {
            while(rs.next()){
                typeCombo.addItem(rs.getString(1));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Edit
        if (r != -1){
            nameField.setText(table.getValueAt(row, 1).toString());
            typeCombo.setSelectedItem(table.getValueAt(row, 2));
            freqCombo.setSelectedItem(table.getValueAt(row, 3));
            assetCombo.setSelectedItem(table.getValueAt(row, 6));
            locationCombo.setSelectedItem(table.getValueAt(row, 7));
            continueButton.setText("Save");
            
            rs = Database.select("SELECT schedule_from_date, schedule_desc FROM schedule WHERE id = ?",
                    new Object[]{table.getValueAt(row, 0)});
            try {
                if(rs.next()){
                    datePicker.setDate(rs.getDate(1).toLocalDate());
                    descArea.setText(rs.getObject(2).toString());
                }
            } catch (SQLException ex) {
                Logger.getLogger(ScheduleFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //View
        if(view){
            nameField.setEditable(false);
            nameField.setFocusable(false);
            nameField.setBackground(Color.WHITE);
            OtherTools.setComboBoxReadOnly(typeCombo);
            OtherTools.setComboBoxReadOnly(freqCombo);
            OtherTools.setComboBoxReadOnly(assetCombo);
            OtherTools.setComboBoxReadOnly(locationCombo);           
            datePicker.getComponentDateTextField().setEditable(false);
            datePicker.getComponentDateTextField().setFocusable(false);
            datePicker.getComponentToggleCalendarButton().setEnabled(false);
            for(MouseListener l : datePicker.getComponentToggleCalendarButton().getMouseListeners())
                datePicker.getComponentToggleCalendarButton().removeMouseListener(l);        
            viewToggleButton.setSelected(true);
            viewToggleButtonActionPerformed(null);
            descViewPane.setFocusable(false);
            continueButton.setEnabled(false);
            continueButton.setVisible(false);
            for(Component c : textTools.getComponents())
                c.setEnabled(false);
            backPanel.setBorder(new MatteBorder(0,0,6,0, backPanel.getBackground()));
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

        descViewScroll = new javax.swing.JScrollPane();
        descViewPane = new javax.swing.JTextPane();
        backPanel = new javax.swing.JPanel();
        continueButton = new javax.swing.JButton();
        textTools = new javax.swing.JToolBar();
        boldButton = new javax.swing.JButton();
        italicsButton = new javax.swing.JButton();
        bulletButton = new javax.swing.JButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        viewToggleButton = new javax.swing.JToggleButton();
        descPanel = new javax.swing.JPanel();
        descScroll = new javax.swing.JScrollPane();
        descArea = new javax.swing.JTextArea();
        topPanel = new javax.swing.JPanel();
        leftPanel = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        dateLabel = new javax.swing.JLabel();
        assetLabel = new javax.swing.JLabel();
        assetCombo = new javax.swing.JComboBox<>();
        datePanel = new javax.swing.JPanel();
        rightPanel = new javax.swing.JPanel();
        typeLabel = new javax.swing.JLabel();
        typeCombo = new javax.swing.JComboBox<>();
        freqLabel = new javax.swing.JLabel();
        freqCombo = new javax.swing.JComboBox<>();
        locationLabel = new javax.swing.JLabel();
        locationCombo = new javax.swing.JComboBox<>();

        descViewPane.setEditable(false);
        descViewPane.setContentType("text/html"); // NOI18N
        descViewPane.setPreferredSize(descArea.getPreferredSize());
        descViewScroll.setViewportView(descViewPane);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("New Scheduled Task");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/dialogs/schedule.png"))); // NOI18N
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
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        continueButton.setText("Add");
        continueButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                continueButtonActionPerformed(evt);
            }
        });

        textTools.setFloatable(false);
        textTools.setRollover(true);

        boldButton.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        boldButton.setText("B");
        boldButton.setFocusable(false);
        boldButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        boldButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        boldButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boldButtonActionPerformed(evt);
            }
        });
        textTools.add(boldButton);

        italicsButton.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
        italicsButton.setText("I");
        italicsButton.setFocusable(false);
        italicsButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        italicsButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        italicsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                italicsButtonActionPerformed(evt);
            }
        });
        textTools.add(italicsButton);

        bulletButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        bulletButton.setText("•");
        bulletButton.setFocusable(false);
        bulletButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bulletButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        bulletButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bulletButtonActionPerformed(evt);
            }
        });
        textTools.add(bulletButton);
        textTools.add(filler1);

        viewToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/eye.png"))); // NOI18N
        viewToggleButton.setFocusable(false);
        viewToggleButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        viewToggleButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        viewToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewToggleButtonActionPerformed(evt);
            }
        });
        textTools.add(viewToggleButton);

        descArea.setColumns(20);
        descArea.setLineWrap(true);
        descArea.setRows(10);
        descArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                descAreaKeyPressed(evt);
            }
        });
        descScroll.setViewportView(descArea);

        javax.swing.GroupLayout descPanelLayout = new javax.swing.GroupLayout(descPanel);
        descPanel.setLayout(descPanelLayout);
        descPanelLayout.setHorizontalGroup(
            descPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(descScroll)
        );
        descPanelLayout.setVerticalGroup(
            descPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, descPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(descScroll))
        );

        topPanel.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        nameLabel.setText("Name:");

        dateLabel.setText("Start Date:");

        assetLabel.setText("Asset:");

        assetCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No Asset" }));
        assetCombo.setPreferredSize(nameField.getPreferredSize());
        assetCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                assetComboItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout datePanelLayout = new javax.swing.GroupLayout(datePanel);
        datePanel.setLayout(datePanelLayout);
        datePanelLayout.setHorizontalGroup(
            datePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        datePanelLayout.setVerticalGroup(
            datePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout leftPanelLayout = new javax.swing.GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(nameField, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
            .addComponent(assetCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nameLabel)
                    .addComponent(dateLabel)
                    .addComponent(assetLabel))
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(datePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addComponent(nameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dateLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(datePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(assetLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(assetCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        topPanel.add(leftPanel);

        typeLabel.setText("Type:");

        typeCombo.setPreferredSize(nameField.getPreferredSize());

        freqLabel.setText("Frequency:");

        freqCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Daily", "Weekly", "Monthly", "Quarterly", "Bianually", "Anually" }));
        freqCombo.setPreferredSize(nameField.getPreferredSize());

        locationLabel.setText("Location:");

        locationCombo.setPreferredSize(nameField.getPreferredSize());

        javax.swing.GroupLayout rightPanelLayout = new javax.swing.GroupLayout(rightPanel);
        rightPanel.setLayout(rightPanelLayout);
        rightPanelLayout.setHorizontalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(typeCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(freqCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(typeLabel)
                    .addComponent(freqLabel)
                    .addComponent(locationLabel))
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(locationCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        rightPanelLayout.setVerticalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addComponent(typeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(typeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(freqLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(freqCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(locationLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(locationCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        topPanel.add(rightPanel);

        javax.swing.GroupLayout backPanelLayout = new javax.swing.GroupLayout(backPanel);
        backPanel.setLayout(backPanelLayout);
        backPanelLayout.setHorizontalGroup(
            backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(topPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
                    .addGroup(backPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(continueButton))
                    .addComponent(textTools, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(descPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        backPanelLayout.setVerticalGroup(
            backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(topPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textTools, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(descPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(continueButton)
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
        String name = nameField.getText(),
                type = typeCombo.getSelectedItem().toString(),
                freq = freqCombo.getSelectedItem().toString(),
                locName = locationCombo.getSelectedItem().toString(),
                assName = assetCombo.getSelectedItem().toString(),
                desc = descArea.getText();
        Date date = DateTools.convertToSQLDate(datePicker.getDate());
        LocalDate lastDate = DateTools.getDueDate(datePicker.getDate(), freq, -1);
        
        int locNum = Integer.parseInt(locName.substring(0, locName.indexOf("-")-1)),
                assNum = assName.equals("No Asset") ? -1 : Integer.parseInt(assName.substring(0, assName.indexOf("-")-1));
        if(name.isEmpty()) nameField.requestFocus();
        else{
            OtherTools.disablePanel(backPanel);
            new Thread(){
                @Override
                public void run(){
                    if(row == -1){ //New schedule
                    //Get next no
                    int scheduleNum = 0;
                    ResultSet rs = Database.select("SELECT MAX(id) FROM schedule");
                    try {
                        if(rs.next()) scheduleNum = rs.getInt(1);
                        rs.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(AssetFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    scheduleNum++;
                    //Insert into DB
                    Database.executeQuery("INSERT INTO schedule (id, schedule_name, schedule_type, schedule_from_date, schedule_last_date, schedule_freq, asset_id, location_id, schedule_desc, archived) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'N')",
                            new Object[]{scheduleNum, name, type, date, DateTools.convertToSQLDate(lastDate), freq , assNum, locNum, desc});
                    //Get due date
                    Object dueDate;
                    if(DateTools.isToday(datePicker.getDate())) dueDate = "<html><b>Today</b></html>";
                    else dueDate = datePicker.getDate();
                    //Update table
                    Object [] o = {scheduleNum, name, type, freq, /*lastDate*/"Never", dueDate, assName, locName};
                    DefaultTableModel m = (DefaultTableModel)table.getModel();
                    m.insertRow(0, o);
                    //Select new row
                    table.setRowSelectionInterval(0, 0);
                    }
                    else{ //Edit schedule
                        //Get selected number
                        int scheduleNum = Integer.parseInt(table.getValueAt(row, 0).toString());
                        //Update database
                        Database.executeQuery("UPDATE schedule SET schedule_name = ?, schedule_type = ?, schedule_from_date = ?, schedule_last_date = ?, schedule_freq = ?, asset_id = ?, location_id = ?, schedule_desc = ? WHERE id = ?",
                                new Object[]{name, type, date, DateTools.convertToSQLDate(lastDate), freq, assNum, locNum, desc, scheduleNum});
                        //Get due date
                        Object dueDate;
                        if(DateTools.isToday(datePicker.getDate())) dueDate = "<html><b>Today</b></html>";
                        else dueDate = datePicker.getDate();
                        //Update table
                        table.setValueAt(name, row, 1);
                        table.setValueAt(type, row, 2);
                        table.setValueAt(freq, row, 3);
                        table.setValueAt(dueDate, row, 5);
                        table.setValueAt(assName, row, 6);
                        table.setValueAt(locName, row, 7);
                        //Select updated row
                        table.setRowSelectionInterval(row, row);
                    }
                    TableTools.resize(table);
                    dispose();
                }
            }.start();
        }
    }//GEN-LAST:event_continueButtonActionPerformed

    private void formInternalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameDeiconified
        getRootPane().setDefaultButton(continueButton);
    }//GEN-LAST:event_formInternalFrameDeiconified

    private void boldButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boldButtonActionPerformed
       if(descArea.getSelectedText() == null){
           int caret = descArea.getCaretPosition();
           descArea.replaceSelection("****");
           descArea.setCaretPosition(caret+2);
       }
       else{
           int start = descArea.getSelectionStart(), end = descArea.getSelectionEnd();
           descArea.replaceSelection("**"+descArea.getSelectedText()+"**");
           descArea.setSelectionStart(start+2);
           descArea.setSelectionEnd(end+2);
       }
    }//GEN-LAST:event_boldButtonActionPerformed

    private void italicsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_italicsButtonActionPerformed
      if(descArea.getSelectedText() == null){
           int caret = descArea.getCaretPosition();
           descArea.replaceSelection("__");
           descArea.setCaretPosition(caret+1);
       }
       else{
           int start = descArea.getSelectionStart(), end = descArea.getSelectionEnd();
           descArea.replaceSelection("_"+descArea.getSelectedText()+"_");
           descArea.setSelectionStart(start+1);
           descArea.setSelectionEnd(end+1);
       }
    }//GEN-LAST:event_italicsButtonActionPerformed

    private void bulletButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bulletButtonActionPerformed
     
    }//GEN-LAST:event_bulletButtonActionPerformed

    private void assetComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_assetComboItemStateChanged
        if(assetCombo.getSelectedItem().equals("No Asset")) locationCombo.setEnabled(true);
        else{
            for(int i = 0; i < locationCombo.getItemCount(); i++){
                if(locationCombo.getItemAt(i).startsWith(assetLocations.get(assetCombo.getSelectedIndex()-1)+"")){
                    locationCombo.setSelectedIndex(i);
                    locationCombo.setEnabled(false);
                    break;
                }
            } 
        }
    }//GEN-LAST:event_assetComboItemStateChanged

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        datePicker.setSize(datePanel.getSize());
    }//GEN-LAST:event_formComponentResized

    private void viewToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewToggleButtonActionPerformed
        if(viewToggleButton.isSelected()){ //Replace text area with view pane
            boolean hadFocus = descArea.hasFocus();
            descScroll.setVisible(false);
            descViewScroll.setVisible(true);
            String text = com.github.rjeschke.txtmark.Processor.process(descArea.getText());
            if(!text.isEmpty()) text = text.substring(3, text.length()-3);
            descViewPane.setText(text);
            GroupLayout descPanelLayout = new javax.swing.GroupLayout(descPanel);
            descPanel.setLayout(descPanelLayout);
            descPanelLayout.setHorizontalGroup(
                descPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(descViewScroll)
            );
            descPanelLayout.setVerticalGroup(
                descPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(descViewScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
            );
            if(hadFocus) descViewPane.requestFocus();
        }
        else{
            boolean hadFocus = descViewPane.hasFocus();
            descViewScroll.setVisible(false);
            descScroll.setVisible(true);
            GroupLayout descPanelLayout = new javax.swing.GroupLayout(descPanel);
            descPanel.setLayout(descPanelLayout);
            descPanelLayout.setHorizontalGroup(
                descPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(descScroll)
            );
            descPanelLayout.setVerticalGroup(
                descPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(descScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
            );
            if(hadFocus) descArea.requestFocus();
        }
    }//GEN-LAST:event_viewToggleButtonActionPerformed

    private void descAreaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_descAreaKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            descArea.insert("  ", descArea.getCaretPosition());
        }
    }//GEN-LAST:event_descAreaKeyPressed
     
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> assetCombo;
    private javax.swing.JLabel assetLabel;
    private javax.swing.JPanel backPanel;
    private javax.swing.JButton boldButton;
    private javax.swing.JButton bulletButton;
    private javax.swing.JButton continueButton;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JPanel datePanel;
    private javax.swing.JTextArea descArea;
    private javax.swing.JPanel descPanel;
    private javax.swing.JScrollPane descScroll;
    private javax.swing.JTextPane descViewPane;
    private javax.swing.JScrollPane descViewScroll;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JComboBox<String> freqCombo;
    private javax.swing.JLabel freqLabel;
    private javax.swing.JButton italicsButton;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JComboBox<String> locationCombo;
    private javax.swing.JLabel locationLabel;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JToolBar textTools;
    private javax.swing.JPanel topPanel;
    private javax.swing.JComboBox<String> typeCombo;
    private javax.swing.JLabel typeLabel;
    private javax.swing.JToggleButton viewToggleButton;
    // End of variables declaration//GEN-END:variables
}
