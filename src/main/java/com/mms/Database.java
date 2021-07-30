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

import com.mms.iframes.LocationFrame;
import com.mms.utilities.Hasher;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author J.B. Slade
 */
public class Database {
    
    private static Connection conn;
    
    public static void registerDriver(Driver d){
        try {
            DriverManager.registerDriver(d);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void setConnection(String s) throws SQLException{
            conn = DriverManager.getConnection(s);
    }

    public static void shutdown(){
        try {
            //Close connection
            if(conn != null){
                conn.close();
                System.out.println("[DATABASE] Connection closed");
            }
            //Shutdown Derby
            if(MMS.getPrefs().get("db_type", "").equals("derby")) 
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException ex) {
            System.out.println("[DATABASE] "+ex.getMessage());
        }
    }
    
    //Create tables
    public static void createTables(String adminPass){
        //Users
        Database.executeQuery("CREATE TABLE users("
                + "user_name VARCHAR(50) PRIMARY KEY,"
                + "password VARCHAR(50),"
                + "salt VARCHAR(16),"
                + "user_level int"
                + ")");
        //Insert Administrator
        String salt = Hasher.getSalt();
        adminPass = Hasher.getHash(adminPass, salt);
        Database.executeQuery("INSERT INTO users (user_name, password, salt, user_level) VALUES (?, ?, ?, ?)",
                new Object[]{"Administrator", adminPass, salt, 3});
        
        //Locations
        Database.executeQuery("CREATE TABLE locations("
                + "id INT PRIMARY KEY,"
                + "location_name VARCHAR(50),"
                + "location_desc VARCHAR(100),"
                + "archived VARCHAR(1)"
                + ")");
        //Insert No Location
        Database.executeQuery("INSERT INTO locations (id, location_name, location_desc, archived) VALUES (-1, 'No Location', '', 'Z')");
        
        //Assets
        Database.executeQuery("CREATE TABLE assets("
                + "id INT PRIMARY KEY,"
                + "asset_name VARCHAR(50),"
                + "asset_desc VARCHAR(100),"
                + "asset_type VARCHAR(50),"
                + "location_id INT,"
                + "archived VARCHAR(1)"
                + ")");
        //Insert No Asset
        Database.executeQuery("INSERT INTO assets (id, asset_name, asset_desc, asset_type, location_id, archived) VALUES (-1, 'No Asset', '', '', -1, 'Z')");
        
        //Parts
        Database.executeQuery("CREATE TABLE parts("
                + "id INT PRIMARY KEY,"
                + "part_name VARCHAR(50),"
                + "part_qty INT,"
                + "part_cost DECIMAL(19, 2),"
                + "archived VARCHAR(1)"
                + ")");
        
        //Employees
        Database.executeQuery("CREATE TABLE employees("
                + "id INT PRIMARY KEY,"
                + "employee_name VARCHAR(50),"
                + "employee_desc VARCHAR(50),"
                + "employee_dept VARCHAR(50),"
                + "archived VARCHAR(1)"
                + ")");
        
        //Schedule
        Database.executeQuery("CREATE TABLE schedule("
                + "id INT PRIMARY KEY,"
                + "schedule_name VARCHAR(50),"
                + "schedule_type VARCHAR(50),"
                + "schedule_from_date DATE,"
                + "schedule_last_date DATE,"
                + "schedule_freq VARCHAR(20),"
                + "schedule_desc VARCHAR(2000),"
                + "location_id INT,"
                + "asset_id INT,"
                + "archived VARCHAR(1)"
                + ")");
        
        //Work Orders
        Database.executeQuery("CREATE TABLE work_orders("
                + "id INT PRIMARY KEY,"
                + "wo_date DATE,"
                + "wo_type VARCHAR(50),"
                + "wo_status VARCHAR(50),"
                + "wo_priority VARCHAR(50),"
                + "wo_desc VARCHAR(2000),"
                + "wo_start_time DATE,"
                + "wo_end_time DATE,"
                + "location_id INT,"
                + "asset_id INT,"
                + "user_name VARCHAR(50),"
                + "archived VARCHAR(1)"
                + ")");
        
        //Work Order Employees
        Database.executeQuery("CREATE TABLE wo_employees("
                + "wo_id INT,"
                + "employee_id INT"
                + ")");
        
        //CustomFields
        Database.executeQuery("CREATE TABLE custom_fields("
                + "custom_type VARCHAR(50),"
                + "custom_value VARCHAR(50)"
                + ")");
        //Insert default values
        Database.executeQuery("INSERT INTO custom_fields (custom_type, custom_value) VALUES (?, ?)", new Object[]{"employee_dept", "Maintenance"});
        Database.executeQuery("INSERT INTO custom_fields (custom_type, custom_value) VALUES (?, ?)", new Object[]{"employee_dept", "Technical"});
        Database.executeQuery("INSERT INTO custom_fields (custom_type, custom_value) VALUES (?, ?)", new Object[]{"employee_dept", "Production"});
        Database.executeQuery("INSERT INTO custom_fields (custom_type, custom_value) VALUES (?, ?)", new Object[]{"employee_dept", "IT"});
        Database.executeQuery("INSERT INTO custom_fields (custom_type, custom_value) VALUES (?, ?)", new Object[]{"employee_dept", "Contractor"});
        Database.executeQuery("INSERT INTO custom_fields (custom_type, custom_value) VALUES (?, ?)", new Object[]{"asset_type", "Production Machine"});
        Database.executeQuery("INSERT INTO custom_fields (custom_type, custom_value) VALUES (?, ?)", new Object[]{"schedule_type", "General"});
        Database.executeQuery("INSERT INTO custom_fields (custom_type, custom_value) VALUES (?, ?)", new Object[]{"schedule_type", "Inspection"});
        Database.executeQuery("INSERT INTO custom_fields (custom_type, custom_value) VALUES (?, ?)", new Object[]{"schedule_type", "Shutdown Maintenance"});
        Database.executeQuery("INSERT INTO custom_fields (custom_type, custom_value) VALUES (?, ?)", new Object[]{"schedule_type", "Building Maintenance"});
        Database.executeQuery("INSERT INTO custom_fields (custom_type, custom_value) VALUES (?, ?)", new Object[]{"schedule_type", "Safety"});
        Database.executeQuery("INSERT INTO custom_fields (custom_type, custom_value) VALUES (?, ?)", new Object[]{"wo_type", "Shutdown"});
        
        System.out.println("[DATABASE] Database tables created");
    }
    
    //QUERY METHODS
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
            Logger.getLogger(LocationFrame.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(LocationFrame.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(LocationFrame.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(LocationFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
    
}
