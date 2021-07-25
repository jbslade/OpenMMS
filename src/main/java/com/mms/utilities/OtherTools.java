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
package com.mms.utilities;

import java.awt.Component;
import java.awt.event.MouseListener;
import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

/**
 *
 * @author J.B. Slade
 */
public class OtherTools {
    
    public static String escapeHTML(String s){
        while(s.contains(">") || s.contains("<")){
            s = s.substring(s.indexOf(">")+1, s.lastIndexOf("<"));
        }
        return s;
    }
    
    public static void disablePanel(JPanel panel) {
        panel.setEnabled(false);
        Component[] components = panel.getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                disablePanel((JPanel)component);
            }
            else if(component instanceof JScrollPane){
               ((JScrollPane)component).getViewport().getView().setEnabled(false);
            }
            else component.setEnabled(false);
        }
    }
    
    public static void setComboBoxReadOnly(JComboBox jcb){
       jcb.setFocusable(false);
       ((JTextField)jcb.getEditor().getEditorComponent()).setEditable(false);

       MouseListener[] mls = jcb.getMouseListeners();
       for (MouseListener listener : mls)
          jcb.removeMouseListener(listener);

       Component[] comps = jcb.getComponents();
       for (Component c : comps)
       {
          if (c instanceof AbstractButton)
          {
             AbstractButton ab = (AbstractButton)c;
             ab.setEnabled(false);

             MouseListener[] mls2 = ab.getMouseListeners();
             for (MouseListener listener : mls2)
                ab.removeMouseListener(listener);
          }
       }
    }
}
