package com.mms;

import com.formdev.flatlaf.intellijthemes.FlatGrayIJTheme;
import com.mms.locations.LocationDialog;
import com.mms.users.LoginDialog;
import java.awt.Color;
import java.awt.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class MMS {
    
    //Variables
    public static final String NAME = "OpenMMS", VERSION = "1.0";
    public static final boolean DEBUG = true;
    public static final JFrame phf = new JFrame();
    private static MainFrame m;
    private static Connection conn;
    private static String user;
    private static ArrayList<String> users;
    private static Preferences p;
    
    //Image icons
    public static final ImageIcon systemIcon = new ImageIcon(MMS.class.getResource("/icon.png"));
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
    public static ArrayList<String> getUsers(){return users;}
    public static Preferences getPrefs(){return p;}
    
    //Setters
    public static void setUser(String u){user = u;}
    public static void setConnection(Connection c){conn = c;}
    
    //Main
    public static void main (String [] args){
        //L&F
        FlatGrayIJTheme.install();
        System.setProperty("flatlaf.menuBarEmbedded", "true");
        UIManager.put("TabbedPane.selectedBackground", Color.white);
        
        //Preferences
        p = Preferences.userNodeForPackage(MMS.class);
        
        //Placeholder frame
        phf.setIconImage(systemIcon.getImage());
        phf.setUndecorated(true);
        phf.setLocationRelativeTo(null);
        
        //Setup
        if(DEBUG) p.putBoolean("firstRun", true);
        OUTER:
        while (true) {
            if (p.getBoolean("firstRun", true)) {
                new Setup(phf, true).setVisible(true);
                break;
            } else {
                switch (p.get("dbType", "")) {
                    case "derby":
                        String dir = p.get("derbyHome", ""), name = p.get("derbyName", "");
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
                            p.putBoolean("firstRun", true);
                        }                 
                    case "mssql":
                        String srvr = p.get("mssqlsrvr", ""), db = p.get("mssqldb", ""), usr = p.get("mssqlusr", ""), pass = p.get("mssqlpass", "");
                        try {
                            //Register driver
                            DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
                            //Try connect
                            conn = DriverManager.getConnection("jdbc:sqlserver://"+srvr+";databaseName="+db+";user="+usr+";password="+pass+";");
                            System.out.println("[DATABASE] Connected to MSSQL: "+db);
                            break OUTER;
                        } catch (SQLException ex) {
                            Logger.getLogger(MMS.class.getName()).log(Level.SEVERE, null, ex);
                            p.putBoolean("firstRun", true);
                        }
                    default:
                        p.putBoolean("firstRun", true);
                        break;
                }
            }
        }
        
        //RESET OLD ADMIN PASSWORD MSSQL (REMOVE ONCE FIXED)
//        String salt = Hasher.getSalt(), pass = Hasher.getHash("admin", salt);
//        MMS.executeQuery("UPDATE Users SET Password = ?, Salt = ? WHERE Username = ?",
//                new Object[]{pass, salt, "Administrator"});
        
        //Login
        loadUsers();
        phf.setVisible(true);
        new LoginDialog(phf, true).setVisible(true);
        
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
        m.loadEmployees(0);
        
        m.setVisible(true);
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
    
    //TABLE METHODS
    //Resize table
    public static void resizeTable(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 100; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width +5 , width);
            }
            if(width > 400) width=400;  
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }
    public static void sortTable(JTable table){
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(sorter);

        List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        //sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
    }
    
    
    //Load users
    public static void loadUsers(){
        users = new ArrayList<>();
            try {
                ResultSet rs = select("SELECT Username, Logged FROM Users");
                while(rs.next()){
                    String s = rs.getString(1).trim();
                    String logged = rs.getString(2) == null ? "N" : rs.getString(2);
                    if(logged.equals("Y")) s = s += " **";
                    users.add(s);
                }
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    //SHUTDOWN
    public static void shutdown(){
        new Thread(){
            public void run(){
                try {
                    //Set user to logged out
                    if(user != null){
                        executeQuery("UPDATE Users SET Logged = 'N' WHERE Username = ?",
                                new Object[]{user});
                    }
                    //Close connection
                    if(conn != null){
                        conn.close();
                        System.out.println("[DATABASE] Connection closed");
                    }
                    //Shutdown Derby
                    if(p.get("dbType", "").equals("derby")) DriverManager.getConnection("jdbc:derby:;shutdown=true");
                } catch (SQLException ex) {
                    System.out.println("[DATABASE] "+ex.getCause());
                }
                System.exit(0);
            }
        }.start();
    }
}
