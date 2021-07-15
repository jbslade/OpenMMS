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

import java.awt.AWTEvent;
import java.awt.ActiveEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.MenuComponent;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author J.B. Slade
 */
public class InternalDialog {
    
    public static int showInternalConfirmDialog(Component parentComponent,
                                               Object message, String title,
                                               int optionType, int messageType, ImageIcon icon)
   {
     JOptionPane pane = new JOptionPane(message, messageType, optionType);
     JInternalFrame frame = pane.createInternalFrame(parentComponent, title);
     frame.setFrameIcon(icon);
     frame.setVisible(true);
     frame.requestFocus();
     
     startModal(frame);
 
     if (pane.getValue() instanceof Integer)
       return ((Integer) pane.getValue()).intValue();
     return -1;
   }
    
    private static void startModal(JInternalFrame f){
      // We need to add an additional glasspane-like component directly
      // below the frame, which intercepts all mouse events that are not
      // directed at the frame itself.
      JPanel modalInterceptor = new JPanel();
      modalInterceptor.setOpaque(false);
      JLayeredPane lp = JLayeredPane.getLayeredPaneAbove(f);
      lp.setLayer(modalInterceptor, JLayeredPane.MODAL_LAYER.intValue());
      modalInterceptor.setBounds(0, 0, lp.getWidth(), lp.getHeight());
      modalInterceptor.addMouseListener(new MouseAdapter(){});
      modalInterceptor.addMouseMotionListener(new MouseMotionAdapter(){});
      lp.add(modalInterceptor);
      f.toFront();

      // We need to explicitly dispatch events when we are blocking the event
      // dispatch thread.
      EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
      try
        {
          while (! f.isClosed())
            {
              if (EventQueue.isDispatchThread())
                {
                  // The getNextEventMethod() issues wait() when no
                  // event is available, so we don't need do explicitly wait().
                  AWTEvent ev = queue.getNextEvent();
                  // This mimics EventQueue.dispatchEvent(). We can't use
                  // EventQueue.dispatchEvent() directly, because it is
                  // protected, unfortunately.
                  if (ev instanceof ActiveEvent)
                    ((ActiveEvent) ev).dispatch();
                  else if (ev.getSource() instanceof Component)
                    ((Component) ev.getSource()).dispatchEvent(ev);
                  else if (ev.getSource() instanceof MenuComponent)
                    ((MenuComponent) ev.getSource()).dispatchEvent(ev);
                  // Other events are ignored as per spec in
                  // EventQueue.dispatchEvent
                }
              else
                {
                  // Give other threads a chance to become active.
                  Thread.yield();
                }
            }
        }
      catch (InterruptedException ex)
        {
          // If we get interrupted, then leave the modal state.
        }
      finally
        {
          // Clean up the modal interceptor.
          lp.remove(modalInterceptor);

          // Remove the internal frame from its parent, so it is no longer
          // lurking around and clogging memory.
          Container parent = f.getParent();
          if (parent != null)
            parent.remove(f);
        }
    }
}
