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
import com.mms.utilities.MenuScroller;
import com.mms.utilities.OtherTools;
import com.mms.utilities.TableTools;
import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
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
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author J.B. Slade
 */
public class WOFrame extends javax.swing.JInternalFrame {
    
    private final int row;
    private final JTable table;
    private final DatePicker datePicker;
    private ArrayList<Integer> assetLocations, employees;

    
    public WOFrame(JTable t, int r, boolean view) {
        initComponents();
        table = t;
        row = r;
        assetLocations = new ArrayList<>();
        employees = new ArrayList<>();
        getRootPane().setDefaultButton(continueButton);

        //Desc view area
        descViewScroll.setVisible(false);
        descViewPane.setBackground(Color.WHITE);
        
        //Set right click listeners
        descArea.addMouseListener(MMS.getMouseListener());
        
        //Set date picker
        datePanel.setPreferredSize(employeeField.getPreferredSize());
        DatePickerSettings pickerSettings = new DatePickerSettings();
        pickerSettings.setFormatForDatesCommonEra(DateTimeFormatter.ISO_LOCAL_DATE);
        pickerSettings.setBorderCalendarPopup(employeeField.getBorder());
        pickerSettings.setAllowEmptyDates(false);
        datePicker = new DatePicker(pickerSettings);
        if(!MMS.DEBUG) pickerSettings.setDateRangeLimits(LocalDate.now(), LocalDate.MAX);
        datePicker.setDateToToday();
        datePicker.getComponentDateTextField().setBorder(employeeField.getBorder());
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
        rs = Database.select("SELECT custom_value FROM custom_fields WHERE custom_type = 'wo_type'");
        try {
            while(rs.next()){
                typeCombo.addItem(rs.getString(1));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Set employees
        rs = Database.select("SELECT id, employee_name FROM employees");
        try {
            while(rs.next()){
                JCheckBoxMenuItem j = new JCheckBoxMenuItem(rs.getString(2));
                j.addActionListener(new EmployeeSelectListener());
                employeePopup.add(j);
                employees.add(rs.getInt(1));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(employeePopup.getComponentCount() > 10) MenuScroller.setScrollerFor(employeePopup, 10);
        employeePopup.show(null, 0, 0);
        employeePopup.setVisible(false);
        
        //Edit
        if (r != -1){
            typeCombo.setSelectedItem(table.getValueAt(row, 2));
            priorityCombo.setSelectedItem(table.getValueAt(row, 3));
            assetCombo.setSelectedItem(table.getValueAt(row, 4));
            locationCombo.setSelectedItem(table.getValueAt(row, 5));
            
            //Employees
            employeeField.setText(table.getValueAt(row, 6).toString());
            for(Component c : employeePopup.getComponents()){
                if(c instanceof JCheckBoxMenuItem && employeeField.getText().contains(((JCheckBoxMenuItem)c).getText()))
                    ((JCheckBoxMenuItem)c).setSelected(true);
            }
                
            continueButton.setText("Save");
            
            //Date and desc
            rs = Database.select("SELECT wo_date, wo_desc FROM schedule WHERE id = ?",
                    new Object[]{table.getValueAt(row, 0)});
            try {
                if(rs.next()){
                    datePicker.setDate(rs.getDate(1).toLocalDate());
                    descArea.setText(rs.getObject(2).toString());
                }
            } catch (SQLException ex) {
                Logger.getLogger(WOFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //View
        if(view){
            employeeField.setEditable(false);
            employeeField.setFocusable(false);
            employeeField.setBackground(Color.WHITE);
            OtherTools.setComboBoxReadOnly(typeCombo);
            OtherTools.setComboBoxReadOnly(priorityCombo);
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
    
    private class EmployeeSelectListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String s = employeeField.getText();
            for(Component c : employeePopup.getComponents()){
                if(c instanceof JCheckBoxMenuItem){
                    JCheckBoxMenuItem m = (JCheckBoxMenuItem)c;
                    if(m.isSelected() && !s.contains(m.getText())){
                        if(s.isEmpty()) employeeField.setText(m.getText());
                        else employeeField.setText(employeeField.getText()+"; "+m.getText());
                    }
                    else if(!m.isSelected()){
                        if(s.contains("; "+m.getText()+"; "))
                            employeeField.setText(employeeField.getText().replaceAll("; "+m.getText()+"; ", ""));
                        else if(s.contains("; "+m.getText()))
                            employeeField.setText(employeeField.getText().replaceAll("; "+m.getText(), ""));
                        else if(s.contains(m.getText()+"; "))
                            employeeField.setText(employeeField.getText().replaceAll(m.getText()+"; ", ""));
                        else if(s.contains(m.getText()))
                            employeeField.setText(employeeField.getText().replaceAll(m.getText(), ""));
                    }
                }
            }
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
        employeePopup = new javax.swing.JPopupMenu();
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
        imageButton = new javax.swing.JButton();
        topPanel = new javax.swing.JPanel();
        leftPanel = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        datePanel = new javax.swing.JPanel();
        freqLabel = new javax.swing.JLabel();
        priorityCombo = new javax.swing.JComboBox<>();
        locationLabel = new javax.swing.JLabel();
        locationCombo = new javax.swing.JComboBox<>();
        rightPanel = new javax.swing.JPanel();
        typeLabel = new javax.swing.JLabel();
        typeCombo = new javax.swing.JComboBox<>();
        assetLabel = new javax.swing.JLabel();
        assetCombo = new javax.swing.JComboBox<>();
        employeeLabel = new javax.swing.JLabel();
        employeeField = new javax.swing.JTextField();
        employeeButton = new javax.swing.JButton();

        descViewPane.setEditable(false);
        descViewPane.setContentType("text/html"); // NOI18N
        descViewPane.setPreferredSize(descArea.getPreferredSize());
        descViewScroll.setViewportView(descViewPane);

        employeePopup.setBackground(new java.awt.Color(255, 255, 255));
        employeePopup.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("New Work Order");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/dialogs/workOrders.png"))); // NOI18N
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
            .addComponent(descScroll, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
        );

        imageButton.setText("Attach Image(s)");

        topPanel.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        nameLabel.setText("Date:");

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

        freqLabel.setText("Priority:");

        priorityCombo.setMaximumRowCount(3);
        priorityCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Low", "Normal", "High" }));
        priorityCombo.setSelectedIndex(1);
        priorityCombo.setPreferredSize(employeeField.getPreferredSize());

        locationLabel.setText("Location:");

        locationCombo.setPreferredSize(employeeField.getPreferredSize());

        javax.swing.GroupLayout leftPanelLayout = new javax.swing.GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(datePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(priorityCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nameLabel)
                    .addComponent(freqLabel)
                    .addComponent(locationLabel))
                .addGap(0, 145, Short.MAX_VALUE))
            .addComponent(locationCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addComponent(nameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(datePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(freqLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(priorityCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(locationLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(locationCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        topPanel.add(leftPanel);

        typeLabel.setText("Type:");

        typeCombo.setPreferredSize(employeeField.getPreferredSize());

        assetLabel.setText("Asset:");

        assetCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No Asset" }));
        assetCombo.setPreferredSize(employeeField.getPreferredSize());
        assetCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                assetComboItemStateChanged(evt);
            }
        });

        employeeLabel.setText("Employee(s):");

        employeeField.setEditable(false);
        employeeField.setBackground(new java.awt.Color(255, 255, 255));

        employeeButton.setText("...");
        employeeButton.setFocusable(false);
        employeeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                employeeButtonMousePressed(evt);
            }
        });

        javax.swing.GroupLayout rightPanelLayout = new javax.swing.GroupLayout(rightPanel);
        rightPanel.setLayout(rightPanelLayout);
        rightPanelLayout.setHorizontalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(typeCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(assetCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(typeLabel)
                    .addComponent(assetLabel)
                    .addComponent(employeeLabel))
                .addGap(0, 126, Short.MAX_VALUE))
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addComponent(employeeField)
                .addGap(3, 3, 3)
                .addComponent(employeeButton))
        );
        rightPanelLayout.setVerticalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addComponent(typeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(typeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(assetLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(assetCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(employeeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(employeeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(employeeButton)))
        );

        topPanel.add(rightPanel);

        javax.swing.GroupLayout backPanelLayout = new javax.swing.GroupLayout(backPanel);
        backPanel.setLayout(backPanelLayout);
        backPanelLayout.setHorizontalGroup(
            backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(topPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, backPanelLayout.createSequentialGroup()
                        .addComponent(imageButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(continueButton))
                    .addComponent(textTools, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(descPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGroup(backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(continueButton)
                    .addComponent(imageButton))
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
        String type = typeCombo.getSelectedItem().toString(),
                prio = priorityCombo.getSelectedItem().toString(),
                locName = locationCombo.getSelectedItem().toString(),
                assName = assetCombo.getSelectedItem().toString(),
                desc = descArea.getText();
        Date date = DateTools.convertToSQLDate(datePicker.getDate());
        
        int locNum = Integer.parseInt(locName.substring(0, locName.indexOf("-")-1)),
                assNum = assName.equals("No Asset") ? -1 : Integer.parseInt(assName.substring(0, assName.indexOf("-")-1));
        if(employeeField.getText().isEmpty()) employeeField.requestFocus();
        else if(desc.isEmpty()) descArea.requestFocus();
        else{
            OtherTools.disablePanel(backPanel);
            new Thread(){
                @Override
                public void run(){
                    if(row == -1){ //New Work Order
                    //Get next no
                    int WONum = 0;
                    ResultSet rs = Database.select("SELECT MAX(id) FROM work_orders");
                    try {
                        if(rs.next()) WONum = rs.getInt(1);
                        rs.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(AssetFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    WONum++;
                    //Insert into DB
                    Database.executeQuery("INSERT INTO work_orders (id, wo_date, wo_type, wo_priority, wo_desc, asset_id, location_id, user_name, wo_status, archived) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'Open', 'N')",
                            new Object[]{WONum, date, type, prio, desc , assNum, locNum, MMS.getUser()});
                    
                    //Insert employees
                    for(int i = 0; i < employeePopup.getComponentCount(); i++){
                        JCheckBoxMenuItem m = (JCheckBoxMenuItem)employeePopup.getComponent(i);
                        if(m.isSelected()){
                            Database.executeQuery("INSERT INTO wo_employees (wo_id, employee_id) VALUES (?, ?)",
                                    new Object[]{WONum, employees.get(i)});
                        }
                    }

                    //Update table
                    Object [] o = {WONum, date, type, prio.equals("High") ? "<html><b>High</b></html>" : prio, assName, locName, employeeField.getText(), "Open"};
                    DefaultTableModel m = (DefaultTableModel)table.getModel();
                    m.insertRow(0, o);
                    //Select new row
                    table.setRowSelectionInterval(0, 0);
                    }
                    else{ //Edit schedule
                        //Get selected number
                        int WONum = Integer.parseInt(table.getValueAt(row, 0).toString());
                        //Update database
                        Database.executeQuery("UPDATE work_orders SET wo_date = ?, wo_type = ?, wo_priority = ?, wo_desc = ?, asset_id = ?, location_id = ? WHERE id = ?",
                                new Object[]{date, type, prio, desc , assNum, locNum, WONum});
                        
                        //SET EMPLOYEES

                        //Update table
                        table.setValueAt(date, row, 1);
                        table.setValueAt(type, row, 2);
                        table.setValueAt(prio, row, 3);
                        table.setValueAt(assName, row, 4);
                        table.setValueAt(locName, row, 5);
                        table.setValueAt(employeeField.getText(), row, 6);
                        table.setValueAt("Open", row, 7);
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

    private void employeeButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_employeeButtonMousePressed
        employeeField.requestFocus();
        employeePopup.show(employeeButton, employeeButton.getWidth()-employeePopup.getWidth(), employeeButton.getHeight()+3);
    }//GEN-LAST:event_employeeButtonMousePressed
     
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> assetCombo;
    private javax.swing.JLabel assetLabel;
    private javax.swing.JPanel backPanel;
    private javax.swing.JButton boldButton;
    private javax.swing.JButton bulletButton;
    private javax.swing.JButton continueButton;
    private javax.swing.JPanel datePanel;
    private javax.swing.JTextArea descArea;
    private javax.swing.JPanel descPanel;
    private javax.swing.JScrollPane descScroll;
    private javax.swing.JTextPane descViewPane;
    private javax.swing.JScrollPane descViewScroll;
    private javax.swing.JButton employeeButton;
    private javax.swing.JTextField employeeField;
    private javax.swing.JLabel employeeLabel;
    private javax.swing.JPopupMenu employeePopup;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel freqLabel;
    private javax.swing.JButton imageButton;
    private javax.swing.JButton italicsButton;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JComboBox<String> locationCombo;
    private javax.swing.JLabel locationLabel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JComboBox<String> priorityCombo;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JToolBar textTools;
    private javax.swing.JPanel topPanel;
    private javax.swing.JComboBox<String> typeCombo;
    private javax.swing.JLabel typeLabel;
    private javax.swing.JToggleButton viewToggleButton;
    // End of variables declaration//GEN-END:variables
}
