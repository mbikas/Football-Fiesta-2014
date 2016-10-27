/*
 * PointTableScreen.java
 *
 * © <your company here>, <year>
 * Confidential and proprietary.
 */

package com.source;

import net.rim.device.api.ui.*;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import java.io.IOException;
import java.io.EOFException;
import java.io.InputStream;
import net.rim.device.api.util.StringUtilities;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Background;

import com.utils.*;
import com.api.ui.component.*;
import com.api.ui.custom.*;
import net.rim.device.api.util.SimpleSortingVector;
import net.rim.device.api.util.Comparator;
import java.util.Vector;

import com.io.*;
/*
@author Bikas
@Date 30-01-2014
*/


public class PointTableScreen extends MyMainScreen implements FieldChangeListener, ResponseListener
{
        private ScreenManager screenManager;
        public PointTableScreen thisClass;
        private int screenIndex = ScreenManager.POINT_TABLE_SCREEN;
        
        public PointTableScreen()
        {
            super();
            screenManager = ScreenManager.getInstance();
            thisClass = this;
            if(Utils.POINTTABLE_UPDATED)
                initialize(false);
            else
                initialize(true);
        }   
        
        private static PointTableScreen pointTableScreen = null;
        synchronized public static PointTableScreen getInstances()
        {
            if (pointTableScreen == null)
                pointTableScreen = new PointTableScreen();
            return pointTableScreen;
        }
        
        public void pushTheScreen(MainScreen previousScreen, int previousScreenIndex)
        {
            if (screenManager.mainScreenArray[screenIndex] == null)
                screenManager.mainScreenArray[screenIndex] = new PointTableScreen();
            screenManager.previousScreenArray[screenIndex].push(previousScreenIndex);
            UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[screenIndex]);            
            previousScreen.close();
        }        
        VerticalScrollManager vfmMain;        
        
        public void initialize(boolean showLoadingMessage)
        {
            this.deleteAll();  
            
            VerticalFieldManager vfmBackground = Utils.getMainVerticalManager(Utils.displayWidth, Utils.displayHeight);
            HorizontalFieldManager imageManager = Utils.getTopImageAndButtonManager("Point Table");            
            vfmBackground.add(imageManager);
            
            VerticalFieldManager vfmBody = new VerticalFieldManager();
            vfmBody.setBackground(Utils.BACKGROUNG_WITH_IMAGE);
            vfmMain = new VerticalScrollManager(Utils.displayWidth, Utils.BODY_HEIGHT);
            vfmMain.add(new SpacerField(Utils.displayWidth, Utils.headerSpacerHeight, Utils.headerSpacerColor));
            
            setColWidth();
            
            if(showLoadingMessage)
            {
                HorizontalFieldManager hfm11 = new HorizontalFieldManager();
                CustomLabelField lblLoading = new CustomLabelField("Please Wait...Loading Point Table", Utils.fontHeightText, Color.RED, DrawStyle.HCENTER);
                lblLoading.setFont(Utils.getNormalFont(Utils.fontHeightText));
                hfm11.add(lblLoading);
                hfm11.setMargin(0,0,0,10);
                vfmMain.add(hfm11);
            }
            
            for(int i=0;i<Utils.pointTableVector.size();i++)
            {
                //Group1 group= (Group1) Utils.groupVector.elementAt(i);
                PointTable pointTable = (PointTable)Utils.pointTableVector.elementAt(i);
                if(i % 4 == 0)
                {
                    VerticalFieldManager vfm = TeamScreen.getSubHeaderManager("Group " + pointTable.groupName);
                    vfmMain.add(vfm);
                    vfmMain.add(getPointTableManager(null, fontHeightMatchTeam, Utils.colorTeamName, "M","W","D","L","GF","GA","GD","Pts"));
                    vfmMain.add(new SpacerField(Utils.displayWidth-10, 2, Utils.colorSpacer));
                }
                Team1 team = Utils.getTeamNameFromId(pointTable.teamId);
                if(pointTable == null)
                    vfmMain.add(getPointTableManager(team, fontHeightMatchText, Utils.colorText, "0","0","0","0","0","0","0","0"));
                else
                    vfmMain.add(getPointTableManager(team, fontHeightMatchText, Utils.colorText, pointTable.MP+"",pointTable.W+"",pointTable.D+"",pointTable.L+"",pointTable.GF+"",pointTable.GA+"",(pointTable.GF-pointTable.GA)+"",pointTable.Pts+""));
                vfmMain.add(new SpacerField(Utils.displayWidth-10, 1, Utils.colorSpacer));
            }
            
            /*
            for(int i=0;i<Utils.groupVector.size();i++)
            {
                Group1 group= (Group1) Utils.groupVector.elementAt(i);
                VerticalFieldManager vfm = TeamScreen.getSubHeaderManager("Group " + group.groupName);
                vfmMain.add(vfm);
                vfmMain.add(getPointTableManager(null, fontHeightMatchTeam, Utils.colorTeamName, "M","W","D","L","GF","GA","Pts"));
                vfmMain.add(new SpacerField(Utils.displayWidth-10, 2, Utils.colorSpacer));
                for(int j=0;j<4;j++)
                {
                    Team1 team = Utils.getTeamNameFromId(group.teamNames[j]);
                    //if(showLoadingMessage)
                    //    vfmMain.add(getPointTableManager(team, fontHeightMatchText, Utils.colorText, "0","0","0","0","0","0","0"));
                    //else
                    {
                        PointTable pointTable = getTeamsByGroupAndId(group.groupName, group.teamNames[j]);
                        if(pointTable == null)
                            vfmMain.add(getPointTableManager(team, fontHeightMatchText, Utils.colorText, "0","0","0","0","0","0","0"));
                        else
                            vfmMain.add(getPointTableManager(team, fontHeightMatchText, Utils.colorText, pointTable.MP+"",pointTable.W+"",pointTable.D+"",pointTable.L+"",pointTable.GF+"",pointTable.GA+"",pointTable.Pts+""));
                    }  
                    vfmMain.add(new SpacerField(Utils.displayWidth-10, 1, Utils.colorSpacer));
                }                
            }   
            */
                      
            vfmBody.add(vfmMain);
            vfmBackground.add(vfmBody);
            this.add(vfmBackground);
        }
        
        private PointTable getTeamsByGroupAndId(String groupName, String teamId)
        {
            for(int i=0;i<Utils.pointTableVector.size();i++)
            {
                PointTable pointTable = (PointTable)Utils.pointTableVector.elementAt(i);
                if(pointTable.groupName.equalsIgnoreCase(groupName) == true &&
                   pointTable.teamId.equalsIgnoreCase(teamId) == true )
                    return pointTable;
            }
            return null;
        }
        
        public int fontHeightMatchTeam = Utils.fontHeightText+3;
        public int fontHeightMatchText = Utils.fontHeightText+1;
        public int colWidths[];
        public void setColWidth()
        {
            int usedWidth = 0;
            int stringLengthPoint, stringLengthTeamName;
            while (true)
            {
                stringLengthPoint = Utils.getStringLength("GAX", Utils.getNormalFont(fontHeightMatchText));
                stringLengthTeamName = Utils.getStringLength("Côte d'IvoireX", Utils.getBoldFont(fontHeightMatchTeam));
                usedWidth = stringLengthTeamName +  (8*stringLengthPoint);
                usedWidth += 35;//flag
                usedWidth += 10;//right margin
                if(usedWidth <= Utils.displayWidth)break;
                fontHeightMatchTeam--;
                fontHeightMatchText--;
            }
            
            
            int leftWidth = Utils.displayWidth - (8 * stringLengthPoint + 10);
            colWidths = new int [9];
            colWidths[0] = leftWidth;
            colWidths[1] = stringLengthPoint;
            colWidths[2] = stringLengthPoint;
            colWidths[3] = stringLengthPoint;
            colWidths[4] = stringLengthPoint;
            colWidths[5] = stringLengthPoint;
            colWidths[6] = stringLengthPoint; 
            colWidths[7] = stringLengthPoint;   
            colWidths[8] = stringLengthPoint;
        }  
        
        public CustomGridFieldManager getPointTableManager(Team1 team, int fontHeightText, int fontColor, String m, String w, String d, String l, String gf, String ga, String gd, String pts)
        {
            CustomLabelField lblM = new CustomLabelField(m, fontHeightText, fontColor);
            lblM.setFont(Utils.getNormalFont(fontHeightText));
            CustomLabelField lblW = new CustomLabelField(w, fontHeightText, fontColor);
            lblW.setFont(Utils.getNormalFont(fontHeightText));
            CustomLabelField lblD = new CustomLabelField(d, fontHeightText, fontColor);
            lblD.setFont(Utils.getNormalFont(fontHeightText));
            CustomLabelField lblL = new CustomLabelField(l, fontHeightText, fontColor);
            lblL.setFont(Utils.getNormalFont(fontHeightText));
            CustomLabelField lblGF = new CustomLabelField(gf, fontHeightText, fontColor);
            lblGF.setFont(Utils.getNormalFont(fontHeightText));
            CustomLabelField lblGA = new CustomLabelField(ga, fontHeightText, fontColor);
            lblGA.setFont(Utils.getNormalFont(fontHeightText));
            
            CustomLabelField lblGD = new CustomLabelField(gd, fontHeightText, fontColor);
            lblGD.setFont(Utils.getNormalFont(fontHeightText));
            
            CustomLabelField lblPts = new CustomLabelField(pts, fontHeightText, fontColor);
            lblPts.setFont(Utils.getNormalFont(fontHeightText));
            
            HorizontalFieldManager hfm = new HorizontalFieldManager();
            if(team == null)
            {
                CustomLabelField lblCountry = new CustomLabelField("Team", fontHeightText, fontColor, DrawStyle.HCENTER);
                lblCountry.setFont(Utils.getBoldFont(fontHeightText));
                hfm.add(lblCountry);
                hfm.setMargin(0,0,0,20);
            }
            else
            {
                long style =  RichTextField.USE_TEXT_WIDTH | DrawStyle.HCENTER | RichTextField.FOCUSABLE;
                //CustomRichTextField lblCountry = new CustomRichTextField(team.name, fontColor, style);
                //lblCountry.setFont(Utils.getBoldFont(fontHeightMatchTeam));
                //lblCountry.setMargin(0,0,0,5);
                
                HyperlinkButtonField btnCountry = new HyperlinkButtonField(team.name, Utils.colorTeamName, Utils.colorTextFocus, Utils.bgColor, 0, 0);
                btnCountry.setFont(Utils.getBoldFont(fontHeightMatchTeam));
                btnCountry.setChangeListener(thisClass);
                btnCountry.setButtonName(team.id);
                btnCountry.setMargin(0,0,0,5);
                
                BitmapField imageField = new BitmapField(Bitmap.getBitmapResource(team.flagImageNameSmall));
                hfm.add(imageField);
                hfm.add(btnCountry);
                hfm.setMargin(0,0,0,10);
            }            
            
            CustomGridFieldManager grid = new CustomGridFieldManager(colWidths, 0);
            grid.add(hfm);
            grid.add(lblM);
            grid.add(lblW);
            grid.add(lblD);            
            grid.add(lblL);
            grid.add(lblGF);
            grid.add(lblGA);
            grid.add(lblGD);
            grid.add(lblPts);
            return grid;            
        }   
        
        protected void makeMenu(Menu menu, int instance)
        {
            //menu.add(reloadMenu);
            menu.add(backMenu);
            menu.add(increaseFontMenu);
            menu.add(decreaseFontMenu);
        }
        private MenuItem reloadMenu = new MenuItem("Reload Table", 100, 10)
        {
            public void run()
            {
                initialize(true);
                callPointTableFromServer();
                
            }
        };
                
        private MenuItem backMenu = new MenuItem(Utils.BACK_TEXT, 100, 10)
        {
            public void run()
            {
                backToPreviousScreen();
            }
        };
        private MenuItem increaseFontMenu = new MenuItem("Increase Font", 110, 10)
        {
                public void run()
                {
                    Utils.increaseFont();
                    initialize(false);
                }
        };
        
        private MenuItem decreaseFontMenu = new MenuItem("Decrease Font", 120, 10)
        {
                public void run()
                {
                    Utils.decreaseFont();
                    initialize(false);
                }
        };
        private void backToPreviousScreen()
        {
            Utils.backToPreviousScreen(screenIndex, thisClass);
        }
        
        public boolean onClose()
        {
            backToPreviousScreen();
            return true;
        }
        private void closeScreen()
        {
            this.close();
        }
        
        public void fieldChanged(Field field, int context)
        {
            String text = Utils.getButtonText(field);
            int buttonId = Utils.getButtonId(field); 
            
            Team1 team = Utils.getTeamNameFromId(text);                
            int nextIndex = ScreenManager.TEAM_DETAILS_SCREEN;
            if (screenManager.mainScreenArray[nextIndex] == null)
                screenManager.mainScreenArray[nextIndex] = new TeamDetailScreen();                
            TeamDetailScreen screen = (TeamDetailScreen)screenManager.mainScreenArray[nextIndex];
            screen.setTeam(team);
            screen.initialize();
            screenManager.previousScreenArray[nextIndex].push(screenIndex);
            UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[nextIndex]); 
            closeScreen();
        }
        
        private int previousWidth, previousHeight;
        public void checkOrientation()
        {
            if(previousWidth != Utils.displayWidth || previousHeight != Utils.displayHeight)
                initialize(false);
            previousWidth = Utils.displayWidth;
            previousHeight = Utils.displayHeight;
        }
        private static int POINT_TABLE_REQUEST_ID = 123456;
        public void callPointTableFromServer()
        {
            
            String[] keys = {""};
            String[] values = {""};
            HttpRequestGet getRequest = new HttpRequestGet(this, Utils.SERVER_URL + "pointTable.php", null, null, POINT_TABLE_REQUEST_ID, null, false, "Please Wait...Loading PointTable"); 
        }
        
        public void getPointTableFromFile()
        {
            Parser parser = new Parser();
            parser.parsePointTableFromFile();
            Utils.POINTTABLE_UPDATED = true;
        }
        
        //Vector poinTableVector = new Vector();
        
        public void responseReceived(final boolean isSuccessful, final String resultString,
                        final int responseId, Object object)
        {
            UiApplication.getUiApplication().invokeLater(new Runnable()
            {
                public void run()
                {
                    if (responseId == POINT_TABLE_REQUEST_ID)
                    {
                        try
                        {
                            String output = resultString.trim();
                            //UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
                            if (isSuccessful)
                            {
                               // Dialog.alert(output);
                                if(Utils.DEBUG)
                                    System.out.println(output);    
                                Parser.parsePointTableResposne(output);
                                initialize(false);
                                Utils.POINTTABLE_UPDATED = true;
                            }
                            else
                            {
                                if(Utils.DEBUG) System.out.println("ERROR: Please check your network connectivity.");
                                Dialog.alert("ERROR: Please check your network connectivity.");
                                initialize(false);
                            }
                        }
                        catch (Exception ex)
                        {
                            if(Utils.DEBUG) System.out.println("EXCEPTION");                         
                        }   
                    }
                }
            });
                
        }

}


