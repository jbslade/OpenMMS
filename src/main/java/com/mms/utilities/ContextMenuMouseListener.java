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

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;

/**
 *
 * @author J.B. Slade
 */
public class ContextMenuMouseListener extends MouseAdapter {
    private final JPopupMenu popup = new JPopupMenu();

    private final Action cutAction;
    private final Action copyAction;
    private final Action pasteAction;
    private final Action undoAction;
    private final Action selectAllAction;

    private JTextComponent textComponent;
    private String savedString = "";
    private Actions lastActionSelected;

    private enum Actions { UNDO, CUT, COPY, PASTE, SELECT_ALL };

    public JPopupMenu getPopupMenu(){return popup;}
    
    public ContextMenuMouseListener(){
        undoAction = new AbstractAction("Undo"){
            @Override
            public void actionPerformed(ActionEvent ae){
                    textComponent.setText("");
                    textComponent.replaceSelection(savedString);

                    lastActionSelected = Actions.UNDO;
            }
        };

        popup.add(undoAction);
        popup.addSeparator();

        cutAction = new AbstractAction("Cut"){
            @Override
            public void actionPerformed(ActionEvent ae){
                lastActionSelected = Actions.CUT;
                savedString = textComponent.getText();
                textComponent.cut();
            }
        };

        popup.add(cutAction);

        copyAction = new AbstractAction("Copy"){
            @Override
            public void actionPerformed(ActionEvent ae){
                lastActionSelected = Actions.COPY;
                textComponent.copy();
            }
        };

        popup.add(copyAction);

        pasteAction = new AbstractAction("Paste"){
            @Override
            public void actionPerformed(ActionEvent ae){
                lastActionSelected = Actions.PASTE;
                savedString = textComponent.getText();
                textComponent.paste();
            }
        };

        popup.add(pasteAction);
        popup.addSeparator();

        selectAllAction = new AbstractAction("Select All"){
            @Override
            public void actionPerformed(ActionEvent ae){
                lastActionSelected = Actions.SELECT_ALL;
                textComponent.selectAll();
            }
        };

        popup.add(selectAllAction);
    }

    @Override
    public void mouseClicked(MouseEvent e){
        if (e.getModifiers() == InputEvent.BUTTON3_MASK){
            if (!(e.getSource() instanceof JTextComponent)){
                return;
            }

            textComponent = (JTextComponent) e.getSource();
            textComponent.requestFocus();

            boolean enabled = textComponent.isEnabled();
            boolean editable = textComponent.isEditable();
            boolean nonempty = !(textComponent.getText() == null || textComponent.getText().equals(""));
            boolean marked = textComponent.getSelectedText() != null;

            boolean pasteAvailable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null).isDataFlavorSupported(DataFlavor.stringFlavor);

            undoAction.setEnabled(enabled && editable && (lastActionSelected == Actions.CUT || lastActionSelected == Actions.PASTE));
            cutAction.setEnabled(enabled && editable && marked);
            copyAction.setEnabled(enabled && marked);
            pasteAction.setEnabled(enabled && editable && pasteAvailable);
            selectAllAction.setEnabled(enabled && nonempty);

            int nx = e.getX();

            if (nx > 500){
                nx = nx - popup.getSize().width;
            }

            popup.show(e.getComponent(), nx, e.getY() - popup.getSize().height);
        }
    }
}
