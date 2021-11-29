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
import com.mms.dialogs.InternalDialog;
import com.mms.modules.Schedule;
import com.mms.utilities.DateTools;
import com.mms.utilities.MenuScroller;
import com.mms.utilities.OtherTools;
import com.mms.utilities.TableTools;
import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.MenuElement;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;


/**
 *
 * @author J.B. Slade
 */
public class WOFrame extends javax.swing.JInternalFrame {
    
    private final int row;
    private final JTable table;
    private final DatePicker datePicker;
    private ArrayList<Integer> assetLocations, employees;
    private ArrayList<String> images;
    private ArrayList<JCheckBoxMenuItem> employeeNames;
    private final Schedule schedule;
    private int scheduleRow;
    
    public WOFrame(JTable t, int r, boolean view, Schedule s) {
        initComponents();
        table = t;
        row = r;
        assetLocations = new ArrayList<>();
        employees = new ArrayList<>();
        employeeNames = new ArrayList<>();
        images = new ArrayList<>();
        schedule = s;
        if(schedule != null) scheduleRow = schedule.getTable().getSelectedRow();
        getRootPane().setDefaultButton(continueButton);
        
        //Closed panel
        closedPanel.setVisible(false);
        splitPane.setDividerSize(0);
        //backPanel.setBorder(new MatteBorder(0,0,0,6, backPanel.getBackground()));

        //Desc view area
        descViewScroll.setVisible(false);
        descViewPane.setBackground(Color.WHITE);
        ((DefaultCaret)descViewPane.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        ((DefaultCaret)descArea.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        
        //Set right click listeners
        descArea.addMouseListener(MMS.getMouseListener());
        
        //Set date picker
        datePanel.setPreferredSize(employeeField.getPreferredSize());
        DatePickerSettings pickerSettings = new DatePickerSettings();
        pickerSettings.setFormatForDatesCommonEra(DateTimeFormatter.ISO_LOCAL_DATE);
        pickerSettings.setBorderCalendarPopup(employeeField.getBorder());
        pickerSettings.setAllowEmptyDates(false);
        datePicker = new DatePicker(pickerSettings);
        if(!view) pickerSettings.setDateRangeLimits(LocalDate.now(), LocalDate.MAX);
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
        rs = Database.select("SELECT custom_value FROM custom_fields WHERE custom_type = 'maintenance_type'");
        try {
            while(rs.next()){
                typeCombo.addItem(rs.getString(1));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Set employees
        rs = Database.select("SELECT id, employee_name FROM employees ORDER BY employee_name");
        try {
            while(rs.next()){
                JCheckBoxMenuItem j = new JCheckBoxMenuItem(rs.getString(2));
                j.addActionListener(new EmployeeSelectListener());
                employeePopup.add(j);
                employeeNames.add(j);
                employees.add(rs.getInt(1));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(employeePopup.getComponentCount() > 10) MenuScroller.setScrollerFor(employeePopup, 10);
        employeePopup.show(null, 0, 0);
        employeePopup.setVisible(false);
        
        //Edit/view
        if (r != -1){
            typeCombo.setSelectedItem(table.getValueAt(row, 2));
            priorityCombo.setSelectedItem(OtherTools.escapeHTML(table.getValueAt(row, 3).toString()));
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
            rs = Database.select("SELECT wo_date, wo_desc FROM work_orders WHERE id = ?",
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
        
        //Schedule
        if(s != null){
            //Get schedule desc
            rs = Database.select("SELECT schedule_desc FROM schedule WHERE id = ?",
                    new Object[]{schedule.getTable().getValueAt(schedule.getTable().getSelectedRow(), 0)});
            //Set values
            try {
                if(rs.next()){
                    descArea.setText(rs.getString(1).trim());
                }
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(WOFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            typeCombo.setSelectedItem(schedule.getTable().getValueAt(schedule.getTable().getSelectedRow(), 2));
            assetCombo.setSelectedItem(schedule.getTable().getValueAt(schedule.getTable().getSelectedRow(), 6));
            locationCombo.setSelectedItem(schedule.getTable().getValueAt(schedule.getTable().getSelectedRow(), 7));
        }
        
        //View
        if(view){
            employeeField.setEditable(false);
            employeeField.setBackground(Color.WHITE);
            employeeButton.setEnabled(false);
            OtherTools.setComboBoxReadOnly(typeCombo);
            OtherTools.setComboBoxReadOnly(priorityCombo);
            OtherTools.setComboBoxReadOnly(assetCombo);
            OtherTools.setComboBoxReadOnly(locationCombo);           
            datePicker.getComponentDateTextField().setEditable(false);
            datePicker.getComponentToggleCalendarButton().setEnabled(false);
            for(MouseListener l : datePicker.getComponentToggleCalendarButton().getMouseListeners())
                datePicker.getComponentToggleCalendarButton().removeMouseListener(l);        
            viewToggleButton.setSelected(true);
            viewToggleButtonActionPerformed(null);
            continueButton.setEnabled(false);
            continueButton.setVisible(false);
            for(Component c : textTools.getComponents())
                c.setEnabled(false);
            
            //Images
            File [] i = new File(System.getProperty("user.dir")+"\\wo_images\\").listFiles();
            for(File f : i){
                if(f.getName().startsWith(table.getValueAt(row, 0).toString()))
                    images.add(f.getName());
            }
            if(images.size() > 0){
                imageButton.setText("Open Image(s)");
                imageLabel.setText(images.size() > 1 ? images.size()+" images attached" : images.size()+" image attached");
                backPanel.setBorder(new MatteBorder(0,0,0,6, backPanel.getBackground()));
            }
            else{
                imageButton.setVisible(false);
                imageLabel.setVisible(false);
                backPanel.setBorder(new MatteBorder(0,0,6,0, backPanel.getBackground()));
            }

            //Closed panel
            if(table.getValueAt(r, 7).equals("Closed")){
                closedPanel.setVisible(true);
                splitPane.setDividerSize(10);
                actionsArea.setBackground(Color.WHITE);
                try {
                    rs = Database.select("SELECT wo_action, wo_start_time, wo_end_time, wo_start_date, wo_end_date FROM work_orders WHERE id = ?",
                        new Object[]{table.getValueAt(row, 0)});
                    if(rs.next()){
                        actionsArea.setText(rs.getString(1).trim());
                        String startTime = rs.getString(2).trim(), endTime = rs.getString(3).trim(),
                                startDate = rs.getString(4).trim(), endDate = rs.getString(5).trim();
                        startTimeField.setText(startDate+" "+startTime.substring(0, 5));
                        endTimeField.setText(endDate+" "+endTime.substring(0, 5));
                    }
                    
                    DefaultTableModel p = (DefaultTableModel)partsTable.getModel();
                    rs = Database.select("SELECT t0.part_id, t1.part_name, t0.qty FROM wo_parts t0 JOIN parts t1 ON t0.part_id = t1.id WHERE t0.wo_id = ?",
                        new Object[]{table.getValueAt(row, 0)});
                    while(rs != null && rs.next()){
                        p.addRow(new Object[]{rs.getString(1).trim(), rs.getString(2).trim(), rs.getInt(3)});
                    }
                    TableTools.format(partsTable);
                    TableTools.resize(partsTable, 10);
                    if(rs != null) rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(WOFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
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
        imageChooser = new javax.swing.JFileChooser();
        splitPane = new javax.swing.JSplitPane();
        closedPanel = new javax.swing.JPanel();
        actionsLabel = new javax.swing.JLabel();
        actionsScroll = new javax.swing.JScrollPane();
        actionsArea = new javax.swing.JTextArea();
        partsScroll = new javax.swing.JScrollPane();
        partsTable = new javax.swing.JTable();
        partsLabel = new javax.swing.JLabel();
        startTimeLabel = new javax.swing.JLabel();
        startTimeField = new javax.swing.JTextField();
        endTimeField = new javax.swing.JTextField();
        endTimeLabel = new javax.swing.JLabel();
        backPanel = new javax.swing.JPanel();
        continueButton = new javax.swing.JButton();
        textTools = new javax.swing.JToolBar();
        boldButton = new javax.swing.JButton();
        italicsButton = new javax.swing.JButton();
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
        imageLabel = new javax.swing.JLabel();

        descViewPane.setEditable(false);
        descViewPane.setContentType("text/html"); // NOI18N
        descViewPane.setPreferredSize(descArea.getPreferredSize());
        descViewScroll.setViewportView(descViewPane);

        employeePopup.setBackground(new java.awt.Color(255, 255, 255));
        employeePopup.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        employeePopup.addMenuKeyListener(new javax.swing.event.MenuKeyListener() {
            public void menuKeyPressed(javax.swing.event.MenuKeyEvent evt) {
                employeePopupMenuKeyPressed(evt);
            }
            public void menuKeyReleased(javax.swing.event.MenuKeyEvent evt) {
            }
            public void menuKeyTyped(javax.swing.event.MenuKeyEvent evt) {
            }
        });

        imageChooser.setAcceptAllFileFilterUsed(false);
        imageChooser.setApproveButtonText("Select");
        imageChooser.setApproveButtonToolTipText("");
        imageChooser.setDialogTitle("Select Image(s)");
        imageChooser.setFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));
        imageChooser.setMultiSelectionEnabled(true);

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

        splitPane.setDividerLocation(500);

        actionsLabel.setText("Actions Performed:");

        actionsArea.setEditable(false);
        actionsArea.setRows(3);
        actionsScroll.setViewportView(actionsArea);

        partsTable.setAutoCreateRowSorter(true);
        partsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                " #", " Name", " Used Qty"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        partsScroll.setViewportView(partsTable);

        partsLabel.setText("Parts Used:");

        startTimeLabel.setText("Start Time:");

        startTimeField.setEditable(false);
        startTimeField.setBackground(new java.awt.Color(255, 255, 255));

        endTimeField.setEditable(false);
        endTimeField.setBackground(new java.awt.Color(255, 255, 255));

        endTimeLabel.setText("End Time:");

        javax.swing.GroupLayout closedPanelLayout = new javax.swing.GroupLayout(closedPanel);
        closedPanel.setLayout(closedPanelLayout);
        closedPanelLayout.setHorizontalGroup(
            closedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(closedPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(closedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(partsScroll, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(actionsScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
                    .addGroup(closedPanelLayout.createSequentialGroup()
                        .addGroup(closedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(partsLabel)
                            .addComponent(actionsLabel))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(closedPanelLayout.createSequentialGroup()
                        .addGroup(closedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(startTimeLabel)
                            .addComponent(endTimeLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(closedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(endTimeField)
                            .addComponent(startTimeField))))
                .addContainerGap())
        );
        closedPanelLayout.setVerticalGroup(
            closedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(closedPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(actionsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(actionsScroll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(partsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(partsScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(closedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startTimeLabel)
                    .addComponent(startTimeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(closedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(endTimeLabel)
                    .addComponent(endTimeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        splitPane.setRightComponent(closedPanel);

        backPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                backPanelComponentResized(evt);
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
        descArea.setAutoscrolls(false);
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
            .addGroup(descPanelLayout.createSequentialGroup()
                .addComponent(descScroll)
                .addGap(0, 0, 0))
        );

        imageButton.setText("Attach Image(s)");
        imageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imageButtonActionPerformed(evt);
            }
        });

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
                .addGap(0, 189, Short.MAX_VALUE))
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
        employeeField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                employeeFieldKeyPressed(evt);
            }
        });

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
                .addGap(0, 170, Short.MAX_VALUE))
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

        imageLabel.setText(" ");

        javax.swing.GroupLayout backPanelLayout = new javax.swing.GroupLayout(backPanel);
        backPanel.setLayout(backPanelLayout);
        backPanelLayout.setHorizontalGroup(
            backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(topPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
                    .addComponent(textTools, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(descPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backPanelLayout.createSequentialGroup()
                        .addComponent(imageButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(imageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(continueButton)))
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
                    .addComponent(imageLabel)
                    .addComponent(imageButton))
                .addContainerGap())
        );

        splitPane.setLeftComponent(backPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(splitPane)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(splitPane)
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
                    int WONum = 0;
                    if(row == -1){ //New Work Order
                        //Get next no
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
                    else{ //Edit WO
                        //Get selected number
                        WONum = Integer.parseInt(table.getValueAt(row, 0).toString());
                        //Update database
                        Database.executeQuery("UPDATE work_orders SET wo_date = ?, wo_type = ?, wo_priority = ?, wo_desc = ?, asset_id = ?, location_id = ? WHERE id = ?",
                                new Object[]{date, type, prio, desc , assNum, locNum, WONum});
                        
                        //Insert employees
                        Database.executeQuery("DELETE FROM wo_employees WHERE wo_id = ?",
                                new Object[]{WONum});
                        for(int i = 0; i < employeePopup.getComponentCount(); i++){
                            JCheckBoxMenuItem m = (JCheckBoxMenuItem)employeePopup.getComponent(i);
                            if(m.isSelected()){
                                Database.executeQuery("INSERT INTO wo_employees (wo_id, employee_id) VALUES (?, ?)",
                                        new Object[]{WONum, employees.get(i)});
                            }
                        }

                        //Update table
                        table.setValueAt(date, row, 1);
                        table.setValueAt(type, row, 2);
                        table.setValueAt(prio.equals("High") ? "<html><b>High</b></html>" : prio, row, 3);
                        table.setValueAt(assName, row, 4);
                        table.setValueAt(locName, row, 5);
                        table.setValueAt(employeeField.getText(), row, 6);
                        //Select updated row
                        table.setRowSelectionInterval(row, row);
                    }
                    
                    //Images
                    if(images.size() > 0){
                        int count = 1;
                        for(String s : images){
                            //Move to images folder
                            File source = new File(s);
                            File dest = new File("wo_images\\"+WONum+"-"+count+source.getName().substring(source.getName().lastIndexOf(".")));
                            try {
                                Files.createDirectories(dest.toPath());
                                Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            } catch (IOException ex) {
                                Logger.getLogger(WOFrame.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            count++;
                        }
                    }
                    
                    TableTools.resize(table, 20);
                    
                    if(schedule != null){
                        String s = OtherTools.escapeHTML(schedule.getTable().getValueAt(scheduleRow, 5).toString());
                        if(s.equals("Today") || s.equals("Overdue")){
                            if(InternalDialog.showInternalConfirmDialog(MMS.getMainFrame().getDesktopPane(), "Would you like to mark Scheduled Task #"+schedule.getTable().getValueAt(scheduleRow, 0).toString()+" as complete?", "Complete Scheduled Task", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null) == 0){
                                schedule.completeTask(scheduleRow);
                            }
                        }
                    }
                    if(MMS.DEBUG){
                        try {
                            Thread.sleep(4000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(WOFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
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
        if(employeeButton.isEnabled()){
            addEmployees();
            employeeField.requestFocus();
            employeePopup.show(employeeButton, employeeButton.getWidth()-employeePopup.getWidth(), employeeButton.getHeight()+3);
        }
    }//GEN-LAST:event_employeeButtonMousePressed

    private void imageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imageButtonActionPerformed
        if(imageButton.getText().equals("Open Image(s)")){
            for(String s : images){
                File f = new File(System.getProperty("user.dir")+"\\wo_images\\"+s);
                try {
                    Desktop.getDesktop().open(f);
                } catch (IOException ex) {
                    Logger.getLogger(WOFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        else if(imageChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            File [] files = imageChooser.getSelectedFiles();
            for(File f : files){
                images.add(f.getAbsolutePath());
            }
            imageLabel.setText(files.length + (files.length > 1 ? " images" : " image") +" selected");
        }
    }//GEN-LAST:event_imageButtonActionPerformed

    private void backPanelComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_backPanelComponentResized
        datePicker.setSize(datePanel.getSize());
    }//GEN-LAST:event_backPanelComponentResized

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        backPanelComponentResized(null);
        splitPane.setDividerLocation(0.66);
        TableTools.resize(partsTable, 10);
    }//GEN-LAST:event_formComponentResized

    private void employeeFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_employeeFieldKeyPressed
        if(employeeButton.isEnabled()){
            addEmployees();
            for(MenuElement m : employeePopup.getSubElements()){
                String t = m.toString(), text = t.substring(t.indexOf("text=")+5, t.indexOf("text=")+6).toLowerCase();
                if(!text.equals(evt.getKeyChar()+"")) employeePopup.remove(m.getComponent());
            }
            employeePopup.show(employeeButton, employeeButton.getWidth()-employeePopup.getWidth(), employeeButton.getHeight()+3);
        }
    }//GEN-LAST:event_employeeFieldKeyPressed

    private void employeePopupMenuKeyPressed(javax.swing.event.MenuKeyEvent evt) {//GEN-FIRST:event_employeePopupMenuKeyPressed
        employeePopup.setVisible(false);
        employeeFieldKeyPressed(evt);
    }//GEN-LAST:event_employeePopupMenuKeyPressed
     
    private void addEmployees(){
        employeePopup.removeAll();
        for(JCheckBoxMenuItem e: employeeNames){
            employeePopup.add(e);
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea actionsArea;
    private javax.swing.JLabel actionsLabel;
    private javax.swing.JScrollPane actionsScroll;
    private javax.swing.JComboBox<String> assetCombo;
    private javax.swing.JLabel assetLabel;
    private javax.swing.JPanel backPanel;
    private javax.swing.JButton boldButton;
    private javax.swing.JPanel closedPanel;
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
    private javax.swing.JTextField endTimeField;
    private javax.swing.JLabel endTimeLabel;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel freqLabel;
    private javax.swing.JButton imageButton;
    private javax.swing.JFileChooser imageChooser;
    private javax.swing.JLabel imageLabel;
    private javax.swing.JButton italicsButton;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JComboBox<String> locationCombo;
    private javax.swing.JLabel locationLabel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel partsLabel;
    private javax.swing.JScrollPane partsScroll;
    private javax.swing.JTable partsTable;
    private javax.swing.JComboBox<String> priorityCombo;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JTextField startTimeField;
    private javax.swing.JLabel startTimeLabel;
    private javax.swing.JToolBar textTools;
    private javax.swing.JPanel topPanel;
    private javax.swing.JComboBox<String> typeCombo;
    private javax.swing.JLabel typeLabel;
    private javax.swing.JToggleButton viewToggleButton;
    // End of variables declaration//GEN-END:variables
}
