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

import com.mms.utilities.ContextMenuMouseListener;
import com.formdev.flatlaf.intellijthemes.FlatGrayIJTheme;
import com.mms.dialogs.LocationDialog;
import com.mms.dialogs.LoginDialog;
import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author J.B. Slade
 */
public class MMS {
    
    //Variables
    public static final String NAME = "OpenMMS", VERSION = "1.0";
    public static final boolean DEBUG = false;
    public static final JFrame phf = new JFrame();
    public static final int DIAG_WIDTH = Toolkit.getDefaultToolkit(). getScreenSize().width/5 > 310 ? 310 : Toolkit.getDefaultToolkit(). getScreenSize().width/5;
    private static MouseListener mouseListener;
    private static Thread shutdown;
    private static MainFrame m;
    private static Connection conn;
    private static String user;
    private static Preferences p;
    
    //Image icons
    public static final ImageIcon systemIcon = new ImageIcon(MMS.class.getResource("/icon.png"));
    public static final ImageIcon dashboardIcon = new ImageIcon(MMS.class.getResource("/tabs/dashboard.png"));
    public static final ImageIcon workOrdersIcon = new ImageIcon(MMS.class.getResource("/tabs/workOrders.png"));
    public static final ImageIcon scheduleIcon = new ImageIcon(MMS.class.getResource("/tabs/schedule.png"));
    public static final ImageIcon locationIcon = new ImageIcon(MMS.class.getResource("/tabs/locations.png"));
    public static final ImageIcon assetsIcon = new ImageIcon(MMS.class.getResource("/tabs/assets.png"));
    public static final ImageIcon partsIcon = new ImageIcon(MMS.class.getResource("/tabs/parts.png"));
    public static final ImageIcon employeesIcon = new ImageIcon(MMS.class.getResource("/tabs/employees.png"));
    public static final ImageIcon reportsIcon = new ImageIcon(MMS.class.getResource("/tabs/reports.png"));
    public static final ImageIcon ajaxIcon = new ImageIcon(MMS.class.getResource("/ajax.gif"));
    
    //Getters
    public static MainFrame getMainFrame(){return m;}
    public static String getUser(){return user;}
    public static Preferences getPrefs(){return p;}
    public static MouseListener getMouseListener(){return mouseListener;}
    
    //Setters
    public static void setUser(String u){user = u;}
    public static void setConnection(Connection c){conn = c;}
    
    //Main
    public static void main (String [] args){
        //L&F
        FlatGrayIJTheme.install();
        System.setProperty("flatlaf.menuBarEmbedded", "true");
        UIManager.put("TabbedPane.selectedBackground", Color.white);
        UIManager.put( "Component.focusWidth", 1 );
        UIManager.put( "TextComponent.arc", 5 );
        
        //Mouse listener
        mouseListener = new ContextMenuMouseListener();
        
        //Shutdown thread
        shutdown = new Thread(){
            @Override
            public void run(){
                try {
                    //Set user to logged out
                    if(user != null){
                        executeQuery("UPDATE users SET logged_in = 'N' WHERE user_name = ?",
                                new Object[]{user});
                    }
                    //Close connection
                    if(conn != null){
                        conn.close();
                        System.out.println("[DATABASE] Connection closed");
                    }
                    //Shutdown Derby
                    if(p.get("db_type", "").equals("derby")) DriverManager.getConnection("jdbc:derby:;shutdown=true");
                } catch (SQLException ex) {
                    System.out.println("[DATABASE] "+ex.getCause());
                    System.exit(0);
                }
                System.exit(0);
            }
        };
        
        //Preferences
        p = Preferences.userNodeForPackage(MMS.class);
        
        //Placeholder frame
        phf.setIconImage(systemIcon.getImage());
        phf.setUndecorated(true);
        phf.setLocationRelativeTo(null);
        
        //Setup & login
        setup();
        
        //RESET OLD ADMIN PASSWORD MSSQL (REMOVE ONCE FIXED)
//        String salt = Hasher.getSalt(), pass = Hasher.getHash("admin", salt);
//        MMS.executeQuery("UPDATE Users SET Password = ?, Salt = ? WHERE Username = ?",
//                new Object[]{pass, salt, "Administrator"});
    }

    //Setup
    public static void setup(){
        //Close MainFrame if open
        if(m != null) m.dispose();
       
        if(DEBUG) p.putBoolean("first_run", true);
        OUTER:
        while (true) {
            if (p.getBoolean("first_run", true)) {
                Setup setup = new Setup(phf, true);
                setup.setSize(DIAG_WIDTH, setup.getHeight());
                setup.setIconImage(systemIcon.getImage());
                setup.setLocationRelativeTo(phf);
                setup.setVisible(true);
                if(setup.success()) break; //SUCCESS
                else{ //FAIL
                    shutdown();
                    try {
                        shutdown.join();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MMS.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                switch (p.get("db_type", "")) {
                    case "derby":
                        String dir = p.get("derby_home", ""), name = p.get("derby_name", "");
                        System.setProperty("derby.system.home", dir);
                        System.setProperty("user.dir", dir);
                        try {
                            //Register driver
                            DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
                            //Try connect
                            conn = DriverManager.getConnection("jdbc:derby:"+name);
                            System.out.println("[DATABASE] Connected to Derby: "+name);
                            break OUTER; 
                        } catch (SQLException ex) {
                            Logger.getLogger(MMS.class.getName()).log(Level.SEVERE, null, ex);
                            p.putBoolean("first_run", true);
                        }                 
                    case "mssql":
                        String srvr = p.get("srvr_ip", ""), db = p.get("srvr_db", ""), usr = p.get("srvr_user", ""), pass = p.get("srvr_pass", "");
                        try {
                            //Register driver
                            DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
                            //Try connect
                            conn = DriverManager.getConnection("jdbc:sqlserver://"+srvr+";databaseName="+db+";user="+usr+";password="+pass+";");
                            System.out.println("[DATABASE] Connected to MSSQL: "+db);
                            break OUTER;
                        } catch (SQLException ex) {
                            Logger.getLogger(MMS.class.getName()).log(Level.SEVERE, null, ex);
                            p.putBoolean("first_run", true);
                        }
                    default:
                        p.putBoolean("first_run", true);
                        break;
                }
            }
        }
        login();
    }
   
    //Login
    private static void login(){
        phf.setVisible(true);
        LoginDialog lg = new LoginDialog(phf, true);
        lg.setSize(DIAG_WIDTH, lg.getHeight());
        lg.setIconImage(systemIcon.getImage());
        lg.setLocationRelativeTo(phf);
        lg.setVisible(true);
        
        if(lg.success()){ //SUCCESS
            //Dispose placeholder frame
            phf.dispose();

            //MainFrame
            m = new MainFrame();
            m.setTitle(NAME+" "+VERSION);
            m.setIconImage(systemIcon.getImage());
            m.setLocationRelativeTo(null);
            m.setExtendedState(JFrame.MAXIMIZED_BOTH);
            //Load tables
            m.loadLocations(0);
            m.loadAssets(0);
            m.loadParts(0);
            m.loadEmployees(0);

            m.setVisible(true);
        }
        else shutdown(); //FAIL       
    }
    
    //Logout
    public static void logout(){
        new Thread(){
            public void run(){  
                //Set user to logged out
                if(user != null){
                    executeQuery("UPDATE users SET logged_in = 'N' WHERE user_name = ?",
                            new Object[]{user});
                }
                m.dispose();
                login();
            }
        }.start();
    }
    
    //Shutdown
    public static void shutdown(){
        shutdown.start();
    }
    
    //DATABASE METHODS
    //Query (no return)
    public static void executeQuery(String sql, Object [] o){
        PreparedStatement stat;
        try {
            stat = conn.prepareStatement(sql);
            for(int i = 0; i < o.length; i++){
                stat.setObject(i+1, o[i]);
            }
            stat.execute();
            stat.close();
        } catch (SQLException ex) {
            Logger.getLogger(LocationDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //Query (no return, no params)
    public static void executeQuery(String sql){
        PreparedStatement stat;
        try {
            stat = conn.prepareStatement(sql);
            stat.execute();
            stat.close();
        } catch (SQLException ex) {
            Logger.getLogger(LocationDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //Select with params
    public static ResultSet select(String sql, Object [] o){
        PreparedStatement stat;
        ResultSet rs = null;
        try {
            stat = conn.prepareStatement(sql);
            for(int i = 0; i < o.length; i++){
                stat.setObject(i+1, o[i]);
            }
            rs = stat.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(LocationDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
    //Select without params
    public static ResultSet select(String sql){
        PreparedStatement stat;
        ResultSet rs = null;
        try {
            stat = conn.prepareStatement(sql);
            rs = stat.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(LocationDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
    
    //Resize table
//    public static void resizeTable(JTable table) {
//        final TableColumnModel columnModel = table.getColumnModel();
//        for (int column = 0; column < table.getColumnCount(); column++) {
//            int width = 15; // Min width
//            for (int row = 0; row < table.getRowCount(); row++) {
//                TableCellRenderer renderer = table.getCellRenderer(row, column);
//                Component comp = table.prepareRenderer(renderer, row, column);
//                width = Math.max(comp.getPreferredSize().width +5 , width);
//            }
//            if(width > 400) width=400;  
//            columnModel.getColumn(column).setPreferredWidth(width);
//        }
//    }
    
    //Resize table
    public static void resizeTable(JTable table) {
        int columnMargin = 15; //Column margin
        JTableHeader tableHeader = table.getTableHeader();
 
        if(tableHeader == null) {
            // can't auto size a table without a header
            return;
        }
 
        FontMetrics headerFontMetrics = tableHeader.getFontMetrics(tableHeader.getFont());
 
        int[] minWidths = new int[table.getColumnCount()];
        int[] maxWidths = new int[table.getColumnCount()];
 
        for(int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
            int headerWidth = headerFontMetrics.stringWidth(table.getColumnName(columnIndex));
 
            minWidths[columnIndex] = headerWidth + columnMargin;
 
            int maxWidth = getMaximalRequiredColumnWidth(table, columnIndex, headerWidth);
 
            maxWidths[columnIndex] = Math.max(maxWidth, minWidths[columnIndex]) + columnMargin;
        }
 
        adjustMaximumWidths(table, minWidths, maxWidths);
 
        for(int i = 0; i < minWidths.length; i++) {
            if(minWidths[i] > 0) {
                table.getColumnModel().getColumn(i).setMinWidth(minWidths[i]);
            }
 
            if(maxWidths[i] > 0) {
                table.getColumnModel().getColumn(i).setMaxWidth(maxWidths[i]);
 
                table.getColumnModel().getColumn(i).setWidth(maxWidths[i]);
            }
        }
    }
    private static void adjustMaximumWidths(JTable table, int[] minWidths, int[] maxWidths) {
        if(table.getWidth() > 0) {
            // to prevent infinite loops in exceptional situations
            int breaker = 0;
 
            // keep stealing one pixel of the maximum width of the highest column until we can fit in the width of the table
            while(sum(maxWidths) > table.getWidth() && breaker < 10000) {
                int highestWidthIndex = findLargestIndex(maxWidths);
 
                maxWidths[highestWidthIndex] -= 1;
 
                maxWidths[highestWidthIndex] = Math.max(maxWidths[highestWidthIndex], minWidths[highestWidthIndex]);
 
                breaker++;
            }
        }
    }
    private static int getMaximalRequiredColumnWidth(JTable table, int columnIndex, int headerWidth) {
        int maxWidth = headerWidth;
 
        TableColumn column = table.getColumnModel().getColumn(columnIndex);
 
        TableCellRenderer cellRenderer = column.getCellRenderer();
 
        if(cellRenderer == null) {
            cellRenderer = new DefaultTableCellRenderer();
        }
 
        for(int row = 0; row < table.getModel().getRowCount(); row++) {
            Component rendererComponent = cellRenderer.getTableCellRendererComponent(table,
                table.getModel().getValueAt(row, columnIndex),
                false,
                false,
                row,
                columnIndex);
 
            double valueWidth = rendererComponent.getPreferredSize().getWidth();
 
            maxWidth = (int) Math.max(maxWidth, valueWidth);
        }
        return maxWidth;
    }
    private static int findLargestIndex(int[] widths) {
        int largestIndex = 0;
        int largestValue = 0;
 
        for(int i = 0; i < widths.length; i++) {
            if(widths[i] > largestValue) {
                largestIndex = i;
                largestValue = widths[i];
            }
        }
        return largestIndex;
    }
    private static int sum(int[] widths) {
        int sum = 0;
        for(int width : widths) {
            sum += width;
        }
        return sum;
    }
}
