package com.mms;

import com.formdev.flatlaf.intellijthemes.FlatArcIJTheme;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

public class MMS {
    
    //Variables
    public static final String NAME = "OpenMMS", VERSION = "1.0";
    public static final boolean DEBUG = false;
    private static MainFrame m;
    private static Connection conn;
    private static String user;
    private static ArrayList<String> users;
    private static Preferences p;
    
    //Image icons
    public static final Image systemIcon = new ImageIcon(MMS.class.getResource("/icon.png")).getImage();
    public static final Image workOrdersIcon = new ImageIcon(MMS.class.getResource("/tabs/workOrders.png")).getImage();
    public static final Image scheduleIcon = new ImageIcon(MMS.class.getResource("/tabs/schedule.png")).getImage();
    public static final Image locationIcon = new ImageIcon(MMS.class.getResource("/tabs/locations.png")).getImage();
    public static final Image assetsIcon = new ImageIcon(MMS.class.getResource("/tabs/assets.png")).getImage();
    public static final Image partsIcon = new ImageIcon(MMS.class.getResource("/tabs/parts.png")).getImage();
    public static final Image employeesIcon = new ImageIcon(MMS.class.getResource("/tabs/employees.png")).getImage();
    public static final Image reportsIcon = new ImageIcon(MMS.class.getResource("/tabs/reports.png")).getImage();
    public static final Image ajaxIcon = new ImageIcon(MMS.class.getResource("/ajax.gif")).getImage();
    
    //Getters
    public static MainFrame getMainFrame(){return m;}
    public static Connection getConnection(){return conn;}
    public static String getUser(){return user;}
    public static ArrayList<String> getUsers(){return users;}
    public static Preferences getPrefs(){return p;}
    
    //Setters
    public static void setUser(String u){user = u;}
    public static void setConnection(Connection c){conn = c;}
    
    //Main
    public static void main (String [] args){
        //L&F
        FlatArcIJTheme.install();
        System.setProperty("flatlaf.menuBarEmbedded", "true");
        UIManager.put("TabbedPane.selectedBackground", Color.white);
        
        //Preferences
        p = Preferences.userNodeForPackage(MMS.class);
        
        //Setup
        if(DEBUG) p.putBoolean("firstRun", true);
        OUTER:
        while (true) {
            if (p.getBoolean("firstRun", true)) {
                new Setup(null, true).setVisible(true);
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
                        } catch (SQLException ex) {
                            Logger.getLogger(MMS.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        System.out.println("[DATABASE] Connected to Derby: "+name);
                        break OUTER;                    
                    case "mssql":
                        String srvr = p.get("mssqlsrvr", ""), db = p.get("mssqldb", ""), usr = p.get("mssqlusr", ""), pass = p.get("mssqlpass", "");
                        try {
                            //Register driver
                            DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
                            //Try connect
                            conn = DriverManager.getConnection("jdbc:sqlserver://"+srvr+";databaseName="+db+";user="+usr+";password="+pass+";");
                        } catch (SQLException ex) {
                            Logger.getLogger(MMS.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        System.out.println("[DATABASE] Connected to MSSQL: "+db);
                        break OUTER;
                    default:
                        p.putBoolean("firstRun", true);
                        break;
                }
            }
        }
        
        //TEST Derby
        if(p.get("dbType", "").equals("derby")){
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT NAME FROM CARS");
                rs.next();
                System.out.println("[DEBUG] "+rs.getString(1));
                rs.close();
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(MMS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //MainFrame
        m = new MainFrame();
        m.setTitle(NAME+" "+VERSION);
        m.setIconImage(systemIcon);
        m.setLocationRelativeTo(null);
        m.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //Load tables
        m.loadLocations(0);
        m.loadAssets(0);
        
        m.setVisible(true);
    }
    
    //UTILITY METHODS
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
    
    //Shutdown
    public static void shutdown(){
        try {
            if(conn != null){
                conn.close();
                System.out.println("[DATABASE] Connection closed");
            }
            if(p.get("dbType", "").equals("derby")) DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException ex) {
            System.out.println("[DATABASE] "+ex.getCause());
        }
        System.exit(0);
    }
}
