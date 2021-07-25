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

import com.mms.dialogs.SetupDialog;
import com.mms.utilities.ContextMenuMouseListener;
import com.formdev.flatlaf.intellijthemes.FlatGrayIJTheme;
import com.mms.dialogs.LoginDialog;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 *
 * @author J.B. Slade
 */
public class MMS {
    
    //Variables
    public static final String NAME = "OpenMMS", VERSION = "1.0";
    public static final boolean DEBUG = false;
    public static final int DIAG_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width/5 > 310 ? 310 : Toolkit.getDefaultToolkit().getScreenSize().width/5;
    private static MouseListener mouseListener;
    private static Thread shutdownThread;
    private static final JFrame splash = new JFrame();
    private static MainFrame m;
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
    
    //Main
    public static void main (String [] args){
        //L&F
        FlatGrayIJTheme.install();
        System.setProperty("flatlaf.menuBarEmbedded", "true");
        UIManager.put("TabbedPane.selectedBackground", Color.white);
        UIManager.put("Component.focusWidth", 1 );
        UIManager.put("TextComponent.arc", 5 );
        UIManager.put("ScrollBar.showButtons", true);
        
        //Splash
        splash.setIconImage(systemIcon.getImage());
        splash.setUndecorated(true);
        splash.setLocationRelativeTo(null);
        
        //Mouse listener
        mouseListener = new ContextMenuMouseListener();
        
        //Shutdown thread
        shutdownThread = new Thread(){
            @Override
            public void run(){
                Database.shutdown();
                System.exit(0);
            }
        };
        
        //Preferences
        p = Preferences.userNodeForPackage(MMS.class);
       
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
                SetupDialog setup = new SetupDialog(splash, true);
                setup.setSize(DIAG_WIDTH, setup.getHeight());
                setup.setIconImage(systemIcon.getImage());
                setup.setLocationRelativeTo(splash);
                splash.setVisible(true);
                setup.setVisible(true);
                if(setup.success()) break; //SUCCESS
                else{ //FAIL
                    shutdown();
                    try {
                        shutdownThread.join();
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
                        
                        //Register driver
                        Database.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
                        //Try connect
                        try {
                            Database.setConnection("jdbc:derby:"+name);
                            
                            System.out.println("[DATABASE] Connected to Derby: "+name);
                            break OUTER;
                        } catch (SQLException ex) {
                            Logger.getLogger(MMS.class.getName()).log(Level.SEVERE, null, ex);
                            MMS.getPrefs().putBoolean("first_run", true);
                        }
                        break;                  
                    case "mssql":
                        String srvr = p.get("srvr_ip", ""), db = p.get("srvr_db", ""), usr = p.get("srvr_user", ""), pass = p.get("srvr_pass", "");
                        //Register driver
                        Database.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
                        //Try connect
                        try {  
                            Database.setConnection("jdbc:sqlserver://"+srvr+";databaseName="+db+";user="+usr+";password="+pass+";");
                            
                            System.out.println("[DATABASE] Connected to MSSQL: "+db);
                            break OUTER;
                        } catch (SQLException ex) {
                            Logger.getLogger(MMS.class.getName()).log(Level.SEVERE, null, ex);
                            MMS.getPrefs().putBoolean("first_run", true);
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
    static void login(){
        splash.setVisible(true);
        LoginDialog lg = new LoginDialog(splash, true);
        lg.setSize(DIAG_WIDTH, lg.getHeight());
        lg.setIconImage(systemIcon.getImage());
        lg.setLocationRelativeTo(splash);
        splash.setVisible(true);
        lg.setVisible(true);
        
        if(lg.success()){ //SUCCESS
            //Dispose splash
            splash.dispose();

            //MainFrame
            m = new MainFrame();
            m.setTitle(NAME+" "+VERSION);
            m.setIconImage(systemIcon.getImage());
            m.setLocationRelativeTo(null);
            m.setExtendedState(JFrame.MAXIMIZED_BOTH);
            m.loadTables();
            m.setVisible(true);
        }
        else shutdown(); //FAIL       
    }
    
    //Logout
    public static void logout(){
        new Thread(){
            @Override
            public void run(){  
                m.dispose();
                login();
            }
        }.start();
    }
    
    //Shutdown
    public static void shutdown(){
        shutdownThread.start();
    }
}
