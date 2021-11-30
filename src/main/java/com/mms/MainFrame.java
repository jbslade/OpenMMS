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
import com.mms.iframes.AboutFrame;
import com.mms.panels.PopupPanel;
import com.mms.iframes.PasswordFrame;
import com.mms.modules.Admin;
import com.mms.modules.Assets;
import com.mms.modules.Employees;
import com.mms.modules.Locations;
import com.mms.modules.Parts;
import com.mms.modules.Reports;
import com.mms.modules.Schedule;
import com.mms.modules.WorkOrders;
import com.mms.utilities.OtherTools;
import com.mms.utilities.Printer;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author J.B. Slade
 */
public class MainFrame extends javax.swing.JFrame {
    
    private FindPanel find;
    private final WorkOrders workOrders;
    private final Schedule schedule;
    private final Locations locations;
    private final Assets assets;
    private final Parts parts;
    private final Employees employees;
    private final Admin admin;
    private final Reports reports;
    private final boolean isAdmin;
    
    public JDesktopPane getDesktopPane(){return desktopPane;}
    
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
        tabbedPane.setIconAt(0, MMS.workOrdersIcon);
        tabbedPane.setIconAt(1, MMS.scheduleIcon);
        tabbedPane.setIconAt(2, MMS.assetsIcon);
        tabbedPane.setIconAt(3, MMS.locationIcon);
        tabbedPane.setIconAt(4, MMS.partsIcon);
        tabbedPane.setIconAt(5, MMS.employeesIcon);
        tabbedPane.setIconAt(6, MMS.reportsIcon);
        tabbedPane.setIconAt(7, MMS.systemIcon);
        
        //User levels
        switch(MMS.getUserLevel()){
            case 0: //Worker
                isAdmin = false;
                break;
            case 1: //Supervisor
                isAdmin = false;
                break;
            case 2: //Manager
                isAdmin = false;
                break;
            case 3: //Administrator
                isAdmin = true;
                break;
            default:
                isAdmin = false;
                break;
        }
        if(!isAdmin){
            tabbedPane.remove(7);
            newReportButton.setVisible(false);
            editReportButton.setVisible(false);
            deleteReportButton.setVisible(false);
        }
        
        //Modules
        workOrders = new WorkOrders(workOrderTable, workLoadLabel);
        schedule = new Schedule(scheduleTable, scheduleLoadLabel);
        locations = new Locations(locationTable, locationLoadLabel);
        assets = new Assets(assetTable, assetLoadLabel);
        parts = new Parts(partTable, partLoadLabel);
        employees = new Employees(employeeTable, employeeLoadLabel);
        reports = new Reports(reportTable, reportCombo);
        if(isAdmin) admin = new Admin(adminUserTable, SchCusList, AssetCusList, EmpCusList, adminGenNameFld);
        else admin = null;
        
        //Table selection listeners
        workOrderTable.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            switch (workOrderTable.getSelectedRowCount()) {
                case 0:
                    editWOButton.setEnabled(false);
                    deleteWOButton.setEnabled(false);
                    archiveWOButton.setEnabled(false);
                    statusWOButton.setEnabled(false);
                    viewWOButton.setEnabled(false);
                    printWOButton.setEnabled(false);
                    break;
                case 1:
                    editWOButton.setEnabled(!workOrderTable.getValueAt(workOrderTable.getSelectedRow(), 7).equals("Closed"));
                    deleteWOButton.setEnabled(true);
                    archiveWOButton.setEnabled(true);
                    viewWOButton.setEnabled(true);
                    printWOButton.setEnabled(true);
                    statusWOButton.setEnabled(!workOrderTable.getValueAt(workOrderTable.getSelectedRow(), 7).equals("Closed"));
                    break;
                default:
                    editWOButton.setEnabled(false);
                    deleteWOButton.setEnabled(true);
                    archiveWOButton.setEnabled(true);
                    statusWOButton.setEnabled(false);
                    viewWOButton.setEnabled(false);
                    printWOButton.setEnabled(true);
                    break;
            }
        });
        scheduleTable.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            switch (scheduleTable.getSelectedRowCount()) {
                case 0:
                    editScheduleButton.setEnabled(false);
                    deleteScheduleButton.setEnabled(false);
                    archiveScheduleButton.setEnabled(false);
                    completeScheduleButton.setEnabled(false);
                    viewScheduleButton.setEnabled(false);
                    createWOButton.setEnabled(false);
                    break;
                case 1:
                    editScheduleButton.setEnabled(true);
                    deleteScheduleButton.setEnabled(true);
                    archiveScheduleButton.setEnabled(true);
                    viewScheduleButton.setEnabled(true);
                    createWOButton.setEnabled(true);
                    String s = OtherTools.escapeHTML(scheduleTable.getValueAt(scheduleTable.getSelectedRow(), 5).toString());
                    completeScheduleButton.setEnabled(s.equals("Today") || s.equals("Overdue"));
                    break;
                default:
                    editScheduleButton.setEnabled(false);
                    deleteScheduleButton.setEnabled(true);
                    archiveScheduleButton.setEnabled(true);
                    completeScheduleButton.setEnabled(false);
                    viewScheduleButton.setEnabled(false);
                    createWOButton.setEnabled(false);
                    break;
            }
        });
        assetTable.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            switch (assetTable.getSelectedRowCount()) {
                case 0:
                    editAssetButton.setEnabled(false);
                    deleteAssetButton.setEnabled(false);
                    archiveAssetButton.setEnabled(false);
                    break;
                case 1:
                    editAssetButton.setEnabled(true);
                    deleteAssetButton.setEnabled(true);
                    archiveAssetButton.setEnabled(true);
                    break;
                default:
                    editAssetButton.setEnabled(false);
                    deleteAssetButton.setEnabled(true);
                    archiveAssetButton.setEnabled(true);
                    break;
            }
        });
        locationTable.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            switch (locationTable.getSelectedRowCount()) {
                case 0:
                    editLocationButton.setEnabled(false);
                    deleteLocationButton.setEnabled(false);
                    archiveLocationButton.setEnabled(false);
                    break;
                case 1:
                    editLocationButton.setEnabled(true);
                    deleteLocationButton.setEnabled(true);
                    archiveLocationButton.setEnabled(true);
                    break;
                default:
                    editLocationButton.setEnabled(false);
                    deleteLocationButton.setEnabled(true);
                    archiveLocationButton.setEnabled(true);
                    break;
            }
        });
        partTable.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            switch (partTable.getSelectedRowCount()) {
                case 0:
                    editPartButton.setEnabled(false);
                    deletePartButton.setEnabled(false);
                    archivePartButton.setEnabled(false);
                    break;
                case 1:
                    editPartButton.setEnabled(true);
                    deletePartButton.setEnabled(true);
                    archivePartButton.setEnabled(true);
                    break;
                default:
                    editPartButton.setEnabled(false);
                    deletePartButton.setEnabled(true);
                    archivePartButton.setEnabled(true);
                    break;
            }
        });
        employeeTable.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            switch (employeeTable.getSelectedRowCount()) {
                case 0:
                    editEmployeeButton.setEnabled(false);
                    deleteEmployeeButton.setEnabled(false);
                    archiveEmployeeButton.setEnabled(false);
                    break;
                case 1:
                    editEmployeeButton.setEnabled(true);
                    deleteEmployeeButton.setEnabled(true);
                    archiveEmployeeButton.setEnabled(true);
                    break;
                default:
                    editEmployeeButton.setEnabled(false);
                    deleteEmployeeButton.setEnabled(true);
                    archiveEmployeeButton.setEnabled(true);
                    break;
            }
        });

        //WO Status popup
        statusPopup.show(null, 0, 0);
        statusPopup.setVisible(false);
        
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
        statusPopup = new javax.swing.JPopupMenu();
        statusOpen = new javax.swing.JRadioButtonMenuItem();
        statusOnHold = new javax.swing.JRadioButtonMenuItem();
        statusClosed = new javax.swing.JRadioButtonMenuItem();
        statusGroup = new javax.swing.ButtonGroup();
        backPanel = new javax.swing.JPanel();
        desktopPane = new javax.swing.JDesktopPane();
        tabbedPane = new javax.swing.JTabbedPane();
        workOrderPanel = new javax.swing.JPanel();
        workOrderTools = new javax.swing.JToolBar();
        newWOButton = new javax.swing.JButton();
        editWOButton = new javax.swing.JButton();
        deleteWOButton = new javax.swing.JButton();
        archiveWOButton = new javax.swing.JButton();
        viewWOButton = new javax.swing.JButton();
        statusWOButton = new javax.swing.JButton();
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
        viewScheduleButton = new javax.swing.JButton();
        createWOButton = new javax.swing.JButton();
        completeScheduleButton = new javax.swing.JButton();
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
        reportScroll = new javax.swing.JScrollPane();
        reportTable = new javax.swing.JTable();
        reportTools = new javax.swing.JToolBar();
        newReportButton = new javax.swing.JButton();
        editReportButton = new javax.swing.JButton();
        deleteReportButton = new javax.swing.JButton();
        runReportButton = new javax.swing.JButton();
        reportCombo = new javax.swing.JComboBox<>();
        adminPanel = new javax.swing.JPanel();
        adminUserPanel = new javax.swing.JPanel();
        adminUserScroll = new javax.swing.JScrollPane();
        adminUserTable = new javax.swing.JTable();
        adminUserAdd = new javax.swing.JButton();
        adminUserDelete = new javax.swing.JButton();
        adminUserReset = new javax.swing.JButton();
        adminGenPanel = new javax.swing.JPanel();
        adminGenNameLbl = new javax.swing.JLabel();
        adminGenNameFld = new javax.swing.JTextField();
        adminGenSave = new javax.swing.JButton();
        adminCusPanel = new javax.swing.JPanel();
        adminCusTab = new javax.swing.JTabbedPane();
        adminTypePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        SchCusList = new javax.swing.JList<>();
        adminAddCusSchedule = new javax.swing.JButton();
        adminDelCusSchedule = new javax.swing.JButton();
        adminAssetCusPanel = new javax.swing.JPanel();
        adminAddCusAsset = new javax.swing.JButton();
        adminDelCusAsset = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        AssetCusList = new javax.swing.JList<>();
        adminDeptCusPanel = new javax.swing.JPanel();
        adminAddCusEmployee = new javax.swing.JButton();
        adminDelCusEmployee = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        EmpCusList = new javax.swing.JList<>();
        adminUpdatePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuFileArchive = new javax.swing.JMenuItem();
        separator1 = new javax.swing.JPopupMenu.Separator();
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
        menuHelpBug = new javax.swing.JMenuItem();
        menuUser = new javax.swing.JMenu();
        menuChangePassword = new javax.swing.JMenuItem();
        menuLogout = new javax.swing.JMenuItem();

        statusGroup.add(statusOpen);
        statusOpen.setText("Open");
        statusOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusOpenActionPerformed(evt);
            }
        });
        statusPopup.add(statusOpen);

        statusGroup.add(statusOnHold);
        statusOnHold.setText("On Hold");
        statusOnHold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusOnHoldActionPerformed(evt);
            }
        });
        statusPopup.add(statusOnHold);

        statusGroup.add(statusClosed);
        statusClosed.setText("Closed");
        statusClosed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusClosedActionPerformed(evt);
            }
        });
        statusPopup.add(statusClosed);

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

        workOrderTools.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        workOrderTools.setFloatable(false);
        workOrderTools.setRollover(true);

        newWOButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/new.png"))); // NOI18N
        newWOButton.setText("New");
        newWOButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newWOButtonActionPerformed(evt);
            }
        });
        workOrderTools.add(newWOButton);

        editWOButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/edit.png"))); // NOI18N
        editWOButton.setText("Edit");
        editWOButton.setEnabled(false);
        editWOButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editWOButtonActionPerformed(evt);
            }
        });
        workOrderTools.add(editWOButton);

        deleteWOButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/delete.png"))); // NOI18N
        deleteWOButton.setText("Delete");
        deleteWOButton.setEnabled(false);
        deleteWOButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteWOButtonActionPerformed(evt);
            }
        });
        workOrderTools.add(deleteWOButton);

        archiveWOButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/archive.png"))); // NOI18N
        archiveWOButton.setText("Archive");
        archiveWOButton.setEnabled(false);
        archiveWOButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                archiveWOButtonActionPerformed(evt);
            }
        });
        workOrderTools.add(archiveWOButton);

        viewWOButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/view.png"))); // NOI18N
        viewWOButton.setText("View");
        viewWOButton.setEnabled(false);
        viewWOButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewWOButtonActionPerformed(evt);
            }
        });
        workOrderTools.add(viewWOButton);

        statusWOButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/status.png"))); // NOI18N
        statusWOButton.setText("Status");
        statusWOButton.setEnabled(false);
        statusWOButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                statusWOButtonMousePressed(evt);
            }
        });
        workOrderTools.add(statusWOButton);

        printWOButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/print.png"))); // NOI18N
        printWOButton.setText("Print");
        printWOButton.setEnabled(false);
        printWOButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printWOButtonActionPerformed(evt);
            }
        });
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
                " #", " Date", " Type", " Priority", " Asset", " Location", " Employee(s)", " Status"
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
        workOrderTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                workOrderTableMouseClicked(evt);
            }
        });
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
            .addComponent(workOrderScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 1133, Short.MAX_VALUE)
            .addComponent(workOrderTools, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        workOrderPanelLayout.setVerticalGroup(
            workOrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(workOrderPanelLayout.createSequentialGroup()
                .addComponent(workOrderTools, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(workOrderScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE))
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
        editScheduleButton.setEnabled(false);
        editScheduleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editScheduleButtonActionPerformed(evt);
            }
        });
        scheduleTools.add(editScheduleButton);

        deleteScheduleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/delete.png"))); // NOI18N
        deleteScheduleButton.setText("Delete");
        deleteScheduleButton.setEnabled(false);
        deleteScheduleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteScheduleButtonActionPerformed(evt);
            }
        });
        scheduleTools.add(deleteScheduleButton);

        archiveScheduleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/archive.png"))); // NOI18N
        archiveScheduleButton.setText("Archive");
        archiveScheduleButton.setEnabled(false);
        archiveScheduleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                archiveScheduleButtonActionPerformed(evt);
            }
        });
        scheduleTools.add(archiveScheduleButton);

        viewScheduleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/view.png"))); // NOI18N
        viewScheduleButton.setText("View");
        viewScheduleButton.setEnabled(false);
        viewScheduleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewScheduleButtonActionPerformed(evt);
            }
        });
        scheduleTools.add(viewScheduleButton);

        createWOButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dialogs/workOrders.png"))); // NOI18N
        createWOButton.setText("Create WO");
        createWOButton.setEnabled(false);
        createWOButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createWOButtonActionPerformed(evt);
            }
        });
        scheduleTools.add(createWOButton);

        completeScheduleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/complete.png"))); // NOI18N
        completeScheduleButton.setText("Complete");
        completeScheduleButton.setEnabled(false);
        completeScheduleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                completeScheduleButtonActionPerformed(evt);
            }
        });
        scheduleTools.add(completeScheduleButton);
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
                " #", " Name", " Type", " Frequency", " Last Done", " Next Due", " Asset", " Location"
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
        scheduleTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scheduleTableMouseClicked(evt);
            }
        });
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
            .addComponent(scheduleTools, javax.swing.GroupLayout.DEFAULT_SIZE, 1133, Short.MAX_VALUE)
            .addComponent(scheduleScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 1133, Short.MAX_VALUE)
        );
        schedulePanelLayout.setVerticalGroup(
            schedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(schedulePanelLayout.createSequentialGroup()
                .addComponent(scheduleTools, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(scheduleScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE))
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
        editAssetButton.setEnabled(false);
        editAssetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editAssetButtonActionPerformed(evt);
            }
        });
        assetTools.add(editAssetButton);

        deleteAssetButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/delete.png"))); // NOI18N
        deleteAssetButton.setText("Delete");
        deleteAssetButton.setEnabled(false);
        deleteAssetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteAssetButtonActionPerformed(evt);
            }
        });
        assetTools.add(deleteAssetButton);

        archiveAssetButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/archive.png"))); // NOI18N
        archiveAssetButton.setText("Archive");
        archiveAssetButton.setEnabled(false);
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
                " #", " Name", " Description", " Type", " Location"
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
            .addComponent(assetTools, javax.swing.GroupLayout.DEFAULT_SIZE, 1133, Short.MAX_VALUE)
            .addComponent(assetScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 1133, Short.MAX_VALUE)
        );
        assetPanelLayout.setVerticalGroup(
            assetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(assetPanelLayout.createSequentialGroup()
                .addComponent(assetTools, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(assetScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE))
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
        editLocationButton.setEnabled(false);
        editLocationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editLocationButtonActionPerformed(evt);
            }
        });
        locationTools.add(editLocationButton);

        deleteLocationButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/delete.png"))); // NOI18N
        deleteLocationButton.setText("Delete");
        deleteLocationButton.setEnabled(false);
        deleteLocationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteLocationButtonActionPerformed(evt);
            }
        });
        locationTools.add(deleteLocationButton);

        archiveLocationButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/archive.png"))); // NOI18N
        archiveLocationButton.setText("Archive");
        archiveLocationButton.setEnabled(false);
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
                " #", " Name", " Description"
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
            .addComponent(locationTools, javax.swing.GroupLayout.DEFAULT_SIZE, 1133, Short.MAX_VALUE)
            .addComponent(locationScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 1133, Short.MAX_VALUE)
        );
        locationPanelLayout.setVerticalGroup(
            locationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(locationPanelLayout.createSequentialGroup()
                .addComponent(locationTools, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(locationScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE))
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
        editPartButton.setEnabled(false);
        editPartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editPartButtonActionPerformed(evt);
            }
        });
        partTools.add(editPartButton);

        deletePartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/delete.png"))); // NOI18N
        deletePartButton.setText("Delete");
        deletePartButton.setEnabled(false);
        deletePartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletePartButtonActionPerformed(evt);
            }
        });
        partTools.add(deletePartButton);

        archivePartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/archive.png"))); // NOI18N
        archivePartButton.setText("Archive");
        archivePartButton.setEnabled(false);
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
                " #", " Name", " Quantity", " Price/Unit"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

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
            .addComponent(partTools, javax.swing.GroupLayout.DEFAULT_SIZE, 1133, Short.MAX_VALUE)
            .addComponent(partScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 1133, Short.MAX_VALUE)
        );
        partPanelLayout.setVerticalGroup(
            partPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(partPanelLayout.createSequentialGroup()
                .addComponent(partTools, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(partScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE))
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
        editEmployeeButton.setEnabled(false);
        editEmployeeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editEmployeeButtonActionPerformed(evt);
            }
        });
        employeeTools.add(editEmployeeButton);

        deleteEmployeeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/delete.png"))); // NOI18N
        deleteEmployeeButton.setText("Delete");
        deleteEmployeeButton.setEnabled(false);
        deleteEmployeeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteEmployeeButtonActionPerformed(evt);
            }
        });
        employeeTools.add(deleteEmployeeButton);

        archiveEmployeeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/archive.png"))); // NOI18N
        archiveEmployeeButton.setText("Archive");
        archiveEmployeeButton.setEnabled(false);
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
                " #", " Name", " Designation", " Department"
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
            .addComponent(employeeTools, javax.swing.GroupLayout.DEFAULT_SIZE, 1133, Short.MAX_VALUE)
            .addComponent(employeeScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 1133, Short.MAX_VALUE)
        );
        employeePanelLayout.setVerticalGroup(
            employeePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeePanelLayout.createSequentialGroup()
                .addComponent(employeeTools, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(employeeScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Employees", employeePanel);

        reportScroll.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(204, 204, 204)));

        reportTable.setAutoCreateRowSorter(true);
        reportTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2"
            }
        ));
        reportTable.setDragEnabled(true);
        reportTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                reportTableKeyPressed(evt);
            }
        });
        reportScroll.setViewportView(reportTable);

        reportTools.setFloatable(false);
        reportTools.setRollover(true);

        newReportButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/new.png"))); // NOI18N
        newReportButton.setText("New");
        newReportButton.setToolTipText("");
        newReportButton.setFocusable(false);
        newReportButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        newReportButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        newReportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newReportButtonActionPerformed(evt);
            }
        });
        reportTools.add(newReportButton);

        editReportButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/edit.png"))); // NOI18N
        editReportButton.setText("Edit");
        editReportButton.setToolTipText("");
        editReportButton.setFocusable(false);
        editReportButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        editReportButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        editReportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editReportButtonActionPerformed(evt);
            }
        });
        reportTools.add(editReportButton);

        deleteReportButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/delete.png"))); // NOI18N
        deleteReportButton.setText("Delete");
        deleteReportButton.setToolTipText("");
        deleteReportButton.setFocusable(false);
        deleteReportButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        deleteReportButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        deleteReportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteReportButtonActionPerformed(evt);
            }
        });
        reportTools.add(deleteReportButton);

        runReportButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/run.png"))); // NOI18N
        runReportButton.setText("Run");
        runReportButton.setToolTipText("");
        runReportButton.setFocusable(false);
        runReportButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        runReportButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        runReportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runReportButtonActionPerformed(evt);
            }
        });
        reportTools.add(runReportButton);

        reportCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        reportTools.add(reportCombo);

        javax.swing.GroupLayout reportPanelLayout = new javax.swing.GroupLayout(reportPanel);
        reportPanel.setLayout(reportPanelLayout);
        reportPanelLayout.setHorizontalGroup(
            reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(reportScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 1133, Short.MAX_VALUE)
            .addComponent(reportTools, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        reportPanelLayout.setVerticalGroup(
            reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, reportPanelLayout.createSequentialGroup()
                .addComponent(reportTools, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(reportScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Reports", reportPanel);

        adminPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        adminPanel.setLayout(new java.awt.GridLayout(2, 3, 10, 10));

        adminUserPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Users"));
        adminUserPanel.setToolTipText("");

        adminUserScroll.setBorder(null);

        adminUserTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                " Name", " Access Level"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        adminUserScroll.setViewportView(adminUserTable);

        adminUserAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/new.png"))); // NOI18N
        adminUserAdd.setText("Add");
        adminUserAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminUserAddActionPerformed(evt);
            }
        });

        adminUserDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/delete.png"))); // NOI18N
        adminUserDelete.setText("Delete");
        adminUserDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminUserDeleteActionPerformed(evt);
            }
        });

        adminUserReset.setText("Reset Password");
        adminUserReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminUserResetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout adminUserPanelLayout = new javax.swing.GroupLayout(adminUserPanel);
        adminUserPanel.setLayout(adminUserPanelLayout);
        adminUserPanelLayout.setHorizontalGroup(
            adminUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adminUserPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(adminUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(adminUserScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE)
                    .addGroup(adminUserPanelLayout.createSequentialGroup()
                        .addComponent(adminUserAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(adminUserDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(adminUserReset)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        adminUserPanelLayout.setVerticalGroup(
            adminUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adminUserPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(adminUserScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(adminUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(adminUserAdd)
                    .addComponent(adminUserDelete)
                    .addComponent(adminUserReset))
                .addContainerGap())
        );

        adminPanel.add(adminUserPanel);

        adminGenPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("General"));

        adminGenNameLbl.setText("Company Name:");

        adminGenNameFld.setText("OpenMMS");

        adminGenSave.setText("Save");
        adminGenSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminGenSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout adminGenPanelLayout = new javax.swing.GroupLayout(adminGenPanel);
        adminGenPanel.setLayout(adminGenPanelLayout);
        adminGenPanelLayout.setHorizontalGroup(
            adminGenPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, adminGenPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(adminGenPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(adminGenPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(adminGenSave))
                    .addGroup(adminGenPanelLayout.createSequentialGroup()
                        .addComponent(adminGenNameLbl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(adminGenNameFld, javax.swing.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE)))
                .addContainerGap())
        );
        adminGenPanelLayout.setVerticalGroup(
            adminGenPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adminGenPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(adminGenPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(adminGenNameLbl)
                    .addComponent(adminGenNameFld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 251, Short.MAX_VALUE)
                .addComponent(adminGenSave)
                .addContainerGap())
        );

        adminPanel.add(adminGenPanel);

        adminCusPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Custom Fields"));

        SchCusList.setModel(new DefaultListModel());
        jScrollPane1.setViewportView(SchCusList);

        adminAddCusSchedule.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/new.png"))); // NOI18N
        adminAddCusSchedule.setText("Add");
        adminAddCusSchedule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminAddCusScheduleActionPerformed(evt);
            }
        });

        adminDelCusSchedule.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/delete.png"))); // NOI18N
        adminDelCusSchedule.setText("Delete");
        adminDelCusSchedule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminDelCusScheduleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout adminTypePanelLayout = new javax.swing.GroupLayout(adminTypePanel);
        adminTypePanel.setLayout(adminTypePanelLayout);
        adminTypePanelLayout.setHorizontalGroup(
            adminTypePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adminTypePanelLayout.createSequentialGroup()
                .addComponent(adminAddCusSchedule)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(adminDelCusSchedule)
                .addContainerGap(339, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        adminTypePanelLayout.setVerticalGroup(
            adminTypePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adminTypePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(adminTypePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(adminAddCusSchedule)
                    .addComponent(adminDelCusSchedule)))
        );

        adminCusTab.addTab("Maintenance Types", adminTypePanel);

        adminAddCusAsset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/new.png"))); // NOI18N
        adminAddCusAsset.setText("Add");
        adminAddCusAsset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminAddCusAssetActionPerformed(evt);
            }
        });

        adminDelCusAsset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/delete.png"))); // NOI18N
        adminDelCusAsset.setText("Delete");
        adminDelCusAsset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminDelCusAssetActionPerformed(evt);
            }
        });

        AssetCusList.setModel(new DefaultListModel());
        jScrollPane3.setViewportView(AssetCusList);

        javax.swing.GroupLayout adminAssetCusPanelLayout = new javax.swing.GroupLayout(adminAssetCusPanel);
        adminAssetCusPanel.setLayout(adminAssetCusPanelLayout);
        adminAssetCusPanelLayout.setHorizontalGroup(
            adminAssetCusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adminAssetCusPanelLayout.createSequentialGroup()
                .addComponent(adminAddCusAsset)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(adminDelCusAsset)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
        );
        adminAssetCusPanelLayout.setVerticalGroup(
            adminAssetCusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, adminAssetCusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(adminAssetCusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(adminAddCusAsset)
                    .addComponent(adminDelCusAsset)))
        );

        adminCusTab.addTab("Asset Types", adminAssetCusPanel);

        adminAddCusEmployee.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/new.png"))); // NOI18N
        adminAddCusEmployee.setText("Add");
        adminAddCusEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminAddCusEmployeeActionPerformed(evt);
            }
        });

        adminDelCusEmployee.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/delete.png"))); // NOI18N
        adminDelCusEmployee.setText("Delete");
        adminDelCusEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminDelCusEmployeeActionPerformed(evt);
            }
        });

        EmpCusList.setModel(new DefaultListModel());
        jScrollPane4.setViewportView(EmpCusList);

        javax.swing.GroupLayout adminDeptCusPanelLayout = new javax.swing.GroupLayout(adminDeptCusPanel);
        adminDeptCusPanel.setLayout(adminDeptCusPanelLayout);
        adminDeptCusPanelLayout.setHorizontalGroup(
            adminDeptCusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adminDeptCusPanelLayout.createSequentialGroup()
                .addComponent(adminAddCusEmployee)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(adminDelCusEmployee)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
        );
        adminDeptCusPanelLayout.setVerticalGroup(
            adminDeptCusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, adminDeptCusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(adminDeptCusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(adminAddCusEmployee)
                    .addComponent(adminDelCusEmployee)))
        );

        adminCusTab.addTab("Employee Departments", adminDeptCusPanel);

        javax.swing.GroupLayout adminCusPanelLayout = new javax.swing.GroupLayout(adminCusPanel);
        adminCusPanel.setLayout(adminCusPanelLayout);
        adminCusPanelLayout.setHorizontalGroup(
            adminCusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adminCusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(adminCusTab)
                .addContainerGap())
        );
        adminCusPanelLayout.setVerticalGroup(
            adminCusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adminCusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(adminCusTab)
                .addContainerGap())
        );

        adminPanel.add(adminCusPanel);

        adminUpdatePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Updates"));

        jLabel1.setText("No updates available.");

        jButton6.setText("Check for Updates");

        javax.swing.GroupLayout adminUpdatePanelLayout = new javax.swing.GroupLayout(adminUpdatePanel);
        adminUpdatePanel.setLayout(adminUpdatePanelLayout);
        adminUpdatePanelLayout.setHorizontalGroup(
            adminUpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adminUpdatePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(adminUpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jButton6))
                .addContainerGap(390, Short.MAX_VALUE))
        );
        adminUpdatePanelLayout.setVerticalGroup(
            adminUpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adminUpdatePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addContainerGap(253, Short.MAX_VALUE))
        );

        adminPanel.add(adminUpdatePanel);

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

        menuFileArchive.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buttons/archive.png"))); // NOI18N
        menuFileArchive.setText("Open Archive");
        menuFile.add(menuFileArchive);
        menuFile.add(separator1);

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

        menuHelpBug.setText("About");
        menuHelpBug.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuHelpBugActionPerformed(evt);
            }
        });
        menuHelp.add(menuHelpBug);

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
        locations.newLocation();
    }//GEN-LAST:event_newLocationButtonActionPerformed

    //Edit Location
    private void editLocationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editLocationButtonActionPerformed
        locations.editLocation();
    }//GEN-LAST:event_editLocationButtonActionPerformed

    //Delete Location
    private void deleteLocationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteLocationButtonActionPerformed
        locations.deleteLocation();
    }//GEN-LAST:event_deleteLocationButtonActionPerformed

    //Archive Location
    private void archiveLocationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_archiveLocationButtonActionPerformed
        locations.archiveLocation();
    }//GEN-LAST:event_archiveLocationButtonActionPerformed

    //New Asset
    private void newAssetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newAssetButtonActionPerformed
        if(locationTable.getRowCount() == 0){
            PopupPanel.display("You must add a Location before you can add an Asset.", newAssetButton.getLocationOnScreen().x+10, newAssetButton.getLocationOnScreen().y+newAssetButton.getHeight()+10);
        }
        else assets.newAsset();
    }//GEN-LAST:event_newAssetButtonActionPerformed

    //Edit Asset
    private void editAssetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editAssetButtonActionPerformed
        assets.editAsset();
    }//GEN-LAST:event_editAssetButtonActionPerformed

    //Delete Asset
    private void deleteAssetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteAssetButtonActionPerformed
        assets.deleteAsset();
    }//GEN-LAST:event_deleteAssetButtonActionPerformed

    //Archive Asset
    private void archiveAssetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_archiveAssetButtonActionPerformed
        assets.archiveAsset();
    }//GEN-LAST:event_archiveAssetButtonActionPerformed

    //Refresh table
    private void menuTableRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTableRefreshActionPerformed
        int index = tabbedPane.getSelectedIndex();
        switch(index){
            case 0: //Work orders
                workOrders.load(); break;
            case 1: //Schedule
                schedule.load(); break;
            case 2: //Assets
                assets.load(); break;
            case 3: //Locations
                locations.load(); break;
            case 4: //Parts
                parts.load(); break;
            case 5: //Employees
                employees.load(); break;
            default: break;
        }
    }//GEN-LAST:event_menuTableRefreshActionPerformed

    //New Employee
    private void newEmployeeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newEmployeeButtonActionPerformed
        employees.newEmployee();
    }//GEN-LAST:event_newEmployeeButtonActionPerformed

    //Edit Employee
    private void editEmployeeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editEmployeeButtonActionPerformed
        employees.editEmployee();
    }//GEN-LAST:event_editEmployeeButtonActionPerformed

    //Delete Employee
    private void deleteEmployeeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteEmployeeButtonActionPerformed
        employees.deleteEmployee();
    }//GEN-LAST:event_deleteEmployeeButtonActionPerformed

    //Archive Employee
    private void archiveEmployeeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_archiveEmployeeButtonActionPerformed
        employees.archiveEmployee();
    }//GEN-LAST:event_archiveEmployeeButtonActionPerformed

    //Find
    private void menuTableFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTableFilterActionPerformed
        JTable table = null;
        JPanel panel = null;
        switch(tabbedPane.getSelectedIndex()){
            case 0: table = workOrderTable; panel = workOrderPanel; break;
            case 1: table = scheduleTable; panel = schedulePanel; break;     
            case 2: table = assetTable; panel = assetPanel; break;
            case 3: table = locationTable; panel = locationPanel; break;
            case 4: table = partTable; panel = partPanel; break;
            case 5: table = employeeTable; panel = employeePanel; break;
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
                setTableSizeRadio("table_size_wo");
                break;
            case 2: //Schedule
                menuTable.setVisible(true);
                setTableSizeRadio("table_size_schedule");
                break;
            case 3: //Assets
                menuTable.setVisible(true);
                setTableSizeRadio("table_size_assets");
                break;
            case 4: //Locations
                menuTable.setVisible(true);
                setTableSizeRadio("table_size_locations");
                break;
            case 5: //Parts
                menuTable.setVisible(true);
                setTableSizeRadio("table_size_parts");
                break;
            case 6: //Employees
                menuTable.setVisible(true);
                setTableSizeRadio("table_size_employees");
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
        PasswordFrame pd = new PasswordFrame(MMS.getUser());
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
        parts.newPart();
    }//GEN-LAST:event_newPartButtonActionPerformed

    //Edit Part
    private void editPartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editPartButtonActionPerformed
        parts.editPart();
    }//GEN-LAST:event_editPartButtonActionPerformed

    //Delete Part
    private void deletePartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletePartButtonActionPerformed
        parts.deletePart();
    }//GEN-LAST:event_deletePartButtonActionPerformed

    //Archive Part
    private void archivePartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_archivePartButtonActionPerformed
        parts.archivePart();
    }//GEN-LAST:event_archivePartButtonActionPerformed

    private void locationTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_locationTableKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_DELETE) deleteLocationButtonActionPerformed(null);
    }//GEN-LAST:event_locationTableKeyPressed

    private void workOrderTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_workOrderTableKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_DELETE) deleteWOButtonActionPerformed(null);
    }//GEN-LAST:event_workOrderTableKeyPressed

    //Delete Work Order
    private void deleteWOButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteWOButtonActionPerformed
        workOrders.deleteWO();
    }//GEN-LAST:event_deleteWOButtonActionPerformed

    //Delete Schedule
    private void deleteScheduleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteScheduleButtonActionPerformed
        schedule.deleteSchedule();
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
            schedule.newSchedule();
        }
    }//GEN-LAST:event_newScheduleButtonActionPerformed

    //Complete task
    private void completeScheduleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_completeScheduleButtonActionPerformed
        schedule.completeTask(scheduleTable.getSelectedRow());
        completeScheduleButton.setEnabled(false);
    }//GEN-LAST:event_completeScheduleButtonActionPerformed

    //Archive Schedule
    private void archiveScheduleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_archiveScheduleButtonActionPerformed
        schedule.archiveSchedule();
    }//GEN-LAST:event_archiveScheduleButtonActionPerformed

    //Edit Schedule
    private void editScheduleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editScheduleButtonActionPerformed
        schedule.editSchedule();
    }//GEN-LAST:event_editScheduleButtonActionPerformed

    //View Schedule
    private void viewScheduleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewScheduleButtonActionPerformed
        schedule.viewSchedule();
    }//GEN-LAST:event_viewScheduleButtonActionPerformed

    //Double click schedule
    private void scheduleTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scheduleTableMouseClicked
        if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
            schedule.viewSchedule();
        }
    }//GEN-LAST:event_scheduleTableMouseClicked

    private void reportTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_reportTableKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_reportTableKeyPressed

    private void adminUserResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminUserResetActionPerformed
        admin.resetUserPass();
    }//GEN-LAST:event_adminUserResetActionPerformed

    private void adminUserAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminUserAddActionPerformed
        admin.addUser();
    }//GEN-LAST:event_adminUserAddActionPerformed

    private void adminUserDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminUserDeleteActionPerformed
        admin.deleteUser();
    }//GEN-LAST:event_adminUserDeleteActionPerformed

    private void newWOButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newWOButtonActionPerformed
        if(locationTable.getRowCount() == 0){
            PopupPanel.display("You must add a Location before you can add a Work Order.", newWOButton.getLocationOnScreen().x+10, newWOButton.getLocationOnScreen().y+newWOButton.getHeight()+10);
        }
        else if(employeeTable.getRowCount() == 0){
            PopupPanel.display("You must add an Employee before you can add a Work Order.", newWOButton.getLocationOnScreen().x+10, newWOButton.getLocationOnScreen().y+newWOButton.getHeight()+10);
        }
        else{
             workOrders.newWO(false, null);
        }
    }//GEN-LAST:event_newWOButtonActionPerformed

    private void editWOButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editWOButtonActionPerformed
        workOrders.editWO();
    }//GEN-LAST:event_editWOButtonActionPerformed

    private void viewWOButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewWOButtonActionPerformed
        workOrders.viewWO();
    }//GEN-LAST:event_viewWOButtonActionPerformed

    private void workOrderTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_workOrderTableMouseClicked
        if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
            workOrders.viewWO();
        }
    }//GEN-LAST:event_workOrderTableMouseClicked

    private void archiveWOButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_archiveWOButtonActionPerformed
        workOrders.archiveWO();
    }//GEN-LAST:event_archiveWOButtonActionPerformed

    //Work order status
    private void statusWOButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statusWOButtonMousePressed
        if(statusWOButton.isEnabled()){
            String status = workOrderTable.getValueAt(workOrderTable.getSelectedRow(), 7).toString();
            switch(status){
                case "Open": statusOpen.setSelected(true); break;
                case "On Hold": statusOnHold.setSelected(true); break;
                case "Closed": statusClosed.setSelected(true); break;
            }
            statusPopup.show(statusWOButton, statusWOButton.getWidth()-statusPopup.getWidth(), statusWOButton.getHeight()+3);
        }
    }//GEN-LAST:event_statusWOButtonMousePressed

    //Set WO status (Open)
    private void statusOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusOpenActionPerformed
        workOrders.setStatus("Open");
    }//GEN-LAST:event_statusOpenActionPerformed

    //Set WO status (On Hold)
    private void statusOnHoldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusOnHoldActionPerformed
        workOrders.setStatus("On Hold");
    }//GEN-LAST:event_statusOnHoldActionPerformed

    //Set WO status (Closed)
    private void statusClosedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusClosedActionPerformed
        workOrders.closeWO(partTable, statusWOButton);
    }//GEN-LAST:event_statusClosedActionPerformed

    private void createWOButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createWOButtonActionPerformed
        workOrders.newWO(true, schedule);
    }//GEN-LAST:event_createWOButtonActionPerformed

    private void adminDelCusEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminDelCusEmployeeActionPerformed
        admin.deleteCustomEmployee();
    }//GEN-LAST:event_adminDelCusEmployeeActionPerformed

    private void adminAddCusEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminAddCusEmployeeActionPerformed
        admin.addCustomEmployee();
    }//GEN-LAST:event_adminAddCusEmployeeActionPerformed

    private void adminDelCusAssetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminDelCusAssetActionPerformed
        admin.deleteCustomAsset();
    }//GEN-LAST:event_adminDelCusAssetActionPerformed

    private void adminAddCusAssetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminAddCusAssetActionPerformed
        admin.addCustomAsset();
    }//GEN-LAST:event_adminAddCusAssetActionPerformed

    private void adminDelCusScheduleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminDelCusScheduleActionPerformed
        admin.deleteCustomMaintType();
    }//GEN-LAST:event_adminDelCusScheduleActionPerformed

    private void adminAddCusScheduleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminAddCusScheduleActionPerformed
        admin.addCustomMaintType();
    }//GEN-LAST:event_adminAddCusScheduleActionPerformed

    private void printWOButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printWOButtonActionPerformed
    int [] rows = workOrderTable.getSelectedRows();
        for(int y = 0; y < rows.length; y++){
            //Get work order detail
            int row = workOrderTable.getSelectedRow();
            Object [] data = new Object[9];
            for (int i = 0 ; i < data.length ; i++){
                if(i < 3) data[i] = workOrderTable.getValueAt(row, i);
                else if(i == 3){
                    ResultSet rs = Database.select("SELECT wo_desc FROM work_orders WHERE id = ?",
                            new Object[]{workOrderTable.getValueAt(row, 0)});
                    try {
                        if(rs.next()){
                            data[i] = com.github.rjeschke.txtmark.Processor.process(rs.getString(1).trim());
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else data[i] = workOrderTable.getValueAt(row, i-1);
            }

            //Go printer
            if(data[2].equals("Schedule")){
                PrinterJob job = PrinterJob.getPrinterJob();
                job.setPrintable(new Printer(data, true, false));
                job.setJobName("MMS-WorkOrder-"+workOrderTable.getValueAt(row, 0));
                boolean doPrint = job.printDialog();
                if (doPrint) {
                    try {
                        job.print();
                    } catch (PrinterException e) {
                        InternalDialog.showInternalConfirmDialog(this, "Something went wrong. Please check your printer settings.", "Printing Error", -1, JOptionPane.ERROR_MESSAGE, null); 
                    }
                }
                job.setPrintable(new Printer(data, true, true));
                job.setJobName("MMS-WorkOrder-"+workOrderTable.getValueAt(row, 0)+"-Schedule");
                //doPrint = job.printDialog();
                if (doPrint) {
                    try {
                        job.print();
                    } catch (PrinterException e) {
                        InternalDialog.showInternalConfirmDialog(this, "Something went wrong. Please check your printer settings.", "Printing Error", -1, JOptionPane.ERROR_MESSAGE, null);
                    }
                }
            }
            else{
                PrinterJob job = PrinterJob.getPrinterJob();
                job.setPrintable(new Printer(data, false, false));
                job.setJobName("MMS-WorkOrder-"+workOrderTable.getValueAt(row, 0));
                boolean doPrint = job.printDialog();
                if (doPrint) {
                    try {
                        job.print();
                    } catch (PrinterException e) {
                        InternalDialog.showInternalConfirmDialog(this, "Something went wrong. Please check your printer settings.", "Printing Error", -1, JOptionPane.ERROR_MESSAGE, null);
                    }
                }
            }
        }
    }//GEN-LAST:event_printWOButtonActionPerformed

    private void adminGenSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminGenSaveActionPerformed
        admin.saveGeneralSettings();
    }//GEN-LAST:event_adminGenSaveActionPerformed

    private void menuHelpBugActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuHelpBugActionPerformed
        AboutFrame f = new AboutFrame();
        f.setLocation(MMS.getMainFrame().getDesktopPane().getWidth()/2-f.getWidth()/2, MMS.getMainFrame().getDesktopPane().getHeight()/2-f.getHeight()/2-50);
        MMS.getMainFrame().getDesktopPane().add(f);
        MMS.getMainFrame().getDesktopPane().setLayer(f, 1);
        f.setVisible(true);
    }//GEN-LAST:event_menuHelpBugActionPerformed

    private void newReportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newReportButtonActionPerformed
        reports.newReport();
    }//GEN-LAST:event_newReportButtonActionPerformed

    private void editReportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editReportButtonActionPerformed
        reports.editReport();
    }//GEN-LAST:event_editReportButtonActionPerformed

    private void deleteReportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteReportButtonActionPerformed
        reports.deleteReport();
    }//GEN-LAST:event_deleteReportButtonActionPerformed

    private void runReportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runReportButtonActionPerformed
        reports.runReport();
    }//GEN-LAST:event_runReportButtonActionPerformed

    //Load tables
    public void loadTables(){
        workOrders.load();
        schedule.load();
        assets.load();
        locations.load();
        parts.load();
        employees.load();
        reports.load();
        if(isAdmin) admin.load();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> AssetCusList;
    private javax.swing.JList<String> EmpCusList;
    private javax.swing.JList<String> SchCusList;
    private javax.swing.JButton adminAddCusAsset;
    private javax.swing.JButton adminAddCusEmployee;
    private javax.swing.JButton adminAddCusSchedule;
    private javax.swing.JPanel adminAssetCusPanel;
    private javax.swing.JPanel adminCusPanel;
    private javax.swing.JTabbedPane adminCusTab;
    private javax.swing.JButton adminDelCusAsset;
    private javax.swing.JButton adminDelCusEmployee;
    private javax.swing.JButton adminDelCusSchedule;
    private javax.swing.JPanel adminDeptCusPanel;
    private javax.swing.JTextField adminGenNameFld;
    private javax.swing.JLabel adminGenNameLbl;
    private javax.swing.JPanel adminGenPanel;
    private javax.swing.JButton adminGenSave;
    private javax.swing.JPanel adminPanel;
    private javax.swing.JPanel adminTypePanel;
    private javax.swing.JPanel adminUpdatePanel;
    private javax.swing.JButton adminUserAdd;
    private javax.swing.JButton adminUserDelete;
    private javax.swing.JPanel adminUserPanel;
    private javax.swing.JButton adminUserReset;
    private javax.swing.JScrollPane adminUserScroll;
    private javax.swing.JTable adminUserTable;
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
    private javax.swing.JButton createWOButton;
    private javax.swing.JButton deleteAssetButton;
    private javax.swing.JButton deleteEmployeeButton;
    private javax.swing.JButton deleteLocationButton;
    private javax.swing.JButton deletePartButton;
    private javax.swing.JButton deleteReportButton;
    private javax.swing.JButton deleteScheduleButton;
    private javax.swing.JButton deleteWOButton;
    private javax.swing.JDesktopPane desktopPane;
    private javax.swing.JButton editAssetButton;
    private javax.swing.JButton editEmployeeButton;
    private javax.swing.JButton editLocationButton;
    private javax.swing.JButton editPartButton;
    private javax.swing.JButton editReportButton;
    private javax.swing.JButton editScheduleButton;
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
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel locationLoadLabel;
    private javax.swing.JPanel locationPanel;
    private javax.swing.JScrollPane locationScroll;
    private javax.swing.JTable locationTable;
    private javax.swing.JToolBar locationTools;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem menuChangePassword;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenuItem menuFileArchive;
    private javax.swing.JMenuItem menuFileDisconnect;
    private javax.swing.JMenu menuHelp;
    private javax.swing.JMenuItem menuHelpBug;
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
    private javax.swing.JButton newReportButton;
    private javax.swing.JButton newScheduleButton;
    private javax.swing.JButton newWOButton;
    private javax.swing.JLabel partLoadLabel;
    private javax.swing.JPanel partPanel;
    private javax.swing.JScrollPane partScroll;
    private javax.swing.JTable partTable;
    private javax.swing.JToolBar partTools;
    private javax.swing.JButton printWOButton;
    private javax.swing.JComboBox<String> reportCombo;
    private javax.swing.JPanel reportPanel;
    private javax.swing.JScrollPane reportScroll;
    private javax.swing.JTable reportTable;
    private javax.swing.JToolBar reportTools;
    private javax.swing.JButton runReportButton;
    private javax.swing.JLabel scheduleLoadLabel;
    private javax.swing.JPanel schedulePanel;
    private javax.swing.JScrollPane scheduleScroll;
    private javax.swing.JTable scheduleTable;
    private javax.swing.JToolBar scheduleTools;
    private javax.swing.JPopupMenu.Separator separator1;
    private javax.swing.JRadioButtonMenuItem statusClosed;
    private javax.swing.ButtonGroup statusGroup;
    private javax.swing.JRadioButtonMenuItem statusOnHold;
    private javax.swing.JRadioButtonMenuItem statusOpen;
    private javax.swing.JPopupMenu statusPopup;
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
