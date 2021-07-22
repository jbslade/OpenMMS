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

import com.mms.panels.FindPanel;
import com.mms.dialogs.InternalDialog;
import com.mms.panels.PopupPanel;
import com.mms.dialogs.AssetDialog;
import com.mms.dialogs.EmployeeDialog;
import com.mms.dialogs.LocationDialog;
import com.mms.dialogs.PartDialog;
import com.mms.dialogs.PasswordDialog;
import com.mms.dialogs.ScheduleDialog;
import com.mms.utilities.DateTools;
import com.mms.utilities.TableTools;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author J.B. Slade
 */
public class MainFrame extends javax.swing.JFrame {
    
    private FindPanel find;
    
    public MainFrame() {
        initComponents();
        //Desktop pane
        desktopPane.setLayer(tabbedPane, 0);
        
        //Menu bar
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(menuUser);
        
        //Tab panes
        tabbedPane.putClientProperty("JTabbedPane.tabWidthMode", "equal");
        tabbedPane.putClientProperty("JTabbedPane.tabIconPlacement", SwingConstants.TOP);
        tabbedPane.setIconAt(0, MMS.dashboardIcon);
        tabbedPane.setIconAt(1, MMS.workOrdersIcon);
        tabbedPane.setIconAt(2, MMS.scheduleIcon);
        tabbedPane.setIconAt(3, MMS.assetsIcon);
        tabbedPane.setIconAt(4, MMS.locationIcon);
        tabbedPane.setIconAt(5, MMS.partsIcon);
        tabbedPane.setIconAt(6, MMS.employeesIcon);
        tabbedPane.setIconAt(7, MMS.reportsIcon);
        tabbedPane.setIconAt(8, MMS.systemIcon);
        adminTabbedPane.putClientProperty("JTabbedPane.tabWidthMode", "equal");
        
        //Tables
        workOrderTable.setBackground(Color.white);
        workOrderTable.getTableHeader().setBackground(Color.white);
        workOrderTable.getTableHeader().setBorder(new MatteBorder(0,0,1,0, workOrderTable.getGridColor()));
        workOrderTable.setShowGrid(true);
        ((DefaultTableCellRenderer)workOrderTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
        scheduleTable.setBackground(Color.white);
        scheduleTable.getTableHeader().setBackground(Color.white);
        scheduleTable.getTableHeader().setBorder(new MatteBorder(0,0,1,0, scheduleTable.getGridColor()));
        scheduleTable.setShowGrid(true);
        ((DefaultTableCellRenderer)scheduleTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
        assetTable.setBackground(Color.white);
        assetTable.getTableHeader().setBackground(Color.white);
        assetTable.getTableHeader().setBorder(new MatteBorder(0,0,1,0, assetTable.getGridColor()));
        assetTable.setShowGrid(true);
        ((DefaultTableCellRenderer)assetTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
        locationTable.setBackground(Color.white);
        locationTable.getTableHeader().setBackground(Color.white);
        locationTable.getTableHeader().setBorder(new MatteBorder(0,0,1,0, locationTable.getGridColor()));
        locationTable.setShowGrid(true);
        ((DefaultTableCellRenderer)locationTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
        partTable.setBackground(Color.white);
        partTable.getTableHeader().setBackground(Color.white);
        partTable.getTableHeader().setBorder(new MatteBorder(0,0,1,0, partTable.getGridColor()));
        partTable.setAutoCreateRowSorter(true);
        partTable.setShowGrid(true);
        ((DefaultTableCellRenderer)partTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
        employeeTable.setBackground(Color.white);
        employeeTable.getTableHeader().setBackground(Color.white);
        employeeTable.getTableHeader().setBorder(new MatteBorder(0,0,1,0, employeeTable.getGridColor()));
        employeeTable.setShowGrid(true);
        ((DefaultTableCellRenderer)employeeTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
        adminUserTable.setBackground(Color.white);
        adminUserTable.getTableHeader().setBackground(Color.white);
        adminUserTable.getTableHeader().setBorder(new MatteBorder(0,0,1,0, adminUserTable.getGridColor()));
        adminUserTable.setShowGrid(true);
        ((DefaultTableCellRenderer)adminUserTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
        
        //Set user
        menuUser.setText(MMS.getUser());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tableSizeGroup = new javax.swing.ButtonGroup();
        backPanel = new javax.swing.JPanel();
        desktopPane = new javax.swing.JDesktopPane();
        tabbedPane = new javax.swing.JTabbedPane();
        dashPanel = new javax.swing.JPanel();
        dashButtonPanel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        workOrderPanel = new javax.swing.JPanel();
        workOrderTools = new javax.swing.JToolBar();
        newWOButton = new javax.swing.JButton();
        editWOButton = new javax.swing.JButton();
        deleteWOButton = new javax.swing.JButton();
        archiveWOButton = new javax.swing.JButton();
        statusWOButton = new javax.swing.JButton();
        viewWOButton = new javax.swing.JButton();
        printWOButton = new javax.swing.JButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        workLoadLabel = new javax.swing.JLabel();
        workOrderScroll = new javax.swing.JScrollPane();
        workOrderTable = new javax.swing.JTable();
        schedulePanel = new javax.swing.JPanel();
        scheduleTools = new javax.swing.JToolBar();
        newScheduleButton = new javax.swing.JButton();
        editScheduleButton = new javax.swing.JButton();
        deleteScheduleButton = new javax.swing.JButton();
        archiveScheduleButton = new javax.swing.JButton();
        completeScheduleButton = new javax.swing.JButton();
        viewScheduleButton = new javax.swing.JButton();
        createWOButton1 = new javax.swing.JButton();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        scheduleLoadLabel = new javax.swing.JLabel();
        scheduleScroll = new javax.swing.JScrollPane();
        scheduleTable = new javax.swing.JTable();
        assetPanel = new javax.swing.JPanel();
        assetTools = new javax.swing.JToolBar();
        newAssetButton = new javax.swing.JButton();
        editAssetButton = new javax.swing.JButton();
        deleteAssetButton = new javax.swing.JButton();
        archiveAssetButton = new javax.swing.JButton();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        assetLoadLabel = new javax.swing.JLabel();
        assetScroll = new javax.swing.JScrollPane();
        assetTable = new javax.swing.JTable();
        locationPanel = new javax.swing.JPanel();
        locationTools = new javax.swing.JToolBar();
        newLocationButton = new javax.swing.JButton();
        editLocationButton = new javax.swing.JButton();
        deleteLocationButton = new javax.swing.JButton();
        archiveLocationButton = new javax.swing.JButton();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        locationLoadLabel = new javax.swing.JLabel();
        locationScroll = new javax.swing.JScrollPane();
        locationTable = new javax.swing.JTable();
        partPanel = new javax.swing.JPanel();
        partTools = new javax.swing.JToolBar();
        newPartButton = new javax.swing.JButton();
        editPartButton = new javax.swing.JButton();
        deletePartButton = new javax.swing.JButton();
        archivePartButton = new javax.swing.JButton();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        partLoadLabel = new javax.swing.JLabel();
        partScroll = new javax.swing.JScrollPane();
        partTable = new javax.swing.JTable();
        employeePanel = new javax.swing.JPanel();
        employeeTools = new javax.swing.JToolBar();
        newEmployeeButton = new javax.swing.JButton();
        editEmployeeButton = new javax.swing.JButton();
        deleteEmployeeButton = new javax.swing.JButton();
        archiveEmployeeButton = new javax.swing.JButton();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        employeeLoadLabel = new javax.swing.JLabel();
        employeeScroll = new javax.swing.JScrollPane();
        employeeTable = new javax.swing.JTable();
        reportPanel = new javax.swing.JPanel();
        adminPanel = new javax.swing.JPanel();
        adminTabbedPane = new javax.swing.JTabbedPane();
        adminUserPanel = new javax.swing.JPanel();
        adminUserScroll = new javax.swing.JScrollPane();
        adminUserTable = new javax.swing.JTable();
        adminUserTools = new javax.swing.JToolBar();
        newUser = new javax.swing.JButton();
        editUser = new javax.swing.JButton();
        deleteUser = new javax.swing.JButton();
        resetUserPass = new javax.swing.JButton();
        adminArchivePanel = new javax.swing.JPanel();
        adminCustomPanel = new javax.swing.JPanel();
        adminUpdatePanel = new javax.swing.JPanel();
        menuBar = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuFileDisconnect = new javax.swing.JMenuItem();
        menuTable = new javax.swing.JMenu();
        menuTableFilter = new javax.swing.JMenuItem();
        menuTableRefresh = new javax.swing.JMenuItem();
        menuTableSize = new javax.swing.JMenu();
        menuTableSizeAll = new javax.swing.JRadioButtonMenuItem();
        menuTableSize1000 = new javax.swing.JRadioButtonMenuItem();
        menuTableSize500 = new javax.swing.JRadioButtonMenuItem();
        menuTableSize250 = new javax.swing.JRadioButtonMenuItem();
        menuHelp = new javax.swing.JMenu();
        menuUser = new javax.swing.JMenu();
        menuChangePassword = new javax.swing.JMenuItem();
        menuLogout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        tabbedPane.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        tabbedPane.setFocusable(false);
        tabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabbedPaneStateChanged(evt);
            }
        });

        dashButtonPanel.setLayout(new java.awt.GridLayout(1, 0));

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tabs/workOrders.png"))); // NOI18N
        jButton1.setText("3 Open Work Orders");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        dashButtonPanel.add(jButton1);

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tabs/schedule.png"))); // NOI18N
        jButton2.setText("3 Tasks Due Today");
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        dashButtonPanel.add(jButton2);

        jButton4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tabs/parts.png"))); // NOI18N
        jButton4.setText("5 Parts Running Low");
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        dashButtonPanel.add(jButton4);

        jButton3.setText("jButton3");
        jButton3.setFocusable(false);
        dashButtonPanel.add(jButton3);

        javax.swing.GroupLayout dashPanelLayout = new javax.swing.GroupLayout(dashPanel);
        dashPanel.setLayout(dashPanelLayout);
        dashPanelLayout.setHorizontalGroup(
            dashPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dashButtonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 927, Short.MAX_VALUE)
                .addContainerGap())
        );
        dashPanelLayout.setVerticalGroup(
            dashPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dashButtonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(424, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Dashboard", dashPanel);

        workOrderTools.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        workOrderTools.setFloatable(false);
        workOrderTools.setRollover(true);

        newWOButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/new.png"))); // NOI18N
        newWOButton.setText("New");
        workOrderTools.add(newWOButton);

        editWOButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/edit.png"))); // NOI18N
        editWOButton.setText("Edit");
        workOrderTools.add(editWOButton);

        deleteWOButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/delete.png"))); // NOI18N
        deleteWOButton.setText("Delete");
        deleteWOButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteWOButtonActionPerformed(evt);
            }
        });
        workOrderTools.add(deleteWOButton);

        archiveWOButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/archive.png"))); // NOI18N
        archiveWOButton.setText("Archive");
        workOrderTools.add(archiveWOButton);

        statusWOButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/status.png"))); // NOI18N
        statusWOButton.setText("Status");
        workOrderTools.add(statusWOButton);

        viewWOButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/view.png"))); // NOI18N
        viewWOButton.setText("View");
        workOrderTools.add(viewWOButton);

        printWOButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/print.png"))); // NOI18N
        printWOButton.setText("Print");
        workOrderTools.add(printWOButton);
        workOrderTools.add(filler1);

        workLoadLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ajax.gif"))); // NOI18N
        workLoadLabel.setText("  ");
        workOrderTools.add(workLoadLabel);

        workOrderScroll.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(204, 204, 204)));

        workOrderTable.setAutoCreateRowSorter(true);
        workOrderTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "Date", "Type", "Priority", "Asset", "Location", "Employee(s)", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        workOrderTable.setDragEnabled(true);
        workOrderTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                workOrderTableKeyPressed(evt);
            }
        });
        workOrderScroll.setViewportView(workOrderTable);

        javax.swing.GroupLayout workOrderPanelLayout = new javax.swing.GroupLayout(workOrderPanel);
        workOrderPanel.setLayout(workOrderPanelLayout);
        workOrderPanelLayout.setHorizontalGroup(
            workOrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(workOrderScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 951, Short.MAX_VALUE)
            .addComponent(workOrderTools, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        workOrderPanelLayout.setVerticalGroup(
            workOrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(workOrderPanelLayout.createSequentialGroup()
                .addComponent(workOrderTools, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(workOrderScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Work Orders", workOrderPanel);

        scheduleTools.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        scheduleTools.setFloatable(false);
        scheduleTools.setRollover(true);

        newScheduleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/new.png"))); // NOI18N
        newScheduleButton.setText("New");
        newScheduleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newScheduleButtonActionPerformed(evt);
            }
        });
        scheduleTools.add(newScheduleButton);

        editScheduleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/edit.png"))); // NOI18N
        editScheduleButton.setText("Edit");
        scheduleTools.add(editScheduleButton);

        deleteScheduleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/delete.png"))); // NOI18N
        deleteScheduleButton.setText("Delete");
        deleteScheduleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteScheduleButtonActionPerformed(evt);
            }
        });
        scheduleTools.add(deleteScheduleButton);

        archiveScheduleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/archive.png"))); // NOI18N
        archiveScheduleButton.setText("Archive");
        scheduleTools.add(archiveScheduleButton);

        completeScheduleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/complete.png"))); // NOI18N
        completeScheduleButton.setText("Complete");
        completeScheduleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                completeScheduleButtonActionPerformed(evt);
            }
        });
        scheduleTools.add(completeScheduleButton);

        viewScheduleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/view.png"))); // NOI18N
        viewScheduleButton.setText("View");
        scheduleTools.add(viewScheduleButton);

        createWOButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/createWO.png"))); // NOI18N
        createWOButton1.setText("Create W/O");
        scheduleTools.add(createWOButton1);
        scheduleTools.add(filler2);

        scheduleLoadLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ajax.gif"))); // NOI18N
        scheduleLoadLabel.setText("  ");
        scheduleTools.add(scheduleLoadLabel);

        scheduleScroll.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(204, 204, 204)));

        scheduleTable.setAutoCreateRowSorter(true);
        scheduleTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "Name", "Type", "Frequency", "Last Done", "Next Due", "Asset", "Location"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scheduleTable.setDragEnabled(true);
        scheduleTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scheduleTableKeyPressed(evt);
            }
        });
        scheduleScroll.setViewportView(scheduleTable);

        javax.swing.GroupLayout schedulePanelLayout = new javax.swing.GroupLayout(schedulePanel);
        schedulePanel.setLayout(schedulePanelLayout);
        schedulePanelLayout.setHorizontalGroup(
            schedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scheduleTools, javax.swing.GroupLayout.DEFAULT_SIZE, 951, Short.MAX_VALUE)
            .addComponent(scheduleScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 951, Short.MAX_VALUE)
        );
        schedulePanelLayout.setVerticalGroup(
            schedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(schedulePanelLayout.createSequentialGroup()
                .addComponent(scheduleTools, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(scheduleScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Schedule", schedulePanel);

        assetTools.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        assetTools.setFloatable(false);
        assetTools.setRollover(true);

        newAssetButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/new.png"))); // NOI18N
        newAssetButton.setText("New");
        newAssetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newAssetButtonActionPerformed(evt);
            }
        });
        assetTools.add(newAssetButton);

        editAssetButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/edit.png"))); // NOI18N
        editAssetButton.setText("Edit");
        editAssetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editAssetButtonActionPerformed(evt);
            }
        });
        assetTools.add(editAssetButton);

        deleteAssetButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/delete.png"))); // NOI18N
        deleteAssetButton.setText("Delete");
        deleteAssetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteAssetButtonActionPerformed(evt);
            }
        });
        assetTools.add(deleteAssetButton);

        archiveAssetButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/archive.png"))); // NOI18N
        archiveAssetButton.setText("Archive");
        archiveAssetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                archiveAssetButtonActionPerformed(evt);
            }
        });
        assetTools.add(archiveAssetButton);
        assetTools.add(filler4);

        assetLoadLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ajax.gif"))); // NOI18N
        assetLoadLabel.setText("  ");
        assetTools.add(assetLoadLabel);

        assetScroll.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(204, 204, 204)));

        assetTable.setAutoCreateRowSorter(true);
        assetTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "Name", "Description", "Type", "Location"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        assetTable.setDragEnabled(true);
        assetTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                assetTableKeyPressed(evt);
            }
        });
        assetScroll.setViewportView(assetTable);

        javax.swing.GroupLayout assetPanelLayout = new javax.swing.GroupLayout(assetPanel);
        assetPanel.setLayout(assetPanelLayout);
        assetPanelLayout.setHorizontalGroup(
            assetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(assetTools, javax.swing.GroupLayout.DEFAULT_SIZE, 951, Short.MAX_VALUE)
            .addComponent(assetScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 951, Short.MAX_VALUE)
        );
        assetPanelLayout.setVerticalGroup(
            assetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(assetPanelLayout.createSequentialGroup()
                .addComponent(assetTools, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(assetScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Assets", assetPanel);

        locationTools.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        locationTools.setFloatable(false);
        locationTools.setRollover(true);

        newLocationButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/new.png"))); // NOI18N
        newLocationButton.setText("New");
        newLocationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newLocationButtonActionPerformed(evt);
            }
        });
        locationTools.add(newLocationButton);

        editLocationButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/edit.png"))); // NOI18N
        editLocationButton.setText("Edit");
        editLocationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editLocationButtonActionPerformed(evt);
            }
        });
        locationTools.add(editLocationButton);

        deleteLocationButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/delete.png"))); // NOI18N
        deleteLocationButton.setText("Delete");
        deleteLocationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteLocationButtonActionPerformed(evt);
            }
        });
        locationTools.add(deleteLocationButton);

        archiveLocationButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/archive.png"))); // NOI18N
        archiveLocationButton.setText("Archive");
        archiveLocationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                archiveLocationButtonActionPerformed(evt);
            }
        });
        locationTools.add(archiveLocationButton);
        locationTools.add(filler3);

        locationLoadLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ajax.gif"))); // NOI18N
        locationLoadLabel.setText("  ");
        locationTools.add(locationLoadLabel);

        locationScroll.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(204, 204, 204)));

        locationTable.setAutoCreateRowSorter(true);
        locationTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "Name", "Description"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        locationTable.setDragEnabled(true);
        locationTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                locationTableKeyPressed(evt);
            }
        });
        locationScroll.setViewportView(locationTable);

        javax.swing.GroupLayout locationPanelLayout = new javax.swing.GroupLayout(locationPanel);
        locationPanel.setLayout(locationPanelLayout);
        locationPanelLayout.setHorizontalGroup(
            locationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(locationTools, javax.swing.GroupLayout.DEFAULT_SIZE, 951, Short.MAX_VALUE)
            .addComponent(locationScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 951, Short.MAX_VALUE)
        );
        locationPanelLayout.setVerticalGroup(
            locationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(locationPanelLayout.createSequentialGroup()
                .addComponent(locationTools, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(locationScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Locations", locationPanel);

        partTools.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        partTools.setFloatable(false);
        partTools.setRollover(true);

        newPartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/new.png"))); // NOI18N
        newPartButton.setText("New");
        newPartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newPartButtonActionPerformed(evt);
            }
        });
        partTools.add(newPartButton);

        editPartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/edit.png"))); // NOI18N
        editPartButton.setText("Edit");
        editPartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editPartButtonActionPerformed(evt);
            }
        });
        partTools.add(editPartButton);

        deletePartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/delete.png"))); // NOI18N
        deletePartButton.setText("Delete");
        deletePartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletePartButtonActionPerformed(evt);
            }
        });
        partTools.add(deletePartButton);

        archivePartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/archive.png"))); // NOI18N
        archivePartButton.setText("Archive");
        archivePartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                archivePartButtonActionPerformed(evt);
            }
        });
        partTools.add(archivePartButton);
        partTools.add(filler5);

        partLoadLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ajax.gif"))); // NOI18N
        partLoadLabel.setText("  ");
        partTools.add(partLoadLabel);

        partScroll.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(204, 204, 204)));

        partTable.setAutoCreateRowSorter(true);
        partTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "Name", "Quantity", "Price/Unit"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        partTable.setDragEnabled(true);
        partTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                partTableKeyPressed(evt);
            }
        });
        partScroll.setViewportView(partTable);

        javax.swing.GroupLayout partPanelLayout = new javax.swing.GroupLayout(partPanel);
        partPanel.setLayout(partPanelLayout);
        partPanelLayout.setHorizontalGroup(
            partPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(partTools, javax.swing.GroupLayout.DEFAULT_SIZE, 951, Short.MAX_VALUE)
            .addComponent(partScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 951, Short.MAX_VALUE)
        );
        partPanelLayout.setVerticalGroup(
            partPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(partPanelLayout.createSequentialGroup()
                .addComponent(partTools, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(partScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Parts", partPanel);

        employeeTools.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        employeeTools.setFloatable(false);
        employeeTools.setRollover(true);

        newEmployeeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/new.png"))); // NOI18N
        newEmployeeButton.setText("New");
        newEmployeeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newEmployeeButtonActionPerformed(evt);
            }
        });
        employeeTools.add(newEmployeeButton);

        editEmployeeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/edit.png"))); // NOI18N
        editEmployeeButton.setText("Edit");
        editEmployeeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editEmployeeButtonActionPerformed(evt);
            }
        });
        employeeTools.add(editEmployeeButton);

        deleteEmployeeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/delete.png"))); // NOI18N
        deleteEmployeeButton.setText("Delete");
        deleteEmployeeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteEmployeeButtonActionPerformed(evt);
            }
        });
        employeeTools.add(deleteEmployeeButton);

        archiveEmployeeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/archive.png"))); // NOI18N
        archiveEmployeeButton.setText("Archive");
        archiveEmployeeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                archiveEmployeeButtonActionPerformed(evt);
            }
        });
        employeeTools.add(archiveEmployeeButton);
        employeeTools.add(filler6);

        employeeLoadLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ajax.gif"))); // NOI18N
        employeeLoadLabel.setText("  ");
        employeeTools.add(employeeLoadLabel);

        employeeScroll.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(204, 204, 204)));

        employeeTable.setAutoCreateRowSorter(true);
        employeeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "Name", "Designation", "Department"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        employeeTable.setDragEnabled(true);
        employeeTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                employeeTableKeyPressed(evt);
            }
        });
        employeeScroll.setViewportView(employeeTable);

        javax.swing.GroupLayout employeePanelLayout = new javax.swing.GroupLayout(employeePanel);
        employeePanel.setLayout(employeePanelLayout);
        employeePanelLayout.setHorizontalGroup(
            employeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(employeeTools, javax.swing.GroupLayout.DEFAULT_SIZE, 951, Short.MAX_VALUE)
            .addComponent(employeeScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 951, Short.MAX_VALUE)
        );
        employeePanelLayout.setVerticalGroup(
            employeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeePanelLayout.createSequentialGroup()
                .addComponent(employeeTools, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(employeeScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Employees", employeePanel);

        javax.swing.GroupLayout reportPanelLayout = new javax.swing.GroupLayout(reportPanel);
        reportPanel.setLayout(reportPanelLayout);
        reportPanelLayout.setHorizontalGroup(
            reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 951, Short.MAX_VALUE)
        );
        reportPanelLayout.setVerticalGroup(
            reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 569, Short.MAX_VALUE)
        );

        tabbedPane.addTab("Reports", reportPanel);

        adminTabbedPane.setFocusable(false);

        adminUserScroll.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(204, 204, 204)));

        adminUserTable.setAutoCreateRowSorter(true);
        adminUserTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Username", "User Level", "Logged In"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        adminUserTable.setDragEnabled(true);
        adminUserScroll.setViewportView(adminUserTable);

        adminUserTools.setFloatable(false);
        adminUserTools.setRollover(true);

        newUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/new.png"))); // NOI18N
        newUser.setText("New");
        newUser.setFocusable(false);
        newUser.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        newUser.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        adminUserTools.add(newUser);

        editUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/edit.png"))); // NOI18N
        editUser.setText("Edit");
        editUser.setFocusable(false);
        editUser.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        editUser.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        adminUserTools.add(editUser);

        deleteUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/delete.png"))); // NOI18N
        deleteUser.setText("Delete");
        deleteUser.setFocusable(false);
        deleteUser.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        deleteUser.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        adminUserTools.add(deleteUser);

        resetUserPass.setText("Reset Password");
        resetUserPass.setFocusable(false);
        resetUserPass.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        resetUserPass.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        adminUserTools.add(resetUserPass);

        javax.swing.GroupLayout adminUserPanelLayout = new javax.swing.GroupLayout(adminUserPanel);
        adminUserPanel.setLayout(adminUserPanelLayout);
        adminUserPanelLayout.setHorizontalGroup(
            adminUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(adminUserScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 946, Short.MAX_VALUE)
            .addComponent(adminUserTools, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        adminUserPanelLayout.setVerticalGroup(
            adminUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, adminUserPanelLayout.createSequentialGroup()
                .addComponent(adminUserTools, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(adminUserScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE))
        );

        adminTabbedPane.addTab("Users", adminUserPanel);

        javax.swing.GroupLayout adminArchivePanelLayout = new javax.swing.GroupLayout(adminArchivePanel);
        adminArchivePanel.setLayout(adminArchivePanelLayout);
        adminArchivePanelLayout.setHorizontalGroup(
            adminArchivePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 946, Short.MAX_VALUE)
        );
        adminArchivePanelLayout.setVerticalGroup(
            adminArchivePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 539, Short.MAX_VALUE)
        );

        adminTabbedPane.addTab("Archive", adminArchivePanel);

        javax.swing.GroupLayout adminCustomPanelLayout = new javax.swing.GroupLayout(adminCustomPanel);
        adminCustomPanel.setLayout(adminCustomPanelLayout);
        adminCustomPanelLayout.setHorizontalGroup(
            adminCustomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 946, Short.MAX_VALUE)
        );
        adminCustomPanelLayout.setVerticalGroup(
            adminCustomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 539, Short.MAX_VALUE)
        );

        adminTabbedPane.addTab("Custom Fields", adminCustomPanel);

        javax.swing.GroupLayout adminUpdatePanelLayout = new javax.swing.GroupLayout(adminUpdatePanel);
        adminUpdatePanel.setLayout(adminUpdatePanelLayout);
        adminUpdatePanelLayout.setHorizontalGroup(
            adminUpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 946, Short.MAX_VALUE)
        );
        adminUpdatePanelLayout.setVerticalGroup(
            adminUpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 541, Short.MAX_VALUE)
        );

        adminTabbedPane.addTab("Updates", adminUpdatePanel);

        javax.swing.GroupLayout adminPanelLayout = new javax.swing.GroupLayout(adminPanel);
        adminPanel.setLayout(adminPanelLayout);
        adminPanelLayout.setHorizontalGroup(
            adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(adminTabbedPane)
        );
        adminPanelLayout.setVerticalGroup(
            adminPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(adminTabbedPane)
        );

        tabbedPane.addTab("Admin", adminPanel);

        desktopPane.setLayer(tabbedPane, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout desktopPaneLayout = new javax.swing.GroupLayout(desktopPane);
        desktopPane.setLayout(desktopPaneLayout);
        desktopPaneLayout.setHorizontalGroup(
            desktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane)
        );
        desktopPaneLayout.setVerticalGroup(
            desktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane)
        );

        javax.swing.GroupLayout backPanelLayout = new javax.swing.GroupLayout(backPanel);
        backPanel.setLayout(backPanelLayout);
        backPanelLayout.setHorizontalGroup(
            backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktopPane)
        );
        backPanelLayout.setVerticalGroup(
            backPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktopPane)
        );

        menuFile.setText("File");

        menuFileDisconnect.setText("Disconnect Database");
        menuFileDisconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFileDisconnectActionPerformed(evt);
            }
        });
        menuFile.add(menuFileDisconnect);

        menuBar.add(menuFile);

        menuTable.setText("Table");

        menuTableFilter.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuTableFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/find.png"))); // NOI18N
        menuTableFilter.setMnemonic('F');
        menuTableFilter.setText("Find");
        menuTableFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTableFilterActionPerformed(evt);
            }
        });
        menuTable.add(menuTableFilter);

        menuTableRefresh.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuTableRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/refresh.png"))); // NOI18N
        menuTableRefresh.setMnemonic('R');
        menuTableRefresh.setText("Refresh");
        menuTableRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTableRefreshActionPerformed(evt);
            }
        });
        menuTable.add(menuTableRefresh);

        menuTableSize.setText("Size");

        tableSizeGroup.add(menuTableSizeAll);
        menuTableSizeAll.setSelected(true);
        menuTableSizeAll.setText("All Rows");
        menuTableSizeAll.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                menuTableSizeAllStateChanged(evt);
            }
        });
        menuTableSize.add(menuTableSizeAll);

        tableSizeGroup.add(menuTableSize1000);
        menuTableSize1000.setText("Top 1000");
        menuTableSize1000.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                menuTableSize1000StateChanged(evt);
            }
        });
        menuTableSize.add(menuTableSize1000);

        tableSizeGroup.add(menuTableSize500);
        menuTableSize500.setText("Top 500");
        menuTableSize500.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                menuTableSize500StateChanged(evt);
            }
        });
        menuTableSize.add(menuTableSize500);

        tableSizeGroup.add(menuTableSize250);
        menuTableSize250.setText("Top 250");
        menuTableSize250.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                menuTableSize250StateChanged(evt);
            }
        });
        menuTableSize.add(menuTableSize250);

        menuTable.add(menuTableSize);

        menuBar.add(menuTable);

        menuHelp.setText("Help");
        menuBar.add(menuHelp);

        menuUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/user.png"))); // NOI18N
        menuUser.setText("Administrator");
        menuUser.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        menuUser.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        menuChangePassword.setText("Change Password");
        menuChangePassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuChangePasswordActionPerformed(evt);
            }
        });
        menuUser.add(menuChangePassword);

        menuLogout.setText("Logout");
        menuLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuLogoutActionPerformed(evt);
            }
        });
        menuUser.add(menuLogout);

        menuBar.add(menuUser);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //Shutdown
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        MMS.shutdown();
    }//GEN-LAST:event_formWindowClosing

    //New Location
    private void newLocationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newLocationButtonActionPerformed
        LocationDialog l = new LocationDialog(locationTable, -1);
        l.setSize(MMS.DIAG_WIDTH, l.getHeight());
        l.setLocation(desktopPane.getWidth()/2-l.getWidth()/2, desktopPane.getHeight()/2-l.getHeight()/2-50);
        desktopPane.add(l);
        desktopPane.setLayer(l, 1);
        l.setVisible(true);
    }//GEN-LAST:event_newLocationButtonActionPerformed

    //Edit Location
    private void editLocationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editLocationButtonActionPerformed
        LocationDialog l = new LocationDialog(locationTable, locationTable.getSelectedRow());
        l.setSize(MMS.DIAG_WIDTH, l.getHeight());
        l.setTitle("Edit Location");
        l.setLocation(desktopPane.getWidth()/2-l.getWidth()/2, desktopPane.getHeight()/2-l.getHeight()/2-50);
        desktopPane.add(l);
        desktopPane.setLayer(l, 1);
        l.setVisible(true);
    }//GEN-LAST:event_editLocationButtonActionPerformed

    //Delete Location
    private void deleteLocationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteLocationButtonActionPerformed
        String message, title;
        if(locationTable.getSelectedRowCount() > 1){
            message = "Are you sure you want to delete these "+locationTable.getSelectedRowCount()+" locations?";
            title = "Delete Locations";
        }
        else{
            message = "Are you sure you want to delete "+locationTable.getValueAt(locationTable.getSelectedRow(), 1)+"?";
            title = "Delete Location";
        }
        if(InternalDialog.showInternalConfirmDialog(desktopPane, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null) == 0){
            int rows [] = locationTable.getSelectedRows();
            //Delete from DB
            for(int row : rows){
                int id = Integer.parseInt(locationTable.getValueAt(row, 0).toString());
                Database.executeQuery("DELETE FROM locations WHERE id = ?",
                        new Object[]{id});
            }
            //Delete from table
            for(int i = rows.length-1; i >= 0; i--){
                DefaultTableModel m = (DefaultTableModel)locationTable.getModel();
                m.removeRow(rows[i]);
            }
            
            //Select first row
            if(locationTable.getRowCount() != 0) locationTable.setRowSelectionInterval(0, 0);
        }
    }//GEN-LAST:event_deleteLocationButtonActionPerformed

    //Archive Location
    private void archiveLocationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_archiveLocationButtonActionPerformed
        String message, title;
        if(locationTable.getSelectedRowCount() > 1){
            message = "Are you sure you want to archive these "+locationTable.getSelectedRowCount()+" locations?";
            title = "Archive Locations";
        }
        else{
            message = "Are you sure you want to archive "+locationTable.getValueAt(locationTable.getSelectedRow(), 1)+"?";
            title = "Archive Location";
        }
        
        if(InternalDialog.showInternalConfirmDialog(desktopPane, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null) == 0){
            int rows [] = locationTable.getSelectedRows();
            //Set Archived = Y
            for(int row : rows){
                int id = Integer.parseInt(locationTable.getValueAt(row, 0).toString());
                Database.executeQuery("UPDATE locations SET archived = ? WHERE id = ?",
                        new Object[]{"Y", id});
            }
            //Delete from table
            for(int i = rows.length-1; i >= 0; i--){
                DefaultTableModel m = (DefaultTableModel)locationTable.getModel();
                m.removeRow(rows[i]);
            }
            
            //Select first row
            if(locationTable.getRowCount() != 0) locationTable.setRowSelectionInterval(0, 0);
        }
    }//GEN-LAST:event_archiveLocationButtonActionPerformed

    //New Asset
    private void newAssetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newAssetButtonActionPerformed
        if(locationTable.getRowCount() == 0){
            PopupPanel.display("You must add a Location before you can add an Asset.", newAssetButton.getLocationOnScreen().x+10, newAssetButton.getLocationOnScreen().y+newAssetButton.getHeight()+10);
        }
        else{
            AssetDialog a = new AssetDialog(assetTable, -1);
            a.setSize(MMS.DIAG_WIDTH, a.getHeight());
            a.setLocation(desktopPane.getWidth()/2-a.getWidth()/2, desktopPane.getHeight()/2-a.getHeight()/2-50);
            desktopPane.add(a);
            desktopPane.setLayer(a, 1);
            a.setVisible(true);
        }
    }//GEN-LAST:event_newAssetButtonActionPerformed

    //Edit Asset
    private void editAssetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editAssetButtonActionPerformed
        AssetDialog a = new AssetDialog(assetTable, assetTable.getSelectedRow());
        a.setSize(MMS.DIAG_WIDTH, a.getHeight());
        a.setTitle("Edit Asset");
        a.setLocation(desktopPane.getWidth()/2-a.getWidth()/2, desktopPane.getHeight()/2-a.getHeight()/2-50);
        desktopPane.add(a);
        desktopPane.setLayer(a, 1);
        a.setVisible(true);
    }//GEN-LAST:event_editAssetButtonActionPerformed

    //Delete Asset
    private void deleteAssetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteAssetButtonActionPerformed
        String message, title;
        if(assetTable.getSelectedRowCount() > 1){
            message = "Are you sure you want to delete these "+assetTable.getSelectedRowCount()+" assets?";
            title = "Delete Assets";
        }
        else{
            message = "Are you sure you want to delete "+assetTable.getValueAt(assetTable.getSelectedRow(), 1)+"?";
            title = "Delete Assets";
        }
        if(InternalDialog.showInternalConfirmDialog(desktopPane, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null) == 0){
            int rows [] = assetTable.getSelectedRows();
            //Delete from DB
            for(int row : rows){
                int id = Integer.parseInt(assetTable.getValueAt(row, 0).toString());
                Database.executeQuery("DELETE FROM assets WHERE id = ?",
                        new Object[]{id});
            }
            //Delete from table
            for(int i = rows.length-1; i >= 0; i--){
                DefaultTableModel m = (DefaultTableModel)assetTable.getModel();
                m.removeRow(rows[i]);
            }
            
            //Select first row
            if(assetTable.getRowCount() != 0) assetTable.setRowSelectionInterval(0, 0);
        }
    }//GEN-LAST:event_deleteAssetButtonActionPerformed

    //Archive Asset
    private void archiveAssetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_archiveAssetButtonActionPerformed
        String message, title;
        if(assetTable.getSelectedRowCount() > 1){
            message = "Are you sure you want to archive these "+assetTable.getSelectedRowCount()+" assets?";
            title = "Archive Assets";
        }
        else{
            message = "Are you sure you want to archive "+assetTable.getValueAt(assetTable.getSelectedRow(), 1)+"?";
            title = "Archive Assets";
        }
        
        if(InternalDialog.showInternalConfirmDialog(desktopPane, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null) == 0){
            int rows [] = assetTable.getSelectedRows();
            //Set Archived = Y
            for(int row : rows){
                int id = Integer.parseInt(assetTable.getValueAt(row, 0).toString());
                Database.executeQuery("UPDATE assets SET archived = ? WHERE id = ?",
                        new Object[]{"Y", id});
            }
            //Delete from table
            for(int i = rows.length-1; i >= 0; i--){
                DefaultTableModel m = (DefaultTableModel)assetTable.getModel();
                m.removeRow(rows[i]);
            }
            
            //Select first row
            if(assetTable.getRowCount() != 0) assetTable.setRowSelectionInterval(0, 0);
        }
    }//GEN-LAST:event_archiveAssetButtonActionPerformed

    //Refresh table
    private void menuTableRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTableRefreshActionPerformed
        int index = tabbedPane.getSelectedIndex();
        switch(index){
            case 1: //Work orders
                break;
            case 2: //Schedule
                loadSchedule(scheduleTable.getSelectedRow()); break;
            case 3: //Assets
                loadAssets(assetTable.getSelectedRow()); break;
            case 4: //Locations
                loadLocations(locationTable.getSelectedRow()); break;
            case 5: //Parts
                loadParts(partTable.getSelectedRow()); break;
            case 6: //Employees
                loadEmployees(employeeTable.getSelectedRow()); break;
            default: break;
        }
    }//GEN-LAST:event_menuTableRefreshActionPerformed

    //New Employee
    private void newEmployeeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newEmployeeButtonActionPerformed
        EmployeeDialog e = new EmployeeDialog(employeeTable, -1);
        e.setSize(MMS.DIAG_WIDTH, e.getHeight());
        e.setLocation(desktopPane.getWidth()/2-e.getWidth()/2, desktopPane.getHeight()/2-e.getHeight()/2-50);
        desktopPane.add(e);
        desktopPane.setLayer(e, 1);
        e.setVisible(true);
    }//GEN-LAST:event_newEmployeeButtonActionPerformed

    //Edit Employee
    private void editEmployeeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editEmployeeButtonActionPerformed
        EmployeeDialog e = new EmployeeDialog(employeeTable, employeeTable.getSelectedRow());
        e.setSize(MMS.DIAG_WIDTH, e.getHeight());
        e.setTitle("Edit Employee");
        e.setLocation(desktopPane.getWidth()/2-e.getWidth()/2, desktopPane.getHeight()/2-e.getHeight()/2-50);
        desktopPane.add(e);
        desktopPane.setLayer(e, 1);
        e.setVisible(true);
    }//GEN-LAST:event_editEmployeeButtonActionPerformed

    //Delete Employee
    private void deleteEmployeeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteEmployeeButtonActionPerformed
        String message, title;
        if(employeeTable.getSelectedRowCount() > 1){
            message = "Are you sure you want to delete these "+employeeTable.getSelectedRowCount()+" employees?";
            title = "Delete Employees";
        }
        else{
            message = "Are you sure you want to delete "+employeeTable.getValueAt(employeeTable.getSelectedRow(), 1)+"?";
            title = "Delete Employee";
        }
        if(InternalDialog.showInternalConfirmDialog(desktopPane, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null) == 0){
            int rows [] = employeeTable.getSelectedRows();
            //Delete from DB
            for(int row : rows){
                int id = Integer.parseInt(employeeTable.getValueAt(row, 0).toString());
                Database.executeQuery("DELETE FROM employees WHERE id = ?",
                        new Object[]{id});
            }
            //Delete from table
            for(int i = rows.length-1; i >= 0; i--){
                DefaultTableModel m = (DefaultTableModel)employeeTable.getModel();
                m.removeRow(rows[i]);
            }
            
            //Select first row
            if(employeeTable.getRowCount() != 0) employeeTable.setRowSelectionInterval(0, 0);
        }
    }//GEN-LAST:event_deleteEmployeeButtonActionPerformed

    //Archive Employee
    private void archiveEmployeeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_archiveEmployeeButtonActionPerformed
        String message, title;
        if(employeeTable.getSelectedRowCount() > 1){
            message = "Are you sure you want to archive these "+employeeTable.getSelectedRowCount()+" employees?";
            title = "Archive Employees";
        }
        else{
            message = "Are you sure you want to archive "+employeeTable.getValueAt(employeeTable.getSelectedRow(), 1)+"?";
            title = "Archive Employee";
        }
        
        if(InternalDialog.showInternalConfirmDialog(desktopPane, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null) == 0){
            int rows [] = employeeTable.getSelectedRows();
            //Set Archived = Y
            for(int row : rows){
                int id = Integer.parseInt(employeeTable.getValueAt(row, 0).toString());
                Database.executeQuery("UPDATE employees SET archived = ? WHERE id = ?",
                        new Object[]{"Y", id});
            }
            //Delete from table
            for(int i = rows.length-1; i >= 0; i--){
                DefaultTableModel m = (DefaultTableModel)employeeTable.getModel();
                m.removeRow(rows[i]);
            }
            
            //Select first row
            if(employeeTable.getRowCount() != 0) employeeTable.setRowSelectionInterval(0, 0);
        }
    }//GEN-LAST:event_archiveEmployeeButtonActionPerformed

    //Find
    private void menuTableFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTableFilterActionPerformed
        JTable table = null;
        JPanel panel = null;
        switch(tabbedPane.getSelectedIndex()){
            case 1: table = workOrderTable; panel = workOrderPanel; break;
            case 2: table = scheduleTable; panel = schedulePanel; break;     
            case 3: table = assetTable; panel = assetPanel; break;
            case 4: table = locationTable; panel = locationPanel; break;
            case 5: table = partTable; panel = partPanel; break;
            case 6: table = employeeTable; panel = employeePanel; break;
            default: break;
        }
        if(table != null && find == null) find = new FindPanel(table, panel, "");
        else if(table != null && !find.isActive()) find = new FindPanel(table, panel, "");
    }//GEN-LAST:event_menuTableFilterActionPerformed

    //Tab changed
    private void tabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabbedPaneStateChanged
        //Close find
        if(find != null) find.close();
        //Close popup
        PopupPanel.close();
        //Tab specific
        switch(tabbedPane.getSelectedIndex()){
            case 0: //Dashboard
                menuTable.setVisible(false);
                break;
            case 1: //Work Orders
                menuTable.setVisible(true);
                setTableSizeRadio("tableSizeWO");
                break;
            case 2: //Schedule
                menuTable.setVisible(true);
                setTableSizeRadio("tableSizeSchedule");
                break;
            case 3: //Assets
                menuTable.setVisible(true);
                setTableSizeRadio("tableSizeAssets");
                break;
            case 4: //Locations
                menuTable.setVisible(true);
                setTableSizeRadio("tableSizeLocations");
                break;
            case 5: //Parts
                menuTable.setVisible(true);
                setTableSizeRadio("tableSizeParts");
                break;
            case 6: //Employees
                menuTable.setVisible(true);
                setTableSizeRadio("tableSizeEmployees");
                break;
            case 7: //Reports
                menuTable.setVisible(false);
                break;
            case 8: //Admin
                menuTable.setVisible(false);
                break;
            default: break;
        }
    }//GEN-LAST:event_tabbedPaneStateChanged

    private void setTableSizeRadio(String s){
        //Table size
        switch(MMS.getPrefs().getInt(s, -1)){
            case 1000: menuTableSize1000.setSelected(true); break;
            case 500: menuTableSize500.setSelected(true); break;
            case 250: menuTableSize250.setSelected(true); break;
            default: menuTableSizeAll.setSelected(true); break;
        }
    }
    
    //Change password
    private void menuChangePasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuChangePasswordActionPerformed
        PasswordDialog pd = new PasswordDialog(MMS.getUser());
        pd.setSize(MMS.DIAG_WIDTH, pd.getHeight());
        pd.setLocation(desktopPane.getWidth()/2-pd.getWidth()/2, desktopPane.getHeight()/2-pd.getHeight()/2-50);
        desktopPane.add(pd);
        desktopPane.setLayer(pd, 1);
        pd.setVisible(true);
    }//GEN-LAST:event_menuChangePasswordActionPerformed

    //Logout
    private void menuLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuLogoutActionPerformed
        MMS.logout();
    }//GEN-LAST:event_menuLogoutActionPerformed

    //New Part
    private void newPartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newPartButtonActionPerformed
        PartDialog p = new PartDialog(partTable, -1);
        p.setSize(MMS.DIAG_WIDTH, p.getHeight());
        p.setLocation(desktopPane.getWidth()/2-p.getWidth()/2, desktopPane.getHeight()/2-p.getHeight()/2-50);
        desktopPane.add(p);
        desktopPane.setLayer(p, 1);
        p.setVisible(true);
    }//GEN-LAST:event_newPartButtonActionPerformed

    //Edit Part
    private void editPartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editPartButtonActionPerformed
        PartDialog p = new PartDialog(partTable, partTable.getSelectedRow());
        p.setSize(MMS.DIAG_WIDTH, p.getHeight());
        p.setTitle("Edit Part");
        p.setLocation(desktopPane.getWidth()/2-p.getWidth()/2, desktopPane.getHeight()/2-p.getHeight()/2-50);
        desktopPane.add(p);
        desktopPane.setLayer(p, 1);
        p.setVisible(true);
    }//GEN-LAST:event_editPartButtonActionPerformed

    //Delete Part
    private void deletePartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletePartButtonActionPerformed
        String message, title;
        if(partTable.getSelectedRowCount() > 1){
            message = "Are you sure you want to delete these "+partTable.getSelectedRowCount()+" parts?";
            title = "Delete Parts";
        }
        else{
            message = "Are you sure you want to delete "+partTable.getValueAt(partTable.getSelectedRow(), 1)+"?";
            title = "Delete Part";
        }
        if(InternalDialog.showInternalConfirmDialog(desktopPane, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null) == 0){
            int rows [] = partTable.getSelectedRows();
            //Delete from DB
            for(int row : rows){
                int id = Integer.parseInt(partTable.getValueAt(row, 0).toString());
                Database.executeQuery("DELETE FROM parts WHERE id = ?",
                        new Object[]{id});
            }
            //Delete from table
            for(int i = rows.length-1; i >= 0; i--){
                DefaultTableModel m = (DefaultTableModel)partTable.getModel();
                m.removeRow(rows[i]);
            }
            
            //Select first row
            if(partTable.getRowCount() != 0) partTable.setRowSelectionInterval(0, 0);
        }
    }//GEN-LAST:event_deletePartButtonActionPerformed

    //Archive Part
    private void archivePartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_archivePartButtonActionPerformed
        String message, title;
        if(partTable.getSelectedRowCount() > 1){
            message = "Are you sure you want to archive these "+employeeTable.getSelectedRowCount()+" parts?";
            title = "Archive Parts";
        }
        else{
            message = "Are you sure you want to archive "+partTable.getValueAt(partTable.getSelectedRow(), 1)+"?";
            title = "Archive Part";
        }
        
        if(InternalDialog.showInternalConfirmDialog(desktopPane, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null) == 0){
            int rows [] = partTable.getSelectedRows();
            //Set Archived = Y
            for(int row : rows){
                int id = Integer.parseInt(partTable.getValueAt(row, 0).toString());
                Database.executeQuery("UPDATE parts SET archived = ? WHERE id = ?",
                        new Object[]{"Y", id});
            }
            //Delete from table
            for(int i = rows.length-1; i >= 0; i--){
                DefaultTableModel m = (DefaultTableModel)partTable.getModel();
                m.removeRow(rows[i]);
            }
            
            //Select first row
            if(partTable.getRowCount() != 0) partTable.setRowSelectionInterval(0, 0);
        }
    }//GEN-LAST:event_archivePartButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        tabbedPane.setSelectedIndex(1);
        find = new FindPanel(workOrderTable, workOrderPanel, "Open");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void locationTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_locationTableKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_DELETE) deleteLocationButtonActionPerformed(null);
    }//GEN-LAST:event_locationTableKeyPressed

    private void workOrderTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_workOrderTableKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_DELETE) deleteWOButtonActionPerformed(null);
    }//GEN-LAST:event_workOrderTableKeyPressed

    //Delete Work Order
    private void deleteWOButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteWOButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_deleteWOButtonActionPerformed

    //Delete Schedule
    private void deleteScheduleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteScheduleButtonActionPerformed
        
    }//GEN-LAST:event_deleteScheduleButtonActionPerformed

    private void scheduleTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scheduleTableKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_DELETE) deleteScheduleButtonActionPerformed(null);
    }//GEN-LAST:event_scheduleTableKeyPressed

    private void assetTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_assetTableKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_DELETE) deleteAssetButtonActionPerformed(null);
    }//GEN-LAST:event_assetTableKeyPressed

    private void partTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_partTableKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_DELETE) deletePartButtonActionPerformed(null);
    }//GEN-LAST:event_partTableKeyPressed

    private void employeeTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_employeeTableKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_DELETE) deleteEmployeeButtonActionPerformed(null);
    }//GEN-LAST:event_employeeTableKeyPressed

    //Table size ALL
    private void menuTableSizeAllStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_menuTableSizeAllStateChanged
        if(menuTableSizeAll.isSelected()){
            String [] names = {"", "table_size_wo", "table_size_schedule", "table_size_assets", "table_size_locations", "table_size_parts", "table_size_employees"};
            MMS.getPrefs().putInt(names[tabbedPane.getSelectedIndex()], -1);
        }
    }//GEN-LAST:event_menuTableSizeAllStateChanged

    //Table size TOP 1000
    private void menuTableSize1000StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_menuTableSize1000StateChanged
        if(menuTableSize1000.isSelected()){
        String [] names = {"", "table_size_wo", "table_size_schedule", "table_size_assets", "table_size_locations", "table_size_parts", "table_size_employees"};
            MMS.getPrefs().putInt(names[tabbedPane.getSelectedIndex()], 1000);
        }
    }//GEN-LAST:event_menuTableSize1000StateChanged

    //Table size TOP 500
    private void menuTableSize500StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_menuTableSize500StateChanged
        if(menuTableSize500.isSelected()){
            String [] names = {"", "table_size_wo", "table_size_schedule", "table_size_assets", "table_size_locations", "table_size_parts", "table_size_employees"};
            MMS.getPrefs().putInt(names[tabbedPane.getSelectedIndex()], 500);
        }
    }//GEN-LAST:event_menuTableSize500StateChanged

    //Table size TOP 250
    private void menuTableSize250StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_menuTableSize250StateChanged
        if(menuTableSize250.isSelected()){
            String [] names = {"", "table_size_wo", "table_size_schedule", "table_size_assets", "table_size_locations", "table_size_parts", "table_size_employees"};
            MMS.getPrefs().putInt(names[tabbedPane.getSelectedIndex()], 250);
        }
    }//GEN-LAST:event_menuTableSize250StateChanged

    //Disconnect database
    private void menuFileDisconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFileDisconnectActionPerformed
        if(InternalDialog.showInternalConfirmDialog(desktopPane, "Disconnecting will close "+MMS.NAME+" and run the setup window again.\nAre you sure you want to disconnect?", "Disconnect Database", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null) == 0){
            MMS.getPrefs().putBoolean("first_run", true);
            Database.shutdown();
            MMS.setup();
        }
    }//GEN-LAST:event_menuFileDisconnectActionPerformed

    //New Schedule
    private void newScheduleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newScheduleButtonActionPerformed
        if(locationTable.getRowCount() == 0){
            PopupPanel.display("You must add a Location before you can add a Scheduled Task.", newScheduleButton.getLocationOnScreen().x+10, newScheduleButton.getLocationOnScreen().y+newScheduleButton.getHeight()+10);
        }
        else{
            ScheduleDialog s = new ScheduleDialog(scheduleTable, -1);
            s.setSize(MMS.DIAG_WIDTH*2-10, s.getHeight());
            s.setLocation(desktopPane.getWidth()/2-s.getWidth()/2, desktopPane.getHeight()/2-s.getHeight()/2-50);
            desktopPane.add(s);
            desktopPane.setLayer(s, 1);
            s.setVisible(true);
        }
    }//GEN-LAST:event_newScheduleButtonActionPerformed

    //Complete task
    private void completeScheduleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_completeScheduleButtonActionPerformed
        int id = Integer.parseInt(scheduleTable.getValueAt(scheduleTable.getSelectedRow(), 0).toString());
        String freq = scheduleTable.getValueAt(scheduleTable.getSelectedRow(), 3).toString();
        //Get last date
        ResultSet rs = Database.select("SELECT schedule_last_date FROM schedule WHERE id = ?",
                new Object[]{id});
        try {
            if(rs.next()){
                LocalDate lastDate = rs.getDate(1).toLocalDate();
                //Update last date (increase by one increment)
                Database.executeQuery("UPDATE schedule SET schedule_last_date = ? WHERE id = ?",
                    new Object[]{DateTools.convertToSQLDate(DateTools.getDueDate(lastDate, freq , 1)), id});
                //Update table
                scheduleTable.setValueAt("Complete", scheduleTable.getSelectedRow(), 5);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_completeScheduleButtonActionPerformed

    //Load locations
    public void loadLocations(int row){
        locationLoadLabel.setVisible(true);
        new Thread(){
            @Override
            public void run(){
                DefaultTableModel t = (DefaultTableModel)locationTable.getModel();
                t.setRowCount(0);
                try {
                    ResultSet rs = Database.select("SELECT id, location_name, location_desc, archived FROM locations ORDER BY id DESC");
                    int size = MMS.getPrefs().getInt("table_size_locations", -1), count = 0;
                    while(rs.next() && (size == -1 || count <= size)){
                        Object [] o = new Object[4];
                        o[0] = rs.getObject(1).toString().trim();
                        o[1] = rs.getObject(2).toString().trim();
                        o[2] = rs.getObject(3).toString().trim();
                        o[3] = rs.getObject(4).toString().trim();
                        if(o[3].equals("N")){
                            t.addRow(o);
                        }
                        count++;
                    }
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                TableTools.resize(locationTable);
                if(t.getRowCount() != 0) locationTable.setRowSelectionInterval(row, row);
                locationLoadLabel.setVisible(false);
            }
        }.start();
    }
    
    //Load assets
    public void loadAssets(int row){
        assetLoadLabel.setVisible(true);
        new Thread(){
            @Override
            public void run(){
                DefaultTableModel t = (DefaultTableModel)assetTable.getModel();
                t.setRowCount(0);
                try {
                    ResultSet rs = Database.select("SELECT t0.id, t0.asset_name, t0.asset_desc, t0.asset_type, t0.location_id, t1.location_name, t0.Archived FROM assets t0 JOIN locations t1 ON t0.location_id = t1.id ORDER BY t0.id DESC");
                    int size = MMS.getPrefs().getInt("table_size_assets", -1), count = 0;
                    while(rs.next() && (size == -1 || count <= size)){
                        Object [] o = new Object[6];
                        o[0] = rs.getObject(1).toString().trim();
                        o[1] = rs.getObject(2).toString().trim();
                        o[2] = rs.getObject(3).toString().trim();
                        o[3] = rs.getObject(4).toString().trim();
                        o[4] = rs.getObject(5).toString().trim()+" - "+rs.getObject(6).toString().trim();
                        o[5] = rs.getObject(7).toString().trim();
                        if(o[5].equals("N")){
                            t.addRow(o);
                        }
                        count++;
                    }
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                TableTools.resize(assetTable);
                if(t.getRowCount() != 0) assetTable.setRowSelectionInterval(row, row);
                assetLoadLabel.setVisible(false);
            }
        }.start();
    }
    
    //Load parts
    public void loadParts(int row){
        partLoadLabel.setVisible(true);
        new Thread(){
            @Override
            public void run(){
                DefaultTableModel t = (DefaultTableModel)partTable.getModel();
                t.setRowCount(0);
                try {
                    ResultSet rs = Database.select("SELECT id, part_name, part_qty, part_cost, archived FROM parts ORDER BY id DESC");
                    int size = MMS.getPrefs().getInt("table_size_parts", -1), count = 0;
                    while(rs.next() && (size == -1 || count <= size)){
                        Object [] o = new Object[5];
                        o[0] = rs.getObject(1).toString().trim();
                        o[1] = rs.getObject(2).toString().trim();
                        o[2] = rs.getObject(3).toString().trim();
                        o[3] = rs.getObject(4).toString().trim();
                        o[4] = rs.getObject(5).toString().trim();
                        if(o[4].equals("N")){
                            t.addRow(o);
                        }
                        count++;
                    }
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                TableTools.resize(partTable);
                if(t.getRowCount() != 0) partTable.setRowSelectionInterval(row, row);
                partLoadLabel.setVisible(false);
            }
        }.start();
    }
    
    //Load employees
    public void loadEmployees(int row){
        employeeLoadLabel.setVisible(true);
        new Thread(){
            @Override
            public void run(){
                DefaultTableModel t = (DefaultTableModel)employeeTable.getModel();
                t.setRowCount(0);
                try {
                    ResultSet rs = Database.select("SELECT id, employee_name, employee_desc, employee_dept, archived FROM employees ORDER BY id DESC");
                    int size = MMS.getPrefs().getInt("table_size_employees", -1), count = 0;
                    while(rs.next() && (size == -1 || count <= size)){
                        Object [] o = new Object[5];
                        o[0] = rs.getObject(1).toString().trim();
                        o[1] = rs.getObject(2).toString().trim();
                        o[2] = rs.getObject(3).toString().trim();
                        o[3] = rs.getObject(4).toString().trim();
                        o[4] = rs.getObject(5).toString().trim();
                        if(o[4].equals("N")){
                            t.addRow(o);
                        }
                        count++;
                    }
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                TableTools.resize(employeeTable);
                if(t.getRowCount() != 0) employeeTable.setRowSelectionInterval(row, row);
                employeeLoadLabel.setVisible(false);
            }
        }.start();
    }
    
    //Load schedule
    public void loadSchedule(int row){
        scheduleLoadLabel.setVisible(true);
        new Thread(){
            @Override
            public void run(){
                DefaultTableModel t = (DefaultTableModel)scheduleTable.getModel();
                t.setRowCount(0);
                try {
                    ResultSet rs = Database.select("SELECT t0.id, t0.schedule_name, t0.schedule_type, t0.schedule_freq, t0.schedule_last_date, t0.asset_id, t1.asset_name, t0.location_id, t2.location_name, t0.Archived"
                            + " FROM schedule t0 JOIN assets t1 ON t0.asset_id = t1.id JOIN locations t2 ON t0.location_id = t2.id ORDER BY t0.id DESC");
                    int size = MMS.getPrefs().getInt("table_size_schedule", -1), count = 0;
                    while(rs.next() && (size == -1 || count <= size)){
                        Object [] o = new Object[9];
                        o[0] = rs.getObject(1).toString().trim();//id
                        o[1] = rs.getObject(2).toString().trim();//name
                        o[2] = rs.getObject(3).toString().trim();//type
                        o[3] = rs.getObject(4).toString().trim();//freq
                        o[4] = rs.getObject(5).toString().trim();//last
                        
                        //Calculate due date
                        Object row;
                        LocalDate dueDate = DateTools.getDueDate(rs.getDate(5).toLocalDate(), o[3].toString(), 1);
                        if(DateTools.isToday(dueDate)) row = "<html><b>Today</b></html>";
                        else if(DateTools.isPassed(dueDate)) row = "<html><p style=\"color:red\"><b>Overdue</b></p><html>";
                        else row = dueDate;
                        o[5] = row;
                        
                        o[6] = rs.getObject(6).toString().trim().equals("-1") ? "No Asset" : rs.getObject(6).toString().trim()+" - "+rs.getObject(7).toString().trim();//asset_id-name
                        o[7] = rs.getObject(8).toString().trim()+" - "+rs.getObject(9).toString().trim();//location_id-name
                        o[8] = rs.getObject(10).toString().trim();//archived
                        if(o[8].equals("N")){
                            t.addRow(o);
                        }
                        count++;
                    }
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                TableTools.resize(scheduleTable);
                if(t.getRowCount() != 0) scheduleTable.setRowSelectionInterval(row, row);
                scheduleLoadLabel.setVisible(false);
            }
        }.start();
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel adminArchivePanel;
    private javax.swing.JPanel adminCustomPanel;
    private javax.swing.JPanel adminPanel;
    private javax.swing.JTabbedPane adminTabbedPane;
    private javax.swing.JPanel adminUpdatePanel;
    private javax.swing.JPanel adminUserPanel;
    private javax.swing.JScrollPane adminUserScroll;
    private javax.swing.JTable adminUserTable;
    private javax.swing.JToolBar adminUserTools;
    private javax.swing.JButton archiveAssetButton;
    private javax.swing.JButton archiveEmployeeButton;
    private javax.swing.JButton archiveLocationButton;
    private javax.swing.JButton archivePartButton;
    private javax.swing.JButton archiveScheduleButton;
    private javax.swing.JButton archiveWOButton;
    private javax.swing.JLabel assetLoadLabel;
    private javax.swing.JPanel assetPanel;
    private javax.swing.JScrollPane assetScroll;
    private javax.swing.JTable assetTable;
    private javax.swing.JToolBar assetTools;
    private javax.swing.JPanel backPanel;
    private javax.swing.JButton completeScheduleButton;
    private javax.swing.JButton createWOButton1;
    private javax.swing.JPanel dashButtonPanel;
    private javax.swing.JPanel dashPanel;
    private javax.swing.JButton deleteAssetButton;
    private javax.swing.JButton deleteEmployeeButton;
    private javax.swing.JButton deleteLocationButton;
    private javax.swing.JButton deletePartButton;
    private javax.swing.JButton deleteScheduleButton;
    private javax.swing.JButton deleteUser;
    private javax.swing.JButton deleteWOButton;
    private javax.swing.JDesktopPane desktopPane;
    private javax.swing.JButton editAssetButton;
    private javax.swing.JButton editEmployeeButton;
    private javax.swing.JButton editLocationButton;
    private javax.swing.JButton editPartButton;
    private javax.swing.JButton editScheduleButton;
    private javax.swing.JButton editUser;
    private javax.swing.JButton editWOButton;
    private javax.swing.JLabel employeeLoadLabel;
    private javax.swing.JPanel employeePanel;
    private javax.swing.JScrollPane employeeScroll;
    private javax.swing.JTable employeeTable;
    private javax.swing.JToolBar employeeTools;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel locationLoadLabel;
    private javax.swing.JPanel locationPanel;
    private javax.swing.JScrollPane locationScroll;
    private javax.swing.JTable locationTable;
    private javax.swing.JToolBar locationTools;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem menuChangePassword;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenuItem menuFileDisconnect;
    private javax.swing.JMenu menuHelp;
    private javax.swing.JMenuItem menuLogout;
    private javax.swing.JMenu menuTable;
    private javax.swing.JMenuItem menuTableFilter;
    private javax.swing.JMenuItem menuTableRefresh;
    private javax.swing.JMenu menuTableSize;
    private javax.swing.JRadioButtonMenuItem menuTableSize1000;
    private javax.swing.JRadioButtonMenuItem menuTableSize250;
    private javax.swing.JRadioButtonMenuItem menuTableSize500;
    private javax.swing.JRadioButtonMenuItem menuTableSizeAll;
    private javax.swing.JMenu menuUser;
    private javax.swing.JButton newAssetButton;
    private javax.swing.JButton newEmployeeButton;
    private javax.swing.JButton newLocationButton;
    private javax.swing.JButton newPartButton;
    private javax.swing.JButton newScheduleButton;
    private javax.swing.JButton newUser;
    private javax.swing.JButton newWOButton;
    private javax.swing.JLabel partLoadLabel;
    private javax.swing.JPanel partPanel;
    private javax.swing.JScrollPane partScroll;
    private javax.swing.JTable partTable;
    private javax.swing.JToolBar partTools;
    private javax.swing.JButton printWOButton;
    private javax.swing.JPanel reportPanel;
    private javax.swing.JButton resetUserPass;
    private javax.swing.JLabel scheduleLoadLabel;
    private javax.swing.JPanel schedulePanel;
    private javax.swing.JScrollPane scheduleScroll;
    private javax.swing.JTable scheduleTable;
    private javax.swing.JToolBar scheduleTools;
    private javax.swing.JButton statusWOButton;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.ButtonGroup tableSizeGroup;
    private javax.swing.JButton viewScheduleButton;
    private javax.swing.JButton viewWOButton;
    private javax.swing.JLabel workLoadLabel;
    private javax.swing.JPanel workOrderPanel;
    private javax.swing.JScrollPane workOrderScroll;
    private javax.swing.JTable workOrderTable;
    private javax.swing.JToolBar workOrderTools;
    // End of variables declaration//GEN-END:variables
}
