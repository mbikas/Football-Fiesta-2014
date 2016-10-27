/*
 * MatchScreen.java
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
import net.rim.device.api.i18n.*;
import java.util.*;
import com.api.ui.dropdown.*;

/*
@author Bikas
@Date 08-01-2014
*/


public class MatchScreen extends MyMainScreen implements FieldChangeListener, ResponseListener
{

        private ScreenManager screenManager;
        public MatchScreen thisClass;
        private int screenIndex = ScreenManager.MATCH_SCREEN;
        
        private boolean timerStarted = false;
        
        public MatchScreen()
        {
            super();
            screenManager = ScreenManager.getInstance();
            thisClass = this;
            sortMatches(this.type);
            setColWidth();
            //initializeComboArrays();
            initialize(false);
        }   
        
        private static MatchScreen matchScreen = null;
        synchronized public static MatchScreen getInstances()
        {
            if (matchScreen == null)
                matchScreen = new MatchScreen();
            return matchScreen;
        }
        
        public void pushTheScreen(MainScreen previousScreen, int previousScreenIndex)
        {
            if (screenManager.mainScreenArray[screenIndex] == null)
                screenManager.mainScreenArray[screenIndex] = new MatchScreen();
            screenManager.previousScreenArray[screenIndex].push(previousScreenIndex);
            UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[screenIndex]);            
            previousScreen.close();
        }
        
        VerticalScrollManager vfmMain;
        
        private int BUTTON_ID_GROUP =10021;
        private int BUTTON_ID_DATE =10031;
        private int BUTTON_ID_TEAM =10035;
        private int type = BUTTON_ID_DATE;
        String headerText = "Matches By Date";
        
        public void initialize(boolean showLoadingMessage)
        {
            this.deleteAll();  
            
            if(type == BUTTON_ID_GROUP)
                headerText = "Matches By Round";
            else if(type == BUTTON_ID_DATE)
                headerText = "Matches By Date";
             else if(type == BUTTON_ID_TEAM)
                headerText = "Matches By Team";
            
            VerticalFieldManager vfmBackground = Utils.getMainVerticalManager(Utils.displayWidth, Utils.displayHeight);
            HorizontalFieldManager imageManager = Utils.getTopImageAndButtonManager(headerText);            
            vfmBackground.add(imageManager);
            
            VerticalFieldManager vfmBody = new VerticalFieldManager();
            vfmBody.setBackground(Utils.BACKGROUNG_WITH_IMAGE);
            vfmMain = new VerticalScrollManager(Utils.displayWidth, Utils.BODY_HEIGHT);
            vfmMain.add(new SpacerField(Utils.displayWidth, Utils.headerSpacerHeight, Utils.headerSpacerColor));
            
            if(showLoadingMessage)
            {
                HorizontalFieldManager hfm11 = new HorizontalFieldManager();
                CustomLabelField lblLoading = new CustomLabelField("Please Wait...Loading Scores", Utils.fontHeightText, Color.RED, DrawStyle.HCENTER);
                lblLoading.setFont(Utils.getNormalFont(Utils.fontHeightText));
                hfm11.add(lblLoading);
                hfm11.setMargin(0,0,0,10);
                vfmMain.add(hfm11);
            }
            
            //vfmMain.add(new TranslatorField(Utils.displayWidth, 5));
            vfmMain.add(new SpacerField(Utils.displayWidth-5, 1, Utils.colorSpacer));
            vfmMain.add(getButtonManager(this.type));
            vfmMain.add(new SpacerField(Utils.displayWidth-5, 1, Utils.colorSpacer));
            vfmMain.add(new TranslatorField(Utils.displayWidth, 5));
            
            if(this.type == BUTTON_ID_DATE)
                vfmMain.add(getComboManager(dates, selectedIndex, 1, type));
            else if(this.type == BUTTON_ID_GROUP)
                vfmMain.add(getComboManager(stages, selectedIndex, 2, type));
            else if(this.type == BUTTON_ID_TEAM)
                vfmMain.add(getComboManager(teamNames, selectedIndex, 11, type));
            
            
            
            
            
            CustomLabelField lblTime = new CustomLabelField("Match times are shown in local time", Utils.fontHeightText-2, Utils.colorText);
            lblTime.setFont(Utils.getNormalFont( Utils.fontHeightText-2));
            lblTime.setMargin(0,0,0,10);
            vfmMain.add(lblTime);
            vfmMain.add(new TranslatorField(Utils.displayWidth, 2));
            
            /*
            if(type == BUTTON_ID_ALL)
            {
                vfmMain.add(TeamScreen.getSubHeaderManager("GROUP STAGE MATCHES"));                
                for(int i=0;i<sortingVector.size();i++)
                {   
                    boolean flag = false;
                    Match1 match = (Match1)sortingVector.elementAt(i);
                    if(match.id == 49)
                        vfmMain.add(TeamScreen.getSubHeaderManager("ROUND OF 16"));
                    else if(match.id == 58)
                        vfmMain.add(TeamScreen.getSubHeaderManager("QUARTER-FINALS"));
                    else if(match.id == 61)
                        vfmMain.add(TeamScreen.getSubHeaderManager("SEMI-FINALS"));
                    else if(match.id == 63)
                        vfmMain.add(TeamScreen.getSubHeaderManager("PLAY-OFF FOR THIRD PLACE"));
                    else if(match.id == 64)
                        vfmMain.add(TeamScreen.getSubHeaderManager("FINAL"));
                    vfmMain.add(getMatchManager(match, true, this));
                    vfmMain.add(new SpacerField(Utils.displayWidth-10, 1, Utils.colorSpacer));
                }
            }
            else */ if(type == BUTTON_ID_DATE)
            {
                String predate = "bikas", date = "";
                String selectedDate = dates[selectedIndex];
                for(int i=0;i<sortingVector.size();i++)
                {
                    Match1 match = (Match1)sortingVector.elementAt(i);
                    date = match.date;
                    
                    if(selectedDate.equalsIgnoreCase("All") == false && date.equalsIgnoreCase(selectedDate) == false) 
                        continue;                    
                    if(date.equalsIgnoreCase(predate) == false)
                        vfmMain.add(TeamScreen.getSubHeaderManager("DATE: " + date));
                    predate = date;
                    vfmMain.add(getMatchManager(match, true, thisClass));
                    vfmMain.add(new SpacerField(Utils.displayWidth-10, 1, Utils.colorSpacer));
                }
            }
            else if(type == BUTTON_ID_TEAM)
            {
                String selectedTeam = teamNamesOriginal[selectedIndex];
                vfmMain.add(TeamScreen.getSubHeaderManager(selectedTeam +": "));
                for(int i=0;i<sortingVector.size();i++)
                {
                    Match1 match = (Match1)sortingVector.elementAt(i);
                    if(match.team1Name.equalsIgnoreCase(selectedTeam) == false && match.team2Name.equalsIgnoreCase(selectedTeam) == false)
                        continue;
                    vfmMain.add(getMatchManager(match, true, thisClass));
                    vfmMain.add(new SpacerField(Utils.displayWidth-10, 1, Utils.colorSpacer));
                }
            }
            else if(type == BUTTON_ID_GROUP)
            {
                String selectedGroup = stagesId[selectedIndex];
                vfmMain.add(TeamScreen.getSubHeaderManager(stages[selectedIndex] + " Matches"));
                boolean isGroupDisplay = false;
                
                for(int i=0;i<sortingVector.size();i++)
                {
                    Match1 match = (Match1)sortingVector.elementAt(i);
                    String groupId = match.title;
                    if(groupId == null){
                         groupId = "";
                         isGroupDisplay = true;
                    }
                         
                    if(groupId.equalsIgnoreCase(selectedGroup) == false) 
                        continue;
                    
                    vfmMain.add(getMatchManager(match, isGroupDisplay, thisClass));
                    vfmMain.add(new SpacerField(Utils.displayWidth-10, 1, Utils.colorSpacer));
                }
            }
            vfmBody.add(vfmMain);
            vfmBackground.add(vfmBody);
            this.add(vfmBackground);
        }
        
        DropdownList choiceFieldDate = null;
        String dates[] = { "All", "12/06", "13/06", "14/06", "15/06", "16/06", "17/06", "18/06", "19/06", "20/06", "21/06", "22/06", "23/06", "24/06", "25/06", "26/06", "28/06", "29/06", "30/06", "01/07", "04/07", "05/07", "08/07", "09/07", "12/07", "13/07"};
        String teamNames[] = {"Algeria", "Argentina", "Australia", "Belgium", "Bosnia", "Brazil", "Cameroon", "Chile", "Colombia", "Costa Rica", "Croatia", "Côte d'Ivoire", "Ecuador", "England", "France", "Germany", "Ghana", "Greece", "Honduras", "Iran", "Italy", "Japan", "Korea", "Mexico", "Netherlands", "Nigeria", "Portugal", "Russia", "Spain", "Switzerland", "USA", "Uruguay"};
        String teamNamesOriginal[] = {"Algeria", "Argentina", "Australia", "Belgium", "Bosnia and Herzegovina", "Brazil", "Cameroon", "Chile", "Colombia", "Costa Rica", "Croatia", "Côte d'Ivoire", "Ecuador", "England", "France", "Germany", "Ghana", "Greece", "Honduras", "Iran", "Italy", "Japan", "Korea Republic", "Mexico", "Netherlands", "Nigeria", "Portugal", "Russia", "Spain", "Switzerland", "USA", "Uruguay"};
        String stages[] = {"Group Stage",  "Round of 16", "Quarter Final", "Semi Final", "Third Place", "Final"};
        String stagesId[] = {"",  "round16", "quarter", "semi", "third", "final"};
        
        int selectedIndex = 0;
        
        int buttonFontHeight = Utils.fontHeightButton;
        String comboTitle = "Date: ";
        private static final int BUTTON_ID_UPDATE = 675;
        private HorizontalFieldManager getComboManager(String str[], int selectedIndex, int longestTextIndex, int type)
        {
            CustomLabelField lblDate = new CustomLabelField(comboTitle, Utils.fontHeightText, Utils.colorTextSubHeader);
            lblDate.setFont(Utils.getBoldFont(Utils.fontHeightText));
            lblDate.setMargin(0,0,0,10);
            
            int comboWidth = Utils.getStringLength(str[longestTextIndex], Utils.getBoldFont(Utils.fontHeightText)) + 20;
            if(type == BUTTON_ID_DATE)
                comboWidth += 20;
            
            
            //if(comboWidth < 50) comboWidth = 50;
            choiceFieldDate = getChoiceField(str, selectedIndex, comboWidth, Utils.getNormalFont(Utils.fontHeightText));
            
            CustomButtonField buttonOK = new CustomButtonField(Utils.buttonImageNormnal, Utils.buttonImageOver, null, "Update", false , Utils.getNormalFont(buttonFontHeight));
            buttonOK.setChangeListener(this);
            buttonOK.setButtonId(BUTTON_ID_UPDATE);
                        
            int managerHeight = choiceFieldDate.getPreferredHeight() + 10;
            if(buttonOK.getPreferredHeight() + 10 > managerHeight) managerHeight = buttonOK.getPreferredHeight() + 10;
            if(choiceFieldDate.getPreferredWidth() > comboWidth)comboWidth = choiceFieldDate.getPreferredWidth();
            HorizontalFieldManager buttonManager = new HorizontalFieldManager(HorizontalFieldManager.USE_ALL_WIDTH); 
            VerticalFieldManager vfmCombo = Utils.getMainVerticalManager(comboWidth,managerHeight);
            
            vfmCombo.add(choiceFieldDate);
            buttonManager.add(lblDate);
            buttonManager.add(vfmCombo);
            buttonManager.add(new TranslatorField(10, managerHeight));
            buttonManager.add(buttonOK);
            
            return buttonManager;            
        }
        
        public static DropdownList getChoiceField(String[] choices, int choiceIndex, int comboWidth, Font font)
        {
            DropdownList dropdown = new DropdownList(choices, choiceIndex, Utils.dropdownImageNormal, Utils.dropdownImageOver, Utils.dropdownIcon, comboWidth, font);
            //dropdown.setMargin(0,0, 0, 10);
            return dropdown;
        }  
        
        Timer timer;
        public void startTimer() 
        {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTick(), 0, Utils.RELOAD_TIME);
            timerStarted = true;
        }
        private class TimerTick extends TimerTask 
        {
            public void run() {    
                UiApplication.getUiApplication().invokeLater(new Runnable() {
                    public void run() {
                        callMatchScoresFromServer();                        
                    }
                });
            }
        }  
        
        
        public static int fontHeightMatchTeam = Utils.fontHeightText;
        public static int fontHeightMatchText = Utils.fontHeightText;
        public static int colWidths[];
        public static void setColWidth()
        {
            int usedWidth = 0;
            int stringLengthTime, stringLengthTeamName, stringLengthID;
            while (true)
            {
                stringLengthTime = Utils.getStringLength("Rio De JaneiroX", Utils.getNormalFont(fontHeightMatchText));
                stringLengthTeamName = Utils.getStringLength("Côte d'IvoireX", Utils.getBoldFont(fontHeightMatchTeam));
                usedWidth = stringLengthTime + (2*stringLengthTeamName);
                usedWidth += 10;
                if(usedWidth <= Utils.displayWidth)break;
                fontHeightMatchTeam--;
                fontHeightMatchText--;
            }
            
            int midWidth = Utils.displayWidth - ((2 * stringLengthTeamName) + 10);
            colWidths = new int [3];
            colWidths[0] = stringLengthTeamName;
            colWidths[1] = midWidth;
            colWidths[2] = stringLengthTeamName;    
        }  
        
        public static CustomGridFieldManager getMatchManager(Match1 match, boolean isGroupDisplay, FieldChangeListener listener)
        {
            return getMatchManager(match, colWidths, isGroupDisplay, true, fontHeightMatchTeam, fontHeightMatchText, listener);
        }
        
        public static CustomGridFieldManager getMatchManager(Match1 match, boolean isGroupDisplay, boolean isDetailsDisplay, FieldChangeListener listener)
        {
            return getMatchManager(match, colWidths, isGroupDisplay, isDetailsDisplay, fontHeightMatchTeam, fontHeightMatchText, listener);
        }
        
        public static CustomGridFieldManager getMatchManager(Match1 match, int colWidths[], boolean isGroupDisplay, boolean isDetailsDisplay, int fontHeightMatchTeam, int fontHeightMatchText, FieldChangeListener listener)
        {
            VerticalFieldManager vfmLeft = new VerticalFieldManager();
            VerticalFieldManager vfmMiddle = new VerticalFieldManager(DrawStyle.HCENTER);
            VerticalFieldManager vfmRight = new VerticalFieldManager();
            CustomLabelField lblRank;
            CustomRichTextField lbl1, lblExtra, lbl2, lbl3, lbl4, lblID;
            
            
            int usedWidth = 0;
            
            long style1 = LabelField.USE_ALL_WIDTH | DrawStyle.HCENTER;
             
            CustomLabelField lblCountry1 = new CustomLabelField(match.team1Name, fontHeightMatchTeam, Utils.colorTeamName, style1);
            lblCountry1.setFont(Utils.getBoldFont(fontHeightMatchTeam));
            vfmLeft.add(lblCountry1);
            if(match.flag1 != null && match.flag1.equalsIgnoreCase("") == false)
            {
                BitmapField imageField1 = new BitmapField(Bitmap.getBitmapResource(match.flag1), DrawStyle.HCENTER | BitmapField.USE_ALL_WIDTH);
                vfmLeft.add(imageField1);
            }
            
            CustomLabelField lblCountry2 = new CustomLabelField(match.team2Name, fontHeightMatchTeam, Utils.colorTeamName, style1);
            lblCountry2.setFont(Utils.getBoldFont(fontHeightMatchTeam));
            vfmRight.add(lblCountry2);
            if(match.flag2 != null && match.flag2.equalsIgnoreCase("") == false)
            {
                BitmapField imageField2 = new BitmapField(Bitmap.getBitmapResource(match.flag2), DrawStyle.HCENTER | BitmapField.USE_ALL_WIDTH);
                vfmRight.add(imageField2);
            }
            
            long style =  RichTextField.USE_ALL_WIDTH | DrawStyle.HCENTER | RichTextField.FOCUSABLE;
            String scoreText = match.team1_score + " : " + match.team2_score;
            lbl1  = new CustomRichTextField( scoreText, Utils.colorText, style);
            lbl2  = new CustomRichTextField(match.stadium, Utils.colorText,style);
            lbl3  = new CustomRichTextField(match.date + " "+match.time, Utils.colorText,style);
            lbl4  = new CustomRichTextField("Group "+match.group, Utils.colorText,style);
            lblID  = new CustomRichTextField("Match: " + match.id, Utils.colorText, style);
            lblID.setFont(Utils.getNormalFont(fontHeightMatchText));
            lbl1.setFont(Utils.getBoldFont(fontHeightMatchText));lbl2.setFont(Utils.getNormalFont(fontHeightMatchText));
            lbl3.setFont(Utils.getNormalFont(fontHeightMatchText));lbl4.setFont(Utils.getNormalFont(fontHeightMatchText));
            vfmMiddle.add(lbl1);
            
            if(match.extra != null && match.extra.equalsIgnoreCase("")==false)
            {
                lblExtra  = new CustomRichTextField(match.extra, Utils.colorText,style);
                lblExtra.setFont(Utils.getBoldFont(fontHeightMatchText));
                vfmMiddle.add(lblExtra);
            }            
            vfmMiddle.add(lbl2);vfmMiddle.add(lbl3);
            if(isGroupDisplay && match.group.equalsIgnoreCase("null") == false)vfmMiddle.add(lbl4);
            if(match.id >= 49) vfmMiddle.add(lblID);
            if(match.title != null && match.title.equalsIgnoreCase("") == false)
            {
                String title = Utils.getPrintableTitleFromKey(match.title);
                CustomRichTextField lblTitle  = new CustomRichTextField(title, Utils.colorText, style);
                lblTitle.setFont(Utils.getNormalFont(fontHeightMatchText));
                vfmMiddle.add(lblTitle);
            }
            
            if(isDetailsDisplay)
            {
                HyperlinkButtonField btnMore = new HyperlinkButtonField("Details", Utils.colorText, Utils.colorTextFocus, Utils.bgColor, 0, 0);
                btnMore.setButtonId(match.id);
                btnMore.setFont(Utils.getBoldFont(Utils.fontHeightText));
                btnMore.setChangeListener(listener);
                vfmMiddle.add(btnMore);
            }
                        
            CustomGridFieldManager grid = new CustomGridFieldManager(colWidths, 0);
            grid.add(vfmLeft);
            grid.add(vfmMiddle);
            grid.add(vfmRight);
            return grid;            
        }
        
        public HorizontalFieldManager getButtonManager(int type)
        {   
            int fontHeight = Utils.fontHeightText;
            VerticalFieldManager vfm1, vfm2, vfm3, vfm4;
            int usedWidth = 0;
            CustomLabelField lbl;
            while (true)
            {
                lbl = new CustomLabelField("Sort By : ", fontHeight, Utils.colorText);
                lbl.setFont(Utils.getBoldFont(fontHeight));
                
                vfm1 = getHyperLinkButton("DATE", BUTTON_ID_DATE, fontHeight);
                vfm2 = getHyperLinkButton("TEAM", BUTTON_ID_TEAM, fontHeight);
                vfm3 = getHyperLinkButton("ROUND", BUTTON_ID_GROUP, fontHeight);
                
                usedWidth = lbl.getPreferredWidth()+ vfm1.getPreferredWidth()+vfm2.getPreferredWidth()+vfm3.getPreferredWidth()+10;
                if(usedWidth < (Utils.displayWidth -20))break;
                fontHeight--;
            }
            
            HorizontalFieldManager hfmButton = new HorizontalFieldManager();
            hfmButton.add(lbl);
            hfmButton.add(vfm1);hfmButton.add(vfm2);hfmButton.add(vfm3);
            
            hfmButton.setMargin(0,0,0,Utils.displayWidth - usedWidth);
            return hfmButton;            
        }
        
        public VerticalFieldManager getHyperLinkButton(String text, int buttonID, int fontHeight)
        {
            VerticalFieldManager vfm = new VerticalFieldManager();
            
            if(type == buttonID)
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
        public CustomRichTextField getTextField(String text, int fontSize, int textColor)
        {            
            CustomRichTextField lbl  = new CustomRichTextField(text, textColor, RichTextField.USE_TEXT_WIDTH | DrawStyle.HCENTER | RichTextField.FOCUSABLE);
            lbl.setFont(Utils.getNormalFont(fontSize));
            return lbl;
        }
        
        
        protected void makeMenu(Menu menu, int instance)
        {
            menu.add(backMenu);
            /*
            menu.add(reloadMenu);
            if(timerStarted)
                menu.add(stopMenu);
            else
                menu.add(startMenu);
            */
            menu.add(increaseFontMenu);
            menu.add(decreaseFontMenu);
        }     
        
        private void reload()
        {
            initialize(true);
            callMatchScoresFromServer();
        }
        
         private MenuItem backMenu = new MenuItem(Utils.BACK_TEXT, 100, 10)
        {
            public void run()
            {
                backToPreviousScreen();
            }
        };
        
        private MenuItem reloadMenu = new MenuItem("Reload Score", 100, 10)
        {
            public void run()
            {
                reload();
            }
        };
        
        private MenuItem startMenu = new MenuItem("Start Autoupdate Score", 100, 10)
        {
            public void run()
            {
                startTimer();
            }
        };
        
        private MenuItem stopMenu = new MenuItem("Stop Autoupdate Score", 100, 10)
        {
            public void run()
            {
                stopTimer();
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
        
        private void stopTimer()
        {
            if(timer != null)
                timer.cancel();
            timerStarted = false;
        }
        private void backToPreviousScreen()
        {
            stopTimer();
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
            
            if(buttonId == BUTTON_ID_UPDATE)
            {
                selectedIndex = choiceFieldDate.getSelectedIndex();
                initialize(false);
            }            
            else if(buttonId == BUTTON_ID_GROUP)
            {
                this.type = BUTTON_ID_GROUP;
                selectedIndex = 0;
                //sortingVector.reSort();
                //sortMatches(this.type);
                initialize(false);
            }
            else if(buttonId == BUTTON_ID_TEAM)
            {
                this.type = BUTTON_ID_TEAM;
                selectedIndex = 0;
                //sortingVector.reSort();
                //sortMatches(this.type);
                initialize(false); 
            }
            else if(buttonId == BUTTON_ID_DATE)
            {
                this.type = BUTTON_ID_DATE;
                selectedIndex = 0;
                //sortingVector.reSort();
                //sortMatches(this.type);
                initialize(false); 
            }
            else
            {
                Match1 match = Utils.getMatchFromID(buttonId);
                MatchScreen.callMatchDetailsScreen(match, ScreenManager.MATCH_DETAILS_SCREEN, screenIndex, thisClass);
            }
            
        }
        
        /*
        private void callMatchDetailsScreen(Match1 match)
        {
            int nextIndex = ScreenManager.MATCH_DETAILS_SCREEN;
            if (screenManager.mainScreenArray[nextIndex] == null)
                screenManager.mainScreenArray[nextIndex] = new MatchDetailsScreen();
            MatchDetailsScreen screen = (MatchDetailsScreen)screenManager.mainScreenArray[nextIndex];
            screen.setMatch(match);
            screen.initialize(true);
            
            screen.callMatchDetailsFromServer();
            //screen.startTimer();
            
            screenManager.previousScreenArray[nextIndex].push(screenIndex);
            UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[nextIndex]); 
            closeScreen();
        }
        */
        public static void callMatchDetailsScreen(Match1 match, int nextIndex, int currentScreenIndex, MainScreen previousScreen)
        {
            //int nextIndex = ScreenManager.MATCH_DETAILS_SCREEN;
            ScreenManager screenManager = ScreenManager.getInstance();
            if (screenManager.mainScreenArray[nextIndex] == null)
                screenManager.mainScreenArray[nextIndex] = new MatchDetailsScreen();
            MatchDetailsScreen screen = (MatchDetailsScreen)screenManager.mainScreenArray[nextIndex];
            screen.setMatch(match);
            screen.initialize(true);
            
            screen.callMatchDetailsFromServer();
            //screen.startTimer();
            
            screenManager.previousScreenArray[nextIndex].push(currentScreenIndex);
            UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[nextIndex]); 
            previousScreen.close();
        }
        
        
        private int previousWidth, previousHeight;
        public void checkOrientation()
        {
            if(previousWidth != Utils.displayWidth || previousHeight != Utils.displayHeight)
                initialize(false);
            previousWidth = Utils.displayWidth;
            previousHeight = Utils.displayHeight;
        }
        
        SimpleSortingVector sortingVector = new SimpleSortingVector();
        public void sortMatches(final int type)
        {
            sortingVector.removeAllElements();
            int size = Utils.matchVector.size();
            sortingVector.setSortComparator(new Comparator() 
            {
                public int compare(Object o1, Object o2) { 
                
                    Match1 match1 = (Match1)o1;
                    Match1 match2 = (Match1)o2;
                    
                    if(type == BUTTON_ID_GROUP)
                    {
                        String group1 = match1.group;
                        String group2 = match2.group;                         
                        int result = group1.compareTo(group2);
                        if(result < 0)          return -1;
                        else if(result > 0)     return 1;
                        else return match1.id - match2.id;
                    }
                    else
                    {
                        String date1 = match1.date;
                        String date2 = match2.date;
                        
                        String str1[] = Utils.split(date1, "/");
                        int dayCount1 = Integer.parseInt(str1[0]) + Integer.parseInt(str1[1]) * 30;
                        
                        String str2[] = Utils.split(date2, "/");
                        int dayCount2 = Integer.parseInt(str2[0]) + Integer.parseInt(str2[1]) * 30;
                        
                        //int result = match1.date.compareTo(match2.date);
                        int result = dayCount1 - dayCount2;
                        if(result < 0)          return -1;
                        else if(result > 0)     return 1;
                        else 
                        {
                            int result1 = match1.time.compareTo(match2.time);
                            if(result1 < 0)          return -1;
                            else if(result1 > 0)     return 1;
                            return match1.id - match2.id;
                        }
                        //return match1.id - match2.id;
                    }
                }
            });
            
            for(int i=0;i<Utils.matchVector.size();i++)
            {
                Match1 match = (Match1)Utils.matchVector.elementAt(i);
                sortingVector.addElement(match);
            } 
            
            for(int i=0;i<Utils.remainingmatchVector.size();i++)
            {
                Match1 match = (Match1)Utils.remainingmatchVector.elementAt(i);
                sortingVector.addElement(match);
            }        
            sortingVector.reSort();             
        }
        
        private static int REMAINING_MATCHES_REQUEST_ID = 123456;
        private int MATCH_SCORES_REQUEST_ID = 13458;
        public void callMatchScoresFromServer()
        {
            HttpRequestGet getRequest = new HttpRequestGet(this, Utils.SERVER_URL + "getRemainingMatchScores.php", null, null, MATCH_SCORES_REQUEST_ID, null, false, "Please Wait...Loading PointTable"); 
        }
        
        public void responseReceived(final boolean isSuccessful, final String resultString,
                        final int responseId, Object object)
        {
            UiApplication.getUiApplication().invokeLater(new Runnable()
            {
                public void run()
                {   
                    try
                    {
                        String output = resultString.trim();
                        if(Utils.DEBUG) System.out.println(output);    
                        //UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
                        if (isSuccessful)
                        {
                            if (responseId == INITIAL_SCORE_REQUEST_ID)
                            {
                                Utils.SCORE_UPDATED = true;
                                callMatchScoresFromServer();
                            }
                            else if (responseId == MATCH_SCORES_REQUEST_ID)
                            {
                                Parser.parseMatchScoresResposne(output);
                                initialize(false);
                                //parseMatchScoresResposne(output);
                            }
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
                        //Dialog.alert("EXECEPTION");
                        System.out.println("EXECEPTION");
                    }
                }
            }); 
        }
        
        private int INITIAL_SCORE_REQUEST_ID = 12318;
        public void callInitialScoresFromServer()
        {
            HttpRequestGet getRequest = new HttpRequestGet(this, Utils.SERVER_URL + "getInitialMatchScores.php", null, null, INITIAL_SCORE_REQUEST_ID, null, false, ""); 
        }
}


