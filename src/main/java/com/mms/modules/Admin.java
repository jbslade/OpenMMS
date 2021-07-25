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
package com.mms.modules;

import com.mms.MMS;
import com.mms.iframes.PasswordFrame;
import com.mms.iframes.UserFrame;
import javax.swing.JTable;

/**
 *
 * @author J.B. Slade
 */
public class Admin {
    
    private final JTable userTable;
    
    public Admin(JTable u){
        userTable = u;
    }
    
    public void loadUsers(){
        
    }
    public void addUser(){
        UserFrame u = new UserFrame(userTable);
        u.setSize(MMS.DIAG_WIDTH, u.getHeight());
        u.setLocation(MMS.getMainFrame().getDesktopPane().getWidth()/2-u.getWidth()/2, MMS.getMainFrame().getDesktopPane().getHeight()/2-u.getHeight()/2-50);
        MMS.getMainFrame().getDesktopPane().add(u);
        MMS.getMainFrame().getDesktopPane().setLayer(u, 1);
        u.setVisible(true);
    }
    public void deleteUser(){
        
    }
    public void resetUserPass(){
        PasswordFrame pd = new PasswordFrame(userTable.getValueAt(userTable.getSelectedRow(), 0).toString());
        pd.setSize(MMS.DIAG_WIDTH, pd.getHeight());
        pd.setLocation(MMS.getMainFrame().getDesktopPane().getWidth()/2-pd.getWidth()/2, MMS.getMainFrame().getDesktopPane().getHeight()/2-pd.getHeight()/2-50);
        MMS.getMainFrame().getDesktopPane().add(pd);
        MMS.getMainFrame().getDesktopPane().setLayer(pd, 1);
        pd.setVisible(true);
    }
    public void editUser(){
        
    }
    
}
