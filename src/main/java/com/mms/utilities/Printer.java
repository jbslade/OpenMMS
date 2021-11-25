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

import java.awt.print.*;
import java.awt.*;
import java.util.Scanner;
import javax.swing.JLabel;

/**
 *
 * @author J.B. Slade
 */
public class Printer
    implements Printable {

  private final Object [] data;
  private final Font title, detail, detailI;
  private final boolean prev, prev2;

    public Printer(Object [] o, boolean p, boolean p2) {
        this.prev = p;
        this.prev2 = p2;
        this.data = o;
        title = new Font("Dialog", Font.BOLD, 12);
        detail = new Font("Dialog", Font.PLAIN, 12);
        detailI = new Font("Dialog", Font.ITALIC, 12);
    }
    
    private JLabel label = null;
    private JLabel getLabel() {
        if (label == null) {
            label = new JLabel();
        }
        return label;
    }
    private void drawHtmlString(Graphics g, String html, int x, int y) {
        y = y-12;
        html = "<html>" + html + "</html>";
        g.translate(x, y);
        getLabel().setSize(g.getFontMetrics().stringWidth(html)+5, 15);
        getLabel().setText(html);
        //the fontMetrics stringWidth and height can be replaced by
        //getLabel().getPreferredSize() if needed
        getLabel().paint(g);
        g.translate(-x, -y);
    }
    
  @Override
  public int print(Graphics g, PageFormat pf, int page)
      throws PrinterException {

    if(page > 0) return NO_SUCH_PAGE;  
      
    Graphics2D g2d = (Graphics2D)g;
    g2d.translate(pf.getImageableX(), pf.getImageableY());
    
    //PREVENTATIVE
    if (prev2) {
        //Title
        g.setFont(title);
        g.drawLine(10, 10, 550, 10);
        g.drawString("PREVENTATIVE TASKS", (275) - (g.getFontMetrics().stringWidth("PREVENTATIVE TASKS")/2), 22);
        g.drawLine(10, 25 , 550, 25);
        
        //Detail
        g.setFont(detail);
        String s = data[3].toString(); //Add /n when line overflows
        s = s.replaceAll("<html>", "").replaceAll("</html>", "").replaceAll("<br>", "\n");
        
        String temp = "";
        for(int i = 0; i < s.length(); i++){
            temp += s.charAt(i);
            if((s.charAt(i)+"").equals("\n")){
                temp = "";
            }
            if(g.getFontMetrics().stringWidth(temp) > 510){
                s = s.substring(0, i) + (s.substring(0, i).endsWith(" ") ? "\n" : "-\n") + s.substring(i, s.length());
                temp = "";
            }
        }
        
        int i = 50;
        if(s.contains("\n")){
            try (Scanner sc = new Scanner(s)) {
                sc.useDelimiter("\n");
                while(sc.hasNext()){
                    drawHtmlString(g, sc.next(), 25, i);
                    i += 15;
                }
            }
        }
        else{
            drawHtmlString(g, s, 25, i);
            i+=15;
        }
        
        //Final line
        g.drawLine(10, i, 550, i);
        //Vertical lines
        g.drawLine(10, 10 , 10, i);
        g.drawLine(550, 10 , 550, i);
        
        return PAGE_EXISTS;
    }
    
    //Title
    g.setFont(title);
    g.drawLine(10, 10, 550, 10);
    g.drawString("MMS - WORK ORDER", (275) - (g.getFontMetrics().stringWidth("MMS - WORK ORDER")/2), 22);
    g.drawLine(10, 25 , 550, 25);
    
    //Details
    g.setFont(detail);
    int l = 50, count = 0;
    for (int i = 0; i < data.length; i++) {
        switch(count){
            case 0: g.drawString("No.:", 25, l); break;
            case 1: g.drawString("Date:", 25, l); break;
            case 2: g.drawString("Type:", 25, l); break;
            case 3: if(!prev) g.drawString("Description:", 25, l); break;
            case 4: g.drawString("Priority:", 25, l); break;
            case 5: g.drawString("Asset:", 25, l); break;
            case 6: g.drawString("Location:", 25, l); break;
            case 7: g.drawString("Employee:", 25, l); break;
            case 8: g.drawString("Status:", 25, l); break;
        } count++;
        String s = data[i].toString();
        s = s.replaceAll("<html>", "").replaceAll("</html>", "").replaceAll("<br>", "\n");
        
        //WRAP
        String temp = "";
        for(int i2 = 0; i2 < s.length(); i2++){
            temp += s.charAt(i2);
            if((s.charAt(i2)+"").equals("\n")){
                temp = "";
            }
            if(g.getFontMetrics().stringWidth(temp) > 415){
                s = s.substring(0, i2) + (s.substring(0, i2).endsWith(" ") ? "\n" : "-\n") + s.substring(i2, s.length());
                temp = "";
            }
        }
        
        if(i == 3 && prev){/*DO NOTHING*/}
        else if(s.contains("\n")){
            try (Scanner sc = new Scanner(s)) {
                sc.useDelimiter("\n");
                while(sc.hasNext()){
                    drawHtmlString(g, sc.next(), 120, l);
                    l += 15;
                }
            }
        }
        else drawHtmlString(g, s, 120, l);
        if(i == 3 && prev){/*DO NOTHING*/}
        else if(!s.contains("\n")) l += 15;
    }
    
    //Action
    g.setFont(title);
    g.drawLine(10, l+=5, 550, l);
    g.drawString("ACTION TAKEN", (275) - (g.getFontMetrics().stringWidth("ACTION TAKEN")/2), l+=12);
    g.drawLine(10, l+=3 , 550, l);//385
    //...
    g.drawLine(25, l+=25 , 535, l);
    g.drawLine(25, l+=15 , 535, l);
    
    //Parts
    g.drawLine(10, l+=25, 550, l);
    g.drawString("PARTS USED", (275) - (g.getFontMetrics().stringWidth("PARTS USED")/2), l+=12);
    g.drawLine(10, l+=3 , 550, l);//480
    //...
    g.drawLine(25, l+=25 , 270, l);
    g.drawLine(290, l , 535, l);
    g.drawLine(25, l+=15 , 270, l);
    g.drawLine(290, l , 535, l);
    
    //Dates/Times
    g.drawLine(10, l+=25, 550, l);
    g.drawString("DATES/TIMES", (275) - (g.getFontMetrics().stringWidth("DATES/TIMES")/2), l+=12);
    g.drawLine(10, l+=3 , 550, l);//285
    //...
    g.setFont(detail);
    g.drawLine(25, l+=30 , 200, l);
    g.drawLine(220, l , 395, l);
    g.drawString("Start Date/Time", 25, l+=15);
    g.drawString("Finish Date/Time", 220, l);//345
    
    //Post-maintenance cleaning
    g.setFont(title);
    g.drawLine(10, l+=25, 550, l);
    g.drawString("POST-MAINTENANCE CLEANING", (275) - (g.getFontMetrics().stringWidth("POST-MAINTENANCE CLEANING")/2), l+=12);
    g.drawLine(10, l+=3 , 550, l);
    //...
    g.setFont(detail);
    int i = 25;
    g.drawString("□ Clear work area", i, l+25);
    g.drawString("□ Remove all tools", i+=((g.getFontMetrics().stringWidth("□ Clear work area"))+10), l+25);
    g.drawString("□ Remove all fasteners", i+=((g.getFontMetrics().stringWidth("□ Remove all tools"))+10), l+25);
    g.drawString("□ Clean all grease marks", i+=((g.getFontMetrics().stringWidth("□ Remove all fasteners"))+10), l+=25);
    
    //Post-maintenance cleaning
    g.setFont(title);
    g.drawLine(10, l+=25, 550, l);
    g.drawString("TEMPORARY FIXES", (275) - (g.getFontMetrics().stringWidth("TEMPORARY FIXES")/2), l+=12);
    g.drawLine(10, l+=3 , 550, l);
    //...
    g.setFont(detail);
    i = 25;
    g.drawString("□ Y", i, l+25);
    g.drawString("□ N", i+=((g.getFontMetrics().stringWidth("□ N"))+10), l+25);
    g.drawString("Comments: __________________________________________________", i+=((g.getFontMetrics().stringWidth("           "))+10), l+=25);
    g.drawString("Date Closed: __________________________________________________", i+=((g.getFontMetrics().stringWidth(""))-7), l+=25);
    
    //Signatures
    g.setFont(title);
    g.drawLine(10, l+=25, 550, l);
    g.drawString("CERTIFICATION OF COMPLETION", (275) - (g.getFontMetrics().stringWidth("CERTIFICATION OF COMPLETION")/2), l+=12);
    g.drawLine(10, l+=3 , 550, l);
    //...
    g.setFont(detailI);
    g.drawString("Employee:", 25, l+=25);
    g.drawLine(25, l+=30 , 200, l);//was 40
    g.drawLine(220, l , 395, l);
    g.setFont(detail);
    g.drawString("Name", 25, l+=15);
    g.drawString("Signature", 220, l);
    g.setFont(detailI);
    g.drawString("Supervisor:", 25, l+=30);//was 40
    g.drawLine(25, l+=30 , 200, l);//was 40
    g.drawLine(220, l , 395, l);
    g.setFont(detail);
    g.drawString("Name", 25, l+=15);
    g.drawString("Signature", 220, l);
       
    //Final line
    g.drawLine(10, l+=25, 550, l);
    //Vertical lines
    g.drawLine(10, 10 , 10, l);
    g.drawLine(550, 10 , 550, l);
    
    return PAGE_EXISTS;
  }
}

