/*
 * TeamScreen.java
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


public class TeamScreen extends MyMainScreen implements FieldChangeListener
{

        private ScreenManager screenManager;
        public TeamScreen thisClass;
        private int screenIndex = ScreenManager.TEAM_SCREEN;
        
        public TeamScreen()
        {
            super();
            screenManager = ScreenManager.getInstance();
            thisClass = this;
            initialize();
        }   
        
        private static TeamScreen teamScreen = null;
        synchronized public static TeamScreen getInstances()
        {
            if (teamScreen == null)
                teamScreen = new TeamScreen();
            return teamScreen;
        }
        
        public void pushTheScreen(MainScreen previousScreen, int previousScreenIndex)
        {
            if (screenManager.mainScreenArray[screenIndex] == null)
                screenManager.mainScreenArray[screenIndex] = new TeamScreen();
            screenManager.previousScreenArray[screenIndex].push(previousScreenIndex);
            UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[screenIndex]);            
            previousScreen.close();
        }
        
        VerticalScrollManager vfmMain;
        String headerText = "";
        public void initialize()
        {
            this.deleteAll();  
            
            if(type == BUTTON_ID_NAME)
                headerText = "Teams By Name";
            if(type == BUTTON_ID_RANK)
                headerText = "Teams By Rank";
            if(type == BUTTON_ID_GROUP)
                headerText = "Teams By Group";
            
            
            int groups = Utils.groupVector.size();
                                       
            VerticalFieldManager vfmBackground = Utils.getMainVerticalManager(Utils.displayWidth, Utils.displayHeight);
            HorizontalFieldManager imageManager = Utils.getTopImageAndButtonManager(headerText);            
            vfmBackground.add(imageManager);
            
            VerticalFieldManager vfmBody = new VerticalFieldManager();
            vfmBody.setBackground(Utils.BACKGROUNG_WITH_IMAGE);
            vfmMain = new VerticalScrollManager(Utils.displayWidth, Utils.BODY_HEIGHT);
            vfmMain.add(new SpacerField(Utils.displayWidth, Utils.headerSpacerHeight, Utils.headerSpacerColor));
            
            vfmMain.add(new SpacerField(Utils.displayWidth-5, 1, Utils.colorSpacer));
            vfmMain.add(getButtonManager(this.type));
            vfmMain.add(new SpacerField(Utils.displayWidth-5, 1, Utils.colorSpacer));
            vfmMain.add(new TranslatorField(Utils.displayWidth, 5));
            
            
            if(type == BUTTON_ID_GROUP)
            {
                for(int i=0;i<groups;i++)
                {
                    Group1 group= (Group1) Utils.groupVector.elementAt(i);
                    VerticalFieldManager vfm = getSubHeaderManager("Group " + group.groupName);
                    vfmMain.add(vfm);
                    for(int j=0;j<4;j+=2)
                    {
                        Team1 team1 = Utils.getTeamNameFromId(group.teamNames[j]);                                    
                        VerticalFieldManager hfmLeft = getGroupTeamManager(team1.name, team1, Utils.displayWidth/2, this);
                                            
                        Team1 team2 = Utils.getTeamNameFromId(group.teamNames[j+1]);                                    
                        VerticalFieldManager hfmRight = getGroupTeamManager(team2.name, team2, Utils.displayWidth/2, this);
                        
                        //int leftGap = Utils.displayWidth/2 - hfmRight.getPreferredWidth() - 5;
                        //hfmRight.setMargin(0,0,0,leftGap);
                        int colWidths[]={Utils.displayWidth/2,Utils.displayWidth/2-10};
                        CustomGridFieldManager grid = new CustomGridFieldManager(colWidths, 0);
                        
                        //HorizontalFieldManager hfm = new HorizontalFieldManager();
                        grid.add(hfmLeft);
                        grid.add(hfmRight);
                        grid.setMargin(0,0,0,10);
                        vfmMain.add(new TranslatorField(Utils.displayWidth,2));  
                        vfmMain.add(grid);    
                        vfmMain.add(new SpacerField(Utils.displayWidth-10, 1, Color.BLACK));                
                    }
                    vfmMain.add(new TranslatorField(Utils.displayWidth,5));                  
                } 
            }
            else
            {   
                sortTeams(this.type);
                //vfmMain.add(getSubHeaderManager(title));
                for(int i=0;i<sortingVector.size();i++)
                {
                    Team1 team = (Team1)sortingVector.elementAt(i);
                                    
                    HorizontalFieldManager hfmTeam = getHorizontalManager(team.name, team, Utils.displayWidth, type,  this);
                    vfmMain.add(new TranslatorField(Utils.displayWidth,3)); 
                    vfmMain.add(hfmTeam);
                    vfmMain.add(new TranslatorField(Utils.displayWidth,3)); 
                    vfmMain.add(new SpacerField(Utils.displayWidth-10, 1, Color.BLACK));   
                }
            }   
            
            vfmBody.add(vfmMain);
            vfmBackground.add(vfmBody);
            this.add(vfmBackground);
        }  
        
        public VerticalFieldManager getGroupTeamManager(String name, Team1 team, int managerWidth, FieldChangeListener listener)
        {
            
            VerticalFieldManager vfm = new VerticalFieldManager();
            
            if(type == BUTTON_ID_GROUP && team.id.equalsIgnoreCase("BIH") == true)
                name = "Bosnia";
            /*
            else if(type == BUTTON_ID_GROUP && team.id.equalsIgnoreCase("KOR") == true)
                name = "Korea";
            */
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
        
        public static final int BUTTON_ID_GROUP =10021;
        public static final int BUTTON_ID_RANK =10031;
        public static final int BUTTON_ID_NAME =10041;
        
        private int type = BUTTON_ID_GROUP;
        private HorizontalFieldManager getButtonManager(int type)
        {   
            
            
            int fontHeight = Utils.fontHeightText;
            VerticalFieldManager vfm1, vfm2, vfm3;
            int usedWidth = 0;
            CustomLabelField lbl;
            while (true)
            {
                lbl = new CustomLabelField("Sort By : ", fontHeight, Utils.colorText);
                lbl.setFont(Utils.getBoldFont(fontHeight));
                
                vfm1 = getHyperLinkButton("GROUP", BUTTON_ID_GROUP, fontHeight);
                vfm2 = getHyperLinkButton("RANK", BUTTON_ID_RANK, fontHeight);
                vfm3 = getHyperLinkButton("NAME", BUTTON_ID_NAME, fontHeight);
                
                usedWidth = lbl.getPreferredWidth()+ vfm1.getPreferredWidth()+vfm2.getPreferredWidth() + vfm3.getPreferredWidth()+10;
                if(usedWidth <= (Utils.displayWidth -15))break;
                fontHeight--;
            }
            
            HorizontalFieldManager hfmButton = new HorizontalFieldManager();
            hfmButton.add(lbl);
            hfmButton.add(vfm1);hfmButton.add(vfm2);hfmButton.add(vfm3);
            
            //int usedWidth = lbl.getPreferredWidth()+ vfm1.getPreferredWidth()+vfm2.getPreferredWidth() + vfm3.getPreferredWidth()+10;
            hfmButton.setMargin(0,0,0,Utils.displayWidth - usedWidth);
            return hfmButton;            
        }
        
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
        
             
        
        public static HorizontalFieldManager getHorizontalManager(String name, Team1 team, int managerWidth, int type, FieldChangeListener listener)
        {
            return getHorizontalManager(name, team, managerWidth, type, true, listener);
        }
        
        public static HorizontalFieldManager getHorizontalManager(String name, Team1 team, int managerWidth, int type, boolean isRankDisplyed, FieldChangeListener listener)
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
            
            if(type == BUTTON_ID_GROUP && team.id.equalsIgnoreCase("BIH") == true)
                name = "Bosnia";
            else if(type == BUTTON_ID_GROUP && team.id.equalsIgnoreCase("KOR") == true)
                name = "Korea";
            
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
            if(isRankDisplyed)
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
        String title = "Teams (By Rank)";
        String btn1Name = "Teams By Group";
        String btn2Name = "Teams By Name";
        
        public void sortTeams(int type1)
        {
            sortingVector.removeAllElements();
            this.type = type1;   
            if(type == BUTTON_ID_RANK)
            {
                title = "Teams (By Rank)";
                btn1Name = "Teams By Group";
                btn2Name = "Teams By Name";
            }
            else if(type == BUTTON_ID_NAME)
            {
                title = "Teams (By Name)";
                btn1Name = "Teams By Group";
                btn2Name = "Teams By Rank";
            }
            
            int size = Utils.teamVector.size();
            sortingVector.setSortComparator(new Comparator() 
            {
                public int compare(Object o1, Object o2) { 
                
                    Team1 team1 = (Team1)o1;
                    Team1 team2 = (Team1)o2;
                                        
                    if(type == BUTTON_ID_RANK)
                    {
                        return team1.rank - team2.rank;
                    }
                    else
                    {    
                        String name1 = team1.name;
                        String name2 = team2.name;
                         
                        int result = name1.compareTo(name2);
                        if(result < 0)          return -1;
                        else if(result == 0)    return 0;
                        else                    return 1;
                    }
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
            
            if(buttonId == BUTTON_ID_RANK || buttonId == BUTTON_ID_NAME || buttonId == BUTTON_ID_GROUP)
            {
                this.type = buttonId;
                initialize();
            }
            else
            {
                Team1 team = Utils.getTeamNameFromId(text);                
                int nextIndex = ScreenManager.TEAM_DETAILS_SCREEN;
                if (screenManager.mainScreenArray[nextIndex] == null)
                    screenManager.mainScreenArray[nextIndex] = new TeamDetailScreen();                
                TeamDetailScreen screen = (TeamDetailScreen)screenManager.mainScreenArray[nextIndex];
                screen.setTeam(team);
                screen.initialize();
                
                //screen.callMatchScoresFromServer();
                
                screenManager.previousScreenArray[nextIndex].push(screenIndex);
                UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[nextIndex]); 
                closeScreen();
            }
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


