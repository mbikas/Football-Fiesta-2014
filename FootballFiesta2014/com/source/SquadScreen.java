/*
 * SquadScreen.java
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

/*
@author Bikas
@Date 01-01-2014
*/


public class SquadScreen extends MyMainScreen implements FieldChangeListener
{

        private ScreenManager screenManager;
        public SquadScreen thisClass;
        private int screenIndex = ScreenManager.SQUAD_SCREEN;
        
        public SquadScreen()
        {
            super();
            screenManager = ScreenManager.getInstance();
            thisClass = this;
            initialize();
        }   
        
        private static SquadScreen squadScreen = null;
        synchronized public static SquadScreen getInstances()
        {
            if (squadScreen == null)
                squadScreen = new SquadScreen();
            return squadScreen;
        }
        
        public void pushTheScreen(MainScreen previousScreen, int previousScreenIndex)
        {
            if (screenManager.mainScreenArray[screenIndex] == null)
                screenManager.mainScreenArray[screenIndex] = new SquadScreen();
            screenManager.previousScreenArray[screenIndex].push(previousScreenIndex);
            UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[screenIndex]);            
            previousScreen.close();
        }
        
        VerticalScrollManager vfmMain;
        String headerText = "";
        public void initialize()
        {
            this.deleteAll();  
            headerText = "Squads -- Select Team";
            int groups = Utils.groupVector.size();
                                       
            VerticalFieldManager vfmBackground = Utils.getMainVerticalManager(Utils.displayWidth, Utils.displayHeight);
            HorizontalFieldManager imageManager = Utils.getTopImageAndButtonManager(headerText);            
            vfmBackground.add(imageManager);
            
            VerticalFieldManager vfmBody = new VerticalFieldManager();
            vfmBody.setBackground(Utils.BACKGROUNG_WITH_IMAGE);
            vfmMain = new VerticalScrollManager(Utils.displayWidth, Utils.BODY_HEIGHT);
            vfmMain.add(new SpacerField(Utils.displayWidth, Utils.headerSpacerHeight, Utils.headerSpacerColor));
            vfmMain.add(new TranslatorField(Utils.displayWidth, 5));
            
            
            sortTeams(this.type);
            //vfmMain.add(getSubHeaderManager(title));
            for(int i=0;i<sortingVector.size();i++)
            {
                Team1 team = (Team1)sortingVector.elementAt(i);
                                
                HorizontalFieldManager hfmTeam = getHorizontalManager(team.name, team, Utils.displayWidth, this);
                vfmMain.add(new TranslatorField(Utils.displayWidth,3)); 
                vfmMain.add(hfmTeam);
                vfmMain.add(new TranslatorField(Utils.displayWidth,3)); 
                vfmMain.add(new SpacerField(Utils.displayWidth-10, 1, Color.BLACK));   
            }         
            
            vfmBody.add(vfmMain);
            vfmBackground.add(vfmBody);
            this.add(vfmBackground);
        }  
        
        public VerticalFieldManager getGroupTeamManager(String name, Team1 team, int managerWidth, FieldChangeListener listener)
        {
            
            VerticalFieldManager vfm = new VerticalFieldManager();
            
            int countryFontSize = Utils.fontHeightText;
            int strLength = Utils.getStringLength(name, Utils.getBoldFont(countryFontSize));
            while(strLength >= managerWidth)
            {
                countryFontSize--;
                strLength = Utils.getStringLength(name, Utils.getBoldFont(countryFontSize));
            }
            
            HyperlinkButtonField btnCountry = new HyperlinkButtonField(name, Utils.colorTeamName, Utils.colorTextFocus, Utils.bgColor, 0, 0);
            btnCountry.setFont(Utils.getBoldFont(countryFontSize));
            btnCountry.setChangeListener(listener);
            btnCountry.setButtonName(team.id);
            //CustomLabelField lblRank = new CustomLabelField(rankText , countryFontSize, Utils.colorText);
            Bitmap flagImage = Bitmap.getBitmapResource(team.flagImageName);
            
            vfm.add(btnCountry);
            //vfm.add(new TranslatorField(managerWidth,1));
            vfm.add(new BitmapField(flagImage));
            vfm.setMargin(0,0,0,10);
            return vfm;            
        } 
        
        public int BUTTON_ID_NAME =10041;
        private int type = BUTTON_ID_NAME;
        private VerticalFieldManager getHyperLinkButton(String text, int buttonID, int fontHeight)
        {
            VerticalFieldManager vfm = new VerticalFieldManager();
            
            if(this.type == buttonID)
            {
                CustomLabelField lbl = new CustomLabelField(text, fontHeight, Utils.colorTextFocus);
                lbl.setFont(Utils.getBoldFont(fontHeight));
                lbl.setMargin(0,0,0,10);
                vfm.add(lbl);
            }
            else
            {
                HyperlinkButtonField btnMatchByGroup = new HyperlinkButtonField(text, Utils.colorText, Utils.colorTextFocus, Utils.bgColor, 0, 0);
                btnMatchByGroup.setButtonId(buttonID);
                btnMatchByGroup.setFont(Utils.getUnderlinedBoldFont(fontHeight));
                btnMatchByGroup.setChangeListener(this);
                btnMatchByGroup.setMargin(0,0,0,10);
                vfm.add(btnMatchByGroup);
            }            
            return vfm;
        }
           
        public static VerticalFieldManager getSubHeaderManager(String title)
        {
            CustomLabelField lbl;
            int fontHeight = Utils.fontHeightText;
            int stringLength = Utils.getStringLength(title, Utils.getBoldFont(fontHeight));
            while(true)
            {
                if(stringLength <= Utils.displayWidth-20)break;
                fontHeight--;
                stringLength = Utils.getStringLength(title, Utils.getBoldFont(fontHeight));
            }
            lbl = new CustomLabelField(title, fontHeight, Utils.colorTextSubHeader);
            lbl.setFont(Utils.getBoldFont(fontHeight));
            lbl.setMargin(3,0,3,10);
            int height = lbl.getPreferredHeight()+6;
            VerticalFieldManager vfm = Utils.getMainVerticalManager(Utils.displayWidth-10, height, Utils.colorGroupBackground);
            vfm.add(lbl);
            return vfm;
        }
        
        
        public HorizontalFieldManager getHorizontalManager(String name, Team1 team, int managerWidth, FieldChangeListener listener)
        {
            managerWidth = managerWidth - 5;//5 for the scroll bar at the right
            int availableWidth = managerWidth;
            
            //Bitmap flagImage = Bitmap.getBitmapResource(team.id+".png");
            Bitmap flagImage = Bitmap.getBitmapResource(team.flagImageName);
            int imageHeight = flagImage.getHeight();
            availableWidth -= flagImage.getWidth();
            availableWidth -= 5; //5 for the left margin
            availableWidth -= 5; //5 for the margin of the text
            
            VerticalFieldManager vfmLeft = new VerticalFieldManager();
            vfmLeft.add(new BitmapField(flagImage));
            
            VerticalFieldManager vfmRight = new VerticalFieldManager();
            
            int countryFontSize = Utils.fontHeightText;
            int strLength = Utils.getStringLength(name, Utils.getBoldFont(countryFontSize));
            while(strLength >= availableWidth)
            {
                countryFontSize--;
                strLength = Utils.getStringLength(name, Utils.getBoldFont(countryFontSize));
            }
            
            HyperlinkButtonField btnCountry = new HyperlinkButtonField(name, Utils.colorTeamName, Utils.colorTextFocus, Utils.bgColor, 0, 0);
            btnCountry.setFont(Utils.getBoldFont(countryFontSize));
            btnCountry.setChangeListener(listener);
            btnCountry.setButtonName(team.id);
                       
            int rankFontSize = countryFontSize;
            String rankText = "Rank - " + team.rank;
            strLength = Utils.getStringLength(rankText, Utils.getNormalFont(rankFontSize));
            while(strLength >= availableWidth)
            {
                rankFontSize--;
                strLength = Utils.getStringLength(rankText, Utils.getNormalFont(rankFontSize));
            }
            CustomLabelField lblRank = new CustomLabelField(rankText , rankFontSize, Utils.colorText);
            
            vfmRight.add(btnCountry);
            vfmRight.add(new TranslatorField(availableWidth,1));
            vfmRight.add(lblRank);
            
            int height = imageHeight;
            if(vfmRight.getPreferredHeight() > height) height = vfmRight.getPreferredHeight();
            height = height+5;
            
            vfmLeft.setMargin((height-vfmLeft.getPreferredHeight())/2,0,0,5);
            vfmRight.setMargin((height-vfmRight.getPreferredHeight())/2,0,0,5);
            
            HorizontalFieldManager hfm = Utils.getFixedHorizontalManager(managerWidth, height);
            hfm.add(vfmLeft);
            hfm.add(vfmRight);
            return hfm;            
        }      
        
        SimpleSortingVector sortingVector = new SimpleSortingVector();
        public void sortTeams(int type1)
        {
            sortingVector.removeAllElements();
            this.type = type1;   
            int size = Utils.teamVector.size();
            sortingVector.setSortComparator(new Comparator() 
            {
                public int compare(Object o1, Object o2) { 
                
                    Team1 team1 = (Team1)o1;
                    Team1 team2 = (Team1)o2;
                                        
                    String name1 = team1.name;
                    String name2 = team2.name;
                        
                    int result = name1.compareTo(name2);
                    if(result < 0)          return -1;
                    else if(result == 0)    return 0;
                    else                    return 1;
                }
            });
            
            for(int i=0;i<Utils.teamVector.size();i++)
            {
                Team1 team = (Team1)Utils.teamVector.elementAt(i);
                sortingVector.addElement(team);
            }        
            sortingVector.reSort();             
        }
        
        protected void makeMenu(Menu menu, int instance)
        {
            menu.add(backMenu);
            menu.add(increaseFontMenu);
            menu.add(decreaseFontMenu);
        }        
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
                    initialize();
                }
        };
        
        private MenuItem decreaseFontMenu = new MenuItem("Decrease Font", 120, 10)
        {
                public void run()
                {
                    Utils.decreaseFont();
                    initialize();
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
            
            Squad squad = Utils.getSquadFromId(text);                
            int nextIndex = ScreenManager.SQUAD_DETAILS_SCREEN;
            if (screenManager.mainScreenArray[nextIndex] == null)
                screenManager.mainScreenArray[nextIndex] = new SquadDetailsScreen();                
            SquadDetailsScreen screen = (SquadDetailsScreen)screenManager.mainScreenArray[nextIndex];
            screen.setSquad(squad);
            screen.initialize();
            screenManager.previousScreenArray[nextIndex].push(screenIndex);
            UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[nextIndex]); 
            closeScreen();
        }
        
        
        private int previousWidth, previousHeight;
        public void checkOrientation()
        {
            if(previousWidth != Utils.displayWidth || previousHeight != Utils.displayHeight)
                initialize();
            previousWidth = Utils.displayWidth;
            previousHeight = Utils.displayHeight;
        }
}


